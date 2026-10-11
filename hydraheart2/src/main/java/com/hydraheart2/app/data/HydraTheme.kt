package com.hydraheart2.app.data

import androidx.compose.ui.graphics.Color

data class HydraTheme(
    val id: String,
    val name: String,
    val emoji: String,
    val bg1: Color,
    val bg2: Color,
    val bg3: Color,
    val accent: Color,
    val accent2: Color,
    val accent3: Color,
    val cardBg: Color,
    val cardBorder: Color,
    val textPrimary: Color,
    val textSoft: Color,
    val textMuted: Color,
    val navBg: Color,
    val glow: Color,
    val hydraLiquid: Color,
    val isLight: Boolean = false,
    val unlockLevel: Int = 0,
    val unlockAchievement: String? = null
)
