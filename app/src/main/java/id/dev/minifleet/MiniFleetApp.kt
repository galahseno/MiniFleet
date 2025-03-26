package id.dev.minifleet

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import id.dev.auth.data.di.authDataModule
import id.dev.auth.di.authPresentationModule
import id.dev.core.data.di.coreDataModule
import id.dev.core.database.di.databaseModule
import id.dev.core.presentation.di.corePresentationModule
import id.dev.dashboard.data.di.dashboardDataModule
import id.dev.dashboard.presentation.di.dashboardPresentationModule
import id.dev.minifleet.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MiniFleetApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MiniFleetApp)
            modules(
                coreDataModule,
                corePresentationModule,
                databaseModule,
                appModule,
                authDataModule,
                authPresentationModule,
                dashboardDataModule,
                dashboardPresentationModule,
            )
        }
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alerts_channel",
                "Vehicle Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts for vehicle events"
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}