package id.dev.core.data

import androidx.lifecycle.asFlow
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import id.dev.core.data.model.VehicleData
import id.dev.core.data.util.toDomain
import id.dev.core.data.util.toTripLog
import id.dev.core.data.util.toVehicleDomain
import id.dev.core.database.dao.TripLogDao
import id.dev.core.database.entity.TripLogEntity
import id.dev.core.domain.CoreRepository
import id.dev.core.domain.UserPreferences
import id.dev.core.domain.model.TripLog
import id.dev.core.domain.model.VehicleDomain
import id.dev.core.domain.work_status.WorkStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.serialization.json.Json
import java.util.UUID
import java.util.concurrent.TimeUnit

class CoreRepositoryImpl(
    private val workManager: WorkManager,
    private val tripLogDao: TripLogDao,
    private val userPreferences: UserPreferences,
    private val applicationScope: CoroutineScope,
) : CoreRepository {
    private val _vehicleDomain = MutableSharedFlow<VehicleDomain>()
    private var simulationWorkId: UUID? = null
    private val json = Json { ignoreUnknownKeys = true }
    private var observationJob: Job? = null

    override fun getVehiclePositionStream(): Flow<VehicleDomain> {
        applicationScope.launch(Dispatchers.IO) {
            startSimulationWorker()
        }
        return _vehicleDomain
    }

    override fun stopSimulation() {
        simulationWorkId?.let { id ->
            workManager.cancelWorkById(id)
            simulationWorkId = null
            applicationScope.launch {
                userPreferences.setSimulationWorkId(null)
                userPreferences.setCurrentIndex(0)
            }
        }
        observationJob?.cancel()
        observationJob = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeExistingWork(): Flow<WorkStatus> {
        return userPreferences
            .getSimulationWorkId()
            .flatMapLatest { idString ->
                idString?.let { uuidString ->
                    try {
                        val uuid = UUID.fromString(uuidString)
                        workManager.getWorkInfoByIdFlow(uuid)
                            .map { workInfo -> workInfo.toDomain() }
                    } catch (e: Exception) {
                        flowOf(WorkStatus.NotRunning)
                    }
                } ?: flowOf(WorkStatus.NotRunning)
            }
    }

    override fun getLogTrip(): Flow<List<TripLog>> {
        return tripLogDao.getAllLogs().map { listTrip ->
            listTrip.map { it.toTripLog() }
        }
    }

    override fun logout() {
        simulationWorkId?.let { id ->
            workManager.cancelWorkById(id)
            simulationWorkId = null
        }

        applicationScope.launch(Dispatchers.IO) {
            userPreferences.setLoggedIn(false)
            userPreferences.setSimulationWorkId(null)
            userPreferences.setCurrentIndex(0)
            tripLogDao.cleanupOldLogs()
        }
    }

    private suspend fun startSimulationWorker() {
        simulationWorkId?.let { id ->
            workManager.cancelWorkById(id)
        }
        val currentIndex = userPreferences.getCurrentIndex()

        if (currentIndex == 0) {
            tripLogDao.cleanupOldLogs()
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        val simulationWork = OneTimeWorkRequestBuilder<VehicleSimulationWorker>()
            .setInitialDelay(0, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(workDataOf(CURRENT_INDEX to currentIndex))
            .build()

        workManager.enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            simulationWork
        )
        simulationWorkId = simulationWork.id
        userPreferences.setSimulationWorkId(simulationWork.id.toString())

        observationJob = workManager.getWorkInfoByIdLiveData(simulationWork.id)
            .asFlow()
            .takeWhile { info ->
                info != null && info.state.isFinished.not()
            }
            .filter { info ->
                info?.state == WorkInfo.State.RUNNING &&
                        info.progress.getString(KEY_VEHICLE_DATA) != null
            }
            .mapNotNull { it.progress.getString(KEY_VEHICLE_DATA) }
            .map { json.decodeFromString<VehicleData>(it) }
            .onEach { data ->
                tripLogDao.insert(
                    TripLogEntity(
                        timestamp = System.currentTimeMillis(),
                        lat = data.position.lat,
                        lon = data.position.lon,
                        speed = data.speedKmh,
                        engineOn = data.engineOn,
                        doorOpen = data.doorOpen
                    )
                )
                _vehicleDomain.emit(data.toVehicleDomain())
            }
            .launchIn(applicationScope + Dispatchers.IO)
    }

    companion object {
        const val KEY_VEHICLE_DATA = "vehicle_data"
        const val CURRENT_INDEX = "current_index"
        const val WORK_NAME = "vehicle_simulation"
    }
}