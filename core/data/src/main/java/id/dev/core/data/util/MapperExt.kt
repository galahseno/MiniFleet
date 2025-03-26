package id.dev.core.data.util

import androidx.work.WorkInfo
import id.dev.core.data.model.VehicleData
import id.dev.core.database.entity.TripLogEntity
import id.dev.core.domain.model.LatLngDomain
import id.dev.core.domain.model.TripLog
import id.dev.core.domain.model.VehicleDomain
import id.dev.core.domain.work_status.WorkStatus

fun TripLogEntity.toTripLog(): TripLog {
    return TripLog(
        timestamp = timestamp,
        location = LatLngDomain(lat, lon),
        speed = speed,
        engineStatus = if (engineOn) "On" else "Off",
        doorStatus = if (doorOpen) "Open" else "Closed"
    )
}

fun WorkInfo.toDomain(): WorkStatus {
    return when (state) {
        WorkInfo.State.RUNNING -> WorkStatus.Running
        else -> WorkStatus.NotRunning
    }
}

fun VehicleData.toVehicleDomain(): VehicleDomain {
    return VehicleDomain(
        position = LatLngDomain(position.lat, position.lon),
        engineOn = engineOn,
        doorOpen = doorOpen,
        speedKmh = speedKmh,
        positionIndex = positionIndex
    )
}