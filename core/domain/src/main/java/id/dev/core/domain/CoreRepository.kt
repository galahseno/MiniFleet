package id.dev.core.domain

import id.dev.core.domain.model.TripLog
import id.dev.core.domain.model.VehicleDomain
import id.dev.core.domain.work_status.WorkStatus
import kotlinx.coroutines.flow.Flow

interface CoreRepository {
    fun getVehiclePositionStream(): Flow<VehicleDomain>
    fun stopSimulation()
    fun observeExistingWork(): Flow<WorkStatus>
    fun getLogTrip(): Flow<List<TripLog>>
    fun logout()
}