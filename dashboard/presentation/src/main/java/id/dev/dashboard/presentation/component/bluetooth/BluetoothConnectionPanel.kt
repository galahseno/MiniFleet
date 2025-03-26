package id.dev.dashboard.presentation.component.bluetooth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.design_system.MiniFleetTheme
import id.dev.dashboard.presentation.DashboardAction
import id.dev.dashboard.presentation.DashboardState

@Composable
fun BluetoothConnectionPanel(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = state.isEnabled, label = "bluetoothToggle")
    val cardElevation by transition.animateDp(label = "elevation") { enabled ->
        if (enabled) 8.dp else 2.dp
    }
    val cardPadding by transition.animateDp(label = "padding") { enabled ->
        if (enabled) 12.dp else 8.dp
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(cardElevation),
    ) {
        Column(
            modifier = Modifier
                .padding(cardPadding)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Bluetooth Sensor",
                    style = MaterialTheme.typography.titleMedium
                )

                Switch(
                    checked = state.isEnabled,
                    onCheckedChange = { onAction(DashboardAction.ToggleBluetooth) }
                )
            }

            AnimatedVisibility(
                visible = state.isEnabled,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                DeviceList(
                    devices = state.availableDevices,
                    connectedDevice = state.connectedDevice,
                    connectingDevice = state.connectingDevice,
                    onConnect = { onAction(DashboardAction.ConnectToDevice(it)) },
                    onDisconnect = { onAction(DashboardAction.Disconnect) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BluetoothConnectionPanelPreview() {
    MiniFleetTheme {
        BluetoothConnectionPanel(
            state = DashboardState(),
            onAction = {}
        )
    }
}