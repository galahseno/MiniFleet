package id.dev.dashboard.presentation.component.bluetooth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.design_system.MiniFleetTheme

@Composable
fun DeviceList(
    devices: List<String>,
    connectedDevice: String?,
    connectingDevice: String?,
    onConnect: (String) -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Available Devices",
                style = MaterialTheme.typography.labelLarge
            )
        }

        LazyColumn(
            modifier = Modifier.heightIn(max = 200.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(devices) { device ->
                DeviceItem(
                    deviceName = device,
                    isConnected = device == connectedDevice,
                    isConnecting = device == connectingDevice,
                    onConnect = { onConnect(it) },
                    onDisconnect = onDisconnect
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceListPreview() {
    MiniFleetTheme {
        DeviceList(
            devices = listOf("Device 1", "Device 2", "Device 3"),
            connectedDevice = "Device 1",
            connectingDevice = null,
            onConnect = {},
            onDisconnect = {}
        )
    }
}
