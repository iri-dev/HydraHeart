package com.hydraheart2.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.R
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

data class InsightStrings(
    val empty: String,
    val streak: String,
    val goal: String,
    val almost: String,
    val evening: String,
    val logs: String,
    val days: String,
    val default: String,
    val mornings: String,
    val afternoons: String,
    val evenings: String
)

private fun historyInsight(viewModel: HydraViewModel, s: InsightStrings): String {
    val todayDrinks = viewModel.todayDrinks
    val pct = viewModel.waterPercent
    val streak = viewModel.streak
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    if (todayDrinks.isEmpty()) return s.empty
    val peakHour = todayDrinks
        .groupBy { Calendar.getInstance().apply { timeInMillis = it.timestamp }.get(Calendar.HOUR_OF_DAY) }
        .maxByOrNull { it.value.size }?.key ?: hour
    val peakPeriod = if (peakHour < 12) s.mornings else if (peakHour < 17) s.afternoons else s.evenings
    return when {
        pct >= 100f && streak >= 7 -> String.format(s.streak, streak, peakPeriod)
        pct >= 100f -> String.format(s.goal, peakPeriod)
        pct >= 75f -> String.format(s.almost, peakPeriod)
        hour >= 20 && pct < 50f -> String.format(s.evening, peakPeriod)
        todayDrinks.size >= 6 -> String.format(s.logs, todayDrinks.size, peakPeriod)
        streak >= 3 -> String.format(s.days, streak, peakPeriod)
        else -> String.format(s.default, peakPeriod)
    }
}

