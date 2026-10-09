package com.example.hydraheart

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont

enum class HydraDestination(val label: String) {
    Home("Home"),
    History("History"),
    Learn("Learn"),
    Trophies("Trophies"),
    LogDrink("Log a Drink"),
    Settings("Settings"),
    YourProfile("Your Profile"),
    HowWeFeel("How We Feel"),
    Personality("Personality"),
    HydraLook("Hydra's Look"),
    DailyGoals("Daily Goals"),
    Calendar("Calendar"),
    Reminders("Reminders"),
    Themes("Themes"),
    Units("Units"),
    General("General"),
    About("About"),
    DeveloperProfile("Developer Profile"),
    PrivacyPolicy("Privacy Policy")
}

private data class HydraNavItem(val destination: HydraDestination)

private val hydraNavItems = listOf(
    HydraNavItem(HydraDestination.Home),
    HydraNavItem(HydraDestination.History),
    HydraNavItem(HydraDestination.Learn),
    HydraNavItem(HydraDestination.Trophies)
)

private val DockBlue = Color(0xFF122047)
private val DockDeep = Color(0xFF060B1B)
private val ActiveBlue = Color(0xFF74A2FF)
private val MutedBlue = Color(0xFF9BABD4)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HydraBottomBar(
    modifier: Modifier = Modifier,
    selected: HydraDestination,
    onDestinationSelected: (HydraDestination) -> Unit,
    onAddClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(116.dp)
    ) {
        DockGlow(modifier = Modifier.align(Alignment.BottomCenter))
        DockGlass(modifier = Modifier.align(Alignment.BottomCenter))

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility)
                .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
                .height(72.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HydraNavButton(
                item = hydraNavItems[0],
                selected = selected == HydraDestination.Home,
                onClick = { onDestinationSelected(HydraDestination.Home) }
            )
            HydraNavButton(
                item = hydraNavItems[1],
                selected = selected == HydraDestination.History,
                onClick = { onDestinationSelected(HydraDestination.History) }
            )
            Spacer(modifier = Modifier.width(42.dp))
            HydraNavButton(
                item = hydraNavItems[2],
                selected = selected == HydraDestination.Learn,
                onClick = { onDestinationSelected(HydraDestination.Learn) }
            )
            HydraNavButton(
                item = hydraNavItems[3],
                selected = selected == HydraDestination.Trophies,
                onClick = { onDestinationSelected(HydraDestination.Trophies) }
            )
        }

        AddButton(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 0.dp),
            onClick = onAddClick
        )
    }
}

@Composable
private fun DockGlow(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(116.dp)
    ) {
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF4A78E8).copy(alpha = 0.20f),
                    Color(0xFF1B2A52).copy(alpha = 0.08f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.50f, size.height * 0.22f),
                radius = size.width * 0.52f
            ),
            topLeft = Offset(size.width * 0.10f, -18.dp.toPx()),
            size = Size(size.width * 0.80f, 86.dp.toPx())
        )
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    DockDeep.copy(alpha = 0.72f)
                ),
                startY = size.height * 0.30f,
                endY = size.height
            )
        )
    }
}

