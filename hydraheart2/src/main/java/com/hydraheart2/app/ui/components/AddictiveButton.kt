package com.hydraheart2.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.util.HapticEngine

@Composable
fun AddictiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    theme: HydraTheme,
    colour: Color = theme.accent,
    isPrimary: Boolean = true,
    cornerRadius: Dp = 16.dp,
    enabled: Boolean = true
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (isPressed && enabled) 0.94f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 1200f),
        label = "btnScale"
    )
    val glowIntensity by animateFloatAsState(
        if (isPressed && enabled) 0.5f else 0f,
        tween(
            if (isPressed) 80 else 300,
            easing = if (isPressed) FastOutSlowInEasing else LinearOutSlowInEasing
        ),
        label = "btnGlow"
    )
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = if (isPressed && enabled) -0.3f else 0f
            }
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(
                brush = if (isPrimary) {
                    Brush.verticalGradient(listOf(colour, colour.copy(alpha = 0.85f)))
                } else {
                    Brush.verticalGradient(listOf(colour.copy(alpha = 0.15f), colour.copy(alpha = 0.08f)))
                }
            )
            .drawBehind {
                if (glowIntensity > 0f && enabled) {
                    drawRoundRect(
                        Color.White.copy(alpha = glowIntensity * 0.3f),
                        cornerRadius = CornerRadius(cornerRadius.toPx())
                    )
                }
            }
            .border(
                width = if (isPrimary) 0.dp else 1.dp,
                color = if (isPrimary) Color.Transparent else colour.copy(alpha = 0.3f),
                shape = shape
            )
            .clickable(interactionSource = interactionSource, indication = null, enabled = enabled) {
                HapticEngine.click(context)
                onClick()
            }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelLarge,
            color = if (isPrimary) theme.bg1 else colour,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}
