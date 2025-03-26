package id.dev.minifleet.di

import id.dev.minifleet.MainViewModel
import id.dev.minifleet.MiniFleetApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as MiniFleetApp).applicationScope
    }
    viewModelOf(::MainViewModel)
}