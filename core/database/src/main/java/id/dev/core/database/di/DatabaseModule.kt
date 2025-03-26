package id.dev.core.database.di

import androidx.room.Room
import id.dev.core.database.TripLogDb
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            TripLogDb::class.java,
            "TripLog.db"
        )
            .build()
    }
    single { get<TripLogDb>().tripLogDao() }
}