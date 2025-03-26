package id.dev.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.WorkManager
import id.dev.core.data.UserPreferencesImpl
import id.dev.core.data.CoreRepositoryImpl
import id.dev.core.data.util.NotificationHelper
import id.dev.core.domain.UserPreferences
import id.dev.core.domain.CoreRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val PREFERENCES_DATA_STORE_NAME = "user_preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_DATA_STORE_NAME
)

val coreDataModule = module {
    singleOf(::CoreRepositoryImpl).bind<CoreRepository>()
    singleOf(::UserPreferencesImpl).bind<UserPreferences>()

    single { androidContext().dataStore }
    single {
        WorkManager.getInstance(get())
    }
    singleOf(::NotificationHelper)
}