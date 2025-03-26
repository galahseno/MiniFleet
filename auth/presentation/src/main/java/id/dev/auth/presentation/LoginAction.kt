package id.dev.auth.presentation

sealed interface LoginAction {
    data class UsernameChanged(val username: String) : LoginAction
    data class PasswordChanged(val password: String) : LoginAction
    data object LoginClicked : LoginAction
    data object OnDismissAlertBanner : LoginAction
}