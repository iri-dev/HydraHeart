package com.hydraheart2.app.ui.components

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.R
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.util.HapticEngine

// Drawn icons for the bottom bar tabs.
private fun navIcon(id: String): ImageVector? = when (id) {
    "home" -> Icons.Rounded.Home
    "history" -> Icons.Rounded.BarChart
    "education" -> Icons.AutoMirrored.Rounded.MenuBook
    "trophies" -> Icons.Rounded.EmojiEvents
    else -> null
}

@Composable
fun NavBar(
    currentScreen: String,
    theme: HydraTheme,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val navHome = stringResource(R.string.nav_home)
    val navHistory = stringResource(R.string.nav_history)
    val navLearn = stringResource(R.string.nav_learn)
    val navTrophies = stringResource(R.string.nav_trophies)
    val items = listOf(
        Triple("home", "🏠", navHome),
        Triple("history", "📊", navHistory),
        Triple("log", "+", ""),
        Triple("education", "📚", navLearn),
        Triple("trophies", "🏆", navTrophies)
    )
    val inf = rememberInfiniteTransition(label = "navGlow")
    val glowPulse by inf.animateFloat(
        0.3f, 0.6f,
        infiniteRepeatable(tween(2400, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "gp"
    )
    val barShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .shadow(
                    elevation = 24.dp,
                    shape = barShape,
                    ambientColor = Color.Black.copy(alpha = 0.15f),
                    spotColor = theme.accent.copy(alpha = 0.2f)
                )
                .clip(barShape)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.25f), theme.navBg.copy(alpha = 0.92f), theme.navBg)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.4f), theme.cardBorder.copy(alpha = 0.2f))
                    ),
                    shape = barShape
                )
                .drawBehind {
                    drawLine(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.3f),
                                theme.accent.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        start = Offset(0f, 0.5.dp.toPx()),
                        end = Offset(size.width, 0.5.dp.toPx()),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .navigationBarsPadding()
                .padding(top = 4.dp, bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { (id, _, label) ->
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val scale by animateFloatAsState(
                        if (isPressed) 0.92f else 1f,
                        spring(dampingRatio = 0.7f, stiffness = 1500f),
                        label = "ns_$id"
                    )
                    if (id == "log") {
                        Spacer(Modifier.size(60.dp))
                    } else {
                        val tint = if (currentScreen == id) theme.accent else theme.textSoft
                        Column(
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(interactionSource = interactionSource, indication = null) {
                                    HapticEngine.tick(context)
                                    onNavigate(id)
                                }
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            navIcon(id)?.let {
                                Icon(it, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
                            }
                            Spacer(Modifier.height(1.dp))
                            Text(
                                label,
                                color = tint,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }

        // Raised "log a drink" button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-10).dp),
            contentAlignment = Alignment.Center
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val logScale by animateFloatAsState(
                if (isPressed) 0.88f else 1f,
                spring(dampingRatio = 0.6f, stiffness = 200f),
                label = "logScale"
            )
            Box(
                Modifier
                    .size(78.dp)
                    .graphicsLayer { alpha = glowPulse }
                    .background(
                        brush = Brush.radialGradient(listOf(theme.accent.copy(alpha = 0.35f), Color.Transparent)),
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .graphicsLayer {
                        scaleX = logScale
                        scaleY = logScale
                    }
                    .shadow(
                        elevation = 18.dp,
                        shape = CircleShape,
                        ambientColor = theme.accent.copy(alpha = 0.3f),
                        spotColor = theme.accent.copy(alpha = 0.5f)
                    )
                    .clip(CircleShape)
                    .background(brush = Brush.verticalGradient(listOf(theme.accent, theme.accent2)))
                    .border(width = 1.dp, color = Color.White.copy(alpha = 0.25f), shape = CircleShape)
                    .clickable(interactionSource = interactionSource, indication = null) {
                        HapticEngine.click(context)
                        onNavigate("log")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "+",
                    color = if (theme.isLight) Color.White else theme.bg1,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}
