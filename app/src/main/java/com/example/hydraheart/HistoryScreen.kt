package com.example.hydraheart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont

private val HistoryBlue = Color(0xFF4E80FF)
private val HistoryPanelBlue = Color(0xFF223F93)
private val HistoryText = Color(0xFFD8E1FF)
private val HistoryMuted = Color(0xFFA6B3DD)
private val HistoryDeep = Color(0xFF09122A)

@Composable
fun HistoryScreen(state: HydraState) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HistoryDeep)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF142044),
                        Color(0xFF101B3C),
                        Color(0xFF0B1530),
                        Color(0xFF071024)
                    ),
                    startY = 0f,
                    endY = size.height
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF315BC5).copy(alpha = 0.16f),
                        Color(0xFF1D377E).copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.54f, size.height * 0.16f),
                    radius = size.width * 0.95f
                ),
                center = Offset(size.width * 0.54f, size.height * 0.16f),
                radius = size.width * 0.95f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 22.dp)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            HistoryHeader()
            Spacer(modifier = Modifier.height(20.dp))
            PeriodTabs()
            Spacer(modifier = Modifier.height(24.dp))
            TodayChartCard(state)
            Spacer(modifier = Modifier.height(16.dp))
            StatGrid(state)
            Spacer(modifier = Modifier.height(20.dp))
            TodayLogsSection(state)
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

@Composable
private fun HistoryHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        HistoryText(
            text = "History",
            color = HistoryBlue,
            fontSize = 25,
            fontWeight = FontWeight.Bold,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(9.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.08f))
        )
    }
}

