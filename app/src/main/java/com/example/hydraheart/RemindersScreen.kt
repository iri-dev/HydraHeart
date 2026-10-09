package com.example.hydraheart

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont

private val RmAccent = Color(0xFF6E9BFF)
private val RmLavender = Color(0xFFEAF0FF)
private val RmMuted = Color(0xFFB8C6EE)

private val rmTileBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF26345E).copy(alpha = 0.45f),
        Color(0xFF131F3D).copy(alpha = 0.65f)
    )
)

private val rmTileBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.22f),
        Color.White.copy(alpha = 0.06f),
        RmAccent.copy(alpha = 0.05f)
    )
)

private val rmSelectedBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3A5BB8).copy(alpha = 0.55f),
        Color(0xFF24417F).copy(alpha = 0.70f)
    )
)

private val rmSelectedBorder = Brush.verticalGradient(
    colors = listOf(
        RmAccent.copy(alpha = 0.85f),
        RmAccent.copy(alpha = 0.40f)
    )
)

private data class ReminderItem(val time: String, val label: String)

private data class SoundOption(val glyph: SoundGlyph, val name: String)

private enum class SoundGlyph { CircleEmpty, CircleDot, Music, Drop, Diamond, BowlDot, Sparkle, BellDot }

private val soundOptions = listOf(
    SoundOption(SoundGlyph.CircleEmpty, "No Sound"),
    SoundOption(SoundGlyph.CircleDot, "Default"),
    SoundOption(SoundGlyph.Music, "Gentle Chime"),
    SoundOption(SoundGlyph.Drop, "Water Drop"),
    SoundOption(SoundGlyph.Diamond, "Crystal"),
    SoundOption(SoundGlyph.BowlDot, "Singing Bowl"),
    SoundOption(SoundGlyph.Sparkle, "Shimmer"),
    SoundOption(SoundGlyph.BellDot, "Soft Bell")
)

