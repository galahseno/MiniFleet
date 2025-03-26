package id.dev.core.presentation.tracking

sealed interface TrackingAction {
    data object OnOpenMapsClick : TrackingAction
    data object OnStartClick : TrackingAction
    data object OnStopClick : TrackingAction
    data object OnBackClick : TrackingAction
    data object OnDismissAlertBanner : TrackingAction
    data object OnLogoutClick : TrackingAction
    data object OnShowRationaleAlert : TrackingAction
}