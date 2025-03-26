package id.dev.core.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import id.dev.core.data.CoreRepositoryImpl.Companion.CURRENT_INDEX
import id.dev.core.data.CoreRepositoryImpl.Companion.KEY_VEHICLE_DATA
import id.dev.core.data.model.LatLngData
import id.dev.core.data.model.VehicleData
import id.dev.core.data.util.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlin.random.Random

class VehicleSimulationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    private val notificationHelper = NotificationHelper(context)

    override suspend fun doWork(): Result {
        val singleRoute = listOf(
            // Purwosari starting point
            LatLngData(-7.565733, 110.814163),
            // Along Jl. Slamet Riyadi
            LatLngData(-7.565851, 110.815508),
            LatLngData(-7.566280, 110.817892),
            LatLngData(-7.566621, 110.819743),
            // Gladak area
            LatLngData(-7.566899, 110.821456),
            LatLngData(-7.567201, 110.823112),
            // U-turn point
            LatLngData(-7.567512, 110.824567),
            // Return path
            LatLngData(-7.567201, 110.823112),
            LatLngData(-7.566899, 110.821456),
            LatLngData(-7.566621, 110.819743),
            LatLngData(-7.566280, 110.817892),
            LatLngData(-7.565851, 110.815508),
            LatLngData(-7.565733, 110.814163)
        )

        val route = List(100) { singleRoute }.flatten()

        var index = inputData.getInt(CURRENT_INDEX, 0)
        var direction = 1
        var previousEngineOn: Boolean? = null

        while (!isStopped) {
            val position = route[index]

            val speed = when {
                index % 5 == 0 -> 0
                index % 7 == 0 -> 80 + Random.nextInt(0, 20)  // 80-100 km/h
                index < route.size / 2 -> 20 + Random.nextInt(20, 40)  // 40-60 km/h
                else -> 30 + Random.nextInt(30, 40)  // 60-70 km/h
            }

            val doorOpen = when {
                speed == 0 -> Random.nextFloat() > 0.5f // 50% chance when stopped
                Random.nextFloat() > 0.75f -> true  // 25% chance while moving
                else -> false
            }

            val engineOn = when {
                speed > 0 -> true // Always on when moving
                else -> Random.nextFloat() > 0.5f
                // When stopped, 50% chance to turn off
            }

            checkForAlerts(
                context = context,
                speed = speed,
                doorOpen = doorOpen,
                engineOn = engineOn,
                previousEngineOn = previousEngineOn
            )
            previousEngineOn = engineOn

            val vehicleData = VehicleData(
                position = position,
                engineOn = engineOn,
                doorOpen = doorOpen,
                speedKmh = speed,
                positionIndex = index
            )

            val outputData = workDataOf(
                KEY_VEHICLE_DATA to Json.encodeToString(vehicleData)
            )

            setProgress(outputData)

            delay(5000)

            index += direction
            if (index >= route.size - 1 || index <= 0) {
                direction *= -1
            }
        }

        return Result.success()
    }

    private fun checkForAlerts(
        context: Context,
        speed: Int,
        doorOpen: Boolean,
        engineOn: Boolean,
        previousEngineOn: Boolean?
    ) {
        if (speed > 80) {
            notificationHelper.showAlertNotification(
                context.getString(R.string.speed_alert_title),
                context.getString(R.string.alert_speed, speed)
            )
        }

        if (doorOpen && speed > 0) {
            notificationHelper.showAlertNotification(
                context.getString(R.string.door_alert_title),
                context.getString(R.string.alert_door_moving)
            )
        }

        previousEngineOn?.let {
            if (engineOn != it) {
                val message = if (engineOn) R.string.alert_engine_on else R.string.alert_engine_off
                notificationHelper.showAlertNotification(
                    context.getString(R.string.engine_alert_title),
                    context.getString(message)
                )
            }
        }
    }
}