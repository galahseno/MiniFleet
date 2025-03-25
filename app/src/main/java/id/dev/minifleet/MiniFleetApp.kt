package id.dev.minifleet

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MiniFleetApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MiniFleetApp)
            modules(

            )
        }
    }
}