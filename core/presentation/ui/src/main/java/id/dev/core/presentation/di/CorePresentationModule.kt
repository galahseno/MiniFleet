package id.dev.core.presentation.di

import id.dev.core.presentation.tracking.TrackingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val corePresentationModule = module {
    viewModelOf(::TrackingViewModel)
}