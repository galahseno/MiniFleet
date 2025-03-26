package id.dev.dashboard.presentation

sealed interface DashboardAction {
    data object ToggleBluetooth : DashboardAction
    data class ConnectToDevice(val deviceId: String) : DashboardAction
    data object Disconnect : DashboardAction
    data object OnDismissAlertBanner : DashboardAction
}