package id.dev.dashboard.domain

import id.dev.dashboard.domain.model.BluetoothDomain
import id.dev.dashboard.domain.model.ConnectionResult
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun observeConnectionState(): Flow<BluetoothDomain>
    fun connectToDevice(deviceId: String): Flow<ConnectionResult>
    fun disconnect()
}