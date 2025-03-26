package id.dev.core.presentation.tracking

import id.dev.core.presentation.ui.UiText

sealed interface TrackingEvent {
    data class Error(val message: UiText) : TrackingEvent
    data object OnSuccessLogout: TrackingEvent
}