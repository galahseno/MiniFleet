package id.dev.dashboard.domain.model

sealed class ConnectionResult {
    data object Connecting : ConnectionResult()
    data object Connected : ConnectionResult()
    data class Error(val message: String) : ConnectionResult()
}