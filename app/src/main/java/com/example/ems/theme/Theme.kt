package com.example.ems.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val PrimaryBlue = Color(0xFF1E88E5)
val SecondaryBlue = Color(0xFF0D47A1)
val AccentTeal = Color(0xFF00897B)
val LightBg = Color(0xFFF8F9FA)
val DarkBg = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD0E4FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary = AccentTeal,
    onSecondary = Color.White,
    background = LightBg,
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    onBackground = Color(0xFF1C1B1F)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF004881),
    onPrimaryContainer = Color(0xFFD0E4FF),
    secondary = Color(0xFF80CBC4),
    onSecondary = Color(0xFF003731),
    background = DarkBg,
    surface = DarkSurface,
    onSurface = Color(0xFFE6E1E5),
    onBackground = Color(0xFFE6E1E5)
)

@Composable
fun EMSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.surface.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
