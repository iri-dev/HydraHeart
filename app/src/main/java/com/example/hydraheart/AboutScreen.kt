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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val AbAccent = Color(0xFF5F89FF)
private val AbLavender = Color(0xFFEAF0FF)
private val AbMuted = Color(0xFFB8C6EE)

private val abCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2D3E70).copy(alpha = 0.62f),
        Color(0xFF1A2A55).copy(alpha = 0.78f),
        Color(0xFF101D3D).copy(alpha = 0.84f)
    )
)

private val abCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.64f),
        Color.White.copy(alpha = 0.22f),
        AbAccent.copy(alpha = 0.15f)
    )
)

private data class AboutAction(val icon: String, val label: String)

private val legalActions = listOf(
    AboutAction("\uD83D\uDCC4", "Privacy Policy"),
    AboutAction("\uD83D\uDCCB", "Terms of Service")
)

private val supportActions = listOf(
    AboutAction("\u2B50", "Rate HydraHeart"),
    AboutAction("\u2709\uFE0F", "Contact Support"),
    AboutAction("\uD83D\uDD17", "Share with a friend")
)

@Composable
fun AboutScreen(
    onBack: () -> Unit = {},
    onDeveloperClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {}
) {
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
            AboutHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(32.dp))
            HeroAboutCard()

            Spacer(modifier = Modifier.height(32.dp))
            AboutSectionLabel("ABOUT")
            Spacer(modifier = Modifier.height(12.dp))
            AboutInfoCard(onDeveloperClick = onDeveloperClick)

            Spacer(modifier = Modifier.height(30.dp))
            AboutSectionLabel("LEGAL")
            Spacer(modifier = Modifier.height(12.dp))
            ActionCard(
                actions = legalActions,
                onActionClick = { action ->
                    if (action.label == "Privacy Policy") {
                        onPrivacyPolicyClick()
                    }
                }
            )

            Spacer(modifier = Modifier.height(30.dp))
            AboutSectionLabel("SUPPORT")
            Spacer(modifier = Modifier.height(12.dp))
            ActionCard(actions = supportActions)

            Spacer(modifier = Modifier.height(34.dp))
            AboutText(
                text = "Crafted with love and intention \uD83D\uDC97",
                modifier = Modifier.fillMaxWidth(),
                color = AbMuted.copy(alpha = 0.86f),
                fontSize = 17,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            AboutText(
                text = "HydraHeart \u00A9 2026",
                modifier = Modifier.fillMaxWidth(),
                color = AbMuted.copy(alpha = 0.52f),
                fontSize = 15,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier
                    .height(48.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun AboutHeader(onBack: () -> Unit) {
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
                    color = AbAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = AbAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = AbAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        AboutText(
            text = "About",
            modifier = Modifier.align(Alignment.Center),
            color = AbAccent,
            fontSize = 26,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HeroAboutCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(abCardBrush)
            .border(
                brush = abCardBorder,
                width = 1.35.dp,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.06f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.50f, size.height * 0.18f),
                    radius = size.width * 0.62f
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AboutLogo(modifier = Modifier.size(108.dp))
            Spacer(modifier = Modifier.height(34.dp))
            AboutText(
                text = "HydraHeart",
                color = AbLavender,
                fontSize = 31,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            AboutText(
                text = "A fresh start is just one glass away. \uD83D\uDCA7",
                color = AbMuted.copy(alpha = 0.88f),
                fontSize = 18,
                lineHeight = 24,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(44.dp))
            AboutText(
                text = "Version 1.0.0",
                color = AbMuted.copy(alpha = 0.42f),
                fontSize = 14,
                letterSpacing = 1.6,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AboutInfoCard(onDeveloperClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(abCardBrush)
            .border(
                brush = abCardBorder,
                width = 1.2.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 22.dp, vertical = 20.dp)
    ) {
        Column {
            AboutText(
                text = "HydraHeart",
                color = AbLavender,
                fontSize = 23,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            AboutText(
                text = "A gentle hydration companion designed to make\nconsistency feel effortless and rewarding.",
                color = AbMuted.copy(alpha = 0.78f),
                fontSize = 17,
                lineHeight = 24
            )
            Spacer(modifier = Modifier.height(34.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onDeveloperClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AboutText(
                        text = "Crafted by",
                        color = AbMuted.copy(alpha = 0.50f),
                        fontSize = 16,
                        letterSpacing = 1.0
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AboutText(
                        text = "iri.dev",
                        color = AbAccent,
                        fontSize = 27,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AboutText(
                        text = "Independently designed and built with care",
                        color = AbMuted.copy(alpha = 0.78f),
                        fontSize = 16,
                        lineHeight = 22
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                AboutChevron()
            }
        }
    }
}

@Composable
private fun ActionCard(
    actions: List<AboutAction>,
    onActionClick: (AboutAction) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(abCardBrush)
            .border(
                brush = abCardBorder,
                width = 1.2.dp,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        actions.forEachIndexed { index, action ->
            ActionRow(
                action = action,
                onClick = { onActionClick(action) }
            )
            if (index < actions.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 90.dp, end = 22.dp)
                        .height(0.7.dp)
                        .background(Color.White.copy(alpha = 0.055f))
                )
            }
        }
    }
}

@Composable
private fun ActionRow(
    action: AboutAction,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconTile(icon = action.icon)
        Spacer(modifier = Modifier.width(18.dp))
        AboutText(
            text = action.label,
            modifier = Modifier.weight(1f),
            color = AbLavender,
            fontSize = 25,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        AboutChevron()
    }
}

@Composable
private fun IconTile(icon: String) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3A5596).copy(alpha = 0.72f),
                        Color(0xFF243A78).copy(alpha = 0.80f)
                    )
                )
            )
            .border(
                width = 0.8.dp,
                color = Color.White.copy(alpha = 0.20f),
                shape = RoundedCornerShape(14.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        AboutText(
            text = icon,
            color = AbLavender,
            fontSize = 28,
            fontFamily = FontFamily.Default,
            fontStyle = null,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AboutLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val w = size.width
        val h = size.height
        val c = center

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF62F0FF).copy(alpha = 0.95f),
                    Color(0xFF3244A0).copy(alpha = 0.92f),
                    Color(0xFF171651).copy(alpha = 0.98f)
                ),
                center = Offset(w * 0.36f, h * 0.30f),
                radius = w * 0.66f
            ),
            radius = w * 0.48f,
            center = c
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.42f),
            radius = w * 0.48f,
            center = c,
            style = Stroke(width = 2.0.dp.toPx())
        )
        drawCircle(
            color = Color(0xFF04123B).copy(alpha = 0.66f),
            radius = w * 0.36f,
            center = c
        )

        repeat(3) { index ->
            val radius = w * (0.44f - index * 0.025f)
            val path = Path()
            for (i in 0..96) {
                val angle = (i / 96f) * 360f
                val wave = sinDeg(angle * 2f + index * 65f) * w * 0.028f
                val r = radius + wave
                val x = c.x + cosDeg(angle) * r
                val y = c.y + sinDeg(angle) * r
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(
                path = path,
                color = when (index) {
                    0 -> Color(0xFF77F6FF).copy(alpha = 0.82f)
                    1 -> Color(0xFFFF5EDB).copy(alpha = 0.70f)
                    else -> Color(0xFFBDF7FF).copy(alpha = 0.55f)
                },
                style = Stroke(width = (3.0f - index * 0.35f).dp.toPx(), cap = StrokeCap.Round)
            )
        }

        val innerSize = w * 0.50f
        val innerTopLeft = Offset(c.x - innerSize / 2, c.y - innerSize / 2)
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.96f),
                    Color(0xFFD9E4FF).copy(alpha = 0.88f)
                )
            ),
            topLeft = innerTopLeft,
            size = Size(innerSize, innerSize),
            cornerRadius = CornerRadius(innerSize * 0.22f, innerSize * 0.22f)
        )

        val heart = Path().apply {
            val left = c.x - innerSize * 0.34f
            val top = c.y - innerSize * 0.24f
            val hw = innerSize * 0.68f
            val hh = innerSize * 0.62f
            moveTo(left + hw * 0.50f, top + hh * 0.92f)
            cubicTo(left + hw * 0.26f, top + hh * 0.76f, left + hw * 0.05f, top + hh * 0.58f, left + hw * 0.05f, top + hh * 0.32f)
            cubicTo(left + hw * 0.05f, top + hh * 0.08f, left + hw * 0.31f, top + hh * 0.00f, left + hw * 0.50f, top + hh * 0.24f)
            cubicTo(left + hw * 0.69f, top + hh * 0.00f, left + hw * 0.95f, top + hh * 0.08f, left + hw * 0.95f, top + hh * 0.32f)
            cubicTo(left + hw * 0.95f, top + hh * 0.58f, left + hw * 0.74f, top + hh * 0.76f, left + hw * 0.50f, top + hh * 0.92f)
            close()
        }
        drawPath(
            path = heart,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFFA7CB),
                    Color(0xFFFF74B2),
                    Color(0xFF4BB9FF)
                )
            )
        )
        clipPath(heart) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        Color(0xFF55D8FF).copy(alpha = 0.88f),
                        Color(0xFF1C8BE7).copy(alpha = 0.96f)
                    )
                )
            )
            for (i in 0..3) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.30f),
                    radius = w * (0.012f + i * 0.002f),
                    center = Offset(c.x - w * 0.11f + i * w * 0.075f, c.y + w * (0.045f - i * 0.012f))
                )
            }
        }
        drawPath(
            path = heart,
            color = Color.White.copy(alpha = 0.76f),
            style = Stroke(width = 1.1.dp.toPx(), join = StrokeJoin.Round)
        )
    }
}

private fun sinDeg(degrees: Float): Float =
    sin(degrees * PI.toFloat() / 180f)

private fun cosDeg(degrees: Float): Float =
    cos(degrees * PI.toFloat() / 180f)

@Composable
private fun AboutSectionLabel(text: String) {
    AboutText(
        text = text,
        modifier = Modifier.padding(start = 6.dp),
        color = AbAccent.copy(alpha = 0.66f),
        fontSize = 13,
        fontWeight = FontWeight.Bold,
        letterSpacing = 3.0
    )
}

@Composable
private fun AboutChevron(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(16.dp)) {
        val w = size.width
        val h = size.height
        drawLine(
            color = AbAccent.copy(alpha = 0.45f),
            start = Offset(w * 0.30f, h * 0.20f),
            end = Offset(w * 0.66f, h * 0.50f),
            strokeWidth = 1.7.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = AbAccent.copy(alpha = 0.45f),
            start = Offset(w * 0.66f, h * 0.50f),
            end = Offset(w * 0.30f, h * 0.80f),
            strokeWidth = 1.7.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun AboutText(
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
        overflow = TextOverflow.Clip,
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
