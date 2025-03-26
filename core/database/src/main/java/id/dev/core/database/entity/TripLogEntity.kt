package id.dev.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_logs")
data class TripLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lat: Double,
    val lon: Double,
    val speed: Int,
    val engineOn: Boolean,
    val doorOpen: Boolean,
    val timestamp: Long,
)