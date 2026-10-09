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
import androidx.compose.foundation.layout.aspectRatio
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

private val HlAccent = Color(0xFF6E9BFF)
private val HlLavender = Color(0xFFEAF0FF)
private val HlMuted = Color(0xFFB8C6EE)
private val HlMutedDim = Color(0xFF8C9BC8)

private val hlCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2C3B66).copy(alpha = 0.55f),
        Color(0xFF16213E).copy(alpha = 0.78f),
        Color(0xFF0C1428).copy(alpha = 0.82f)
    )
)

private val hlCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.50f),
        Color.White.copy(alpha = 0.13f),
        HlAccent.copy(alpha = 0.10f)
    )
)

private val hlTileBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF26345E).copy(alpha = 0.55f),
        Color(0xFF131F3D).copy(alpha = 0.78f)
    )
)

private val hlTileBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.22f),
        Color.White.copy(alpha = 0.06f),
        HlAccent.copy(alpha = 0.05f)
    )
)

private val hlSelectedBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3A5BB8).copy(alpha = 0.78f),
        Color(0xFF24417F).copy(alpha = 0.88f)
    )
)

private val hlSelectedBorder = Brush.verticalGradient(
    colors = listOf(
        HlAccent.copy(alpha = 0.85f),
        HlAccent.copy(alpha = 0.40f)
    )
)

private data class ShapeOption(val emoji: String, val name: String)

private val shapeOptions = listOf(
    ShapeOption("💕", "Heart"),
    ShapeOption("💧", "Droplet"),
    ShapeOption("🍼", "Bottle"),
    ShapeOption("☕", "Cup"),
    ShapeOption("👗", "Woman"),
    ShapeOption("👙", "Bra")
)

private data class Accessory(val name: String, val unlockNote: String)

private val jewelleryItems = listOf(
    Accessory("Gold Necklace", "Lvl 2"),
    Accessory("Crystal Bracelet", "Lvl 5"),
    Accessory("Pearl Ring", "Lvl 8")
)
private val headFaceItems = listOf(
    Accessory("Mini Crown", "Lvl 6"),
    Accessory("Silk Bow", "Lvl 5"),
    Accessory("Golden Halo", "7-day streak")
)
private val visualEffectsItems = listOf(
    Accessory("Sparkles", "Lvl 3"),
    Accessory("Rose Aura", "Lvl 7"),
    Accessory("Stardust", "Lvl 10")
)
private val decorativeItems = listOf(
    Accessory("Tiny Flower", "Lvl 7"),
    Accessory("Satin Ribbon", "Lvl 4"),
    Accessory("Red Rose", "Lvl 9")
)

private data class BubbleStyle(
    val name: String,
    val locked: Boolean = false,
    val unlockNote: String = ""
)

private val bubbleStyles = listOf(
    BubbleStyle("Classic"),
    BubbleStyle("Outlined"),
    BubbleStyle("Minimal", locked = true, unlockNote = "Lvl 2"),
    BubbleStyle("Solid", locked = true, unlockNote = "Lvl 4")
)

