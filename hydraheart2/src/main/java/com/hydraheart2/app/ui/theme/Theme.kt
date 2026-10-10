package com.hydraheart2.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.hydraheart2.app.data.HydraTheme

@Composable
fun HydraHeart2Theme(hydraTheme: HydraTheme, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = hydraTheme.accent,
            onPrimary = hydraTheme.bg1,
            secondary = hydraTheme.accent2,
            tertiary = hydraTheme.accent3,
            background = hydraTheme.bg1,
            onBackground = hydraTheme.textPrimary,
            surface = hydraTheme.bg2,
            onSurface = hydraTheme.textPrimary,
            surfaceVariant = hydraTheme.cardBg,
            onSurfaceVariant = hydraTheme.textSoft,
            outline = hydraTheme.cardBorder
        ),
        typography = HydraTypography,
        content = content
    )
}
