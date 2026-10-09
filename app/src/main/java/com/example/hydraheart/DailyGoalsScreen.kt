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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont

private val DgAccent = Color(0xFF6E9BFF)
private val DgLavender = Color(0xFFEAF0FF)
private val DgMuted = Color(0xFFB8C6EE)

private val dgCardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2C3B66).copy(alpha = 0.55f),
        Color(0xFF16213E).copy(alpha = 0.78f),
        Color(0xFF0C1428).copy(alpha = 0.82f)
    )
)

private val dgCardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.50f),
        Color.White.copy(alpha = 0.13f),
        DgAccent.copy(alpha = 0.10f)
    )
)

private val dgTileBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF26345E).copy(alpha = 0.55f),
        Color(0xFF131F3D).copy(alpha = 0.78f)
    )
)

private val dgTileBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.22f),
        Color.White.copy(alpha = 0.06f),
        DgAccent.copy(alpha = 0.05f)
    )
)

private val dgSelectedBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3A5BB8).copy(alpha = 0.78f),
        Color(0xFF24417F).copy(alpha = 0.88f)
    )
)

private val dgSelectedBorder = Brush.verticalGradient(
    colors = listOf(
        DgAccent.copy(alpha = 0.85f),
        DgAccent.copy(alpha = 0.40f)
    )
)

private data class BalanceTarget(val emoji: String, val name: String, val value: String)

private val balanceTargets = listOf(
    BalanceTarget("☕", "Caffeine", "400mg"),
    BalanceTarget("🍬", "Sugar", "50g"),
    BalanceTarget("🥛", "Calcium", "1000mg"),
    BalanceTarget("🍷", "Alcohol", "16g")
)

