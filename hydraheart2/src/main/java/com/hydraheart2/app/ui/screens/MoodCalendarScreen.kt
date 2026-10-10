package com.hydraheart2.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.data.models.MoodEntry
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val MOOD_COLORS = mapOf(
    "Happy" to Color(0xFFFFB7C5),
    "Calm" to Color(0xFFB8E6C1),
    "Okay" to Color(0xFFCDCDCD),
    "Tired" to Color(0xFFADD8E6),
    "Stressed" to Color(0xFFD4B3E8),
)

private val MOOD_EMOJIS = mapOf(
    "Happy" to "😊", "Calm" to "😌", "Okay" to "😐", "Tired" to "😴", "Stressed" to "😣",
)

@Composable
fun MoodCalendarScreen(viewModel: HydraViewModel, onBack: () -> Unit) {
    val theme = viewModel.currentTheme
    val ctx = LocalContext.current

    var loaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { loaded = true }

    val entryMap = remember(viewModel.moodEntries.size) {
        viewModel.moodEntries.associateBy { it.date }
    }

    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Date()) }
    val dayFmt = remember { SimpleDateFormat("yyyy-MM-dd", Locale.UK) }
    val displayFmt = remember { SimpleDateFormat("EEE, d MMM", Locale.UK) }

    var selectedEntry by remember { mutableStateOf<MoodEntry?>(null) }

    val weekEntries = remember(viewModel.moodEntries.size) {
        val cal = Calendar.getInstance()
        (0..6).mapNotNull { offset ->
            cal.time = Date()
            cal.add(Calendar.DAY_OF_YEAR, -offset)
            entryMap[dayFmt.format(cal.time)]
        }
    }
    val mostCommonThisWeek = weekEntries.groupBy { it.mood }
        .maxByOrNull { it.value.size }?.key

    val todayEntry = entryMap[today]

    val weekDays = remember(viewModel.moodEntries.size) {
        val cal = Calendar.getInstance()
        (6 downTo 0).map { offset ->
            cal.time = Date()
            cal.add(Calendar.DAY_OF_YEAR, -offset)
            val key = dayFmt.format(cal.time)
            Triple(key, SimpleDateFormat("EEE", Locale.UK).format(cal.time), entryMap[key])
        }
    }

    if (selectedEntry != null) {
        MoodDayDialog(selectedEntry!!, theme, displayFmt, dayFmt) { selectedEntry = null }
    }

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
                .padding(horizontal = 20.dp)
                .padding(bottom = 80.dp)
        ) {
            AnimatedVisibility(visible = loaded, enter = fadeIn(tween(250))) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    IconButton(onClick = {
                        HapticEngine.tick(ctx)
                        onBack()
                    }) {
                        Text("←", fontSize = 22.sp, color = theme.accent)
                    }
                    Text("Mood Calendar", style = MaterialTheme.typography.headlineMedium, color = theme.accent3)
                }
            }

            AnimatedVisibility(
                visible = loaded && mostCommonThisWeek != null,
                enter = fadeIn(tween(380, delayMillis = 80)) +
                    slideInVertically(tween(340, delayMillis = 80)) { it / 3 }
            ) {
                if (mostCommonThisWeek != null) {
                    val c = MOOD_COLORS[mostCommonThisWeek] ?: theme.accent
                    val emoji = MOOD_EMOJIS[mostCommonThisWeek] ?: ""
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(18.dp),
                                ambientColor = c.copy(alpha = 0.15f),
                                spotColor = c.copy(alpha = 0.1f),
                            )
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        c.copy(alpha = 0.22f),
                                        c.copy(alpha = 0.1f),
                                        theme.bg2.copy(alpha = 0.55f),
                                    )
                                )
                            )
                            .border(0.75.dp, c.copy(alpha = 0.38f), RoundedCornerShape(18.dp))
                            .padding(horizontal = 18.dp, vertical = 14.dp)
                    ) {
                        Text(
                            "Lately you've been feeling mostly $mostCommonThisWeek $emoji",
                            style = MaterialTheme.typography.bodyMedium,
                            color = theme.textPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }

            AnimatedVisibility(
                visible = loaded,
                enter = fadeIn(tween(360, delayMillis = 120)) +
                    slideInVertically(tween(320, delayMillis = 120)) { it / 3 }
            ) {
                Column {
                    SectionLabel("Today", theme)
                    Spacer(Modifier.height(10.dp))
                    if (todayEntry != null) {
                        TodayCard(todayEntry, theme) { selectedEntry = todayEntry }
                    } else {
                        FrostedGlass(modifier = Modifier.fillMaxWidth(), theme = theme) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("💧", fontSize = 36.sp)
                                Spacer(Modifier.height(10.dp))
                                Text("No mood logged today", style = MaterialTheme.typography.titleSmall, color = theme.textPrimary)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Head to Mood Tracking to check in ✨",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = theme.textSoft,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(26.dp))
                }
            }

            AnimatedVisibility(
                visible = loaded,
                enter = fadeIn(tween(360, delayMillis = 180)) +
                    slideInVertically(tween(320, delayMillis = 180)) { it / 3 }
            ) {
                Column {
                    SectionLabel("This Week", theme)
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        weekDays.forEach { (dateKey, dayLabel, entry) ->
                            val isToday = dateKey == today
                            val mc = entry?.let { MOOD_COLORS[it.mood] }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(
                                        elevation = if (isToday || entry != null) 4.dp else 1.dp,
                                        shape = RoundedCornerShape(14.dp),
                                        ambientColor = (mc ?: theme.accent).copy(alpha = if (entry != null) 0.15f else 0.03f),
                                        spotColor = (mc ?: theme.accent).copy(alpha = if (entry != null) 0.1f else 0.02f),
                                    )
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (mc != null) {
                                            Brush.verticalGradient(listOf(mc.copy(alpha = 0.28f), mc.copy(alpha = 0.12f)))
                                        } else {
                                            Brush.verticalGradient(
                                                listOf(theme.cardBg.copy(alpha = 0.22f), theme.cardBg.copy(alpha = 0.1f))
                                            )
                                        }
                                    )
                                    .border(
                                        if (isToday) 1.5.dp else 0.75.dp,
                                        when {
                                            isToday -> theme.accent.copy(alpha = 0.65f)
                                            mc != null -> mc.copy(alpha = 0.3f)
                                            else -> theme.cardBorder.copy(alpha = 0.2f)
                                        },
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable(
                                        enabled = entry != null,
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                    ) { entry?.let { HapticEngine.tick(ctx); selectedEntry = it } }
                                    .padding(horizontal = 4.dp, vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    dayLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isToday) theme.accent else theme.textMuted,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                )
                                Spacer(Modifier.height(6.dp))
                                if (entry != null) {
                                    Text(MOOD_EMOJIS[entry.mood] ?: "•", fontSize = 20.sp)
                                } else {
                                    Box(modifier = Modifier.size(20.dp), contentAlignment = Alignment.Center) {
                                        Box(
                                            Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(theme.cardBorder.copy(alpha = 0.35f))
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (mostCommonThisWeek != null) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "Most common this week: $mostCommonThisWeek ${MOOD_EMOJIS[mostCommonThisWeek] ?: ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = theme.textSoft,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                    Spacer(Modifier.height(26.dp))
                }
            }

            AnimatedVisibility(
                visible = loaded,
                enter = fadeIn(tween(380, delayMillis = 240)) +
                    slideInVertically(tween(340, delayMillis = 240)) { it / 3 }
            ) {
                val cal = Calendar.getInstance()
                val currentYear = cal.get(Calendar.YEAR)
                val currentMonth = cal.get(Calendar.MONTH)
                Column {
                    SectionLabel("This Year", theme)
                    Spacer(Modifier.height(10.dp))
                    (0..currentMonth).reversed().forEach { month ->
                        MonthGrid(
                            year = currentYear,
                            month = month,
                            today = today,
                            entryMap = entryMap,
                            theme = theme,
                            dayFmt = dayFmt,
                            onDayTapped = { entry -> HapticEngine.tick(ctx); selectedEntry = entry },
                        )
                        Spacer(Modifier.height(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayCard(entry: MoodEntry, theme: HydraTheme, onClick: () -> Unit) {
    val moodColor = MOOD_COLORS[entry.mood] ?: theme.accent
    val emoji = MOOD_EMOJIS[entry.mood] ?: "💧"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = moodColor.copy(alpha = 0.22f),
                spotColor = moodColor.copy(alpha = 0.16f),
            )
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        moodColor.copy(alpha = 0.28f),
                        moodColor.copy(alpha = 0.14f),
                        theme.bg2.copy(alpha = 0.6f),
                    )
                )
            )
            .border(1.dp, moodColor.copy(alpha = 0.45f), RoundedCornerShape(22.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 22.dp, vertical = 22.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(moodColor.copy(alpha = 0.3f))
                    .border(1.dp, moodColor.copy(alpha = 0.55f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 32.sp)
            }
            Spacer(Modifier.width(18.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entry.mood,
                    style = MaterialTheme.typography.titleLarge,
                    color = moodColor,
                    fontWeight = FontWeight.Bold,
                )
                if (entry.note.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        entry.note,
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                        color = theme.textSoft,
                        maxLines = 2,
                    )
                } else {
                    Spacer(Modifier.height(2.dp))
                    Text("Tap to view details", style = MaterialTheme.typography.labelSmall, color = theme.textMuted)
                }
            }
        }
    }
}

@Composable
private fun MonthGrid(
    year: Int,
    month: Int,
    today: String,
    entryMap: Map<String, MoodEntry>,
    theme: HydraTheme,
    dayFmt: SimpleDateFormat,
    onDayTapped: (MoodEntry) -> Unit,
) {
    val monthName = SimpleDateFormat("MMMM", Locale.UK).format(Calendar.getInstance().also { it.set(year, month, 1) }.time)
    val cal = Calendar.getInstance().also { it.set(year, month, 1) }
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDow = (cal.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7

    FrostedGlass(modifier = Modifier.fillMaxWidth(), theme = theme, padding = PaddingValues(16.dp)) {
        Text(monthName, style = MaterialTheme.typography.titleSmall, color = theme.textPrimary, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth()) {
            listOf("M", "T", "W", "T", "F", "S", "S").forEach { d ->
                Text(
                    d,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = theme.textMuted,
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        val totalCells = firstDow + daysInMonth
        val rows = (totalCells + 6) / 7
        for (row in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val dayNum = cellIndex - firstDow + 1
                    if (dayNum < 1 || dayNum > daysInMonth) {
                        Spacer(Modifier.weight(1f).aspectRatio(1f))
                    } else {
                        val dayCal = Calendar.getInstance().also { it.set(year, month, dayNum) }
                        val dateKey = dayFmt.format(dayCal.time)
                        val entry = entryMap[dateKey]
                        val isToday = dateKey == today
                        val mc = entry?.let { MOOD_COLORS[it.mood] }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clip(RoundedCornerShape(9.dp))
                                .background(
                                    when {
                                        mc != null -> mc.copy(alpha = 0.3f)
                                        isToday -> theme.accent.copy(alpha = 0.12f)
                                        else -> Color.Transparent
                                    }
                                )
                                .border(
                                    if (isToday) 1.dp else 0.dp,
                                    if (isToday) theme.accent.copy(alpha = 0.55f) else Color.Transparent,
                                    RoundedCornerShape(9.dp)
                                )
                                .clickable(
                                    enabled = entry != null,
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                ) { entry?.let { onDayTapped(it) } },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "$dayNum",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = when {
                                        isToday -> theme.accent
                                        mc != null -> mc.copy(alpha = 0.9f)
                                        else -> theme.textMuted.copy(alpha = 0.55f)
                                    },
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                )
                                if (entry != null) {
                                    Text(MOOD_EMOJIS[entry.mood] ?: "", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodDayDialog(
    entry: MoodEntry,
    theme: HydraTheme,
    displayFmt: SimpleDateFormat,
    dayFmt: SimpleDateFormat,
    onDismiss: () -> Unit,
) {
    val moodColor = MOOD_COLORS[entry.mood] ?: theme.accent
    val emoji = MOOD_EMOJIS[entry.mood] ?: "💧"
    val date = runCatching { displayFmt.format(dayFmt.parse(entry.date)!!) }.getOrDefault(entry.date)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = moodColor.copy(alpha = 0.2f),
                    spotColor = moodColor.copy(alpha = 0.14f),
                )
                .clip(RoundedCornerShape(26.dp))
                .background(Brush.verticalGradient(listOf(moodColor.copy(alpha = 0.18f), theme.bg2, theme.bg3)))
                .border(0.75.dp, moodColor.copy(alpha = 0.4f), RoundedCornerShape(26.dp))
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 44.sp)
            Spacer(Modifier.height(10.dp))
            Text(
                entry.mood,
                style = MaterialTheme.typography.headlineSmall,
                color = moodColor,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(date, style = MaterialTheme.typography.bodySmall, color = theme.textMuted)
            if (entry.note.isNotBlank()) {
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(theme.bg2.copy(alpha = 0.5f))
                        .border(0.75.dp, theme.cardBorder.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        entry.note,
                        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                        color = theme.textSoft,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Spacer(Modifier.height(22.dp))
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(moodColor.copy(alpha = 0.12f))
            ) {
                Text("Close", color = moodColor, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String, theme: HydraTheme) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall,
        color = theme.textSoft,
        fontWeight = FontWeight.SemiBold,
    )
}
