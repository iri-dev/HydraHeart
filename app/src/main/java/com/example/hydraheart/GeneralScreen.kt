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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont

private val GnAccent = Color(0xFF4F7DFF)
private val GnAccentSoft = Color(0xFF6E9BFF)
private val GnLavender = Color(0xFFEAF0FF)
private val GnMuted = Color(0xFFB8C6EE)

private val gnPanelBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF1C2A55).copy(alpha = 0.56f),
        Color(0xFF17244A).copy(alpha = 0.62f),
        Color(0xFF111D3D).copy(alpha = 0.72f)
    )
)

private val gnCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF21315F).copy(alpha = 0.48f),
        Color(0xFF142245).copy(alpha = 0.72f)
    )
)

private val gnCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.15f),
        Color.White.copy(alpha = 0.05f),
        GnAccent.copy(alpha = 0.10f)
    )
)

private val gnSelectedBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF263D85).copy(alpha = 0.56f),
        Color(0xFF1E3372).copy(alpha = 0.74f)
    )
)

private val gnSelectedBorder = Brush.verticalGradient(
    colors = listOf(
        GnAccent.copy(alpha = 0.86f),
        GnAccent.copy(alpha = 0.46f)
    )
)

private data class GeneralOption(val icon: String, val label: String)

private val interactionOptions = listOf(
    GeneralOption("", "Off"),
    GeneralOption("", "Soft"),
    GeneralOption("", "Balanced"),
    GeneralOption("", "Full")
)

private val experienceOptions = listOf(
    GeneralOption("\uD83D\uDD0B", "Saver"),
    GeneralOption("\u2696\uFE0F", "Balanced"),
    GeneralOption("\u2728", "Full")
)