@Composable
fun DailyGoalsScreen(
    onBack: () -> Unit = {},
    currentGoalMl: Float = 3_000f,
    onSaveGoal: (Float) -> Unit = {}
) {
    var mode by remember { mutableStateOf("Custom") }
    var preset by remember { mutableStateOf("3L") }
    var customAmount by remember(currentGoalMl) { mutableStateOf(currentGoalMl.toInt().toString()) }
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
            DgHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    BasicText(
                        text = "Daily Flow",
                        style = TextStyle(
                            color = DgAccent,
                            fontSize = 30.sp,
                            fontFamily = AppFont,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    BasicText(
                        text = "Set your hydration intention",
                        style = TextStyle(
                            color = DgLavender.copy(alpha = 0.88f),
                            fontSize = 15.sp,
                            fontFamily = AppFont,
                            fontStyle = FontStyle.Italic
                        )
                    )
                }
                GettingStartedPill()
            }

            Spacer(modifier = Modifier.height(18.dp))
            HeroAmountCard(amountMl = customAmount.toIntOrNull() ?: 0)

            Spacer(modifier = Modifier.height(18.dp))
            ModeToggle(selected = mode, onSelect = { mode = it })

            Spacer(modifier = Modifier.height(14.dp))
            PresetRow(
                selected = preset,
                onSelect = { p ->
                    preset = p
                    customAmount = when (p) {
                        "1.5L" -> "1500"
                        "2L" -> "2000"
                        "2.5L" -> "2500"
                        "3L" -> "3000"
                        else -> customAmount
                    }
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
            CustomAmountField(
                value = customAmount,
                onValueChange = { new ->
                    customAmount = new.filter { it.isDigit() }.take(5)
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
            TipCard()

            Spacer(modifier = Modifier.height(16.dp))
            WhyThisGoal()

            Spacer(modifier = Modifier.height(16.dp))
            SetDailyFlowButton(onClick = {
                customAmount.toFloatOrNull()?.let(onSaveGoal)
            })

            Spacer(modifier = Modifier.height(26.dp))
            DgSectionLabel("BALANCE TARGETS")
            Spacer(modifier = Modifier.height(10.dp))
            BalanceTargetsCard()

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun DgHeader(onBack: () -> Unit) {
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
                    color = DgAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = DgAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = DgAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        BasicText(
            text = "Daily Flow",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = DgAccent,
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
private fun DgSectionLabel(label: String) {
    BasicText(
        text = label,
        modifier = Modifier.padding(start = 6.dp),
        style = TextStyle(
            color = DgAccent.copy(alpha = 0.72f),
            fontSize = 13.sp,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.0.sp
        )
    )
}

@Composable
private fun GettingStartedPill() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(dgTileBrush)
            .border(
                brush = dgTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {}
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicText(
                text = "Getting Started",
                style = TextStyle(
                    color = DgAccent,
                    fontSize = 14.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            BasicText(
                text = "🌱",
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}

@Composable
private fun HeroAmountCard(amountMl: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(dgCardBrush)
            .border(
                brush = dgCardBorder,
                width = 0.95.dp,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                BasicText(
                    text = amountMl.toString(),
                    style = TextStyle(
                        color = DgAccent,
                        fontSize = 72.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                BasicText(
                    text = "ml",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        color = DgAccent.copy(alpha = 0.75f),
                        fontSize = 22.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            BasicText(
                text = "A gentle reset — every sip counts",
                style = TextStyle(
                    color = DgLavender.copy(alpha = 0.86f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}

@Composable
private fun ModeToggle(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(dgTileBrush)
            .border(
                brush = dgTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listOf("Auto", "Custom").forEach { option ->
            val isSelected = option == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(13.dp))
                    .background(if (isSelected) dgSelectedBrush else Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Transparent)))
                    .border(
                        brush = if (isSelected) dgSelectedBorder else Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Transparent)),
                        width = if (isSelected) 1.0.dp else 0.dp,
                        shape = RoundedCornerShape(13.dp)
                    )
                    .clickable { onSelect(option) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = option,
                    style = TextStyle(
                        color = if (isSelected) DgAccent else DgLavender,
                        fontSize = 17.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun PresetRow(selected: String, onSelect: (String) -> Unit) {
    val presets = listOf("1.5L", "2L", "2.5L", "3L")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        presets.forEach { p ->
            PresetPill(
                label = p,
                selected = p == selected,
                onClick = { onSelect(p) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PresetPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) dgSelectedBrush else dgTileBrush)
            .border(
                brush = if (selected) dgSelectedBorder else dgTileBorder,
                width = if (selected) 1.2.dp else 0.85.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = label,
            style = TextStyle(
                color = if (selected) DgAccent else DgLavender,
                fontSize = 18.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(DgAccent)
            )
        }
    }
}

@Composable
private fun CustomAmountField(value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(dgTileBrush)
            .border(
                brush = dgTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column {
            BasicText(
                text = "Custom amount",
                style = TextStyle(
                    color = DgAccent.copy(alpha = 0.72f),
                    fontSize = 13.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = true,
                        cursorBrush = SolidColor(DgAccent),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(
                            color = DgLavender,
                            fontSize = 28.sp,
                            fontFamily = AppFont,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                BasicText(
                    text = "ml",
                    style = TextStyle(
                        color = DgLavender.copy(alpha = 0.80f),
                        fontSize = 18.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}

@Composable
private fun TipCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(dgTileBrush)
            .border(
                brush = dgTileBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row {
            BasicText(
                text = "💡",
                style = TextStyle(fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicText(
                text = "Only 2 strong days this week. A smaller goal today might give you the win you need.",
                style = TextStyle(
                    color = DgLavender,
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}

@Composable
private fun WhyThisGoal() {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clickable { expanded = !expanded }
            .padding(start = 6.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = "Why this goal?",
            style = TextStyle(
                color = DgAccent.copy(alpha = 0.85f),
                fontSize = 16.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        BasicText(
            text = if (expanded) "▲" else "▼",
            style = TextStyle(
                color = DgAccent.copy(alpha = 0.85f),
                fontSize = 12.sp
            )
        )
    }
}

@Composable
private fun SetDailyFlowButton(onClick: () -> Unit) {
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
                        DgAccent.copy(alpha = 0.05f)
                    )
                ),
                width = 1.0.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = "Set Daily Flow",
            style = TextStyle(
                color = Color(0xFFF1F5FF),
                fontSize = 20.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun BalanceTargetsCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(dgCardBrush)
            .border(
                brush = dgCardBorder,
                width = 0.95.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column {
            balanceTargets.forEach { target ->
                BalanceTargetRow(target)
            }
        }
    }
}

@Composable
private fun BalanceTargetRow(target: BalanceTarget) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = target.emoji,
            style = TextStyle(fontSize = 22.sp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        BasicText(
            text = target.name,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                color = DgLavender,
                fontSize = 20.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
        BasicText(
            text = target.value,
            style = TextStyle(
                color = DgAccent,
                fontSize = 18.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicText(
            text = "▼",
            style = TextStyle(
                color = DgAccent.copy(alpha = 0.85f),
                fontSize = 12.sp
            )
        )
    }
}
