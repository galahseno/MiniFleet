package id.dev.core.presentation.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.CoreRepository
import id.dev.core.domain.UserPreferences
import id.dev.core.domain.model.VehicleDomain
import id.dev.core.domain.work_status.WorkStatus
import id.dev.core.presentation.ui.R
import id.dev.core.presentation.ui.UiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrackingViewModel(
    private val coreRepository: CoreRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(TrackingState())
    val state = _state.asStateFlow()

    private val _event = Channel<TrackingEvent>()
    val event = _event.receiveAsFlow()

    private var positionJob: Job? = null

    init {
        _state.map { it.isTracking }
            .distinctUntilChanged()
            .onEach { start ->
                positionJob?.cancel()

                if (start) {
                    positionJob = coreRepository.getVehiclePositionStream()
                        .onEach { data ->
                            checkForAlerts(data)
                            _state.update {
                                it.copy(
                                    vehicleDomain = data
                                )
                            }
                            userPreferences.setCurrentIndex(data.positionIndex)
                        }.catch { e ->
                            _event.trySend(TrackingEvent.Error(UiText.DynamicString(e.message.toString())))
                        }.launchIn(viewModelScope)
                } else {
                    _state.update {
                        it.copy(
                            vehicleDomain = null
                        )
                    }
                    positionJob = null
                }
            }.launchIn(viewModelScope)

        coreRepository.getLogTrip()
            .onEach { listTrip ->
                _state.update { it.copy(tripLogs = listTrip) }
            }
            .launchIn(viewModelScope)

        coreRepository.observeExistingWork()
            .distinctUntilChanged()
            .onEach { status ->
                when (status) {
                    WorkStatus.Running -> _state.update { it.copy(isTracking = true) }
                    else -> _state.update { it.copy(isTracking = false) }
                }
            }.launchIn(viewModelScope)
    }

    fun onAction(action: TrackingAction) {
        when (action) {
            is TrackingAction.OnStartClick -> {
                _state.update { it.copy(isTracking = true) }
            }

            is TrackingAction.OnStopClick -> {
                _state.update { it.copy(isTracking = false) }
                coreRepository.stopSimulation()
            }

            is TrackingAction.OnDismissAlertBanner -> dismissAlertImmediately()
            is TrackingAction.OnLogoutClick -> {
                viewModelScope.launch {
                    coreRepository.logout()
                    _event.trySend(TrackingEvent.OnSuccessLogout)
                }
            }

            is TrackingAction.OnShowRationaleAlert -> {
                showAlert(UiText.StringResource(R.string.need_permission_notification))
            }

            else -> Unit
        }
    }

    private fun checkForAlerts(data: VehicleDomain) {
        val currentState = _state.value

        if (data.speedKmh > 80) {
            showAlert(UiText.StringResource(R.string.alert_speed, arrayOf(data.speedKmh)))
        }

        if (data.doorOpen && data.speedKmh > 0) {
            showAlert(UiText.StringResource(R.string.alert_door_moving))
        }

        currentState.vehicleDomain?.let { previousData ->
            if (data.engineOn != previousData.engineOn) {
                val message = if (data.engineOn) R.string.alert_engine_on
                else R.string.alert_engine_off
                showAlert(UiText.StringResource(message))
            }
        }
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