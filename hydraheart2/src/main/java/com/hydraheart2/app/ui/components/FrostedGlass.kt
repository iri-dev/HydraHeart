package com.hydraheart2.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.util.HapticEngine

@Composable
fun FrostedGlass(
    modifier: Modifier = Modifier,
    theme: HydraTheme,
    cornerRadius: Dp = 22.dp,
    blurRadius: Dp = 24.dp,
    onClick: (() -> Unit)? = null,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    borderAlpha: Float = 0.12f,
    isProminent: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val shape = RoundedCornerShape(cornerRadius)

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.982f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "pressScale"
    )
    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.12f else if (isProminent) 0.06f else 0f,
        animationSpec = tween(150),
        label = "glowAlpha"
    )

    val baseAlpha = if (isProminent) 0.26f else 0.2f
    val shimmerAlpha = if (isProminent) 0.12f else 0.08f

    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isProminent) 16.dp else 10.dp,
                shape = shape,
                ambientColor = theme.accent.copy(alpha = if (isProminent) 0.14f else 0.1f),
                spotColor = theme.accent.copy(alpha = if (isProminent) 0.1f else 0.06f)
            )
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = shimmerAlpha),
                        Color.White.copy(alpha = shimmerAlpha / 3f),
                        theme.bg2.copy(alpha = baseAlpha)
                    )
                )
            )
            .drawWithContent {
                drawContent()
                val radius = cornerRadius.toPx()
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = if (isProminent) 1f else 0.98f),
                            Color.White.copy(alpha = 0.45f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = 2.5.dp.toPx()
                    ),
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(radius, radius)
                )
                if (glowAlpha > 0f) {
                    drawRoundRect(
                        color = theme.accent.copy(alpha = glowAlpha),
                        cornerRadius = CornerRadius(radius, radius)
                    )
                }
            }
            .border(
                width = if (isProminent) 1.25.dp else 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = if (isProminent) 0.6f else 0.45f),
                        theme.cardBorder.copy(alpha = if (isProminent) 1.8f * borderAlpha else borderAlpha)
                    )
                ),
                shape = shape
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(interactionSource = interactionSource, indication = null) {
                        HapticEngine.tick(context)
                        onClick()
                    }
                } else {
                    Modifier
                }
            )
            .padding(padding)
    ) {
        content()
    }
}

@Composable
fun HydraSpeechBubble(
    text: String,
    styleId: String,
    theme: HydraTheme,
    modifier: Modifier = Modifier
) {
    val textStyle = MaterialTheme.typography.bodyMedium
    val shape18 = RoundedCornerShape(18.dp)
    val shape14 = RoundedCornerShape(14.dp)
    when (styleId) {
        "outlined" -> {
            Box(
                modifier = modifier
                    .clip(shape18)
                    .border(1.5.dp, theme.accent.copy(alpha = 0.6f), shape18)
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text, color = theme.textPrimary, textAlign = TextAlign.Center, lineHeight = 20.sp, style = textStyle)
            }
        }
        "minimal" -> {
            Box(
                modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text, color = theme.textSoft, textAlign = TextAlign.Center, lineHeight = 20.sp, style = textStyle)
            }
        }
        "solid" -> {
            Box(
                modifier = modifier
                    .clip(shape18)
                    .background(theme.accent.copy(alpha = 0.88f))
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    style = textStyle.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
        "gradient" -> {
            Box(
                modifier = modifier
                    .clip(shape18)
                    .background(
                        Brush.horizontalGradient(
                            listOf(theme.accent.copy(alpha = 0.55f), theme.accent2.copy(alpha = 0.55f))
                        )
                    )
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text, color = theme.textPrimary, textAlign = TextAlign.Center, lineHeight = 20.sp, style = textStyle)
            }
        }
        "dark" -> {
            Box(
                modifier = modifier
                    .clip(shape18)
                    .background(theme.bg1.copy(alpha = 0.88f))
                    .border(0.5.dp, Color.White.copy(alpha = 0.1f), shape18)
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text, color = theme.textPrimary, textAlign = TextAlign.Center, lineHeight = 20.sp, style = textStyle)
            }
        }
        "glow" -> {
            Box(
                modifier = modifier
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                listOf(theme.accent.copy(alpha = 0.22f), Color.Transparent),
                                center = Offset(size.width / 2f, size.height / 2f),
                                radius = size.width * 0.72f
                            ),
                            radius = size.width * 0.72f,
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    }
                    .clip(shape18)
                    .background(theme.accent.copy(alpha = 0.1f))
                    .border(1.dp, theme.accent.copy(alpha = 0.38f), shape18)
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text, color = theme.textPrimary, textAlign = TextAlign.Center, lineHeight = 20.sp, style = textStyle)
            }
        }
        "neon" -> {
            Box(
                modifier = modifier
                    .clip(shape14)
                    .background(Color.Black.copy(alpha = 0.8f))
                    .border(1.5.dp, theme.accent, shape14)
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text,
                    color = theme.accent,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    style = textStyle.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
        else -> {
            Box(
                modifier = modifier
                    .shadow(
                        elevation = 10.dp,
                        shape = shape18,
                        ambientColor = theme.accent.copy(alpha = 0.1f),
                        spotColor = theme.accent.copy(alpha = 0.06f)
                    )
                    .clip(shape18)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.08f),
                                Color.White.copy(alpha = 0.025f),
                                theme.bg2.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            listOf(Color.White.copy(alpha = 0.45f), theme.cardBorder.copy(alpha = 0.12f))
                        ),
                        shape = shape18
                    )
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text, color = theme.textSoft, textAlign = TextAlign.Center, lineHeight = 20.sp, style = textStyle)
            }
        }
    }
}
