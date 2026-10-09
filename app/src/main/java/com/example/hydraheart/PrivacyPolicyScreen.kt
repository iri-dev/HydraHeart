package com.example.hydraheart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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

private val PpAccent = Color(0xFF5F89FF)
private val PpLavender = Color(0xFFEAF0FF)

private val ppCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2D3E70).copy(alpha = 0.62f),
        Color(0xFF1A2A55).copy(alpha = 0.78f),
        Color(0xFF101D3D).copy(alpha = 0.84f)
    )
)

private val ppCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.64f),
        Color.White.copy(alpha = 0.22f),
        PpAccent.copy(alpha = 0.15f)
    )
)

private val privacyPolicyText = """
At HydraHeart, your privacy is a core feature, not an afterthought.

1. Data Collection: We do not collect, store, or share any personal information. All your hydration data, profile settings, and mood logs are stored locally on your device.

2. Third Parties: No third-party tracking or analytics are used. We believe your habits should stay yours.

3. Permissions: We only ask for permissions necessary to run the app (like notifications for reminders).

4. Transparency: If you delete the app, all your data is permanently removed from your device. We have no way to recover it because we never had it.
""".trimIndent()

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit = {}) {
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
            PrivacyPolicyHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(78.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(ppCardBrush)
                    .border(
                        brush = ppCardBorder,
                        width = 1.3.dp,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 48.dp, vertical = 54.dp)
            ) {
                PolicyText(
                    text = privacyPolicyText,
                    color = PpLavender,
                    fontSize = 24,
                    lineHeight = 38
                )
            }

            Spacer(
                modifier = Modifier
                    .height(48.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun PrivacyPolicyHeader(onBack: () -> Unit) {
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
                    color = PpAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = PpAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = PpAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        PolicyText(
            text = "Privacy Policy",
            modifier = Modifier.align(Alignment.Center),
            color = PpAccent,
            fontSize = 28,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PolicyText(
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
