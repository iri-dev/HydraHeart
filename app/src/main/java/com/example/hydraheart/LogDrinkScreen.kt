package com.example.hydraheart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont

private val LogDeep = Color(0xFF081229)
private val LogText = Color(0xFFEAF0FF)
private val LogMuted = Color(0xFFA8B8E3)
private val LogAccent = Color(0xFF74A2FF)
private val CardBorder = Color(0xFF3D5286)

private data class DrinkCard(
    val type: HydraDrinkType,
    val emoji: String,
    val name: String,
    val subtitle: String,
    val tileTop: Color,
    val tileBottom: Color,
    val highlighted: Boolean = false
)

@Composable
fun LogDrinkScreen(
    drinkTypes: List<HydraDrinkType>,
    onDrinkLogged: (HydraDrinkType) -> Unit,
    onBack: () -> Unit
) {
    val drinkCards = drinkTypes.map { type ->
        DrinkCard(
            type = type,
            emoji = type.emoji,
            name = "${type.name} ${type.defaultAmountMl.toInt()}ml",
            subtitle = when (type.category) {
                HydraDrinkCategory.WATER -> "Pure hydration"
                HydraDrinkCategory.MILK -> "Nourishing calcium"
                HydraDrinkCategory.CAFFEINE -> "A warm or energising boost"
                HydraDrinkCategory.OTHER -> "A drink worth tracking"
            },
            tileTop = when (type.category) {
                HydraDrinkCategory.WATER -> Color(0xFF2A4E8C)
                HydraDrinkCategory.MILK -> Color(0xFF293553)
                HydraDrinkCategory.CAFFEINE -> Color(0xFF3B2A1C)
                HydraDrinkCategory.OTHER -> Color(0xFF3B2046)
            },
            tileBottom = when (type.category) {
                HydraDrinkCategory.WATER -> Color(0xFF1B2E55)
                HydraDrinkCategory.MILK -> Color(0xFF161E33)
                HydraDrinkCategory.CAFFEINE -> Color(0xFF211611)
                HydraDrinkCategory.OTHER -> Color(0xFF1F1224)
            },
            highlighted = type.category == HydraDrinkCategory.WATER
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LogDeep)
    ) {
        LogDrinkBackground()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicText(
                            text = "‹",
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable(onClick = onBack),
                            style = TextStyle(color = LogAccent, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                        )
                        BasicText(
                            text = "Log a Drink",
                            style = TextStyle(
                                color = LogText,
                                fontSize = 32.sp,
                                lineHeight = 38.sp,
                                fontFamily = AppFont,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    BasicText(
                        text = "What are you drinking right now?",
                        style = TextStyle(
                            color = LogMuted,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontFamily = AppFont,
                            fontStyle = FontStyle.Italic
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            items(drinkCards) { category ->
                DrinkCategoryCard(category, onClick = { onDrinkLogged(category.type) })
                Spacer(modifier = Modifier.height(14.dp))
            }
            item { Spacer(modifier = Modifier.height(158.dp)) }
        }
    }
}

@Composable
private fun LogDrinkBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF122044),
                    Color(0xFF101D3F),
                    Color(0xFF0D1936),
                    Color(0xFF081126)
                ),
                startY = 0f,
                endY = size.height
            )
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF335EC9).copy(alpha = 0.16f),
                    Color(0xFF1E397F).copy(alpha = 0.07f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.32f, size.height * 0.12f),
                radius = size.width * 0.86f
            ),
            center = Offset(size.width * 0.32f, size.height * 0.12f),
            radius = size.width * 0.86f
        )
    }
}

@Composable
private fun DrinkCategoryCard(category: DrinkCard, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A2A55).copy(alpha = 0.72f),
                        Color(0xFF101D3F).copy(alpha = 0.84f),
                        Color(0xFF0A1431).copy(alpha = 0.92f)
                    )
                )
            )
            .border(
                width = 0.9.dp,
                color = CardBorder.copy(alpha = 0.55f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIconTile(category.emoji, category.tileTop, category.tileBottom)
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(end = 32.dp)
            ) {
                BasicText(
                    text = category.name,
                    style = TextStyle(
                        color = if (category.highlighted) LogAccent else LogText,
                        fontSize = 22.sp,
                        lineHeight = 26.sp,
                        fontFamily = AppFont,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = category.subtitle,
                    style = TextStyle(
                        color = LogMuted.copy(alpha = 0.84f),
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = "Tap to log ${category.type.defaultAmountMl.toInt()}ml",
                    style = TextStyle(
                        color = LogMuted.copy(alpha = 0.62f),
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
        BasicText(
            text = ">",
            modifier = Modifier.align(Alignment.CenterEnd),
            style = TextStyle(
                color = LogMuted.copy(alpha = 0.70f),
                fontSize = 22.sp,
                fontFamily = AppFont,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun CategoryIconTile(emoji: String, top: Color, bottom: Color) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.verticalGradient(colors = listOf(top, bottom)))
            .border(
                width = 0.8.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(14.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = emoji,
            style = TextStyle(
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}
