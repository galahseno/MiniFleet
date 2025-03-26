package id.dev.auth.presentation

import id.dev.core.presentation.ui.UiText

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val isAlertVisible: Boolean = false,
)