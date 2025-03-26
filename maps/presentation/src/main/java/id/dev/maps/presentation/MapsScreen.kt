package id.dev.maps.presentation

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import id.dev.core.presentation.design_system.MiniFleetTheme
import id.dev.core.presentation.design_system.component.AlertBanner
import id.dev.core.presentation.tracking.TrackingAction
import id.dev.core.presentation.tracking.TrackingEvent
import id.dev.core.presentation.tracking.TrackingState
import id.dev.core.presentation.tracking.TrackingViewModel
import id.dev.core.presentation.ui.ObserveAsEvents
import id.dev.maps.presentation.util.hasNotificationPermission
import id.dev.maps.presentation.util.shouldShowNotificationRationale

@Composable
fun MapsScreenRoot(
    onBackClick: () -> Unit,
    viewModel: TrackingViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            is TrackingEvent.Error -> {
                Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    MapsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is TrackingAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun MapsScreen(
    state: TrackingState,
    onAction: (TrackingAction) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isDarkMode = remember(configuration) {
        configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            onAction(
                if (state.isTracking) TrackingAction.OnStopClick
                else TrackingAction.OnStartClick
            )
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-2.2436434, 113.096937), 3.4f)
    }
    val mapProperties = remember(isDarkMode) {
        MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                context,
                if (isDarkMode) R.raw.maps_dark_style else R.raw.maps_light_style
            ),
            isBuildingEnabled = true
        )
    }

    LaunchedEffect(state.vehicleDomain) {
        state.vehicleDomain?.position?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(LatLng(it.lat, it.lon), 18f)
                ),
                durationMs = 1000,
            )
        } ?: cameraPositionState.animate(
            update = CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(
                    LatLng(
                        -2.2436434,
                        113.096937
                    ), 3.4f
                )
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
        ) {
            state.vehicleDomain?.position?.let { position ->
                Marker(
                    state = MarkerState(position = LatLng(position.lat, position.lon)),
                    icon = bitmapDescriptorFromVector(
                        context, R.drawable.ic_vehicle
                    )
                )
            }
        }

        IconButton(
            onClick = {
                onAction(TrackingAction.OnBackClick)
            },
            modifier = Modifier
                .systemBarsPadding()
                .padding(vertical = 16.dp, horizontal = 24.dp)
                .size(30.dp)
                .clip(RoundedCornerShape(30))
                .align(Alignment.TopStart)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                contentDescription = ""
            )
        }

        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when {
                        context.hasNotificationPermission() -> {
                            onAction(
                                if (state.isTracking) TrackingAction.OnStopClick
                                else TrackingAction.OnStartClick
                            )
                        }

                        context.shouldShowNotificationRationale() -> {
                            onAction(TrackingAction.OnShowRationaleAlert)
                        }

                        else -> {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                } else {
                    onAction(
                        if (state.isTracking) TrackingAction.OnStopClick
                        else TrackingAction.OnStartClick
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            Text(text = if (state.isTracking) "Stop" else "Start")
        }

        AlertBanner(
            isAlertVisible = state.isAlertVisible,
            alertMessage = state.alertMessage?.asString(context) ?: "",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            onDismiss = { onAction(TrackingAction.OnDismissAlertBanner) }
        )
    }
}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

@Preview(showBackground = true)
@Composable
private fun MapsScreenPreview() {
    MiniFleetTheme {
        MapsScreen(
            state = TrackingState(),
            onAction = {}
        )
    }
}