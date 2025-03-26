package id.dev.dashboard.data.di

import id.dev.dashboard.data.DashboardRepositoryImpl
import id.dev.dashboard.domain.DashboardRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardDataModule = module {
    singleOf(::DashboardRepositoryImpl).bind<DashboardRepository>()
}