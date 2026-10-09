package com.example.hydraheart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont

private val HwfAccent = Color(0xFF6E9BFF)
private val HwfLavender = Color(0xFFEAF0FF)
private val HwfMuted = Color(0xFFB8C6EE)

private val hwfCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2C3B66).copy(alpha = 0.55f),
        Color(0xFF16213E).copy(alpha = 0.78f),
        Color(0xFF0C1428).copy(alpha = 0.82f)
    )
)

private val hwfCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.50f),
        Color.White.copy(alpha = 0.13f),
        HwfAccent.copy(alpha = 0.10f)
    )
)

private val hwfTileBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2E3D6A).copy(alpha = 0.85f),
        Color(0xFF1A2649).copy(alpha = 0.90f),
        Color(0xFF111B36).copy(alpha = 0.92f)
    )
)

private val hwfTileBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.38f),
        Color.White.copy(alpha = 0.10f),
        HwfAccent.copy(alpha = 0.08f)
    )
)

private data class HwfRow(val emoji: String, val label: String, val onClick: () -> Unit = {})

@Composable
fun HowWeFeelScreen(onBack: () -> Unit = {}) {
    val rows = listOf(
        HwfRow("🌸", "Log Today's Mood"),
        HwfRow("📅", "Emotional History")
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
                .padding(horizontal = 22.dp)
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            HwfHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(28.dp))
            BasicText(
                text = "How We Feel",
                style = TextStyle(
                    color = HwfAccent,
                    fontSize = 30.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Track your emotional wellness",
                style = TextStyle(
                    color = HwfLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(hwfCardBrush)
                    .border(
                        brush = hwfCardBorder,
                        width = 0.95.dp,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 6.dp)
            ) {
                Column {
                    rows.forEach { HwfRowItem(it) }
                }
            }
        }
    }
}

@Composable
private fun HwfHeader(onBack: () -> Unit) {
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
                    color = HwfAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = HwfAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = HwfAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        BasicText(
            text = "How We Feel",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = HwfAccent,
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
private fun HwfRowItem(row: HwfRow) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = row.onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HwfIconTile(row.emoji)
        Spacer(modifier = Modifier.width(16.dp))
        BasicText(
            text = row.label,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                color = HwfLavender,
                fontSize = 22.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
        HwfChevron()
    }
}

@Composable
private fun HwfIconTile(emoji: String) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(hwfTileBrush)
            .border(
                brush = hwfTileBorder,
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
private fun HwfChevron() {
    Canvas(modifier = Modifier.size(14.dp)) {
        val w = size.width
        val h = size.height
        drawLine(
            color = HwfMuted.copy(alpha = 0.70f),
            start = Offset(w * 0.30f, h * 0.20f),
            end = Offset(w * 0.66f, h * 0.50f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = HwfMuted.copy(alpha = 0.70f),
            start = Offset(w * 0.66f, h * 0.50f),
            end = Offset(w * 0.30f, h * 0.80f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}
