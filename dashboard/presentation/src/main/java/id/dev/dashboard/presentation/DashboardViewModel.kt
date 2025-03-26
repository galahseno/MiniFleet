package id.dev.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.CoreRepository
import id.dev.core.presentation.ui.UiText
import id.dev.dashboard.domain.DashboardRepository
import id.dev.dashboard.domain.model.ConnectionResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dashboardRepository: DashboardRepository,
    private val coreRepository: CoreRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private val _event = Channel<DashboardEvent>()
    val event = _event.receiveAsFlow()

    init {
        dashboardRepository.observeConnectionState()
            .distinctUntilChanged()
            .onEach { state ->
                _state.update {
                    it.copy(
                        isEnabled = state.isEnabled,
                        connectedDevice = state.connectedDevice,
                        availableDevices = state.availableDevices
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.ToggleBluetooth -> toggleBluetooth()
            is DashboardAction.ConnectToDevice -> connectToDevice(action.deviceId)
            is DashboardAction.Disconnect -> disconnect()
            is DashboardAction.OnDismissAlertBanner -> dismissAlertImmediately()
        }
    }

    private fun toggleBluetooth() {
        _state.update { it.copy(isEnabled = !it.isEnabled) }
    }

    private fun connectToDevice(deviceId: String) {
        dashboardRepository.connectToDevice(deviceId)
            .onStart {
                _state.update { it.copy(connectingDevice = deviceId) }
            }
            .onEach { result ->
                when (result) {
                    ConnectionResult.Connected -> {
                        showAlert(UiText.StringResource(R.string.connected_message))
                        _state.update {
                            it.copy(
                                connectingDevice = null,
                                connectedDevice = deviceId
                            )
                        }
                    }

                    is ConnectionResult.Error -> {
                        showAlert(UiText.DynamicString(result.message))
                        _state.update { it.copy(connectingDevice = null) }
                    }

                    ConnectionResult.Connecting -> Unit /* Already handled by onStart */
                }
            }
            .launchIn(viewModelScope)
    }

    private fun disconnect() {
        dashboardRepository.disconnect()
        coreRepository.stopSimulation()
        _event.trySend(DashboardEvent.DisconnectedDevice)
        showAlert(UiText.StringResource(R.string.disconnect_message))
    }

    private fun showAlert(message: UiText) {
        if (_state.value.isAlertVisible) return

        _state.update {
            it.copy(
                isAlertVisible = true, alertMessage = message
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