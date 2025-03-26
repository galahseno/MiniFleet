package id.dev.core.presentation.design_system.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.design_system.MiniFleetTheme

@Composable
fun SensorCard(
    title: String,
    value: String,
    unit: String = "",
    imageVector: ImageVector,
    statusColor: Color = MaterialTheme.colorScheme.primary
) {
    val animatedColor by animateColorAsState(
        targetValue = statusColor,
        animationSpec = tween(durationMillis = 300),
        label = "color_animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = title,
                tint = animatedColor,
                modifier = Modifier
                    .size(40.dp)
                    .animateContentSize()
            )

            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Crossfade(
                    targetState = value,
                    animationSpec = tween(300),
                    label = "sensor_card_value"
                ) { currentValue ->
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = currentValue,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        if (unit.isNotEmpty()) {
                            Text(
                                text = unit,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(animatedColor)
                    .animateContentSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SensorCardPreview() {
    MiniFleetTheme {
        SensorCard(
            title = "Engine On",
            value = "Yes",
            imageVector = Icons.Rounded.Speed,
            statusColor = MaterialTheme.colorScheme.primary
        )
    }
}