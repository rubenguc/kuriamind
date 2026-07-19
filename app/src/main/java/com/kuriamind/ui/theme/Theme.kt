package com.kuriamind.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Blue500,
    onPrimary = White,
    primaryContainer = Blue500.copy(alpha = 0.15f),
    onPrimaryContainer = Blue400,
    secondary = Blue500,
    onSecondary = White,
    secondaryContainer = Blue500.copy(alpha = 0.15f),
    onSecondaryContainer = Blue400,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    error = StatusRed,
    onError = White,
)

private val LightColorScheme = lightColorScheme(
    primary = Blue500,
    onPrimary = White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue900,
    secondary = Blue500,
    onSecondary = White,
    secondaryContainer = Blue100,
    onSecondaryContainer = Blue900,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    error = StatusRed,
    onError = White,
)

@Composable
fun KuriamindTheme(
    themeMode: String = "system",
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
