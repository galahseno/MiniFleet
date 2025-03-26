package id.dev.dashboard.presentation

import id.dev.core.presentation.ui.UiText

data class DashboardState(
    val isEnabled: Boolean = false,
    val connectingDevice: String? = null,
    val connectedDevice: String? = null,
    val availableDevices: List<String> = emptyList(),
    val alertMessage: UiText? = null,
    val isAlertVisible: Boolean = false,
)