@Composable
private fun DockGlass(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(98.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 36.dp,
                    topEnd = 36.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val round = 34.dp.toPx()
            val inset = 1.dp.toPx()
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF38496E).copy(alpha = 0.46f),
                        Color(0xFF1A2647).copy(alpha = 0.66f),
                        Color(0xFF080D1F).copy(alpha = 0.84f)
                    ),
                    startY = 0f,
                    endY = size.height
                )
            )
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color(0xFF7AA4FF).copy(alpha = 0.07f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.18f, size.height * 0.10f),
                    radius = size.width * 0.52f
                ),
                topLeft = Offset(-size.width * 0.08f, -size.height * 0.58f),
                size = Size(size.width * 0.64f, size.height * 1.28f)
            )
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFF05080F).copy(alpha = 0.40f)
                    ),
                    startY = size.height * 0.70f,
                    endY = size.height
                )
            )
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.30f),
                        Color(0xFF7AA4FF).copy(alpha = 0.07f),
                        Color.Transparent
                    )
                ),
                cornerRadius = CornerRadius(round, round),
                style = Stroke(width = 1.dp.toPx())
            )
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.12f),
                topLeft = Offset(inset * 2f, inset * 2f),
                size = Size(size.width - inset * 4f, size.height + round),
                cornerRadius = CornerRadius(round - inset, round - inset),
                style = Stroke(width = 1.1.dp.toPx())
            )
            drawPath(
                path = Path().apply {
                    moveTo(22.dp.toPx(), 16.dp.toPx())
                    cubicTo(
                        size.width * 0.25f,
                        1.dp.toPx(),
                        size.width * 0.72f,
                        1.dp.toPx(),
                        size.width - 22.dp.toPx(),
                        16.dp.toPx()
                    )
                },
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.28f),
                        Color(0xFF7AA4FF).copy(alpha = 0.14f),
                        Color.Transparent
                    )
                ),
                style = Stroke(width = 1.2.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun HydraNavButton(
    item: HydraNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) ActiveBlue else MutedBlue.copy(alpha = 0.74f)
    val textColor = if (selected) ActiveBlue else MutedBlue.copy(alpha = 0.76f)

    Column(
        modifier = Modifier
            .width(62.dp)
            .fillMaxHeight()
            .clickable(onClick = onClick)
            .padding(top = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HydraNavIcon(
            destination = item.destination,
            color = color,
            selected = selected
        )
        Spacer(modifier = Modifier.height(3.dp))
        BasicText(
            text = item.destination.label,
            style = TextStyle(
                color = textColor,
                fontSize = 11.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun HydraNavIcon(
    destination: HydraDestination,
    color: Color,
    selected: Boolean
) {
    Canvas(modifier = Modifier.size(25.dp)) {
        val stroke = Stroke(
            width = if (selected) 1.55.dp.toPx() else 1.35.dp.toPx(),
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
            join = androidx.compose.ui.graphics.StrokeJoin.Round
        )
        val w = size.width
        val h = size.height

        when (destination) {
            HydraDestination.Home -> {
                val roof = Path().apply {
                    moveTo(w * 0.18f, h * 0.48f)
                    lineTo(w * 0.50f, h * 0.20f)
                    lineTo(w * 0.82f, h * 0.48f)
                }
                val body = Path().apply {
                    moveTo(w * 0.27f, h * 0.47f)
                    lineTo(w * 0.27f, h * 0.82f)
                    lineTo(w * 0.73f, h * 0.82f)
                    lineTo(w * 0.73f, h * 0.47f)
                }
                drawPath(roof, color, style = stroke)
                drawPath(body, color, style = stroke)
                drawLine(color, Offset(w * 0.48f, h * 0.82f), Offset(w * 0.48f, h * 0.64f), stroke.width)
                drawLine(color, Offset(w * 0.58f, h * 0.82f), Offset(w * 0.58f, h * 0.64f), stroke.width)
            }

            HydraDestination.History -> {
                val xs = listOf(0.28f, 0.46f, 0.64f)
                val tops = listOf(0.66f, 0.50f, 0.34f)
                xs.forEachIndexed { index, x ->
                    drawLine(
                        color = color,
                        start = Offset(w * x, h * 0.82f),
                        end = Offset(w * x, h * tops[index]),
                        strokeWidth = stroke.width,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }
                drawLine(color, Offset(w * 0.22f, h * 0.72f), Offset(w * 0.68f, h * 0.30f), stroke.width)
            }

            HydraDestination.Learn -> {
                val left = Path().apply {
                    moveTo(w * 0.17f, h * 0.30f)
                    cubicTo(w * 0.30f, h * 0.22f, w * 0.42f, h * 0.24f, w * 0.50f, h * 0.34f)
                    lineTo(w * 0.50f, h * 0.80f)
                    cubicTo(w * 0.38f, h * 0.70f, w * 0.27f, h * 0.70f, w * 0.17f, h * 0.78f)
                    close()
                }
                val right = Path().apply {
                    moveTo(w * 0.83f, h * 0.30f)
                    cubicTo(w * 0.70f, h * 0.22f, w * 0.58f, h * 0.24f, w * 0.50f, h * 0.34f)
                    lineTo(w * 0.50f, h * 0.80f)
                    cubicTo(w * 0.62f, h * 0.70f, w * 0.73f, h * 0.70f, w * 0.83f, h * 0.78f)
                    close()
                }
                drawPath(left, color, style = stroke)
                drawPath(right, color, style = stroke)
            }

            HydraDestination.Trophies -> {
                val cup = Path().apply {
                    moveTo(w * 0.32f, h * 0.30f)
                    lineTo(w * 0.68f, h * 0.30f)
                    cubicTo(w * 0.66f, h * 0.56f, w * 0.58f, h * 0.66f, w * 0.50f, h * 0.68f)
                    cubicTo(w * 0.42f, h * 0.66f, w * 0.34f, h * 0.56f, w * 0.32f, h * 0.30f)
                }
                drawPath(cup, color, style = stroke)
                drawArc(color, 175f, 115f, false, Offset(w * 0.16f, h * 0.32f), Size(w * 0.26f, h * 0.25f), stroke.width)
                drawArc(color, -110f, 115f, false, Offset(w * 0.58f, h * 0.32f), Size(w * 0.26f, h * 0.25f), stroke.width)
                drawLine(color, Offset(w * 0.50f, h * 0.68f), Offset(w * 0.50f, h * 0.80f), stroke.width)
                drawLine(color, Offset(w * 0.34f, h * 0.82f), Offset(w * 0.66f, h * 0.82f), stroke.width)
            }

            HydraDestination.LogDrink -> {}
            HydraDestination.Settings -> {}
            HydraDestination.YourProfile -> {}
            HydraDestination.HowWeFeel -> {}
            HydraDestination.Personality -> {}
            HydraDestination.HydraLook -> {}
            HydraDestination.DailyGoals -> {}
            HydraDestination.Calendar -> {}
            HydraDestination.Reminders -> {}
            HydraDestination.Themes -> {}
            HydraDestination.Units -> {}
            HydraDestination.General -> {}
            HydraDestination.About -> {}
            HydraDestination.DeveloperProfile -> {}
            HydraDestination.PrivacyPolicy -> {}
        }
    }
}

@Composable
private fun AddButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hydraAddPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.025f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hydraAddScale"
    )

    Box(
        modifier = modifier
            .size(84.dp)
            .scale(pulseScale)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9DBBFF),
                        Color(0xFF6E96F2),
                        Color(0xFF4669D7),
                        Color(0xFF263F96)
                    )
                ),
                radius = size.width * 0.37f,
                center = center
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.30f),
                        Color.White.copy(alpha = 0.05f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.38f, size.height * 0.28f),
                radius = size.width * 0.37f
            ),
                radius = size.width * 0.37f,
                center = Offset(size.width * 0.38f, size.height * 0.28f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.28f),
                radius = size.width * 0.37f,
                center = center,
                style = Stroke(width = 1.1.dp.toPx())
            )

            val plusColor = Color(0xFF0B1532)
            val plusStroke = 1.45.dp.toPx()
            drawLine(
                color = plusColor,
                start = Offset(size.width * 0.50f, size.height * 0.32f),
                end = Offset(size.width * 0.50f, size.height * 0.68f),
                strokeWidth = plusStroke,
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            drawLine(
                color = plusColor,
                start = Offset(size.width * 0.32f, size.height * 0.50f),
                end = Offset(size.width * 0.68f, size.height * 0.50f),
                strokeWidth = plusStroke,
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
    }
}
