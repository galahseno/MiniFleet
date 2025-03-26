package id.dev.dashboard.presentation.di

import id.dev.dashboard.presentation.DashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardPresentationModule = module {
    viewModelOf(::DashboardViewModel)
}