@Composable
fun GeneralScreen(onBack: () -> Unit = {}) {
    var interaction by remember { mutableStateOf("Full") }
    var experience by remember { mutableStateOf("Full") }
    var keepAwake by remember { mutableStateOf(false) }
    var reduceMotion by remember { mutableStateOf(false) }
    var batteryAware by remember { mutableStateOf(false) }
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
                        Color(0xFF101A3B),
                        Color(0xFF101A36),
                        Color(0xFF0D1730),
                        Color(0xFF081025)
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
            GeneralHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(30.dp))
            GeneralText(
                text = "General",
                color = GnAccent,
                fontSize = 30,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            GeneralText(
                text = "How we move, feel, and speak together",
                color = GnMuted.copy(alpha = 0.86f),
                fontSize = 17,
                lineHeight = 22
            )

            Spacer(modifier = Modifier.height(14.dp))
            GeneralHero()

            Spacer(modifier = Modifier.height(30.dp))
            SectionLabel("INTERACTION STYLE")
            Spacer(modifier = Modifier.height(4.dp))
            GeneralText(
                text = "Choose how I reach you - I'll match your energy",
                color = GnMuted.copy(alpha = 0.82f),
                fontSize = 17,
                lineHeight = 23
            )
            Spacer(modifier = Modifier.height(18.dp))
            GeneralText(
                text = "Full immersive feedback - every sense engaged.",
                color = GnMuted.copy(alpha = 0.84f),
                fontSize = 16,
                lineHeight = 22
            )
            Spacer(modifier = Modifier.height(18.dp))
            InteractionGrid(
                selected = interaction,
                onSelect = { interaction = it }
            )

            Spacer(modifier = Modifier.height(30.dp))
            SectionLabel("EXPERIENCE MODE")
            Spacer(modifier = Modifier.height(18.dp))
            GeneralText(
                text = experience,
                color = GnLavender,
                fontSize = 22,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            GeneralText(
                text = when (experience) {
                    "Saver" -> "Gentler visuals - saving energy where it counts."
                    "Balanced" -> "Soft visual fidelity - steady and smooth."
                    else -> "Full visual fidelity - every effect at maximum."
                },
                color = GnMuted.copy(alpha = 0.82f),
                fontSize = 16,
                lineHeight = 22
            )
            Spacer(modifier = Modifier.height(18.dp))
            ExperienceModeRow(
                selected = experience,
                onSelect = { experience = it }
            )

            Spacer(modifier = Modifier.height(34.dp))
            SectionLabel("DEVICE BEHAVIOUR")
            Spacer(modifier = Modifier.height(14.dp))
            DeviceToggleRow(
                title = "Keep Screen Awake",
                subtitle = "Prevent display sleep while using the app",
                checked = keepAwake,
                onCheckedChange = { keepAwake = it }
            )
            Spacer(modifier = Modifier.height(12.dp))
            DeviceToggleRow(
                title = "Reduce Motion",
                subtitle = "Simplify animations for accessibility",
                checked = reduceMotion,
                onCheckedChange = { reduceMotion = it }
            )
            Spacer(modifier = Modifier.height(12.dp))
            DeviceToggleRow(
                title = "Battery-Aware Mode",
                subtitle = "Auto-reduce effects when battery is low",
                checked = batteryAware,
                onCheckedChange = { batteryAware = it }
            )

            Spacer(modifier = Modifier.height(34.dp))
            SectionLabel("LANGUAGE")
            Spacer(modifier = Modifier.height(14.dp))
            LanguageRow()

            Spacer(
                modifier = Modifier
                    .height(48.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun GeneralHeader(onBack: () -> Unit) {
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
                    color = GnAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = GnAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = GnAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        GeneralText(
            text = "General",
            modifier = Modifier.align(Alignment.Center),
            color = GnAccent,
            fontSize = 26,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GeneralHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(314.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(gnPanelBrush)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        GnAccentSoft.copy(alpha = 0.18f),
                        GnAccent.copy(alpha = 0.07f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.50f, size.height * 0.50f),
                    radius = size.width * 0.34f
                ),
                radius = size.width * 0.34f,
                center = Offset(size.width * 0.50f, size.height * 0.50f)
            )
        }

        GeneralHeart(
            modifier = Modifier
                .align(Alignment.Center)
                .size(150.dp)
        )

        GeneralText(
            text = "Every sense engaged - I'm fully here with you \u2728",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 26.dp),
            color = GnMuted.copy(alpha = 0.88f),
            fontSize = 16,
            lineHeight = 22,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GeneralHeart(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.aspectRatio(0.96f)) {
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
        drawPath(
            path = heartPath,
            color = Color(0xFF7FA0FF).copy(alpha = 0.06f),
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        drawPath(
            path = heartPath,
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color(0xFF9DB8FF),
                    0.34f to Color(0xFF5B82E8),
                    0.72f to Color(0xFF355BB8),
                    1.0f to Color(0xFF1E3B84)
                ),
                center = Offset(w * 0.25f, h * 0.16f),
                radius = w * 1.08f
            )
        )
        clipPath(heartPath) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.19f),
                        Color(0xFFCFE0FF).copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.22f, h * 0.17f),
                    radius = w * 0.78f
                )
            )
        }

        val faceCenterY = h * 0.405f
        val faceScale = 0.75f
        val eyeSpacing = 13.0.dp.toPx() * faceScale
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
        val browW = 4.9.dp.toPx() * faceScale
        val browH = 1.8.dp.toPx() * faceScale
        val browOffset = 4.2.dp.toPx() * faceScale
        drawLine(
            color = Color.White.copy(alpha = 0.94f),
            start = Offset(w * 0.5f - eyeSpacing / 2 - browW / 2, faceCenterY - browOffset - browH),
            end = Offset(w * 0.5f - eyeSpacing / 2 + browW / 2, faceCenterY - browOffset),
            strokeWidth = 0.9.dp.toPx() * faceScale,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.White.copy(alpha = 0.94f),
            start = Offset(w * 0.5f + eyeSpacing / 2 + browW / 2, faceCenterY - browOffset - browH),
            end = Offset(w * 0.5f + eyeSpacing / 2 - browW / 2, faceCenterY - browOffset),
            strokeWidth = 0.9.dp.toPx() * faceScale,
            cap = StrokeCap.Round
        )
        val frown = Path().apply {
            moveTo(w * 0.5f - 4.2.dp.toPx() * faceScale, faceCenterY + 8.4.dp.toPx() * faceScale)
            cubicTo(
                w * 0.5f - 2.0.dp.toPx() * faceScale,
                faceCenterY + 5.0.dp.toPx() * faceScale,
                w * 0.5f + 2.0.dp.toPx() * faceScale,
                faceCenterY + 5.0.dp.toPx() * faceScale,
                w * 0.5f + 4.2.dp.toPx() * faceScale,
                faceCenterY + 8.4.dp.toPx() * faceScale
            )
        }
        drawPath(
            path = frown,
            color = Color.White.copy(alpha = 0.92f),
            style = Stroke(width = 0.82.dp.toPx() * faceScale, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun InteractionGrid(
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        interactionOptions.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { option ->
                    InteractionTile(
                        label = option.label,
                        selected = option.label == selected,
                        onClick = { onSelect(option.label) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun InteractionTile(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(62.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) gnSelectedBrush else gnCardBrush)
            .border(
                brush = if (selected) gnSelectedBorder else gnCardBorder,
                width = if (selected) 1.25.dp else 0.75.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SelectionDot(selected = selected, small = false)
            Spacer(modifier = Modifier.width(12.dp))
            GeneralText(
                text = label,
                color = if (selected) GnAccentSoft else GnMuted.copy(alpha = 0.70f),
                fontSize = 18,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ExperienceModeRow(
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        experienceOptions.forEach { option ->
            ModeTile(
                option = option,
                selected = option.label == selected,
                onClick = { onSelect(option.label) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ModeTile(
    option: GeneralOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(62.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) gnSelectedBrush else gnCardBrush)
            .border(
                brush = if (selected) gnSelectedBorder else gnCardBorder,
                width = if (selected) 1.25.dp else 0.75.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GeneralText(
                text = option.icon,
                color = GnLavender,
                fontSize = 18,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(8.dp))
            GeneralText(
                text = option.label,
                color = if (selected) GnAccentSoft else GnMuted.copy(alpha = 0.76f),
                fontSize = 16,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun DeviceToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(gnCardBrush)
            .border(
                brush = gnCardBorder,
                width = 0.75.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            GeneralText(
                text = title,
                color = GnLavender,
                fontSize = 20,
                lineHeight = 23,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            GeneralText(
                text = subtitle,
                color = GnMuted.copy(alpha = 0.72f),
                fontSize = 15,
                lineHeight = 19
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        ToggleSwitch(checked = checked)
    }
}

@Composable
private fun ToggleSwitch(checked: Boolean) {
    Box(
        modifier = Modifier
            .size(width = 66.dp, height = 40.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    colors = if (checked) {
                        listOf(Color(0xFF3D6EFF), Color(0xFF2749AD))
                    } else {
                        listOf(Color(0xFF243A77), Color(0xFF1C2E62))
                    }
                )
            )
            .border(
                width = 0.8.dp,
                color = Color.White.copy(alpha = if (checked) 0.18f else 0.07f),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(5.dp)
    ) {
        Box(
            modifier = Modifier
                .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFE9EEFF),
                            Color(0xFF9EAED8)
                        )
                    )
                )
        )
    }
}

@Composable
private fun LanguageRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(gnCardBrush)
            .border(
                brush = gnCardBorder,
                width = 0.75.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {}
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            GeneralText(
                text = "Language",
                color = GnLavender,
                fontSize = 20,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            GeneralText(
                text = "English (UK)",
                color = GnMuted.copy(alpha = 0.78f),
                fontSize = 15
            )
        }
        Chevron()
    }
}

@Composable
private fun SelectionDot(selected: Boolean, small: Boolean) {
    Canvas(modifier = Modifier.size(if (small) 10.dp else 14.dp)) {
        if (selected) {
            drawCircle(color = GnAccent, radius = size.minDimension * 0.46f)
        } else {
            drawCircle(
                color = GnMuted.copy(alpha = 0.54f),
                radius = size.minDimension * 0.42f,
                style = Stroke(width = 1.1.dp.toPx())
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    GeneralText(
        text = text,
        modifier = Modifier.padding(start = 6.dp),
        color = GnAccent.copy(alpha = 0.66f),
        fontSize = 13,
        fontWeight = FontWeight.Bold,
        letterSpacing = 3.0
    )
}

@Composable
private fun Chevron() {
    Canvas(modifier = Modifier.size(14.dp)) {
        val w = size.width
        val h = size.height
        drawLine(
            color = GnAccent.copy(alpha = 0.42f),
            start = Offset(w * 0.30f, h * 0.20f),
            end = Offset(w * 0.66f, h * 0.50f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = GnAccent.copy(alpha = 0.42f),
            start = Offset(w * 0.66f, h * 0.50f),
            end = Offset(w * 0.30f, h * 0.80f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun GeneralText(
    text: String,
    color: Color,
    fontSize: Int,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = AppFont,
    fontStyle: FontStyle? = FontStyle.Italic,
    fontWeight: FontWeight? = null,
    lineHeight: Int? = null,
    letterSpacing: Double = 0.0,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE
) {
    BasicText(
        text = text,
        modifier = modifier,
        maxLines = maxLines,
        style = TextStyle(
            color = color,
            fontSize = fontSize.sp,
            fontFamily = fontFamily,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            lineHeight = (lineHeight ?: fontSize).sp,
            letterSpacing = letterSpacing.sp,
            textAlign = textAlign
        )
    )
}
