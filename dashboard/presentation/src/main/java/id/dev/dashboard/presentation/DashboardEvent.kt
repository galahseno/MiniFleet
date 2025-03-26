package id.dev.dashboard.presentation

sealed interface DashboardEvent {
    data object DisconnectedDevice: DashboardEvent
}