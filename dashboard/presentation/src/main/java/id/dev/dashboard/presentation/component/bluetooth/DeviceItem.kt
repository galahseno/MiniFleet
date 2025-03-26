package id.dev.dashboard.presentation.component.bluetooth

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.design_system.MiniFleetTheme

@Composable
fun DeviceItem(
    deviceName: String,
    isConnected: Boolean,
    isConnecting: Boolean,
    onConnect: (String) -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val connectionTransition = updateTransition(
        targetState = Triple(isConnected, isConnecting, deviceName),
        label = "connectionState"
    )

    val cardColor by connectionTransition.animateColor(label = "cardColor") { (connected, _, _) ->
        if (connected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant
    }

    val borderWidth by connectionTransition.animateDp(label = "borderWidth") { (connected, _, _) ->
        if (connected) 2.dp else 1.dp
    }

    Card(
        onClick = {
            if (!isConnected && !isConnecting) onConnect(deviceName)
        },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = BorderStroke(borderWidth, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Connection indicator
            when {
                isConnecting -> {
                    val pulse by rememberInfiniteTransition(label = "").animateFloat(
                        initialValue = 0.4f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(800),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulseAnimation"
                    )
                    Icon(
                        imageVector = Icons.Rounded.Bluetooth,
                        contentDescription = "Connecting",
                        modifier = Modifier
                            .size(24.dp)
                            .scale(pulse)
                            .alpha(pulse),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                isConnected -> {
                    IconButton(onClick = onDisconnect) {
                        Icon(
                            imageVector = Icons.Rounded.Link,
                            contentDescription = "Connected",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                else -> {
                    Icon(
                        imageVector = Icons.Rounded.Bluetooth,
                        contentDescription = "Disconnected",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Text(deviceName, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isConnecting -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            isConnected -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.outline
                        }
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceItemPreview() {
    MiniFleetTheme {
        DeviceItem(
            deviceName = "Device Name",
            isConnected = true,
            isConnecting = false,
            onConnect = {},
            onDisconnect = {}
        )
    }
}