package com.hydraheart2.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hydraheart2.app.data.HydraTheme
import kotlinx.coroutines.delay

@Composable
fun HydraTimePicker(
    initialHour: Int,
    initialMinute: Int,
    theme: HydraTheme,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var hour by remember { mutableIntStateOf(initialHour) }
    var minute by remember { mutableIntStateOf(initialMinute) }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        FrostedGlass(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .wrapContentHeight(),
            theme = theme,
            cornerRadius = 28.dp,
            padding = PaddingValues(28.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "💕 Reminder Time",
                    color = theme.accent,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "tap ▲▼ or hold to scroll fast",
                    color = theme.textMuted,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeSpinner(value = hour, max = 23, label = "Hour", theme = theme, onValueChange = { hour = it })
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier
                            .size(7.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(theme.accent)
                    )
                    Box(
                        Modifier
                            .size(7.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(theme.accent)
                    )
                }
                TimeSpinner(value = minute, max = 59, label = "Min", theme = theme, onValueChange = { minute = it })
            }
            Spacer(Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text("Cancel", color = theme.textMuted, fontWeight = FontWeight.Medium)
                }
                AddictiveButton(
                    text = "Set 💕",
                    onClick = { onConfirm(hour, minute) },
                    modifier = Modifier.weight(2f),
                    theme = theme,
                    colour = theme.accent,
                    isPrimary = true,
                    cornerRadius = 16.dp
                )
            }
        }
    }
}

@Composable
private fun TimeSpinner(
    value: Int,
    max: Int,
    label: String,
    theme: HydraTheme,
    onValueChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            color = theme.textSoft,
            letterSpacing = 1.sp,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(Modifier.height(10.dp))
        SpinnerArrowButton(
            symbol = "▲",
            theme = theme,
            onClick = { onValueChange((value + 1) % (max + 1)) }
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(80.dp, 68.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(theme.accent.copy(alpha = 0.22f), theme.accent.copy(alpha = 0.1f))
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(theme.accent.copy(alpha = 0.5f), theme.accent.copy(alpha = 0.2f))
                    ),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                String.format("%02d", value),
                color = theme.accent,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall
            )
        }
        Spacer(Modifier.height(8.dp))
        SpinnerArrowButton(
            symbol = "▼",
            theme = theme,
            onClick = { onValueChange((value - 1 + max + 1) % (max + 1)) }
        )
    }
}

@Composable
private fun SpinnerArrowButton(
    symbol: String,
    theme: HydraTheme,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (isPressed) 0.8f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 10000f),
        label = "arrowScale"
    )
    // Hold to repeat
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(340)
            while (isPressed) {
                onClick()
                delay(80)
            }
        }
    }
    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .size(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        theme.accent.copy(alpha = if (isPressed) 0.28f else 0.14f),
                        theme.accent.copy(alpha = if (isPressed) 0.18f else 0.08f)
                    )
                )
            )
            .border(
                width = 0.75.dp,
                color = theme.accent.copy(alpha = if (isPressed) 0.45f else 0.2f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(symbol, color = theme.accent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
