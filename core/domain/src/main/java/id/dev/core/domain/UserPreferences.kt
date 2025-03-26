package id.dev.core.domain

import kotlinx.coroutines.flow.Flow

interface UserPreferences {
    fun isLoggedIn(): Flow<Boolean>
    suspend fun setLoggedIn(isLoggedIn: Boolean)
    suspend fun setSimulationWorkId(id: String?)
    fun getSimulationWorkId(): Flow<String?>
    suspend fun setConnectedDevice(deviceId: String?)
    fun getConnectedDevice(): Flow<String?>
    suspend fun setCurrentIndex(index: Int)
    suspend fun getCurrentIndex(): Int
}