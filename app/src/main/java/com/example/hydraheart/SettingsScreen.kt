package com.example.hydraheart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont

private val SettingsAccent = Color(0xFF6E9BFF)
private val SettingsLavender = Color(0xFFEAF0FF)
private val SettingsMuted = Color(0xFFB8C6EE)

private val settingsCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2C3B66).copy(alpha = 0.55f),
        Color(0xFF16213E).copy(alpha = 0.78f),
        Color(0xFF0C1428).copy(alpha = 0.82f)
    )
)

private val settingsCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.50f),
        Color.White.copy(alpha = 0.13f),
        SettingsAccent.copy(alpha = 0.10f)
    )
)

private val iconTileBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2E3D6A).copy(alpha = 0.85f),
        Color(0xFF1A2649).copy(alpha = 0.90f),
        Color(0xFF111B36).copy(alpha = 0.92f)
    )
)

private val iconTileBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.38f),
        Color.White.copy(alpha = 0.10f),
        SettingsAccent.copy(alpha = 0.08f)
    )
)

private data class SettingsRow(
    val emoji: String,
    val label: String,
    val onClick: () -> Unit = {}
)

@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onHowWeFeelClick: () -> Unit = {},
    onPersonalityClick: () -> Unit = {},
    onHydraLookClick: () -> Unit = {},
    onDailyGoalsClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onRemindersClick: () -> Unit = {},
    onThemesClick: () -> Unit = {},
    onUnitsClick: () -> Unit = {},
    onGeneralClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    val profileRows = listOf(
        SettingsRow("👤", "Your Profile", onClick = onProfileClick),
        SettingsRow("💖", "How We Feel", onClick = onHowWeFeelClick)
    )
    val hydraRows = listOf(
        SettingsRow("💬", "Personality", onClick = onPersonalityClick),
        SettingsRow("💕", "Hydra's Look", onClick = onHydraLookClick)
    )
    val trackingRows = listOf(
        SettingsRow("🎯", "Daily Goals", onClick = onDailyGoalsClick),
        SettingsRow("📅", "Calendar", onClick = onCalendarClick),
        SettingsRow("🔔", "Reminders", onClick = onRemindersClick)
    )
    val appRows = listOf(
        SettingsRow("🎨", "Themes", onClick = onThemesClick),
        SettingsRow("📐", "Units", onClick = onUnitsClick),
        SettingsRow("⚙️", "General", onClick = onGeneralClick),
        SettingsRow("ℹ️", "About", onClick = onAboutClick)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F22))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF18203F),
                        Color(0xFF121A33),
                        Color(0xFF0C1228),
                        Color(0xFF070B1C)
                    ),
                    startY = 0f,
                    endY = size.height
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 22.dp)
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            SettingsHeader(onBack = onBack)
            Spacer(modifier = Modifier.height(28.dp))

            SettingsSection(label = "PROFILE", rows = profileRows)
            Spacer(modifier = Modifier.height(26.dp))

            SettingsSection(label = "HYDRA", rows = hydraRows)
            Spacer(modifier = Modifier.height(26.dp))

            SettingsSection(label = "TRACKING", rows = trackingRows)
            Spacer(modifier = Modifier.height(26.dp))

            SettingsSection(label = "APP", rows = appRows)
            Spacer(modifier = Modifier.height(160.dp))
        }
    }
}

@Composable
private fun SettingsHeader(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(36.dp)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(22.dp)) {
                val w = size.width
                val h = size.height
                drawLine(
                    color = SettingsAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = SettingsAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = SettingsAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        BasicText(
            text = "Settings",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = SettingsAccent,
                fontSize = 26.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun SettingsSection(label: String, rows: List<SettingsRow>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BasicText(
            text = label,
            modifier = Modifier.padding(start = 6.dp, bottom = 10.dp),
            style = TextStyle(
                color = SettingsAccent.copy(alpha = 0.72f),
                fontSize = 13.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.0.sp
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(settingsCardBrush)
                .border(
                    brush = settingsCardBorder,
                    width = 0.95.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(vertical = 6.dp)
        ) {
            Column {
                rows.forEach { row ->
                    SettingsRowItem(row)
                }
            }
        }
    }
}

@Composable
private fun SettingsRowItem(row: SettingsRow) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = row.onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconTile(row.emoji)
        Spacer(modifier = Modifier.width(16.dp))
        BasicText(
            text = row.label,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                color = SettingsLavender,
                fontSize = 22.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
        Chevron()
    }
}

@Composable
private fun IconTile(emoji: String) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(iconTileBrush)
            .border(
                brush = iconTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = emoji,
            style = TextStyle(
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun Chevron() {
    Canvas(modifier = Modifier.size(14.dp)) {
        val w = size.width
        val h = size.height
        drawLine(
            color = SettingsMuted.copy(alpha = 0.70f),
            start = Offset(w * 0.30f, h * 0.20f),
            end = Offset(w * 0.66f, h * 0.50f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = SettingsMuted.copy(alpha = 0.70f),
            start = Offset(w * 0.66f, h * 0.50f),
            end = Offset(w * 0.30f, h * 0.80f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}