@Composable
fun HistoryScreen(viewModel: HydraViewModel, onNavigate: (String) -> Unit = {}) {
    val theme = viewModel.currentTheme
    val ctx = LocalContext.current
    var period by remember { mutableStateOf("day") }
    val locale = Locale.getDefault()
    val timeFmt = remember(locale) { SimpleDateFormat("HH:mm", locale) }
    var selectedOffset by remember { mutableIntStateOf(0) }
    LaunchedEffect(period) { selectedOffset = 0 }
    val isCurrentPeriod = selectedOffset >= 0
    val todayLabel = stringResource(R.string.history_period_today)
    val yesterdayLabel = stringResource(R.string.history_period_yesterday)
    val total = viewModel.effectiveWater
    val avgSize = if (viewModel.todayDrinks.isNotEmpty()) {
        viewModel.todayDrinks.sumOf { it.drink.sizeMl } / viewModel.todayDrinks.size
    } else {
        0
    }
    val mostLogged = viewModel.todayDrinks.groupBy { it.drink.name }.maxByOrNull { it.value.size }?.key ?: "—"
    val goalHit = isCurrentPeriod && viewModel.waterPercent >= 100f
    val goalTarget = viewModel.goals.waterMl.toFloat()
    val periodLabel = remember(period, selectedOffset, todayLabel, yesterdayLabel, locale) {
        val cal = Calendar.getInstance()
        when (period) {
            "day" -> when (selectedOffset) {
                0 -> todayLabel
                -1 -> yesterdayLabel
                else -> {
                    cal.add(Calendar.DAY_OF_YEAR, selectedOffset)
                    SimpleDateFormat("d MMM yyyy", locale).format(cal.time)
                }
            }
            "week" -> {
                cal.add(Calendar.DAY_OF_YEAR, -((cal.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7))
                cal.add(Calendar.WEEK_OF_YEAR, selectedOffset)
                val fmt = SimpleDateFormat("d MMM", locale)
                val start = fmt.format(cal.time)
                cal.add(Calendar.DAY_OF_YEAR, 6)
                "$start – ${fmt.format(cal.time)}"
            }
            "month" -> {
                cal.add(Calendar.MONTH, selectedOffset)
                SimpleDateFormat("MMMM yyyy", locale).format(cal.time)
            }
            else -> {
                cal.add(Calendar.YEAR, selectedOffset)
                SimpleDateFormat("yyyy", locale).format(cal.time)
            }
        }
    }
    val hourlyData = remember(viewModel.todayDrinks.size) {
        FloatArray(24).apply {
            viewModel.todayDrinks.forEach { l ->
                val h = Calendar.getInstance().apply { timeInMillis = l.timestamp }.get(Calendar.HOUR_OF_DAY)
                this[h] += l.drink.waterMl
            }
        }
    }
    val nowHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val todayDow = remember { (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7 }
    val todayDom = remember { Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1 }
    val todayMonth = remember { Calendar.getInstance().get(Calendar.MONTH) }
    var graphAnimTarget by remember(period, selectedOffset) { mutableFloatStateOf(0f) }
    LaunchedEffect(period, selectedOffset) { graphAnimTarget = 1f }
    val graphProgress by animateFloatAsState(
        graphAnimTarget,
        tween(900, easing = FastOutSlowInEasing),
        label = "graphProgress"
    )
    var loaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { loaded = true }
    val entryOffset by animateFloatAsState(
        if (loaded) 0f else 28f,
        tween(380, easing = FastOutSlowInEasing),
        label = "entry"
    )
    val insightStrings = InsightStrings(
        stringResource(R.string.history_insight_empty),
        stringResource(R.string.history_insight_streak),
        stringResource(R.string.history_insight_goal),
        stringResource(R.string.history_insight_almost),
        stringResource(R.string.history_insight_evening),
        stringResource(R.string.history_insight_logs),
        stringResource(R.string.history_insight_days),
        stringResource(R.string.history_insight_default),
        stringResource(R.string.history_time_mornings),
        stringResource(R.string.history_time_afternoons),
        stringResource(R.string.history_time_evenings)
    )
    val pastNoData = stringResource(R.string.history_past_no_data)
    val insight = if (isCurrentPeriod) historyInsight(viewModel, insightStrings) else pastNoData

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(bottom = 100.dp)
                .graphicsLayer { translationY = entryOffset }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.history_title),
                    modifier = Modifier.weight(1f),
                    color = theme.accent.copy(alpha = if (theme.isLight) 0.82f else 0.92f),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                color = theme.cardBorder.copy(alpha = 0.22f)
            )

            // Period tabs
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(theme.cardBg.copy(alpha = 0.3f))
                    .border(0.75.dp, theme.cardBorder.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf(
                        stringResource(R.string.history_tab_day) to "day",
                        stringResource(R.string.history_tab_week) to "week",
                        stringResource(R.string.history_tab_month) to "month",
                        stringResource(R.string.history_tab_year) to "year"
                    ).forEach { (label, value) ->
                        val selected = period == value
                        val pillAlpha by animateFloatAsState(if (selected) 1f else 0f, tween(220), label = "pill$label")
                        val tabScale by animateFloatAsState(
                            if (selected) 1.02f else 1f,
                            spring(dampingRatio = 0.5f, stiffness = 1500f),
                            label = "tabSc$label"
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .graphicsLayer {
                                    scaleX = tabScale
                                    scaleY = tabScale
                                }
                                .clip(RoundedCornerShape(10.dp))
                                .background(theme.accent.copy(alpha = pillAlpha * 0.16f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    HapticEngine.tick(ctx)
                                    period = value
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                label,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (selected) theme.accent else theme.textMuted,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            FrostedGlass(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                theme = theme,
                cornerRadius = 20.dp,
                padding = PaddingValues(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavArrow(symbol = "‹", theme = theme, enabled = true, onClick = {
                        HapticEngine.tick(ctx)
                        selectedOffset -= 1
                    })
                    AnimatedContent(
                        targetState = periodLabel,
                        transitionSpec = {
                            (fadeIn(tween(200)) + slideInHorizontally { -it / 4 }) togetherWith
                                (fadeOut(tween(150)) + slideOutHorizontally { it / 4 })
                        },
                        label = "periodLabel"
                    ) { label ->
                        Text(
                            label,
                            modifier = Modifier.weight(1f),
                            color = theme.textPrimary,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                    NavArrow(symbol = "›", theme = theme, enabled = !isCurrentPeriod, onClick = {
                        HapticEngine.tick(ctx)
                        selectedOffset += 1
                    })
                }
                Spacer(Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Column {
                        Text(
                            if (!isCurrentPeriod) {
                                stringResource(R.string.history_no_data)
                            } else if (period == "day") {
                                stringResource(R.string.history_total_today)
                            } else {
                                stringResource(R.string.history_daily_avg)
                            },
                            color = theme.textSoft,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.height(2.dp))
                        AnimatedContent(
                            targetState = if (!isCurrentPeriod) {
                                "—"
                            } else if (total >= 1000) {
                                String.format(Locale.UK, "%.1f", total / 1000f) + " L"
                            } else {
                                "$total ml"
                            },
                            transitionSpec = {
                                (fadeIn(tween(300)) + slideInVertically { -it / 3 }) togetherWith
                                    (fadeOut(tween(200)) + slideOutVertically { it / 3 })
                            },
                            label = "valueAnim"
                        ) { v ->
                            Text(
                                v,
                                color = theme.accent,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.displaySmall.copy(fontSize = 32.sp)
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    if (goalHit) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 6.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(theme.accent.copy(alpha = 0.14f))
                                .border(0.75.dp, theme.accent.copy(alpha = 0.28f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                stringResource(R.string.history_goal_badge),
                                color = theme.accent,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))
                AnimatedContent(
                    targetState = period,
                    transitionSpec = { fadeIn(tween(280)) togetherWith fadeOut(tween(200)) },
                    label = "chartSwitch"
                ) { p ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(168.dp)
                    ) {
                        Canvas(Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val pL = 4f
                            val pR = 4f
                            val pT = 8f
                            val pB = 26f
                            val cw = w - pL - pR
                            val ch = h - pT - pB
                            val barData: FloatArray
                            val barCount: Int
                            val highlightIdx: Int
                            val goalLineVal: Float
                            when (p) {
                                "day" -> {
                                    barData = if (isCurrentPeriod) hourlyData else FloatArray(24)
                                    barCount = 24
                                    highlightIdx = if (isCurrentPeriod) nowHour else -1
                                    goalLineVal = goalTarget
                                }
                                "week" -> {
                                    barData = if (isCurrentPeriod) {
                                        FloatArray(7) { if (it == todayDow) total.toFloat() else 0f }
                                    } else {
                                        FloatArray(7)
                                    }
                                    barCount = 7
                                    highlightIdx = if (isCurrentPeriod) todayDow else -1
                                    goalLineVal = goalTarget
                                }
                                "month" -> {
                                    val days = Calendar.getInstance()
                                        .apply { add(Calendar.MONTH, selectedOffset) }
                                        .getActualMaximum(Calendar.DAY_OF_MONTH)
                                    barData = if (isCurrentPeriod) {
                                        FloatArray(days) { if (it == todayDom) total.toFloat() else 0f }
                                    } else {
                                        FloatArray(days)
                                    }
                                    barCount = days
                                    highlightIdx = if (isCurrentPeriod) todayDom else -1
                                    goalLineVal = goalTarget
                                }
                                else -> {
                                    barData = if (isCurrentPeriod) {
                                        FloatArray(12) { if (it == todayMonth) total.toFloat() else 0f }
                                    } else {
                                        FloatArray(12)
                                    }
                                    barCount = 12
                                    highlightIdx = if (isCurrentPeriod) todayMonth else -1
                                    goalLineVal = 30f * goalTarget
                                }
                            }
                            val maxDataVal = barData.maxOrNull()?.coerceAtLeast(1f) ?: 1f
                            val maxV = max(maxDataVal, goalLineVal.coerceAtLeast(1f)) * 1.15f

                            // Grid lines
                            for (i in 0 until 3) {
                                val y = pT + ch - i * ch / 2f
                                drawLine(
                                    theme.cardBorder.copy(alpha = 0.14f),
                                    Offset(pL, y),
                                    Offset(w - pR, y),
                                    strokeWidth = 1f
                                )
                            }
                            // Goal line
                            val goalY = pT + ch - (goalLineVal / maxV) * ch
                            drawLine(
                                theme.accent.copy(alpha = 0.38f),
                                Offset(pL, goalY),
                                Offset(w - pR, goalY),
                                strokeWidth = 1.5f,
                                cap = StrokeCap.Round,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))
                            )
                            // Bars
                            val spacing = cw / barCount
                            val bw = (if (barCount > 20) 0.52f else 0.62f) * spacing
                            for (i in 0 until barCount) {
                                val cx = pL + (i + 0.5f) * spacing
                                val rawVal = barData[i]
                                val animVal = rawVal * graphProgress
                                val isHighlight = i == highlightIdx
                                val stubH = ch * 0.028f + 1f
                                val stubCol = if (isHighlight) {
                                    theme.accent.copy(alpha = 0.12f)
                                } else {
                                    theme.cardBorder.copy(alpha = 0.15f)
                                }
                                drawRoundRect(
                                    stubCol,
                                    topLeft = Offset(cx - bw / 2f, pT + ch - stubH),
                                    size = Size(bw, stubH),
                                    cornerRadius = CornerRadius(2f)
                                )
                                if (animVal > 2f) {
                                    val bh = (animVal / maxV) * ch
                                    val barTop = pT + ch - bh
                                    val cornerR = min(bw / 2f, 7f)
                                    if (isHighlight) {
                                        drawRoundRect(
                                            theme.accent.copy(alpha = 0.12f),
                                            topLeft = Offset(cx - bw / 2f - 3f, barTop - 2f),
                                            size = Size(bw + 6f, bh + 4f),
                                            cornerRadius = CornerRadius(cornerR + 3f)
                                        )
                                    }
                                    drawRoundRect(
                                        brush = Brush.verticalGradient(
                                            if (isHighlight) {
                                                listOf(theme.accent, theme.accent.copy(alpha = 0.52f))
                                            } else {
                                                listOf(theme.accent.copy(alpha = 0.68f), theme.accent.copy(alpha = 0.28f))
                                            },
                                            startY = barTop,
                                            endY = pT + ch
                                        ),
                                        topLeft = Offset(cx - bw / 2f, barTop),
                                        size = Size(bw, bh),
                                        cornerRadius = CornerRadius(cornerR)
                                    )
                                    if (isHighlight && bh > 6f) {
                                        drawRoundRect(
                                            brush = Brush.verticalGradient(
                                                listOf(Color.White.copy(alpha = 0.22f), Color.Transparent),
                                                startY = barTop,
                                                endY = barTop + min(bh * 0.4f, 12f)
                                            ),
                                            topLeft = Offset(cx - bw / 2f, barTop),
                                            size = Size(bw, min(bh * 0.4f, 12f)),
                                            cornerRadius = CornerRadius(cornerR)
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val labels = when (p) {
                                "day" -> listOf(
                                    stringResource(R.string.history_chart_12am),
                                    stringResource(R.string.history_chart_6am),
                                    stringResource(R.string.history_chart_12pm),
                                    stringResource(R.string.history_chart_6pm),
                                    stringResource(R.string.history_chart_now)
                                )
                                "week" -> listOf(
                                    stringResource(R.string.history_chart_mon),
                                    stringResource(R.string.history_chart_tue),
                                    stringResource(R.string.history_chart_wed),
                                    stringResource(R.string.history_chart_thu),
                                    stringResource(R.string.history_chart_fri),
                                    stringResource(R.string.history_chart_sat),
                                    stringResource(R.string.history_chart_sun)
                                )
                                "month" -> listOf("1", "7", "14", "21", "28")
                                else -> listOf(
                                    stringResource(R.string.history_chart_jan),
                                    stringResource(R.string.history_chart_apr),
                                    stringResource(R.string.history_chart_jul),
                                    stringResource(R.string.history_chart_oct),
                                    stringResource(R.string.history_chart_dec)
                                )
                            }
                            labels.forEach {
                                Text(
                                    it,
                                    color = theme.textMuted,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                AnimatedContent(
                    targetState = insight,
                    transitionSpec = { fadeIn(tween(450)) togetherWith fadeOut(tween(250)) },
                    label = "insightAnim"
                ) { text ->
                    Text(
                        text,
                        color = theme.textSoft,
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HistoryStatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.history_stat_total),
                    value = if (total >= 1000) {
                        "${String.format(locale, "%.1f", total / 1000f)} L"
                    } else {
                        "$total ml"
                    },
                    icon = "💧",
                    theme = theme
                )
                HistoryStatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.history_stat_avg_size),
                    value = if (avgSize > 0) "$avgSize ml" else "—",
                    icon = "🍶",
                    theme = theme
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HistoryStatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.history_stat_most_logged),
                    value = mostLogged,
                    icon = "📊",
                    theme = theme
                )
                HistoryStatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.history_stat_streak),
                    value = "${viewModel.streak}d",
                    icon = "🔥",
                    theme = theme
                )
            }
            Spacer(Modifier.height(28.dp))
            Text(
                stringResource(R.string.history_today_logs),
                modifier = Modifier.padding(horizontal = 20.dp),
                color = theme.textPrimary,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(10.dp))
            if (viewModel.todayDrinks.isEmpty()) {
                FrostedGlass(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    theme = theme,
                    cornerRadius = 16.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("💧", fontSize = 32.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.history_empty_title),
                            color = theme.textSoft,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            stringResource(R.string.history_empty_sub),
                            color = theme.textMuted,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                viewModel.todayDrinks.reversed().forEach { l ->
                    FrostedGlass(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 5.dp),
                        theme = theme,
                        cornerRadius = 16.dp,
                        padding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(l.drink.fillColour.copy(alpha = 0.18f), RoundedCornerShape(12.dp))
                                    .border(0.75.dp, l.drink.fillColour.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(l.categoryIcon, fontSize = 20.sp)
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    l.drink.name,
                                    color = theme.textPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "${l.drink.sizeMl}${viewModel.unitLabel()}",
                                        color = theme.textSoft,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    if (l.drink.dehydrationFactor > 0.05f) {
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            "(-${(l.drink.sizeMl * l.drink.dehydrationFactor).toInt()}${viewModel.unitLabel()})",
                                            color = Color(0xFFEF4444),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    timeFmt.format(Date(l.timestamp)),
                                    color = theme.textMuted,
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    "+${l.drink.waterMl}ml",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = theme.accent.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun NavArrow(
    symbol: String,
    theme: HydraTheme,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (isPressed && enabled) 0.86f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 1500f),
        label = "arrowScale"
    )
    val alpha = if (enabled) 1f else 0.28f
    Box(
        modifier = Modifier
            .size(40.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .clip(RoundedCornerShape(12.dp))
            .background(theme.accent.copy(alpha = 0.1f))
            .border(0.75.dp, theme.accent.copy(alpha = 0.22f), RoundedCornerShape(12.dp))
            .then(
                if (enabled) {
                    Modifier.clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(symbol, color = theme.accent, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun HistoryStatCard(
    modifier: Modifier,
    label: String,
    value: String,
    icon: String,
    theme: HydraTheme
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = theme.accent.copy(alpha = 0.1f),
                spotColor = theme.accent.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.12f), Color.White.copy(alpha = 0.05f), theme.bg2.copy(alpha = 0.6f))
                )
            )
            .border(0.75.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(label, color = theme.textSoft, style = MaterialTheme.typography.bodySmall)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(theme.accent.copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 15.sp)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                value,
                color = theme.textPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
