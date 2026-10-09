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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont

private val DpAccent = Color(0xFF5F89FF)
private val DpLavender = Color(0xFFEAF0FF)
private val DpMuted = Color(0xFFB8C6EE)

private val dpCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2D3E70).copy(alpha = 0.62f),
        Color(0xFF1A2A55).copy(alpha = 0.78f),
        Color(0xFF101D3D).copy(alpha = 0.84f)
    )
)

private val dpCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.64f),
        Color.White.copy(alpha = 0.22f),
        DpAccent.copy(alpha = 0.15f)
    )
)

@Composable
fun DeveloperProfileScreen(onBack: () -> Unit = {}) {
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
            DeveloperHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(48.dp))
            DeveloperCard()

            Spacer(modifier = Modifier.height(44.dp))
            DeveloperText(
                text = "Thank you for being part of this journey. \uD83E\uDD0D",
                modifier = Modifier.fillMaxWidth(),
                color = DpMuted.copy(alpha = 0.82f),
                fontSize = 18,
                lineHeight = 24,
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
private fun DeveloperHeader(onBack: () -> Unit) {
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
                    color = DpAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = DpAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = DpAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        DeveloperText(
            text = "Developer Profile",
            modifier = Modifier.align(Alignment.Center),
            color = DpAccent,
            fontSize = 27,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DeveloperCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(dpCardBrush)
            .border(
                brush = dpCardBorder,
                width = 1.35.dp,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(horizontal = 24.dp, vertical = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DeveloperText(
            text = "iri.dev",
            color = DpAccent,
            fontSize = 32,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(34.dp))
        DeveloperText(
            text = "I am an independent developer\ndedicated to crafting\nhigh-quality, intentional digital\nexperiences that respect the user\nand enhance daily life.",
            color = DpLavender,
            fontSize = 24,
            lineHeight = 36,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(34.dp))
        DeveloperText(
            text = "HydraHeart was born from a desire to\nmake self-care feel beautiful rather\nthan mechanical. Every animation,\nsound, and word is chosen to make\nyour hydration journey feel personal,\ncalming, and rewarding.",
            color = DpMuted.copy(alpha = 0.78f),
            fontSize = 20,
            lineHeight = 31,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(42.dp))
        GitHubButton()
    }
}

@Composable
private fun GitHubButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.78f)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF263B7A).copy(alpha = 0.58f),
                        Color(0xFF172A62).copy(alpha = 0.72f)
                    )
                )
            )
            .clickable {}
            .padding(vertical = 17.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DeveloperText(
                text = "\uD83D\uDCBB",
                color = DpLavender,
                fontSize = 24,
                fontFamily = FontFamily.Default,
                fontStyle = null
            )
            Spacer(modifier = Modifier.width(14.dp))
            DeveloperText(
                text = "GitHub",
                color = DpAccent,
                fontSize = 23,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DeveloperText(
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
