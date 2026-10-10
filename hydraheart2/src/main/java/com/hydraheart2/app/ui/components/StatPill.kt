package com.hydraheart2.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hydraheart2.app.data.HydraTheme

@Composable
fun StatPill(
    label: String,
    value: Float,
    max: Float,
    unit: String,
    colour: Color,
    theme: HydraTheme,
    modifier: Modifier = Modifier
) {
    val percent = minOf(value / max, 1f)
    val isOver = value > max
    val barColour by animateColorAsState(
        if (isOver) Color(0xFFEF4444) else colour,
        tween(400),
        label = "bc"
    )
    val fillPct by animateFloatAsState(
        percent,
        spring(dampingRatio = 0.75f, stiffness = 400f),
        label = "fp"
    )
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = theme.textSoft, style = MaterialTheme.typography.labelMedium)
            Text(
                "${value.toInt()}/${max.toInt()}$unit",
                color = if (isOver) Color(0xFFEF4444) else colour,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(theme.cardBorder.copy(alpha = 0.3f))
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fillPct)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Brush.horizontalGradient(listOf(barColour.copy(alpha = 0.7f), barColour)))
            )
        }
    }
}