@Composable
private fun PeriodTabs() {
    val tabs = listOf("D", "W", "M", "Y")
    var selected by remember { mutableStateOf("D") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2A479B).copy(alpha = 0.92f),
                        Color(0xFF1C347A).copy(alpha = 0.96f)
                    )
                )
            )
            .border(
                color = Color(0xFF6E9BFF).copy(alpha = 0.18f),
                width = 1.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(13.dp))
                    .then(
                        if (isSelected) {
                            Modifier.background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF3154B6),
                                        Color(0xFF263F93)
                                    )
                                )
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickable { selected = tab },
                contentAlignment = Alignment.Center
            ) {
                HistoryText(
                    text = tab,
                    color = if (isSelected) HistoryBlue else HistoryMuted.copy(alpha = 0.72f),
                    fontSize = 15,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun TodayChartCard(state: HydraState) {
    HistoryGlassCard(
        height = 390.dp,
        corner = 24.dp,
        padding = 18.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            ChevronButton(
                pointsLeft = true,
                modifier = Modifier.align(Alignment.TopStart)
            )
            HistoryText(
                text = "Today",
                color = Color.White,
                fontSize = 18,
                fontWeight = FontWeight.Bold,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            ChevronButton(
                pointsLeft = false,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
        HistoryText(
            text = "Total today",
            color = HistoryMuted,
            fontSize = 15,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            HistoryText(
                text = state.todayWaterMl.toInt().toString(),
                color = HistoryBlue,
                fontSize = 38,
                fontWeight = FontWeight.Bold,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.width(8.dp))
            HistoryText(
                text = "ml",
                color = HistoryBlue.copy(alpha = 0.88f),
                fontSize = 28,
                fontWeight = FontWeight.Bold,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        ChartEmptyArea()

        Spacer(modifier = Modifier.height(7.dp))
        HistoryText(
            text = if (state.todayLogs.isEmpty()) {
                "Log your first drink today to start your chart."
            } else {
                "${state.todayLogs.size} drink${if (state.todayLogs.size == 1) "" else "s"} logged today."
            },
            color = HistoryMuted,
            fontSize = 15,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ChartEmptyArea() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val dashY = size.height * 0.34f
            drawLine(
                color = Color(0xFF6E9BFF).copy(alpha = 0.18f),
                start = Offset(2.dp.toPx(), dashY),
                end = Offset(size.width - 2.dp.toPx(), dashY),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(8.dp.toPx(), 7.dp.toPx()),
                    0f
                )
            )

            val lowY = size.height * 0.85f
            val tickWidth = 10.dp.toPx()
            val gap = 9.dp.toPx()
            var x = size.width * 0.08f
            while (x < size.width * 0.92f) {
                drawRoundRect(
                    color = Color(0xFF233B83).copy(alpha = 0.16f),
                    topLeft = Offset(x, lowY),
                    size = Size(tickWidth, 6.dp.toPx()),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )
                x += tickWidth + gap
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("12am", "6am", "12pm", "6pm", "Now").forEach { label ->
                HistoryText(
                    text = label,
                    color = HistoryMuted.copy(alpha = 0.78f),
                    fontSize = 11,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun ChevronButton(pointsLeft: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        HistoryPanelBlue.copy(alpha = if (pointsLeft) 0.62f else 0.20f),
                        Color(0xFF172A63).copy(alpha = if (pointsLeft) 0.72f else 0.22f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color(0xFF6E9BFF).copy(alpha = if (pointsLeft) 0.30f else 0.08f),
                shape = RoundedCornerShape(15.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(14.dp)) {
            val w = size.width
            val h = size.height
            val stroke = 2.4.dp.toPx()
            val color = HistoryBlue.copy(alpha = if (pointsLeft) 0.92f else 0.20f)
            if (pointsLeft) {
                drawLine(color, Offset(w * 0.64f, h * 0.18f), Offset(w * 0.36f, h * 0.50f), stroke, cap = StrokeCap.Round)
                drawLine(color, Offset(w * 0.36f, h * 0.50f), Offset(w * 0.64f, h * 0.82f), stroke, cap = StrokeCap.Round)
            } else {
                drawLine(color, Offset(w * 0.36f, h * 0.18f), Offset(w * 0.64f, h * 0.50f), stroke, cap = StrokeCap.Round)
                drawLine(color, Offset(w * 0.64f, h * 0.50f), Offset(w * 0.36f, h * 0.82f), stroke, cap = StrokeCap.Round)
            }
        }
    }
}

@Composable
private fun StatGrid(state: HydraState) {
    val totalMl = state.logs.sumOf { it.amountMl.toDouble() }.toInt()
    val averageMl = if (state.logs.isEmpty()) 0 else totalMl / state.logs.size
    val mostLogged = state.logs.groupingBy { it.drinkName }.eachCount().maxByOrNull { it.value }?.key
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard("Total", "", "$totalMl ml", Modifier.weight(1f), useWaterIcon = true)
            StatCard("Avg. Size", "", if (averageMl == 0) "—" else "$averageMl ml", Modifier.weight(1f), useGlassIcon = true)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard("Most Logged", "", mostLogged ?: "—", Modifier.weight(1f), useCrownIcon = true)
            StatCard("Streak", "", "${state.currentStreak}d", Modifier.weight(1f), useStreakIcon = true)
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    emoji: String,
    value: String,
    modifier: Modifier = Modifier,
    useWaterIcon: Boolean = false,
    useStreakIcon: Boolean = false,
    useCrownIcon: Boolean = false,
    useGlassIcon: Boolean = false
) {
    Box(
        modifier = modifier
            .height(96.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2C3E6C).copy(alpha = 0.78f),
                        Color(0xFF172752).copy(alpha = 0.86f),
                        Color(0xFF111C3F).copy(alpha = 0.90f)
                    )
                )
            )
            .border(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.34f),
                        Color.White.copy(alpha = 0.10f),
                        Color(0xFF6E9BFF).copy(alpha = 0.08f)
                    )
                ),
                width = 1.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(15.dp)
    ) {
        HistoryText(
            text = label,
            color = HistoryMuted,
            fontSize = 14,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.TopStart)
        )
        if (useWaterIcon) {
            WaterDropBadge(modifier = Modifier.align(Alignment.TopEnd))
        } else if (useStreakIcon) {
            StreakIconBadge(modifier = Modifier.align(Alignment.TopEnd))
        } else if (useCrownIcon) {
            CrownBadge(modifier = Modifier.align(Alignment.TopEnd))
        } else if (useGlassIcon) {
            GlassBadge(modifier = Modifier.align(Alignment.TopEnd))
        } else {
            EmojiBadge(
                emoji = emoji,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        HistoryText(
            text = value,
            color = Color.White,
            fontSize = 18,
            fontWeight = FontWeight.Bold,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
private fun GlassBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3154B6).copy(alpha = 0.56f),
                        Color(0xFF1A316F).copy(alpha = 0.72f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        GlassIcon(modifier = Modifier.size(26.dp))
    }
}

@Composable
private fun GlassIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val left = w * 0.24f
        val right = w * 0.76f
        val top = h * 0.18f
        val bottom = h * 0.86f
        val waterY = h * 0.50f
        val cornerR = w * 0.07f

        val emptyTop = Path().apply {
            moveTo(left, top)
            lineTo(right, top)
            lineTo(right, waterY)
            lineTo(left, waterY)
            close()
        }
        drawPath(
            path = emptyTop,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFBDDEFF),
                    Color(0xFF8FB6E6)
                ),
                startY = top,
                endY = waterY
            )
        )

        val water = Path().apply {
            moveTo(left, waterY)
            lineTo(left, bottom - cornerR)
            cubicTo(left, bottom, left + cornerR * 0.5f, bottom, left + cornerR, bottom)
            lineTo(right - cornerR, bottom)
            cubicTo(right - cornerR * 0.5f, bottom, right, bottom, right, bottom - cornerR)
            lineTo(right, waterY)
            close()
        }
        drawPath(
            path = water,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFB6E8FF),
                    Color(0xFF4FA5F5),
                    Color(0xFF1956C4)
                ),
                center = Offset(left + (right - left) * 0.32f, waterY + (bottom - waterY) * 0.30f),
                radius = (bottom - waterY) * 1.25f
            )
        )

        drawLine(
            color = Color(0xFFEAFBFF).copy(alpha = 0.80f),
            start = Offset(left, waterY),
            end = Offset(right, waterY),
            strokeWidth = 0.8.dp.toPx()
        )

        val outline = Path().apply {
            moveTo(left, top)
            lineTo(left, bottom - cornerR)
            cubicTo(left, bottom, left + cornerR * 0.5f, bottom, left + cornerR, bottom)
            lineTo(right - cornerR, bottom)
            cubicTo(right - cornerR * 0.5f, bottom, right, bottom, right, bottom - cornerR)
            lineTo(right, top)
        }
        drawPath(
            path = outline,
            color = Color(0xFFEAFBFF),
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )

        val shine = Path().apply {
            moveTo(left + w * 0.04f, top + h * 0.10f)
            cubicTo(
                left + w * 0.005f, top + h * 0.32f,
                left + w * 0.005f, top + h * 0.52f,
                left + w * 0.04f, bottom - h * 0.12f
            )
        }
        drawPath(
            path = shine,
            color = Color.White.copy(alpha = 0.55f),
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun CrownBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3154B6).copy(alpha = 0.56f),
                        Color(0xFF1A316F).copy(alpha = 0.72f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        CrownIcon(modifier = Modifier.size(26.dp))
    }
}

@Composable
private fun CrownIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val crown = Path().apply {
            moveTo(w * 0.15f, h * 0.68f)
            lineTo(w * 0.10f, h * 0.42f)
            cubicTo(w * 0.22f, h * 0.56f, w * 0.34f, h * 0.62f, w * 0.40f, h * 0.43f)
            lineTo(w * 0.50f, h * 0.16f)
            lineTo(w * 0.60f, h * 0.43f)
            cubicTo(w * 0.66f, h * 0.62f, w * 0.78f, h * 0.56f, w * 0.90f, h * 0.42f)
            lineTo(w * 0.85f, h * 0.68f)
            cubicTo(w * 0.66f, h * 0.64f, w * 0.34f, h * 0.64f, w * 0.15f, h * 0.68f)
            close()
        }
        val goldFill = Brush.linearGradient(
            colors = listOf(
                Color(0xFFEDB153),
                Color(0xFFC47729),
                Color(0xFF81501C)
            ),
            start = Offset(w * 0.27f, h * 0.18f),
            end = Offset(w * 0.74f, h * 0.82f)
        )
        val baseBrush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFE09D3C),
                Color(0xFFB06B24),
                Color(0xFF7A4918)
            )
        )
        drawPath(
            path = crown,
            brush = goldFill
        )
        drawPath(
            path = crown,
            color = Color(0xFFFFD992).copy(alpha = 0.22f),
            style = Stroke(width = 0.5.dp.toPx())
        )
        listOf(
            Offset(w * 0.10f, h * 0.40f) to w * 0.065f,
            Offset(w * 0.50f, h * 0.13f) to w * 0.075f,
            Offset(w * 0.90f, h * 0.40f) to w * 0.065f
        ).forEach { (center, radius) ->
            drawCircle(
                brush = goldFill,
                radius = radius,
                center = center
            )
            drawCircle(
                color = Color(0xFFFFD992).copy(alpha = 0.16f),
                radius = radius,
                center = center,
                style = Stroke(width = 0.36.dp.toPx())
            )
        }
        drawRoundRect(
            brush = baseBrush,
            topLeft = Offset(w * 0.16f, h * 0.65f),
            size = Size(w * 0.68f, h * 0.22f),
            cornerRadius = CornerRadius(w * 0.05f, w * 0.05f)
        )
    }
}

