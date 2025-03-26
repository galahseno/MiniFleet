package id.dev.auth.presentation

sealed interface LoginEvent {
    data object NavigateToDashboard : LoginEvent
}