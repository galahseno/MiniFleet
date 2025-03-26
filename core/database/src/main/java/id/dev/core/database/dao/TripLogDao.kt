package id.dev.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.dev.core.database.entity.TripLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripLogDao {
    @Insert
    suspend fun insert(log: TripLogEntity)

    @Query("SELECT * FROM trip_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<TripLogEntity>>

    @Query("DELETE FROM trip_logs")
    suspend fun cleanupOldLogs()
}