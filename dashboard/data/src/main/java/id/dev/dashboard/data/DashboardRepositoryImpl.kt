package id.dev.dashboard.data

import id.dev.core.domain.UserPreferences
import id.dev.dashboard.domain.DashboardRepository
import id.dev.dashboard.domain.model.BluetoothDomain
import id.dev.dashboard.domain.model.ConnectionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardRepositoryImpl(
    private val userPreferences: UserPreferences,
    private val applicationScope: CoroutineScope
) : DashboardRepository {
    private val _state = MutableStateFlow(
        BluetoothDomain(
            isEnabled = false,
            connectedDevice = null,
            availableDevices = listOf("MockDevice_01", "MockDevice_02", "MockDevice_03")
        )
    )

    init {
        userPreferences.getConnectedDevice()
            .onEach { device ->
                if (device != null) {
                    _state.update {
                        it.copy(
                            connectedDevice = device,
                        )
                    }
                }
            }.launchIn(applicationScope)
    }

    override fun observeConnectionState(): Flow<BluetoothDomain> = _state

    override fun connectToDevice(deviceId: String): Flow<ConnectionResult> = flow {
        emit(ConnectionResult.Connecting)
        delay(2500)

        if (deviceId in _state.value.availableDevices) {
            _state.update { it.copy(connectedDevice = deviceId, isEnabled = true) }
            userPreferences.setConnectedDevice(deviceId)
            emit(ConnectionResult.Connected)
        } else {
            emit(ConnectionResult.Error("Device not found"))
        }
    }

    override fun disconnect() {
        _state.update { it.copy(connectedDevice = null) }
        applicationScope.launch {
            userPreferences.setConnectedDevice(null)

        }
    }
}