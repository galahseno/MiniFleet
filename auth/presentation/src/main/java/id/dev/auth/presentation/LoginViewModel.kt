package id.dev.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.auth.domain.AuthRepository
import id.dev.auth.domain.model.LoginCredentials
import id.dev.core.domain.util.Result
import id.dev.core.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(event: LoginAction) {
        when (event) {
            is LoginAction.UsernameChanged -> {
                _state.update { it.copy(username = event.username) }
            }

            is LoginAction.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }

            is LoginAction.LoginClicked -> login()
            is LoginAction.OnDismissAlertBanner -> dismissAlertImmediately()
        }
    }

    private fun login() {
        dismissAlertImmediately()
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = authRepository.login(
                LoginCredentials(
                    username = _state.value.username,
                    password = _state.value.password
                )
            )

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> {
                    if (result.data) {
                        _event.send(LoginEvent.NavigateToDashboard)
                    } else {
                        showAlert(UiText.StringResource(R.string.invalid_credential))
                    }
                }

                is Result.Error -> {
                    showAlert(UiText.StringResource(R.string.error_process))
                }
            }
        }
    }

    private fun showAlert(message: UiText) {
        if (_state.value.isAlertVisible) return

        _state.update {
            it.copy(
                isAlertVisible = true, errorMessage = message
            )
        }
        dismissAlert()
    }

    private fun dismissAlert() {
        viewModelScope.launch {
            delay(3000)
            _state.update { it.copy(isAlertVisible = false) }
        }
    }

    private fun dismissAlertImmediately() {
        _state.update { it.copy(isAlertVisible = false) }
    }
}