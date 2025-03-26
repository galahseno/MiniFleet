package id.dev.dashboard.domain.model

data class BluetoothDomain(
    val isEnabled: Boolean,
    val connectedDevice: String?,
    val availableDevices: List<String>
)