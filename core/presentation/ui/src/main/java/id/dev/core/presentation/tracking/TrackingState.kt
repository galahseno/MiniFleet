package id.dev.core.presentation.tracking

import id.dev.core.domain.model.TripLog
import id.dev.core.domain.model.VehicleDomain
import id.dev.core.presentation.ui.UiText

data class TrackingState(
    val vehicleDomain: VehicleDomain? = null,
    val isTracking: Boolean = false,
    val alertMessage: UiText? = null,
    val isAlertVisible: Boolean = false,
    val tripLogs: List<TripLog> = emptyList(),
)