@Composable
fun RemindersScreen(
    onBack: () -> Unit = {},
    savedReminders: List<HydraReminder> = emptyList(),
    remindersEnabled: Boolean = false,
    selectedSound: String = "Shimmer",
    onToggle: (Boolean) -> Unit = {},
    onAddReminder: () -> Unit = {},
    onRemoveReminder: (HydraReminder) -> Unit = {},
    onSoundSelected: (String) -> Unit = {}
) {
    val reminderItems = remember(savedReminders) {
        savedReminders.map { ReminderItem(it.timeText, it.label) }
    }
    var customMessage by remember { mutableStateOf("") }
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
            RmHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(28.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    BasicText(
                        text = "Reminders",
                        style = TextStyle(
                            color = RmAccent,
                            fontSize = 30.sp,
                            fontFamily = AppFont,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    BasicText(
                        text = "Your gentle hydration flow",
                        style = TextStyle(
                            color = RmLavender.copy(alpha = 0.88f),
                            fontSize = 15.sp,
                            fontFamily = AppFont,
                            fontStyle = FontStyle.Italic
                        )
                    )
                }
                RmSwitch(checked = remindersEnabled, onChange = onToggle)
            }

            Spacer(modifier = Modifier.height(20.dp))
            HeroHeartArea()

            Spacer(modifier = Modifier.height(28.dp))
            FlowQuality(progress = 0.70f, quality = "Good flow", caption = "Nice and steady")

            Spacer(modifier = Modifier.height(28.dp))
            RmSectionLabel("YOUR FLOW")
            Spacer(modifier = Modifier.height(8.dp))
            RemindersTimeline(
                reminders = reminderItems,
                onRemove = { idx -> savedReminders.getOrNull(idx)?.let(onRemoveReminder) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            AddReminderButton(onClick = onAddReminder)

            Spacer(modifier = Modifier.height(28.dp))
            RmSectionLabel("NOTIFICATION SOUND")
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Tap any sound to preview it",
                modifier = Modifier.padding(start = 6.dp),
                style = TextStyle(
                    color = RmLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            SoundGrid(selected = selectedSound, onSelect = onSoundSelected)

            Spacer(modifier = Modifier.height(28.dp))
            RmSectionLabel("HYDRA'S VOICE")
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Hydra picks a message at random when checking in",
                modifier = Modifier.padding(start = 6.dp),
                style = TextStyle(
                    color = RmLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            PresetMessagesCard()

            Spacer(modifier = Modifier.height(12.dp))
            CustomMessageRow(value = customMessage, onValueChange = { customMessage = it })

            Spacer(modifier = Modifier.height(28.dp))
            AdvancedRow()

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun RmHeader(onBack: () -> Unit) {
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
                    color = RmAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = RmAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = RmAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        BasicText(
            text = "Reminders",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = RmAccent,
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
private fun RmSectionLabel(label: String) {
    BasicText(
        text = label,
        modifier = Modifier.padding(start = 6.dp),
        style = TextStyle(
            color = RmAccent.copy(alpha = 0.72f),
            fontSize = 13.sp,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.0.sp
        )
    )
}

@Composable
private fun RmSwitch(checked: Boolean, onChange: (Boolean) -> Unit) {
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
                color = if (checked) RmAccent.copy(alpha = 0.50f) else Color.White.copy(alpha = 0.10f),
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
private fun HeroHeartArea() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SmilingHeart()
        Spacer(modifier = Modifier.height(14.dp))
        BasicText(
            text = "Looking good. I'll be here every step 💧",
            style = TextStyle(
                color = RmLavender,
                fontSize = 16.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun SmilingHeart() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.50f)
            .aspectRatio(0.96f)
    ) {
        val w = size.width
        val h = size.height
        val heartPath = Path().apply {
            moveTo(w * 0.5f, h * 0.940f)
            cubicTo(w * 0.392f, h * 0.872f, w * 0.248f, h * 0.750f, w * 0.155f, h * 0.600f)
            cubicTo(w * 0.068f, h * 0.460f, w * 0.030f, h * 0.305f, w * 0.096f, h * 0.178f)
            cubicTo(w * 0.166f, h * 0.044f, w * 0.326f, h * 0.040f, w * 0.396f, h * 0.100f)
            cubicTo(w * 0.455f, h * 0.150f, w * 0.486f, h * 0.222f, w * 0.5f, h * 0.292f)
            cubicTo(w * 0.516f, h * 0.222f, w * 0.552f, h * 0.150f, w * 0.622f, h * 0.100f)
            cubicTo(w * 0.702f, h * 0.040f, w * 0.862f, h * 0.044f, w * 0.934f, h * 0.178f)
            cubicTo(w * 1.000f, h * 0.305f, w * 0.942f, h * 0.460f, w * 0.848f, h * 0.600f)
            cubicTo(w * 0.752f, h * 0.750f, w * 0.608f, h * 0.872f, w * 0.5f, h * 0.940f)
            close()
        }

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    RmAccent.copy(alpha = 0.32f),
                    RmAccent.copy(alpha = 0.06f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.75f
            ),
            radius = w * 0.75f,
            center = Offset(w * 0.5f, h * 0.5f)
        )

        drawPath(
            path = heartPath,
            color = Color(0xFF8FAAFF).copy(alpha = 0.055f),
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        drawPath(
            path = heartPath,
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color(0xFF8FAAFF),
                    0.36f to Color(0xFF4F76D8),
                    0.72f to Color(0xFF25427F),
                    1.0f to Color(0xFF172A5A)
                ),
                center = Offset(w * 0.24f, h * 0.18f),
                radius = w * 1.10f
            )
        )

        clipPath(heartPath) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFEAF1FF).copy(alpha = 0.165f),
                        Color(0xFFCFE0FF).copy(alpha = 0.070f),
                        Color(0xFF8FAAFF).copy(alpha = 0.024f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.22f, h * 0.18f),
                    radius = w * 0.74f
                )
            )
        }

        val faceCenterY = h * 0.420f
        val faceScale = 0.95f
        val eyeSpacing = 10.5.dp.toPx() * faceScale
        drawCircle(
            color = Color.White.copy(alpha = 0.94f),
            radius = 1.1.dp.toPx() * faceScale,
            center = Offset(w * 0.5f - eyeSpacing / 2, faceCenterY)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.94f),
            radius = 1.1.dp.toPx() * faceScale,
            center = Offset(w * 0.5f + eyeSpacing / 2, faceCenterY)
        )
        val smile = Path().apply {
            moveTo(w * 0.5f - 4.0.dp.toPx() * faceScale, faceCenterY + 4.0.dp.toPx() * faceScale)
            cubicTo(
                w * 0.5f - 2.0.dp.toPx() * faceScale,
                faceCenterY + 8.6.dp.toPx() * faceScale,
                w * 0.5f + 2.0.dp.toPx() * faceScale,
                faceCenterY + 8.6.dp.toPx() * faceScale,
                w * 0.5f + 4.0.dp.toPx() * faceScale,
                faceCenterY + 4.0.dp.toPx() * faceScale
            )
        }
        drawPath(
            path = smile,
            color = Color.White.copy(alpha = 0.94f),
            style = Stroke(width = 0.85.dp.toPx() * faceScale, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun FlowQuality(progress: Float, quality: String, caption: String) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            BasicText(
                text = "FLOW QUALITY",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                style = TextStyle(
                    color = RmAccent.copy(alpha = 0.72f),
                    fontSize = 13.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.0.sp
                )
            )
            BasicText(
                text = quality,
                style = TextStyle(
                    color = RmLavender,
                    fontSize = 17.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(Color(0xFF1B2A52).copy(alpha = 0.85f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF3A6BE0), Color(0xFF8FB8FF))
                        )
                    )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        BasicText(
            text = caption,
            modifier = Modifier.padding(start = 6.dp),
            style = TextStyle(
                color = RmLavender.copy(alpha = 0.85f),
                fontSize = 14.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic
            )
        )
    }
}

@Composable
private fun RemindersTimeline(reminders: List<ReminderItem>, onRemove: (Int) -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        Column {
            reminders.forEachIndexed { index, item ->
                ReminderRow(
                    item = item,
                    showLineAbove = index > 0,
                    showLineBelow = index < reminders.size - 1,
                    onRemove = { onRemove(index) }
                )
            }
        }
    }
}

@Composable
private fun ReminderRow(
    item: ReminderItem,
    showLineAbove: Boolean,
    showLineBelow: Boolean,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(34.dp)
                .height(78.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                if (showLineAbove) {
                    drawLine(
                        color = RmMuted.copy(alpha = 0.35f),
                        start = Offset(cx, 0f),
                        end = Offset(cx, cy - 6.dp.toPx()),
                        strokeWidth = 1.4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                if (showLineBelow) {
                    drawLine(
                        color = RmMuted.copy(alpha = 0.35f),
                        start = Offset(cx, cy + 6.dp.toPx()),
                        end = Offset(cx, size.height),
                        strokeWidth = 1.4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                drawCircle(
                    color = RmMuted.copy(alpha = 0.55f),
                    radius = 5.dp.toPx(),
                    center = Offset(cx, cy)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = item.time,
                style = TextStyle(
                    color = RmLavender.copy(alpha = 0.78f),
                    fontSize = 30.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            BasicText(
                text = item.label,
                style = TextStyle(
                    color = RmMuted.copy(alpha = 0.70f),
                    fontSize = 14.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
        }
        Box(
            modifier = Modifier
                .size(28.dp)
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "✕",
                style = TextStyle(
                    color = RmMuted.copy(alpha = 0.55f),
                    fontSize = 18.sp,
                    fontFamily = AppFont
                )
            )
        }
    }
}

@Composable
private fun AddReminderButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(rmTileBrush)
            .border(
                brush = rmTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = "+  Add Reminder",
            style = TextStyle(
                color = RmAccent,
                fontSize = 18.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun SoundGrid(selected: String, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        soundOptions.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { option ->
                    SoundPill(
                        option = option,
                        selected = option.name == selected,
                        onClick = { onSelect(option.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SoundPill(
    option: SoundOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) rmSelectedBrush else rmTileBrush)
            .border(
                brush = if (selected) rmSelectedBorder else rmTileBorder,
                width = if (selected) 1.1.dp else 0.85.dp,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SoundGlyphIcon(glyph = option.glyph, selected = selected)
            Spacer(modifier = Modifier.width(8.dp))
            BasicText(
                text = option.name,
                style = TextStyle(
                    color = if (selected) RmAccent else RmLavender,
                    fontSize = 16.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun SoundGlyphIcon(glyph: SoundGlyph, selected: Boolean) {
    val tint = if (selected) RmAccent else RmLavender.copy(alpha = 0.88f)
    Canvas(modifier = Modifier.size(18.dp)) {
        val w = size.width
        val h = size.height
        val stroke = 1.3.dp.toPx()
        when (glyph) {
            SoundGlyph.CircleEmpty -> {
                drawCircle(
                    color = tint,
                    radius = w * 0.42f,
                    center = Offset(w * 0.5f, h * 0.5f),
                    style = Stroke(width = stroke)
                )
            }
            SoundGlyph.CircleDot -> {
                drawCircle(
                    color = tint,
                    radius = w * 0.42f,
                    center = Offset(w * 0.5f, h * 0.5f),
                    style = Stroke(width = stroke)
                )
                drawCircle(
                    color = tint,
                    radius = w * 0.14f,
                    center = Offset(w * 0.5f, h * 0.5f)
                )
            }
            SoundGlyph.Music -> {
                drawLine(
                    color = tint,
                    start = Offset(w * 0.35f, h * 0.20f),
                    end = Offset(w * 0.35f, h * 0.80f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round
                )
                drawCircle(
                    color = tint,
                    radius = w * 0.13f,
                    center = Offset(w * 0.27f, h * 0.80f)
                )
                drawLine(
                    color = tint,
                    start = Offset(w * 0.35f, h * 0.20f),
                    end = Offset(w * 0.78f, h * 0.30f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round
                )
            }
            SoundGlyph.Drop -> {
                val drop = Path().apply {
                    moveTo(w * 0.50f, h * 0.10f)
                    cubicTo(w * 0.85f, h * 0.40f, w * 0.80f, h * 0.85f, w * 0.50f, h * 0.90f)
                    cubicTo(w * 0.20f, h * 0.85f, w * 0.15f, h * 0.40f, w * 0.50f, h * 0.10f)
                    close()
                }
                drawPath(
                    path = drop,
                    color = Color(0xFF6FB8FF)
                )
            }
            SoundGlyph.Diamond -> {
                val diamond = Path().apply {
                    moveTo(w * 0.50f, h * 0.10f)
                    lineTo(w * 0.88f, h * 0.50f)
                    lineTo(w * 0.50f, h * 0.90f)
                    lineTo(w * 0.12f, h * 0.50f)
                    close()
                }
                drawPath(
                    path = diamond,
                    color = tint,
                    style = Stroke(width = stroke, join = StrokeJoin.Round)
                )
            }
            SoundGlyph.BowlDot -> {
                drawCircle(
                    color = tint,
                    radius = w * 0.42f,
                    center = Offset(w * 0.5f, h * 0.5f),
                    style = Stroke(width = stroke)
                )
                drawCircle(
                    color = tint,
                    radius = w * 0.18f,
                    center = Offset(w * 0.5f, h * 0.5f)
                )
            }
            SoundGlyph.Sparkle -> {
                val sparkle = Path().apply {
                    moveTo(w * 0.50f, h * 0.10f)
                    lineTo(w * 0.62f, h * 0.42f)
                    lineTo(w * 0.90f, h * 0.50f)
                    lineTo(w * 0.62f, h * 0.58f)
                    lineTo(w * 0.50f, h * 0.90f)
                    lineTo(w * 0.38f, h * 0.58f)
                    lineTo(w * 0.10f, h * 0.50f)
                    lineTo(w * 0.38f, h * 0.42f)
                    close()
                }
                drawPath(path = sparkle, color = RmAccent)
            }
            SoundGlyph.BellDot -> {
                drawCircle(
                    color = tint,
                    radius = w * 0.16f,
                    center = Offset(w * 0.5f, h * 0.5f)
                )
                drawCircle(
                    color = tint,
                    radius = w * 0.42f,
                    center = Offset(w * 0.5f, h * 0.5f),
                    style = Stroke(width = stroke)
                )
            }
        }
    }
}

@Composable
private fun PresetMessagesCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(rmTileBrush)
            .border(
                brush = rmTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {}
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                BasicText(
                    text = "50 preset messages",
                    style = TextStyle(
                        color = RmLavender,
                        fontSize = 19.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = "Gentle, playful, encouraging, and more",
                    style = TextStyle(
                        color = RmMuted.copy(alpha = 0.80f),
                        fontSize = 14.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
            BasicText(
                text = "+",
                style = TextStyle(
                    color = RmAccent.copy(alpha = 0.85f),
                    fontSize = 24.sp,
                    fontFamily = AppFont
                )
            )
        }
    }
}

@Composable
private fun CustomMessageRow(value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(rmTileBrush)
            .border(
                brush = rmTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Box {
            if (value.isEmpty()) {
                BasicText(
                    text = "Add your own message…",
                    style = TextStyle(
                        color = RmMuted.copy(alpha = 0.65f),
                        fontSize = 16.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                cursorBrush = SolidColor(RmAccent),
                textStyle = TextStyle(
                    color = RmLavender,
                    fontSize = 16.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AdvancedRow() {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(horizontal = 6.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = "ADVANCED",
            modifier = Modifier.weight(1f),
            style = TextStyle(
                color = RmAccent.copy(alpha = 0.72f),
                fontSize = 13.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.0.sp
            )
        )
        BasicText(
            text = if (expanded) "︿" else "﹀",
            style = TextStyle(
                color = RmAccent.copy(alpha = 0.85f),
                fontSize = 18.sp,
                fontFamily = AppFont
            )
        )
    }
}
