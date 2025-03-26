package id.dev.dashboard.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.dev.core.domain.model.LatLngDomain
import id.dev.core.domain.model.TripLog
import id.dev.core.presentation.design_system.MiniFleetTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TripLogItem(
    log: TripLog,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        .format(Date(log.timestamp)),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${log.speed} km/h",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = if (log.speed > 80) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusChip(
                    label = "Engine: ${log.engineStatus}",
                    isActive = log.engineStatus == "On"
                )
                StatusChip(
                    label = "Door: ${log.doorStatus}",
                    isActive = log.doorStatus == "Closed"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TripLogItemPreview() {
    MiniFleetTheme {
        TripLogItem(
            log = TripLog(
                timestamp = System.currentTimeMillis(),
                speed = 70,
                engineStatus = "On",
                doorStatus = "Open",
                location = LatLngDomain(
                    lat = -6.1753924,
                    lon = 106.8271528
                )
            )
        )
    }
}