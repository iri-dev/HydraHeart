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
import androidx.compose.ui.geometry.Size
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

private val ProfileAccent = Color(0xFF6E9BFF)
private val ProfileLavender = Color(0xFFEAF0FF)
private val ProfileMuted = Color(0xFFB8C6EE)
private val ProfileMutedDim = Color(0xFF8C9BC8)
private val ProfileCoral = Color(0xFFE89A8E)
private val ProfileResetRed = Color(0xFFE57978)

private val profileCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2C3B66).copy(alpha = 0.55f),
        Color(0xFF16213E).copy(alpha = 0.78f),
        Color(0xFF0C1428).copy(alpha = 0.82f)
    )
)

private val profileCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.50f),
        Color.White.copy(alpha = 0.13f),
        ProfileAccent.copy(alpha = 0.10f)
    )
)

private val innerTileBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF26345E).copy(alpha = 0.70f),
        Color(0xFF131F3D).copy(alpha = 0.85f)
    )
)

private val innerTileBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.22f),
        Color.White.copy(alpha = 0.06f),
        ProfileAccent.copy(alpha = 0.05f)
    )
)

@Composable
fun YourProfileScreen(
    onBack: () -> Unit = {},
    onReset: () -> Unit = {}
) {
    var name by remember { mutableStateOf("iris") }
    var gender by remember { mutableStateOf("Female") }
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
            ProfileHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(22.dp))
            ProfileHeroHeart()

            Spacer(modifier = Modifier.height(10.dp))
            BasicText(
                text = "Your Profile",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    color = ProfileAccent,
                    fontSize = 34.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            BasicText(
                text = "Hydra uses your profile for smarter targets",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    color = ProfileLavender,
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(28.dp))
            SectionLabel("IDENTITY")
            Spacer(modifier = Modifier.height(10.dp))
            ProfileCard {
                NameField(name = name, onValueChange = { name = it })
                Spacer(modifier = Modifier.height(10.dp))
                LabeledRow(emoji = "🎂", label = "Birthday", value = "07/03/2006")
                Spacer(modifier = Modifier.height(10.dp))
                LabeledRow(emoji = "🎀", label = "Age", value = "20 years old")
            }

            Spacer(modifier = Modifier.height(22.dp))
            SectionLabel("GENDER")
            Spacer(modifier = Modifier.height(10.dp))
            GenderPills(selected = gender, onSelect = { gender = it })

            Spacer(modifier = Modifier.height(22.dp))
            SectionLabel("BODY")
            Spacer(modifier = Modifier.height(10.dp))
            ProfileCard {
                LabeledRow(emoji = "📏", label = "Height", value = "185 cm")
                Spacer(modifier = Modifier.height(10.dp))
                LabeledRow(emoji = "⚖️", label = "Weight", value = "98 kg")
            }

            Spacer(modifier = Modifier.height(22.dp))
            SectionLabel("LIFESTYLE")
            Spacer(modifier = Modifier.height(10.dp))
            ProfileCard {
                LabeledRowWithSubtitle(
                    emoji = "💪",
                    label = "Activity Level",
                    value = "Very Active",
                    subtitle = "Intense training, high output"
                )
                Spacer(modifier = Modifier.height(10.dp))
                LabeledRowWithSubtitle(
                    emoji = "🌤️",
                    label = "Climate",
                    value = "Moderate",
                    subtitle = "Comfortable conditions"
                )
            }

            Spacer(modifier = Modifier.height(26.dp))
            RecalculateButton()

            Spacer(modifier = Modifier.height(26.dp))
            SectionLabel("DATA & RESET", color = ProfileResetRed.copy(alpha = 0.85f))
            Spacer(modifier = Modifier.height(10.dp))
            DataResetCard(onReset = onReset)

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun ProfileHeader(onBack: () -> Unit) {
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
                    color = ProfileAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ProfileAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ProfileAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        BasicText(
            text = "Your Profile",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = ProfileAccent,
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
private fun ProfileHeroHeart() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.62f)
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
                            Color(0xFFDDE8FF).copy(alpha = 0.105f),
                            Color(0xFF9EB8FF).copy(alpha = 0.055f),
                            Color(0xFF6288F0).copy(alpha = 0.020f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.36f, h * 0.34f),
                        radius = w * 0.84f
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
            val faceScale = 0.88f
            val eyeSpacing = 9.4.dp.toPx() * faceScale
            val eyeY = faceCenterY

            drawCircle(
                color = Color.White.copy(alpha = 0.92f),
                radius = 0.72.dp.toPx() * faceScale,
                center = Offset(w * 0.5f - eyeSpacing / 2, eyeY)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.92f),
                radius = 0.72.dp.toPx() * faceScale,
                center = Offset(w * 0.5f + eyeSpacing / 2, eyeY)
            )

            val browW = 4.1.dp.toPx() * faceScale
            val browH = 1.75.dp.toPx() * faceScale
            val browOffset = 3.4.dp.toPx() * faceScale
            drawLine(
                color = Color.White.copy(alpha = 0.94f),
                start = Offset(w * 0.5f - eyeSpacing / 2 - browW / 2, eyeY - browOffset - browH),
                end = Offset(w * 0.5f - eyeSpacing / 2 + browW / 2, eyeY - browOffset),
                strokeWidth = 0.85.dp.toPx() * faceScale,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White.copy(alpha = 0.94f),
                start = Offset(w * 0.5f + eyeSpacing / 2 + browW / 2, eyeY - browOffset - browH),
                end = Offset(w * 0.5f + eyeSpacing / 2 - browW / 2, eyeY - browOffset),
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
private fun ProfileCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(profileCardBrush)
            .border(
                brush = profileCardBorder,
                width = 0.95.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column { content() }
    }
}

@Composable
private fun SectionLabel(
    label: String,
    color: Color = ProfileCoral.copy(alpha = 0.78f)
) {
    BasicText(
        text = label,
        modifier = Modifier.padding(start = 6.dp),
        style = TextStyle(
            color = color,
            fontSize = 13.sp,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.0.sp
        )
    )
}

@Composable
private fun NameField(name: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(innerTileBrush)
            .border(
                brush = innerTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = name,
            onValueChange = onValueChange,
            singleLine = true,
            cursorBrush = SolidColor(ProfileAccent),
            textStyle = TextStyle(
                color = ProfileLavender,
                fontSize = 22.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LabeledRow(emoji: String, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(innerTileBrush)
            .border(
                brush = innerTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable {}
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EmojiCircle(emoji)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = label,
                style = TextStyle(
                    color = ProfileAccent.copy(alpha = 0.72f),
                    fontSize = 13.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            BasicText(
                text = value,
                style = TextStyle(
                    color = ProfileLavender,
                    fontSize = 20.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Chevron()
    }
}

@Composable
private fun LabeledRowWithSubtitle(
    emoji: String,
    label: String,
    value: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(innerTileBrush)
            .border(
                brush = innerTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable {}
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EmojiCircle(emoji)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = label,
                style = TextStyle(
                    color = ProfileAccent.copy(alpha = 0.72f),
                    fontSize = 13.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            BasicText(
                text = value,
                style = TextStyle(
                    color = ProfileLavender,
                    fontSize = 20.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            BasicText(
                text = subtitle,
                style = TextStyle(
                    color = ProfileMuted.copy(alpha = 0.78f),
                    fontSize = 13.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
        }
        Chevron()
    }
}

@Composable
private fun EmojiCircle(emoji: String) {
    Box(
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = emoji,
            style = TextStyle(
                fontSize = 26.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun Chevron() {
    Canvas(modifier = Modifier.size(14.dp)) {
        val w = size.width
        val h = size.height
        drawLine(
            color = ProfileMuted.copy(alpha = 0.65f),
            start = Offset(w * 0.30f, h * 0.20f),
            end = Offset(w * 0.66f, h * 0.50f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = ProfileMuted.copy(alpha = 0.65f),
            start = Offset(w * 0.66f, h * 0.50f),
            end = Offset(w * 0.30f, h * 0.80f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun GenderPills(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOf("Female", "Male", "Other").forEach { option ->
            GenderPill(
                label = option,
                selected = option == selected,
                onClick = { onSelect(option) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun GenderPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF3A5BB8).copy(alpha = 0.65f),
                Color(0xFF24417F).copy(alpha = 0.78f)
            )
        )
    } else {
        innerTileBrush
    }
    val border = if (selected) {
        Brush.verticalGradient(
            colors = listOf(
                ProfileAccent.copy(alpha = 0.65f),
                ProfileAccent.copy(alpha = 0.25f)
            )
        )
    } else {
        innerTileBorder
    }
    val textColor = if (selected) ProfileAccent else ProfileLavender

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(
                brush = border,
                width = if (selected) 1.2.dp else 0.85.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = label,
            style = TextStyle(
                color = textColor,
                fontSize = 18.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun RecalculateButton() {
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
                        ProfileAccent.copy(alpha = 0.05f)
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
                text = "💕",
                style = TextStyle(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicText(
                text = "Recalculate with Hydra",
                style = TextStyle(
                    color = Color(0xFF0B1532),
                    fontSize = 19.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun DataResetCard(onReset: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(profileCardBrush)
            .border(
                brush = profileCardBorder,
                width = 0.95.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Column {
            BasicText(
                text = "Permanently erase all of Hydra's progress and memories — HP, streaks, history, unlocked personalities, and all customisations. The app will return to a fresh install as if opened for the very first time.",
                style = TextStyle(
                    color = ProfileLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF4A1F26).copy(alpha = 0.35f),
                                Color(0xFF2A0F14).copy(alpha = 0.45f)
                            )
                        )
                    )
                    .border(
                        width = 1.0.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ProfileResetRed.copy(alpha = 0.65f),
                                ProfileResetRed.copy(alpha = 0.25f)
                            )
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable(onClick = onReset)
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "Reset Hydra",
                    style = TextStyle(
                        color = ProfileResetRed,
                        fontSize = 18.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
