package id.dev.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.dev.core.database.dao.TripLogDao
import id.dev.core.database.entity.TripLogEntity

@Database(
    version = 1,
    entities = [TripLogEntity::class],
    exportSchema = false
)
abstract class TripLogDb : RoomDatabase() {
    abstract fun tripLogDao(): TripLogDao
}