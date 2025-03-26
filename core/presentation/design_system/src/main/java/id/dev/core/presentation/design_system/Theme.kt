package id.dev.core.presentation.design_system

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF69F0AE),
    onPrimary = Color(0xFF00391D),
    primaryContainer = Color(0xFF00512B),
    onPrimaryContainer = Color(0xFF8BF7C1),

    tertiary = Color(0xFFA5D6A7),
    onTertiary = Color(0xFF0D4E14),
    tertiaryContainer = Color(0xFF1A4C1E),
    onTertiaryContainer = Color(0xFFC1EAC5),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    surface = Color(0xFF191C1A),
    onSurface = Color(0xFFE1E3E0),
    surfaceVariant = Color(0xFF404943),
    onSurfaceVariant = Color(0xFFBFC9C2),

    background = Color(0xFF191C1A),
    onBackground = Color(0xFFE1E3E0),
    outline = Color(0xFF89938B)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006D40),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF002110),

    tertiary = Color(0xFF2E7D32),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFB1F0B5),
    onTertiaryContainer = Color(0xFF002107),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    surface = Color(0xFFFBFDF8),
    onSurface = Color(0xFF191C1A),
    surfaceVariant = Color(0xFFDDE5DB),
    onSurfaceVariant = Color(0xFF404943),

    background = Color(0xFFFBFDF8),
    onBackground = Color(0xFF191C1A),

    outline = Color(0xFF707973)  )

@Composable
fun MiniFleetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}