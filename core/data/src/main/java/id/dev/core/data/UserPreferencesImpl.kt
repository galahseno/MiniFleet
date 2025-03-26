package id.dev.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import id.dev.core.domain.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesImpl(
    private val dataStore: DataStore<Preferences>,
) : UserPreferences {
    override fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map {
            it[IS_LOGGED_IN_KEY] ?: false
        }
    }

    override suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit {
            it[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    override suspend fun setSimulationWorkId(id: String?) {
        dataStore.edit {
            if (id != null) {
                it[SIMULATION_WORK_ID_KEY] = id
            } else {
                it.remove(SIMULATION_WORK_ID_KEY)
            }
        }
    }

    override fun getSimulationWorkId(): Flow<String?> {
        return dataStore.data.map { it[SIMULATION_WORK_ID_KEY] }
    }

    override suspend fun setConnectedDevice(deviceId: String?) {
        dataStore.edit {
            if (deviceId != null) {
                it[CONNECTED_DEVICE_KEY] = deviceId
            } else {
                it.remove(CONNECTED_DEVICE_KEY)
            }
        }
    }

    override fun getConnectedDevice(): Flow<String?> {
        return dataStore.data.map { it[CONNECTED_DEVICE_KEY] }
    }

    override suspend fun setCurrentIndex(index: Int) {
        dataStore.edit {
            it[CURRENT_INDEX_KEY] = index
        }
    }

    override suspend fun getCurrentIndex(): Int {
        return dataStore.data.map { it[CURRENT_INDEX_KEY] ?: 0 }.first()
    }

    private companion object {
        val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        val SIMULATION_WORK_ID_KEY = stringPreferencesKey("simulation_work_id")
        val CONNECTED_DEVICE_KEY = stringPreferencesKey("connected_device")
        val CURRENT_INDEX_KEY = intPreferencesKey("current_index")
    }
}