package com.hydraheart2.app.ui.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun AnimatedCounter(
    targetValue: Int,
    style: TextStyle,
    modifier: Modifier = Modifier,
    suffix: String = "",
    prefix: String = ""
) {
    val animatedValue by animateIntAsState(
        targetValue,
        spring(dampingRatio = 1f, stiffness = 200f),
        label = "counter"
    )
    Text("$prefix$animatedValue$suffix", modifier = modifier, style = style)
}
