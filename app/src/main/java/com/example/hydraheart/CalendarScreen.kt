package com.example.hydraheart

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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

private val CalAccent = Color(0xFF6E9BFF)
private val CalLavender = Color(0xFFEAF0FF)
private val CalMuted = Color(0xFFB8C6EE)

private val calTileBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF26345E).copy(alpha = 0.55f),
        Color(0xFF131F3D).copy(alpha = 0.78f)
    )
)

private val calTileBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.22f),
        Color.White.copy(alpha = 0.06f),
        CalAccent.copy(alpha = 0.05f)
    )
)

private val calSelectedBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3A5BB8).copy(alpha = 0.55f),
        Color(0xFF24417F).copy(alpha = 0.70f)
    )
)

private val calSelectedBorder = Brush.verticalGradient(
    colors = listOf(
        CalAccent.copy(alpha = 0.85f),
        CalAccent.copy(alpha = 0.40f)
    )
)

@Composable
fun CalendarScreen(onBack: () -> Unit = {}) {
    var dateOrder by remember { mutableStateOf("DayFirst") }
    var timeFormat by remember { mutableStateOf("12") }
    var showDayName by remember { mutableStateOf(true) }
    var showTime by remember { mutableStateOf(true) }
    var weekStart by remember { mutableStateOf("Mon") }
    val scrollState = rememberScrollState()

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
            CalHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(28.dp))
            BasicText(
                text = "Date & Time",
                style = TextStyle(
                    color = CalAccent,
                    fontSize = 30.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Customise how time appears throughout the app",
                style = TextStyle(
                    color = CalLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            PreviewPill(
                dateOrder = dateOrder,
                timeFormat = timeFormat,
                showDayName = showDayName,
                showTime = showTime
            )

            Spacer(modifier = Modifier.height(22.dp))
            CalSectionLabel("DATE ORDER")
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BigChoiceCard(
                    value = "29 Mar",
                    label = "Day First",
                    selected = dateOrder == "DayFirst",
                    onClick = { dateOrder = "DayFirst" },
                    modifier = Modifier.weight(1f)
                )
                BigChoiceCard(
                    value = "Mar 29",
                    label = "Month First",
                    selected = dateOrder == "MonthFirst",
                    onClick = { dateOrder = "MonthFirst" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))
            CalSectionLabel("TIME FORMAT")
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BigChoiceCard(
                    value = "2:45 PM",
                    label = "12-Hour",
                    selected = timeFormat == "12",
                    onClick = { timeFormat = "12" },
                    modifier = Modifier.weight(1f)
                )
                BigChoiceCard(
                    value = "14:45",
                    label = "24-Hour",
                    selected = timeFormat == "24",
                    onClick = { timeFormat = "24" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            CalSectionLabel("DISPLAY DETAILS")
            Spacer(modifier = Modifier.height(14.dp))
            FlatToggleRow(
                title = "Day Name",
                subtitle = "Mon, Tue, Wed beside the date",
                checked = showDayName,
                onChange = { showDayName = it }
            )
            Spacer(modifier = Modifier.height(18.dp))
            FlatToggleRow(
                title = "Time",
                subtitle = "Time shown in history entries",
                checked = showTime,
                onChange = { showTime = it }
            )

            Spacer(modifier = Modifier.height(28.dp))
            CalSectionLabel("WEEK & TRACKING")
            Spacer(modifier = Modifier.height(14.dp))
            WeekStartRow(
                selected = weekStart,
                onSelect = { weekStart = it }
            )
            Spacer(modifier = Modifier.height(18.dp))
            DayResetsRow()

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun CalHeader(onBack: () -> Unit) {
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
                    color = CalAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = CalAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = CalAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        BasicText(
            text = "Calendar",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = CalAccent,
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
private fun CalSectionLabel(label: String) {
    BasicText(
        text = label,
        modifier = Modifier.padding(start = 6.dp),
        style = TextStyle(
            color = CalAccent.copy(alpha = 0.72f),
            fontSize = 13.sp,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.0.sp
        )
    )
}

@Composable
private fun PreviewPill(
    dateOrder: String,
    timeFormat: String,
    showDayName: Boolean,
    showTime: Boolean
) {
    val date = if (dateOrder == "DayFirst") "29 Mar" else "Mar 29"
    val time = if (timeFormat == "12") "2:45 PM" else "14:45"
    val text = buildString {
        if (showDayName) {
            append("Wed · ")
        }
        append(date)
        if (showTime) {
            append(" · ")
            append(time)
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(calTileBrush)
                .border(
                    brush = calTileBorder,
                    width = 0.85.dp,
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 22.dp, vertical = 14.dp)
        ) {
            BasicText(
                text = text,
                style = TextStyle(
                    color = CalLavender,
                    fontSize = 18.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun BigChoiceCard(
    value: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(if (selected) calSelectedBrush else calTileBrush)
            .border(
                brush = if (selected) calSelectedBorder else calTileBorder,
                width = if (selected) 1.3.dp else 0.85.dp,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BasicText(
                text = value,
                style = TextStyle(
                    color = if (selected) CalAccent else CalLavender,
                    fontSize = 26.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicText(
                text = label,
                style = TextStyle(
                    color = CalMuted.copy(alpha = 0.78f),
                    fontSize = 14.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}

@Composable
private fun FlatToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChange(!checked) }
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = title,
                style = TextStyle(
                    color = CalLavender,
                    fontSize = 22.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            BasicText(
                text = subtitle,
                style = TextStyle(
                    color = CalMuted.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        CalSwitch(checked = checked, onChange = onChange)
    }
}

@Composable
private fun CalSwitch(checked: Boolean, onChange: (Boolean) -> Unit) {
    val trackWidth = 56.dp
    val trackHeight = 32.dp
    val thumbSize = 26.dp
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - 3.dp else 3.dp,
        label = "thumb"
    )
    Box(
        modifier = Modifier
            .size(width = trackWidth, height = trackHeight)
            .clip(CircleShape)
            .background(
                if (checked) {
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF5A82F0), Color(0xFF4A6CD8))
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF1B2A52), Color(0xFF18254A))
                    )
                }
            )
            .border(
                width = 0.8.dp,
                color = if (checked) CalAccent.copy(alpha = 0.50f) else Color.White.copy(alpha = 0.10f),
                shape = CircleShape
            )
            .clickable { onChange(!checked) }
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset, y = 3.dp)
                .size(thumbSize)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

@Composable
private fun WeekStartRow(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = "Week Starts On",
                style = TextStyle(
                    color = CalLavender,
                    fontSize = 22.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            BasicText(
                text = "Used in weekly summaries",
                style = TextStyle(
                    color = CalMuted.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DayPill(
                label = "Mon",
                selected = selected == "Mon",
                onClick = { onSelect("Mon") }
            )
            DayPill(
                label = "Sun",
                selected = selected == "Sun",
                onClick = { onSelect("Sun") }
            )
        }
    }
}

@Composable
private fun DayPill(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) calSelectedBrush else calTileBrush)
            .border(
                brush = if (selected) calSelectedBorder else calTileBorder,
                width = if (selected) 1.1.dp else 0.85.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = label,
            style = TextStyle(
                color = if (selected) CalAccent else CalLavender,
                fontSize = 16.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun DayResetsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = "Day Resets At",
                style = TextStyle(
                    color = CalLavender,
                    fontSize = 22.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            BasicText(
                text = "When your daily tracking starts fresh",
                style = TextStyle(
                    color = CalMuted.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        BasicText(
            text = "12:00 AM",
            style = TextStyle(
                color = CalAccent,
                fontSize = 18.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        BasicText(
            text = "›",
            style = TextStyle(
                color = CalAccent.copy(alpha = 0.85f),
                fontSize = 22.sp,
                fontFamily = AppFont
            )
        )
    }
}