@Composable
private fun WaterDropBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3154B6).copy(alpha = 0.56f),
                        Color(0xFF1A316F).copy(alpha = 0.72f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        WaterDropIcon(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun WaterDropIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val drop = Path().apply {
            moveTo(w * 0.50f, h * 0.04f)
            cubicTo(w * 0.66f, h * 0.27f, w * 0.77f, h * 0.45f, w * 0.79f, h * 0.63f)
            cubicTo(w * 0.82f, h * 0.86f, w * 0.67f, h * 0.98f, w * 0.50f, h * 0.98f)
            cubicTo(w * 0.33f, h * 0.98f, w * 0.18f, h * 0.86f, w * 0.21f, h * 0.63f)
            cubicTo(w * 0.23f, h * 0.45f, w * 0.34f, h * 0.27f, w * 0.50f, h * 0.04f)
            close()
        }
        drawPath(
            path = drop,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE9FBFF),
                    Color(0xFF72DDFF),
                    Color(0xFF2F86FF),
                    Color(0xFF1956C4)
                ),
                center = Offset(w * 0.36f, h * 0.26f),
                radius = w * 0.76f
            )
        )
        drawPath(
            path = drop,
            color = Color.White.copy(alpha = 0.38f),
            style = Stroke(width = 0.75.dp.toPx())
        )
        val shine = Path().apply {
            moveTo(w * 0.39f, h * 0.25f)
            cubicTo(w * 0.32f, h * 0.39f, w * 0.30f, h * 0.52f, w * 0.34f, h * 0.63f)
        }
        drawPath(
            path = shine,
            color = Color.White.copy(alpha = 0.62f),
            style = Stroke(width = 1.25.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun StreakIconBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3154B6).copy(alpha = 0.56f),
                        Color(0xFF1A316F).copy(alpha = 0.72f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.streak_flame),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
private fun EmojiBadge(emoji: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3154B6).copy(alpha = 0.56f),
                        Color(0xFF1A316F).copy(alpha = 0.72f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicText(text = emoji, style = TextStyle(fontSize = 18.sp))
    }
}

