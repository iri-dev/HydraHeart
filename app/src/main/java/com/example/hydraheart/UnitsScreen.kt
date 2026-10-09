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

private val UnAccent = Color(0xFF6E9BFF)
private val UnLavender = Color(0xFFEAF0FF)
private val UnMuted = Color(0xFFB8C6EE)
private val UnMutedDim = Color(0xFF8C9BC8)

private val unCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2C3B66).copy(alpha = 0.45f),
        Color(0xFF16213E).copy(alpha = 0.68f),
        Color(0xFF0C1428).copy(alpha = 0.78f)
    )
)

private val unCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.45f),
        Color.White.copy(alpha = 0.10f),
        UnAccent.copy(alpha = 0.10f)
    )
)

private val unTileBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF26345E).copy(alpha = 0.55f),
        Color(0xFF131F3D).copy(alpha = 0.78f)
    )
)

private val unTileBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.22f),
        Color.White.copy(alpha = 0.06f),
        UnAccent.copy(alpha = 0.05f)
    )
)

private val unSelectedBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3A5BB8).copy(alpha = 0.60f),
        Color(0xFF24417F).copy(alpha = 0.75f)
    )
)

private val unSelectedBorder = Brush.verticalGradient(
    colors = listOf(
        UnAccent.copy(alpha = 0.90f),
        UnAccent.copy(alpha = 0.45f)
    )
)

private data class UnitOption(val symbol: String, val name: String, val description: String)

private val unitOptions = listOf(
    UnitOption("ml", "Millilitres", "Most common worldwide"),
    UnitOption("oz", "Fluid Ounces", "US & UK standard"),
    UnitOption("L", "Litres", "Larger volume tracking")
)

@Composable
fun UnitsScreen(onBack: () -> Unit = {}) {
    var selected by remember { mutableStateOf("ml") }
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
            UnHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(28.dp))
            BasicText(
                text = "Measurement",
                style = TextStyle(
                    color = UnAccent,
                    fontSize = 30.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Your way, your world",
                style = TextStyle(
                    color = UnLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )

            Spacer(modifier = Modifier.height(28.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                UnitsSmilingHeart()
            }

            Spacer(modifier = Modifier.height(18.dp))
            BasicText(
                text = "Precise and personal — just how I like it 💧",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    color = UnLavender,
                    fontSize = 16.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(26.dp))
            UnSectionLabel("VOLUME")
            Spacer(modifier = Modifier.height(10.dp))
            VolumeGrid(selected = selected, onSelect = { selected = it })

            Spacer(modifier = Modifier.height(20.dp))
            LivePreviewCard(selected = selected)

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun UnHeader(onBack: () -> Unit) {
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
                    color = UnAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = UnAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = UnAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        BasicText(
            text = "Units",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = UnAccent,
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
private fun UnSectionLabel(label: String) {
    BasicText(
        text = label,
        modifier = Modifier.padding(start = 6.dp),
        style = TextStyle(
            color = UnAccent.copy(alpha = 0.72f),
            fontSize = 13.sp,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.0.sp
        )
    )
}

@Composable
private fun UnitsSmilingHeart() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.40f)
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
        }

        val faceCenterY = h * 0.420f
        val faceScale = 1.00f
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
private fun VolumeGrid(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        unitOptions.forEach { option ->
            VolumeTile(
                option = option,
                selected = option.symbol == selected,
                onClick = { onSelect(option.symbol) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun VolumeTile(
    option: UnitOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(0.88f)
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) unSelectedBrush else unTileBrush)
            .border(
                brush = if (selected) unSelectedBorder else unTileBorder,
                width = if (selected) 1.3.dp else 0.85.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BasicText(
                text = option.symbol,
                style = TextStyle(
                    color = if (selected) UnAccent else UnLavender,
                    fontSize = 44.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicText(
                text = option.name,
                style = TextStyle(
                    color = if (selected) UnAccent.copy(alpha = 0.88f) else UnMuted.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            BasicText(
                text = option.description,
                style = TextStyle(
                    color = UnMutedDim.copy(alpha = 0.78f),
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun LivePreviewCard(selected: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(unCardBrush)
            .border(
                brush = unCardBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column {
            BasicText(
                text = "LIVE PREVIEW",
                style = TextStyle(
                    color = UnAccent.copy(alpha = 0.72f),
                    fontSize = 12.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.0.sp
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicText(
                    text = "Water bottle",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        color = UnLavender,
                        fontSize = 18.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
                ConversionBar(selected = selected)
            }
        }
    }
}

@Composable
private fun ConversionBar(selected: String) {
    val entries = listOf(
        "ml" to "500 ml",
        "oz" to "16.9 oz",
        "L" to "0.5 L"
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        entries.forEachIndexed { index, (key, label) ->
            val isSelected = key == selected
            BasicText(
                text = label,
                style = TextStyle(
                    color = if (isSelected) UnAccent else UnMutedDim.copy(alpha = 0.65f),
                    fontSize = if (isSelected) 22.sp else 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            if (index < entries.size - 1) {
                BasicText(
                    text = "  ·  ",
                    style = TextStyle(
                        color = UnMutedDim.copy(alpha = 0.55f),
                        fontSize = 15.sp,
                        fontFamily = AppFont
                    )
                )
            }
        }
    }
}
