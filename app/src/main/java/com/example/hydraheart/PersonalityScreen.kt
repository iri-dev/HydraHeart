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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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

private val PnAccent = Color(0xFF6E9BFF)
private val PnLavender = Color(0xFFEAF0FF)
private val PnMuted = Color(0xFFB8C6EE)

private val pnCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2C3B66).copy(alpha = 0.55f),
        Color(0xFF16213E).copy(alpha = 0.78f),
        Color(0xFF0C1428).copy(alpha = 0.82f)
    )
)

private val pnCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.50f),
        Color.White.copy(alpha = 0.13f),
        PnAccent.copy(alpha = 0.10f)
    )
)

private val pnTileBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF26345E).copy(alpha = 0.55f),
        Color(0xFF131F3D).copy(alpha = 0.78f)
    )
)

private val pnTileBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.22f),
        Color.White.copy(alpha = 0.06f),
        PnAccent.copy(alpha = 0.05f)
    )
)

private val pnTileSelectedBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3A5BB8).copy(alpha = 0.78f),
        Color(0xFF24417F).copy(alpha = 0.88f)
    )
)

private val pnTileSelectedBorder = Brush.verticalGradient(
    colors = listOf(
        PnAccent.copy(alpha = 0.85f),
        PnAccent.copy(alpha = 0.40f)
    )
)

private data class Personality(
    val emoji: String,
    val name: String,
    val description: String
)

private val visiblePersonalities = listOf(
    Personality("💕", "Bestie", "Your hype girl who celebrates everything"),
    Personality("👑", "Slay Queen", "Dramatic, extra, and fully committed"),
    Personality("🧸", "Soft Girl", "Tender, nurturing, and gentle"),
    Personality("😈", "Villain Era", "Chaotic, funny, lovingly unhinged"),
    Personality("🧘‍♀️", "Zen Master", "Calm, wise, and grounding"),
    Personality("💪", "Coach", "Direct, focused, results-oriented")
)

private const val HIDDEN_COUNT = 2

@Composable
fun PersonalityScreen(onBack: () -> Unit = {}) {
    var selected by remember { mutableStateOf("Bestie") }
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
            PnHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(28.dp))
            BasicText(
                text = "Who is Hydra to you?",
                style = TextStyle(
                    color = PnAccent,
                    fontSize = 26.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Choose how she speaks, encourages, and shows up for you",
                style = TextStyle(
                    color = PnLavender.copy(alpha = 0.88f),
                    fontSize = 14.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            PreviewCard(selected = selected)

            Spacer(modifier = Modifier.height(18.dp))
            PersonalityGrid(
                items = visiblePersonalities,
                selected = selected,
                onSelect = { selected = it }
            )

            Spacer(modifier = Modifier.height(18.dp))
            HiddenHint()

            Spacer(modifier = Modifier.height(18.dp))
            LockInButton()

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun PnHeader(onBack: () -> Unit) {
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
                    color = PnAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = PnAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = PnAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        BasicText(
            text = "Personality",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = PnAccent,
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
private fun PreviewCard(selected: String) {
    val message = when (selected) {
        "Bestie" -> "I am SO here for you, iris! 💕"
        "Slay Queen" -> "Werk it, iris — you OWN this. 👑"
        "Soft Girl" -> "Take it slow, sweet iris. 🧸"
        "Villain Era" -> "Hydrate or perish, iris. 😈"
        "Zen Master" -> "Be present, iris. Sip mindfully."
        "Coach" -> "Get the water in, iris. Now. 💪"
        else -> "I am SO here for you, iris! 💕"
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(pnCardBrush)
            .border(
                brush = pnCardBorder,
                width = 0.95.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MiniHeart(modifier = Modifier.size(74.dp))
            Spacer(modifier = Modifier.width(14.dp))
            BasicText(
                text = message,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    color = PnLavender,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun MiniHeart(modifier: Modifier = Modifier) {
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
            color = Color(0xFF8FAAFF).copy(alpha = 0.055f),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
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

        val faceCenterY = h * 0.350f
        val faceScale = 0.62f
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

@Composable
private fun PersonalityGrid(
    items: List<Personality>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    PersonalityTile(
                        item = item,
                        selected = item.name == selected,
                        onClick = { onSelect(item.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun PersonalityTile(
    item: Personality,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) pnTileSelectedBrush else pnTileBrush)
            .border(
                brush = if (selected) pnTileSelectedBorder else pnTileBorder,
                width = if (selected) 1.3.dp else 0.85.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Column {
            BasicText(
                text = item.emoji,
                style = TextStyle(fontSize = 32.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicText(
                text = item.name,
                style = TextStyle(
                    color = if (selected) PnAccent else PnLavender,
                    fontSize = 19.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            BasicText(
                text = item.description,
                style = TextStyle(
                    color = PnMuted.copy(alpha = 0.78f),
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}

@Composable
private fun HiddenHint() {
    val label = if (HIDDEN_COUNT == 1) {
        "1 hidden personality awaits"
    } else {
        "$HIDDEN_COUNT hidden personalities await"
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(pnCardBrush)
            .border(
                brush = pnCardBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicText(
                text = "🔒",
                style = TextStyle(fontSize = 22.sp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                BasicText(
                    text = label,
                    style = TextStyle(
                        color = PnLavender,
                        fontSize = 15.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = "Hydra reveals them through your journey",
                    style = TextStyle(
                        color = PnMuted.copy(alpha = 0.78f),
                        fontSize = 13.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}

@Composable
private fun LockInButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF8CA9FF),
                        Color(0xFF6188F0),
                        Color(0xFF4669D7)
                    )
                )
            )
            .border(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.50f),
                        Color.White.copy(alpha = 0.12f),
                        PnAccent.copy(alpha = 0.05f)
                    )
                ),
                width = 1.0.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {}
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicText(
                text = "Lock in this vibe",
                style = TextStyle(
                    color = Color(0xFF0B1532),
                    fontSize = 19.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            BasicText(
                text = "✨",
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }
}