@Composable
fun HydraLookScreen(onBack: () -> Unit = {}) {
    var showFace by remember { mutableStateOf(true) }
    var enableSpeech by remember { mutableStateOf(true) }
    var selectedShape by remember { mutableStateOf("Heart") }
    var selectedBubble by remember { mutableStateOf("Classic") }
    var useCustomMessages by remember { mutableStateOf(false) }
    var customMessageDraft by remember { mutableStateOf("") }
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
            HlHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(28.dp))
            BasicText(
                text = "Hydra's Look",
                style = TextStyle(
                    color = HlAccent,
                    fontSize = 30.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Personalise your companion",
                style = TextStyle(
                    color = HlLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            LivePreviewCard(showSpeech = enableSpeech, showFace = showFace)

            Spacer(modifier = Modifier.height(20.dp))
            HlSectionLabel("DISPLAY")
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(hlCardBrush)
                    .border(
                        brush = hlCardBorder,
                        width = 0.95.dp,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    ToggleRow(
                        title = "Show Hydra's Face",
                        subtitle = "Lets Hydra express emotions and mood",
                        checked = showFace,
                        onChange = { showFace = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ToggleRow(
                        title = "Enable Speech",
                        subtitle = "Hydra shares thoughts when she can see you",
                        checked = enableSpeech,
                        onChange = { enableSpeech = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HlSectionLabel("SHAPE")
            Spacer(modifier = Modifier.height(10.dp))
            ShapeGrid(selected = selectedShape, onSelect = { selectedShape = it })

            Spacer(modifier = Modifier.height(22.dp))
            HlSectionLabel("ACCESSORIES")
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Collect items as you grow with Hydra",
                modifier = Modifier.padding(start = 6.dp),
                style = TextStyle(
                    color = HlLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )

            Spacer(modifier = Modifier.height(14.dp))
            AccessorySubsection("💎", "JEWELLERY", jewelleryItems)
            Spacer(modifier = Modifier.height(18.dp))
            AccessorySubsection("👑", "HEAD & FACE", headFaceItems)
            Spacer(modifier = Modifier.height(18.dp))
            AccessorySubsection("✨", "VISUAL EFFECTS", visualEffectsItems)
            Spacer(modifier = Modifier.height(18.dp))
            AccessorySubsection("🌸", "DECORATIVE", decorativeItems)

            Spacer(modifier = Modifier.height(22.dp))
            HlSectionLabel("SPEECH BUBBLES")
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Pick a bubble design for Hydra's speech",
                modifier = Modifier.padding(start = 6.dp),
                style = TextStyle(
                    color = HlLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            BubbleRow(
                items = bubbleStyles,
                selected = selectedBubble,
                onSelect = { selectedBubble = it }
            )

            Spacer(modifier = Modifier.height(22.dp))
            HlSectionLabel("CUSTOM MESSAGES")
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Write your own messages for Hydra to say",
                modifier = Modifier.padding(start = 6.dp),
                style = TextStyle(
                    color = HlLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(hlCardBrush)
                    .border(
                        brush = hlCardBorder,
                        width = 0.95.dp,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                ToggleRow(
                    title = "Use Custom Messages",
                    subtitle = "Your words replace Hydra's default speech",
                    checked = useCustomMessages,
                    onChange = { useCustomMessages = it }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            CustomMessageInput(
                value = customMessageDraft,
                onValueChange = { customMessageDraft = it },
                onAdd = { customMessageDraft = "" }
            )

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun HlHeader(onBack: () -> Unit) {
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
                    color = HlAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = HlAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = HlAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        BasicText(
            text = "Hydra's Look",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = HlAccent,
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
private fun HlSectionLabel(label: String) {
    BasicText(
        text = label,
        modifier = Modifier.padding(start = 6.dp),
        style = TextStyle(
            color = HlAccent.copy(alpha = 0.72f),
            fontSize = 13.sp,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.0.sp
        )
    )
}

@Composable
private fun LivePreviewCard(showSpeech: Boolean, showFace: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(hlCardBrush)
            .border(
                brush = hlCardBorder,
                width = 0.95.dp,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (showSpeech) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF223158).copy(alpha = 0.55f),
                                    Color(0xFF0E1730).copy(alpha = 0.72f)
                                )
                            )
                        )
                        .border(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.28f),
                                    Color.White.copy(alpha = 0.08f),
                                    HlAccent.copy(alpha = 0.05f)
                                )
                            ),
                            width = 1.0.dp,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    BasicText(
                        text = "It's really late iris and nothing was logged today 💔 before you slee…",
                        style = TextStyle(
                            color = HlLavender,
                            fontSize = 16.sp,
                            lineHeight = 21.sp,
                            fontFamily = AppFont,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            PreviewHeart(showFace = showFace)
            Spacer(modifier = Modifier.height(10.dp))
            BasicText(
                text = "LIVE PREVIEW",
                style = TextStyle(
                    color = HlMutedDim.copy(alpha = 0.78f),
                    fontSize = 12.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.0.sp
                )
            )
        }
    }
}

@Composable
private fun PreviewHeart(showFace: Boolean) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.55f)
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
        drawPath(
            path = heartPath,
            color = Color(0xFF8FAAFF).copy(alpha = 0.055f),
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
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
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        Color(0xFF1D3C79).copy(alpha = 0.036f),
                        Color(0xFF10244D).copy(alpha = 0.130f)
                    ),
                    startY = h * 0.46f,
                    endY = h
                )
            )
        }

        if (showFace) {
            val faceCenterY = h * 0.350f
            val faceScale = 0.88f
            val eyeSpacing = 9.4.dp.toPx() * faceScale
            drawCircle(
                color = Color.White.copy(alpha = 0.92f),
                radius = 0.72.dp.toPx() * faceScale,
                center = Offset(w * 0.5f - eyeSpacing / 2, faceCenterY)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.92f),
                radius = 0.72.dp.toPx() * faceScale,
                center = Offset(w * 0.5f + eyeSpacing / 2, faceCenterY)
            )
            val browW = 4.1.dp.toPx() * faceScale
            val browH = 1.75.dp.toPx() * faceScale
            val browOffset = 3.4.dp.toPx() * faceScale
            drawLine(
                color = Color.White.copy(alpha = 0.94f),
                start = Offset(w * 0.5f - eyeSpacing / 2 - browW / 2, faceCenterY - browOffset - browH),
                end = Offset(w * 0.5f - eyeSpacing / 2 + browW / 2, faceCenterY - browOffset),
                strokeWidth = 0.85.dp.toPx() * faceScale,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White.copy(alpha = 0.94f),
                start = Offset(w * 0.5f + eyeSpacing / 2 + browW / 2, faceCenterY - browOffset - browH),
                end = Offset(w * 0.5f + eyeSpacing / 2 - browW / 2, faceCenterY - browOffset),
                strokeWidth = 0.85.dp.toPx() * faceScale,
                cap = StrokeCap.Round
            )
            val frown = Path().apply {
                moveTo(w * 0.5f - 3.7.dp.toPx() * faceScale, faceCenterY + 7.4.dp.toPx() * faceScale)
                cubicTo(
                    w * 0.5f - 1.9.dp.toPx() * faceScale,
                    faceCenterY + 4.2.dp.toPx() * faceScale,
                    w * 0.5f + 1.9.dp.toPx() * faceScale,
                    faceCenterY + 4.2.dp.toPx() * faceScale,
                    w * 0.5f + 3.7.dp.toPx() * faceScale,
                    faceCenterY + 7.4.dp.toPx() * faceScale
                )
            }
            drawPath(
                path = frown,
                color = Color.White.copy(alpha = 0.92f),
                style = Stroke(width = 0.78.dp.toPx() * faceScale, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = title,
                style = TextStyle(
                    color = HlLavender,
                    fontSize = 19.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            BasicText(
                text = subtitle,
                style = TextStyle(
                    color = HlMuted.copy(alpha = 0.80f),
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        HydraSwitch(checked = checked, onChange = onChange)
    }
}

@Composable
private fun HydraSwitch(checked: Boolean, onChange: (Boolean) -> Unit) {
    val trackWidth = 52.dp
    val trackHeight = 30.dp
    val thumbSize = 24.dp
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
                        colors = listOf(
                            Color(0xFF1B2A52),
                            Color(0xFF18254A)
                        )
                    )
                }
            )
            .border(
                width = 0.8.dp,
                color = if (checked) HlAccent.copy(alpha = 0.50f) else Color.White.copy(alpha = 0.10f),
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
private fun ShapeGrid(selected: String, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        shapeOptions.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    ShapeTile(
                        option = item,
                        selected = item.name == selected,
                        onClick = { onSelect(item.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size < 3) {
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ShapeTile(
    option: ShapeOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1.05f)
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) hlSelectedBrush else hlTileBrush)
            .border(
                brush = if (selected) hlSelectedBorder else hlTileBorder,
                width = if (selected) 1.3.dp else 0.85.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BasicText(
                text = option.emoji,
                style = TextStyle(fontSize = 32.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicText(
                text = option.name,
                style = TextStyle(
                    color = if (selected) HlAccent else HlLavender,
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
private fun AccessorySubsection(emoji: String, label: String, items: List<Accessory>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicText(
                text = emoji,
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            BasicText(
                text = label,
                style = TextStyle(
                    color = HlAccent.copy(alpha = 0.78f),
                    fontSize = 13.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.0.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEach { item ->
                LockedTile(item, modifier = Modifier.weight(1f))
            }
            if (items.size < 3) {
                repeat(3 - items.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun LockedTile(item: Accessory, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(0.95f)
            .clip(RoundedCornerShape(18.dp))
            .background(hlTileBrush)
            .border(
                brush = hlTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BasicText(
                text = "🔒",
                style = TextStyle(fontSize = 26.sp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            BasicText(
                text = item.name,
                style = TextStyle(
                    color = HlLavender.copy(alpha = 0.78f),
                    fontSize = 13.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            BasicText(
                text = item.unlockNote,
                style = TextStyle(
                    color = HlMuted.copy(alpha = 0.62f),
                    fontSize = 12.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun BubbleRow(items: List<BubbleStyle>, selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.forEach { style ->
            BubbleTile(
                style = style,
                selected = style.name == selected,
                onClick = { if (!style.locked) onSelect(style.name) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BubbleTile(
    style: BubbleStyle,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(0.92f)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) hlSelectedBrush else hlTileBrush)
            .border(
                brush = if (selected) hlSelectedBorder else hlTileBorder,
                width = if (selected) 1.3.dp else 0.85.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BubbleGlyph(style = style.name)
            Spacer(modifier = Modifier.height(6.dp))
            BasicText(
                text = style.name,
                style = TextStyle(
                    color = if (selected) HlAccent else HlLavender,
                    fontSize = 14.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            if (style.locked) {
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = style.unlockNote,
                    style = TextStyle(
                        color = HlMuted.copy(alpha = 0.62f),
                        fontSize = 12.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}

@Composable
private fun BubbleGlyph(style: String) {
    Canvas(modifier = Modifier.size(width = 38.dp, height = 28.dp)) {
        val w = size.width
        val h = size.height
        when (style) {
            "Classic" -> {
                drawCircle(
                    color = Color(0xFFB8CFFF),
                    radius = h * 0.42f,
                    center = Offset(w * 0.40f, h * 0.42f)
                )
                drawCircle(
                    color = Color(0xFFFFFFFF).copy(alpha = 0.85f),
                    radius = h * 0.12f,
                    center = Offset(w * 0.28f, h * 0.32f)
                )
                drawCircle(
                    color = Color(0xFF8FB8FF),
                    radius = h * 0.16f,
                    center = Offset(w * 0.72f, h * 0.78f)
                )
                drawCircle(
                    color = Color(0xFF8FB8FF).copy(alpha = 0.65f),
                    radius = h * 0.10f,
                    center = Offset(w * 0.86f, h * 0.94f)
                )
            }
            "Outlined" -> {
                drawRect(
                    color = Color.White.copy(alpha = 0.92f),
                    topLeft = Offset(w * 0.22f, h * 0.18f),
                    size = androidx.compose.ui.geometry.Size(w * 0.56f, h * 0.62f)
                )
            }
            "Minimal" -> {
                drawCircle(
                    color = Color(0xFF5A6582).copy(alpha = 0.70f),
                    radius = h * 0.30f,
                    center = Offset(w * 0.40f, h * 0.50f)
                )
                drawCircle(
                    color = Color(0xFF5A6582).copy(alpha = 0.45f),
                    radius = h * 0.22f,
                    center = Offset(w * 0.68f, h * 0.40f)
                )
            }
            "Solid" -> {
                drawCircle(
                    color = Color(0xFF7C5BB8),
                    radius = h * 0.42f,
                    center = Offset(w * 0.50f, h * 0.52f)
                )
            }
        }
    }
}

@Composable
private fun CustomMessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(hlTileBrush)
            .border(
                brush = hlTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                BasicText(
                    text = "Add a message for Hydra…",
                    style = TextStyle(
                        color = HlMuted.copy(alpha = 0.65f),
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
                cursorBrush = SolidColor(HlAccent),
                textStyle = TextStyle(
                    color = HlLavender,
                    fontSize = 16.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(28.dp)
                .clickable(onClick = onAdd),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(18.dp)) {
                val w = size.width
                val h = size.height
                drawLine(
                    color = HlMuted.copy(alpha = 0.78f),
                    start = Offset(w * 0.50f, h * 0.10f),
                    end = Offset(w * 0.50f, h * 0.90f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = HlMuted.copy(alpha = 0.78f),
                    start = Offset(w * 0.10f, h * 0.50f),
                    end = Offset(w * 0.90f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
