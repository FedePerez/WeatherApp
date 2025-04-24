package com.example.weatherapp.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val Blue100 = Color(0xFFBBDEFB)
val Blue200 = Color(0xFF90CAF9)
val Blue500 = Color(0xFF2196F3)
val Blue700 = Color(0xFF1976D2)
val Blue900 = Color(0xFF0D47A1)

val Orange100 = Color(0xFFFFE0B2)
val Orange200 = Color(0xFFFFCC80)
val Orange500 = Color(0xFFFF9800)
val Orange700 = Color(0xFFF57C00)
val Orange900 = Color(0xFFE65100)

val Gray100 = Color(0xFFF5F5F5)
val Gray200 = Color(0xFFEEEEEE)
val Gray500 = Color(0xFF9E9E9E)
val Gray700 = Color(0xFF616161)
val Gray800 = Color(0xFF424242)
val Gray900 = Color(0xFF212121)

val White = Color(0xFFFFFFFF)

private val LightColorScheme = lightColorScheme(
    primary = Blue500,
    onPrimary = White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue900,
    secondary = Orange500,
    onSecondary = White,
    secondaryContainer = Orange100,
    onSecondaryContainer = Orange900,
    tertiary = Gray500,
    onTertiary = White,
    tertiaryContainer = Gray100,
    onTertiaryContainer = Gray900,
    background = White,
    onBackground = Gray900,
    surface = White,
    onSurface = Gray900,
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue200,
    onPrimary = Blue900,
    primaryContainer = Blue700,
    onPrimaryContainer = Blue100,
    secondary = Orange200,
    onSecondary = Orange900,
    secondaryContainer = Orange700,
    onSecondaryContainer = Orange100,
    tertiary = Gray200,
    onTertiary = Gray900,
    tertiaryContainer = Gray700,
    onTertiaryContainer = Gray100,
    background = Gray900,
    onBackground = Gray100,
    surface = Gray800,
    onSurface = Gray100,
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = Color.Transparent.toArgb()

            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}