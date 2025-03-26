package id.dev.dashboard.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DoorFront
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.core.domain.model.LatLngDomain
import id.dev.core.domain.model.TripLog
import id.dev.core.presentation.design_system.MiniFleetTheme
import id.dev.core.presentation.design_system.component.AlertBanner
import id.dev.core.presentation.design_system.component.SensorCard
import id.dev.core.presentation.tracking.TrackingAction
import id.dev.core.presentation.tracking.TrackingEvent
import id.dev.core.presentation.tracking.TrackingState
import id.dev.core.presentation.tracking.TrackingViewModel
import id.dev.core.presentation.ui.ObserveAsEvents
import id.dev.core.presentation.ui.UiText
import id.dev.dashboard.presentation.component.TripLogItem
import id.dev.dashboard.presentation.component.bluetooth.BluetoothConnectionPanel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreenRoot(
    onSuccessLogout: () -> Unit,
    onOpenMapsClick: () -> Unit,
    trackingViewModel: TrackingViewModel,
    dashboardViewModel: DashboardViewModel = koinViewModel()
) {
    val trackingState by trackingViewModel.state.collectAsStateWithLifecycle()
    val dashboardState by dashboardViewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(trackingViewModel.event) { event ->
        when (event) {
            is TrackingEvent.OnSuccessLogout -> onSuccessLogout()
            else -> Unit
        }
    }

    ObserveAsEvents(dashboardViewModel.event) { event ->
        when (event) {
            is DashboardEvent.DisconnectedDevice -> {
                trackingViewModel.onAction(TrackingAction.OnStopClick)
            }

            else -> Unit
        }
    }

    DashboardScreen(
        trackingState = trackingState,
        onTrackingAction = { action ->
            when (action) {
                is TrackingAction.OnOpenMapsClick -> onOpenMapsClick()
                else -> Unit
            }
            trackingViewModel.onAction(action)
        },
        dashboardState = dashboardState,
        onDashboardAction = dashboardViewModel::onAction
    )
}

@Composable
private fun DashboardScreen(
    trackingState: TrackingState,
    onTrackingAction: (TrackingAction) -> Unit,
    dashboardState: DashboardState,
    onDashboardAction: (DashboardAction) -> Unit
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val itemCount by rememberUpdatedState(trackingState.tripLogs.size)

    LaunchedEffect(itemCount) {
        if (itemCount > 0) {
            lazyListState.scrollToItem(0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            BluetoothConnectionPanel(
                state = dashboardState,
                onAction = onDashboardAction,
                modifier = Modifier.padding(8.dp)
            )

            SensorCard(
                title = "Speed",
                value = trackingState.vehicleDomain?.speedKmh?.toString() ?: "--",
                unit = "km/h",
                imageVector = Icons.Rounded.Speed,
                statusColor = if ((trackingState.vehicleDomain?.speedKmh ?: 0) > 80)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )

            SensorCard(
                title = "Engine",
                value = if (trackingState.vehicleDomain?.engineOn == true) "Running" else "Off",
                imageVector = Icons.Rounded.Engineering,
                statusColor = if (trackingState.vehicleDomain?.engineOn == true)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.tertiary
            )

            SensorCard(
                title = "Doors",
                value = if (trackingState.vehicleDomain?.doorOpen == true) "Open" else "Closed",
                imageVector = Icons.Rounded.DoorFront,
                statusColor = if (trackingState.vehicleDomain?.doorOpen == true)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.tertiary
            )

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (dashboardState.connectedDevice != null) {
                            onTrackingAction(TrackingAction.OnOpenMapsClick)
                        }
                    },
                    enabled = dashboardState.connectedDevice != null
                ) {
                    Text(
                        text = if (dashboardState.connectedDevice != null) "View Vehicle on Map"
                        else "Connect to Device",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Button(
                    onClick = {
                        onTrackingAction(TrackingAction.OnLogoutClick)
                        onDashboardAction(DashboardAction.Disconnect)
                    },
                ) {
                    Text("Logout")
                }
            }


            if (trackingState.tripLogs.isNotEmpty()) {
                Text(
                    text = "Trip History",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(trackingState.tripLogs, key = { it.timestamp }) { log ->
                        var visible by remember { mutableStateOf(false) }

                        LaunchedEffect(Unit) {
                            visible = true
                        }

                        AnimatedVisibility(visible = visible,
                            enter = slideInVertically { it } + fadeIn(),
                            modifier = Modifier.animateItem(
                                fadeInSpec = null, fadeOutSpec = null
                            )) {
                            TripLogItem(log = log)
                        }
                    }
                }
            }
        }

        AlertBanner(
            isAlertVisible = trackingState.isAlertVisible,
            alertMessage = trackingState.alertMessage?.asString(context) ?: "",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            onDismiss = { onTrackingAction(TrackingAction.OnDismissAlertBanner) })


        AlertBanner(
            isAlertVisible = dashboardState.isAlertVisible,
            alertMessage = dashboardState.alertMessage?.asString(context) ?: "",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            onDismiss = { onDashboardAction(DashboardAction.OnDismissAlertBanner) })
    }

}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    MiniFleetTheme {
        DashboardScreen(
            trackingState = TrackingState(
                isAlertVisible = true,
                alertMessage = UiText.DynamicString("Test error"),
                tripLogs = listOf(
                    TripLog(
                        timestamp = System.currentTimeMillis(),
                        speed = 70,
                        engineStatus = "On",
                        doorStatus = "Open",
                        location = LatLngDomain(
                            lat = -6.1753924, lon = 106.8271528
                        )
                    )
                )
            ),
            onTrackingAction = {},
            dashboardState = DashboardState(),
            onDashboardAction = {}
        )
    }
}