@Composable
private fun TodayLogsSection(state: HydraState) {
    val visibleLogs = state.todayLogs.take(3)
    Column(modifier = Modifier.fillMaxWidth()) {
        HistoryText(
            text = "Today's Logs",
            color = Color.White,
            fontSize = 18,
            fontWeight = FontWeight.Bold,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(14.dp))
        HistoryGlassCard(
            height = (124 + visibleLogs.size * 42).dp,
            corner = 18.dp,
            padding = 16.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (visibleLogs.isEmpty()) {
                    WaterDropIcon(modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    HistoryText(
                        text = "Nothing logged yet today",
                        color = HistoryText,
                        fontSize = 14,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    HistoryText(
                        text = "Head to Log to track your first drink",
                        color = HistoryMuted.copy(alpha = 0.80f),
                        fontSize = 12,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    visibleLogs.forEach { log ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HistoryText(
                                text = "${log.emoji} ${log.drinkName}",
                                color = HistoryText,
                                fontSize = 14,
                                fontFamily = AppFont,
                                fontStyle = FontStyle.Italic
                            )
                            HistoryText(
                                text = "${log.amountMl.toInt()} ml",
                                color = HistoryBlue,
                                fontSize = 13,
                                fontFamily = AppFont,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryGlassCard(
    height: Dp,
    corner: Dp,
    padding: Dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(corner))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2C3E6C).copy(alpha = 0.78f),
                        Color(0xFF182953).copy(alpha = 0.84f),
                        Color(0xFF101B3A).copy(alpha = 0.90f)
                    )
                )
            )
            .border(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.52f),
                        Color.White.copy(alpha = 0.14f),
                        Color(0xFF6E9BFF).copy(alpha = 0.08f)
                    )
                ),
                width = 1.dp,
                shape = RoundedCornerShape(corner)
            )
            .padding(padding),
        content = content
    )
}

@Composable
private fun HistoryText(
    text: String,
    color: Color,
    fontSize: Int,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = FontFamily.Default,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign = TextAlign.Start
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            color = color,
            fontSize = fontSize.sp,
            fontFamily = fontFamily,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            textAlign = textAlign
        )
    )
}
