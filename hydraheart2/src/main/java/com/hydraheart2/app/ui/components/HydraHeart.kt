package com.hydraheart2.app.ui.components

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.viewmodel.HydraViewModel
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

@Composable
fun HydraHeartCanvas(
    fillPercent: Float,
    mood: HydraViewModel.HydraMood,
    theme: HydraTheme,
    showFace: Boolean,
    hydraShape: String,
    drinkColours: List<HydraViewModel.DrinkColourPortion>,
    level: Int,
    showNecklace: Boolean = true,
    equippedAccessories: Set<String> = emptySet(),
    size: Dp = 280.dp,
    onTap: (() -> Unit)? = null
) {
    val inf = rememberInfiniteTransition(label = "hb")
    val breathe by inf.animateFloat(
        1f, 1.004f,
        infiniteRepeatable(tween(5000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "br"
    )
    val wave1 by inf.animateFloat(
        0f, (2 * PI).toFloat(),
        infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Restart),
        label = "w1"
    )
    val wave2 by inf.animateFloat(
        0f, (2 * PI).toFloat(),
        infiniteRepeatable(tween(11000, easing = LinearEasing), RepeatMode.Restart),
        label = "w2"
    )
    val drift by inf.animateFloat(
        0f, (2 * PI).toFloat(),
        infiniteRepeatable(tween(9000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "dr"
    )
    val look by inf.animateFloat(
        -1f, 1f,
        infiniteRepeatable(tween(14000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "lk"
    )
    val lookDrift by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(19000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "ld"
    )
    val glowPulse by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(7000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "gp"
    )
    val sparkle by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(2200, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "sp"
    )
    val celebPulse by inf.animateFloat(
        0.99f, 1.01f,
        infiniteRepeatable(tween(1400, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "cp"
    )
    val blink by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(4800, easing = LinearEasing), RepeatMode.Restart),
        label = "bk"
    )
    val bubblePhase by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Restart),
        label = "bp"
    )
    val fill by animateFloatAsState(fillPercent, spring(dampingRatio = 0.5f, stiffness = 400f), label = "fi")

    var tapped by remember { mutableStateOf(false) }
    var bloomVisible by remember { mutableStateOf(false) }
    val bounceScale by animateFloatAsState(
        if (tapped) 1.05f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 400f),
        label = "bs"
    )
    val bloom by animateFloatAsState(
        if (bloomVisible) 1f else 0f,
        tween(if (bloomVisible) 55 else 360),
        label = "bl"
    )
    LaunchedEffect(tapped) {
        if (tapped) {
            delay(180)
            tapped = false
        }
    }
    LaunchedEffect(bloomVisible) {
        if (bloomVisible) {
            delay(55)
            bloomVisible = false
        }
    }
    val eyeOpen = blink < 0.93f || blink > 0.97f

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                val s = breathe * (if (fill >= 99f) celebPulse else 1f) * bounceScale
                scaleX = s
                scaleY = s
            }
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    tapped = true
                    bloomVisible = true
                    onTap?.invoke()
                })
            }
    ) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val fillFraction = (fill / 100f).coerceIn(0f, 1f)
        val levelY = h * (1f - fillFraction)
        val shapePath = when (hydraShape) {
            "droplet" -> dropletPath(w, h)
            "cup" -> cupPath(w, h)
            "bra" -> braPath(w, h)
            "bottle" -> bottlePath(w, h)
            "body_m" -> malePath(w, h)
            "body_f" -> femalePath(w, h)
            else -> heartPath(w, h)
        }

        // Soft glow behind Hydra, brighter as she fills up
        val lightFactor = if (theme.isLight) 0.55f else 1f
        val glowAlpha = (0.1f + glowPulse * 0.07f) * (0.16f + 0.84f * fillFraction) * lightFactor
        drawRect(
            brush = Brush.radialGradient(
                0f to theme.glow.copy(alpha = glowAlpha),
                0.22f to theme.glow.copy(alpha = glowAlpha * 0.62f),
                0.44f to theme.glow.copy(alpha = glowAlpha * 0.3f),
                0.66f to theme.glow.copy(alpha = glowAlpha * 0.09f),
                1f to Color.Transparent,
                center = Offset(cx, cy + h * 0.04f),
                radius = w * 1.55f
            )
        )

        // Drop shadow
        val shadowAlpha = if (theme.isLight) 0.1f else 0.2f
        withTransform({ translate(2.5f, 4.5f) }) {
            drawPath(
                shapePath,
                brush = Brush.radialGradient(
                    0f to theme.accent2.copy(alpha = shadowAlpha),
                    0.5f to theme.accent2.copy(alpha = shadowAlpha * 0.4f),
                    1f to Color.Transparent,
                    center = Offset(cx, cy + h * 0.05f),
                    radius = w * 0.7f
                )
            )
        }

        // Tap bloom
        if (bloom > 0.01f) {
            drawRect(
                brush = Brush.radialGradient(
                    0f to Color.White.copy(alpha = bloom * 0.2f),
                    0.38f to theme.accent.copy(alpha = bloom * 0.1f),
                    1f to Color.Transparent,
                    center = Offset(cx, cy),
                    radius = w * 0.88f
                )
            )
        }

        clipPath(shapePath) {
            // Glass body
            val topAlpha = if (theme.isLight) 0.45f else 0.62f
            drawRect(
                brush = Brush.linearGradient(
                    0f to Color.White.copy(alpha = topAlpha),
                    0.18f to theme.accent.copy(alpha = 0.52f),
                    0.5f to theme.accent.copy(alpha = 0.42f),
                    0.76f to theme.accent2.copy(alpha = 0.46f),
                    1f to theme.accent2.copy(alpha = 0.62f),
                    start = Offset(w * 0.03f, h * 0.03f),
                    end = Offset(w * 0.97f, h * 0.97f)
                )
            )
            val sweep = sin(drift) * w * 0.038f
            drawRect(
                brush = Brush.linearGradient(
                    0f to Color.Transparent,
                    0.42f to Color.White.copy(alpha = 0.022f + max(0f, sin(drift)) * 0.016f),
                    0.58f to Color.White.copy(alpha = 0.014f),
                    1f to Color.Transparent,
                    start = Offset(w * 0.06f + sweep, h * 0.1f),
                    end = Offset(w * 0.94f + sweep, h * 0.9f)
                )
            )

            if (fill > 0f) {
                val liquidH = h - levelY
                val liquidSize = Size(w, liquidH)

                // Liquid colours, one band per drink logged today
                val portions = if (drinkColours.isNotEmpty()) {
                    drinkColours
                } else {
                    listOf(HydraViewModel.DrinkColourPortion(theme.hydraLiquid, 1f))
                }
                val count = portions.size
                val stops = mutableListOf<Pair<Float, Color>>()
                var cumulative = 0f
                portions.forEachIndexed { i, portion ->
                    val end = (cumulative + portion.proportion).coerceAtMost(1f)
                    val span = end - cumulative
                    val blend = min(0.05f, span * 0.18f)
                    val phase = i * 1.618f
                    val shimmer = sin(drift + phase) * 0.075f
                    if (i == 0) {
                        stops += 0f to portion.colour.copy(alpha = (0.88f + shimmer).coerceIn(0.68f, 0.98f))
                        stops += (cumulative + span * 0.16f) to
                            portion.colour.copy(alpha = (0.8f + shimmer * 0.9f).coerceIn(0.62f, 0.92f))
                    }
                    stops += (cumulative + span * 0.42f) to
                        portion.colour.copy(alpha = (0.68f + shimmer * 0.8f).coerceIn(0.5f, 0.82f))
                    stops += (cumulative + span * 0.66f) to
                        portion.colour.copy(alpha = (0.54f + shimmer * 0.6f).coerceIn(0.38f, 0.68f))
                    if (i < count - 1) {
                        val nextColour = portions[i + 1].colour
                        val nextShimmer = sin(drift + (i + 1) * 1.618f) * 0.065f
                        val wobble = look * span * 0.016f
                        val blendStart = (end - blend + wobble).coerceIn(cumulative + span * 0.68f, end)
                        val blendEnd = (end + blend + wobble).coerceIn(end, 1f)
                        stops += blendStart to
                            portion.colour.copy(alpha = (0.44f + shimmer * 0.5f).coerceIn(0.3f, 0.58f))
                        stops += end to lerp(portion.colour, nextColour, 0.5f).copy(alpha = 0.44f)
                        stops += blendEnd to
                            nextColour.copy(alpha = (0.54f + nextShimmer * 0.6f).coerceIn(0.38f, 0.68f))
                    } else {
                        val tail = sin(drift + phase + 0.8f) * 0.038f
                        stops += (end - span * 0.22f) to
                            portion.colour.copy(alpha = (0.42f + tail).coerceIn(0.28f, 0.56f))
                        stops += 1f to portion.colour.copy(alpha = (0.26f + tail).coerceIn(0.14f, 0.4f))
                    }
                    cumulative = end
                }
                val liquidStops = stops
                    .sortedBy { it.first }
                    .distinctBy { (it.first * 1000f).toInt() }
                    .map { it.first.coerceIn(0f, 1f) to it.second }
                    .toTypedArray()

                // Wavy surface
                val tilt = look * liquidH * 0.032f
                val slosh = sin(drift) * liquidH * 0.028f
                val wavePath = Path()
                wavePath.moveTo(0f, levelY)
                for (x in 0..w.toInt() step 2) {
                    wavePath.lineTo(
                        x.toFloat(),
                        levelY + sin(x * 0.02f + wave1) * 1.4f + sin(x * 0.048f + wave2) * 0.5f
                    )
                }
                wavePath.lineTo(w, h)
                wavePath.lineTo(0f, h)
                wavePath.close()
                drawPath(
                    wavePath,
                    brush = Brush.linearGradient(
                        *liquidStops,
                        start = Offset(cx + tilt, h + slosh),
                        end = Offset(cx - tilt, levelY - slosh)
                    )
                )

                // Light catching the liquid as Hydra "looks" around
                val lean = look * 0.12f
                val sheenAlpha = look.coerceAtLeast(0f) * 0.038f
                val shadeAlpha = (-look).coerceAtLeast(0f) * 0.03f
                drawPath(
                    wavePath,
                    brush = Brush.linearGradient(
                        0f to Color.Transparent,
                        0.3f to Color.White.copy(alpha = sheenAlpha),
                        0.55f to Color.Transparent,
                        0.78f to portions.last().colour.copy(alpha = shadeAlpha),
                        1f to Color.Transparent,
                        start = Offset(cx * (1f + lean), levelY),
                        end = Offset(cx * (1f - lean), h)
                    )
                )
                val glintX = (0.14f + lookDrift * 0.72f) * w
                val glintY = levelY + liquidH * (0.36f + look * 0.06f)
                drawRect(
                    brush = Brush.radialGradient(
                        0f to Color.White.copy(alpha = 0.1f),
                        0.42f to Color.White.copy(alpha = 0.032f),
                        1f to Color.Transparent,
                        center = Offset(glintX, glintY),
                        radius = w * 0.46f
                    ),
                    topLeft = Offset(0f, levelY),
                    size = liquidSize
                )
                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(Color.Transparent, Color.White.copy(alpha = 0.1f), Color.Transparent)
                    ),
                    start = Offset(w * 0.1f, levelY),
                    end = Offset(w * 0.9f, levelY),
                    strokeWidth = 5f
                )
                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(Color.Transparent, Color.White.copy(alpha = 0.14f), Color.Transparent)
                    ),
                    start = Offset(w * 0.16f, levelY),
                    end = Offset(w * 0.84f, levelY),
                    strokeWidth = 1.2f
                )
                drawRect(
                    brush = Brush.linearGradient(
                        0f to Color.White.copy(alpha = 0.16f),
                        0.25f to Color.White.copy(alpha = 0.05f),
                        0.55f to Color.Transparent,
                        1f to Color(0xFF000000).copy(alpha = 0.05f),
                        start = Offset(w * 0.03f, levelY + liquidH * 0.03f),
                        end = Offset(w * 0.97f, h)
                    ),
                    topLeft = Offset(0f, levelY),
                    size = liquidSize
                )

                // Rising bubbles
                if (fill > 5f) {
                    for (i in 0 until 4) {
                        val t = (bubblePhase + i * 0.25f) % 1f
                        val bx = cx + sin(i * 2.31f + bubblePhase * 2f * 3.1415927f) * w * 0.22f
                        val by = h - (h - levelY) * t * 1.03f
                        val bubbleAlpha = (sin(t * 3.1415927f) * 0.16f).coerceIn(0f, 1f)
                        val bubbleR = 1f + sin(i * 1.73f + wave1 * 0.22f) * 0.4f
                        drawCircle(Color.White.copy(alpha = bubbleAlpha), bubbleR, Offset(bx, by))
                    }
                }
            }

            // Glossy highlights
            drawRect(
                brush = Brush.radialGradient(
                    0f to Color.White.copy(alpha = 0.26f),
                    0.36f to Color.White.copy(alpha = 0.1f),
                    0.66f to Color.White.copy(alpha = 0.02f),
                    1f to Color.Transparent,
                    center = Offset(w * 0.26f, h * 0.15f),
                    radius = w * 0.46f
                )
            )
            drawRect(
                brush = Brush.radialGradient(
                    0f to Color.White.copy(alpha = 0.4f),
                    0.5f to Color.White.copy(alpha = 0.07f),
                    1f to Color.Transparent,
                    center = Offset(w * 0.17f, h * 0.1f),
                    radius = w * 0.12f
                )
            )

            if (showFace) {
                drawFace(
                    cx, cy, w, h, hydraShape, mood, theme, eyeOpen, level, showNecklace, sparkle,
                    equippedAccessories
                )
            }
        }

        // Rim
        drawPath(
            shapePath,
            brush = Brush.linearGradient(
                listOf(Color.White.copy(alpha = 0.3f), theme.accent.copy(alpha = 0.06f)),
                start = Offset(w * 0.04f, h * 0.04f),
                end = Offset(w * 0.96f, h * 0.96f)
            ),
            style = Stroke(width = 0.9f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        if (equippedAccessories.isNotEmpty()) {
            val s = when (hydraShape) {
                "body_f", "body_m" -> 0.5f
                "bottle" -> 0.6f
                "bra" -> 0.65f
                "cup" -> 0.7f
                "droplet" -> 0.85f
                else -> 0.95f
            }
            val anchorY = when (hydraShape) {
                "body_f", "body_m" -> h * 0.1f
                "bottle" -> h * 0.28f
                "bra" -> cy * 0.72f
                "cup" -> cy * 0.58f
                "droplet" -> cy * 0.7f
                else -> cy * 0.72f
            }
            if (equippedAccessories.contains("aura")) {
                val auraAlpha = 0.1f + glowPulse * 0.07f
                drawRect(
                    brush = Brush.radialGradient(
                        0.28f to Color.Transparent,
                        0.52f to Color(0xFFFF69B4).copy(alpha = auraAlpha * 0.5f),
                        0.68f to Color(0xFFFF69B4).copy(alpha = auraAlpha),
                        0.83f to Color(0xFFFF69B4).copy(alpha = auraAlpha * 0.4f),
                        1f to Color.Transparent,
                        center = Offset(cx, cy),
                        radius = w * 0.68f
                    )
                )
            }
            if (equippedAccessories.contains("halo")) {
                val haloR = s * 13f
                val haloY = anchorY - 29f * s
                drawArc(
                    Color(0xFFFFD700).copy(alpha = 0.82f + sparkle * 0.1f),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - haloR, haloY - haloR * 0.5f),
                    size = Size(haloR * 2f, haloR),
                    style = Stroke(width = s * 2.2f, cap = StrokeCap.Round)
                )
                drawArc(
                    Color(0xFFFFD700).copy(alpha = 0.26f),
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - haloR, haloY - haloR * 0.5f),
                    size = Size(haloR * 2f, haloR),
                    style = Stroke(width = s * 1.2f, cap = StrokeCap.Round)
                )
            }
            if (equippedAccessories.contains("bow")) {
                val bowY = anchorY - 26f * s
                val left = Path()
                left.moveTo(cx, bowY)
                left.cubicTo(cx - s * 13f, bowY - 9f * s, cx - s * 13f, bowY + 9f * s, cx, bowY)
                drawPath(left, Color(0xFFFF69B4).copy(alpha = 0.82f))
                val right = Path()
                right.moveTo(cx, bowY)
                right.cubicTo(cx + s * 13f, bowY - 9f * s, cx + 13f * s, bowY + 9f * s, cx, bowY)
                drawPath(right, Color(0xFFFF69B4).copy(alpha = 0.82f))
                drawCircle(Color(0xFFFF1493).copy(alpha = 0.9f), radius = s * 3f, center = Offset(cx, bowY))
            }
            if (equippedAccessories.contains("sparkles")) {
                val colours = listOf(theme.accent, theme.accent2, Color(0xFFFFD700), Color.White, Color(0xFFFF69B4))
                for (i in 0 until 8) {
                    val angle = (i * 45f + wave1 * 22f) * 3.1415927f / 180f
                    val dist = w * 0.52f + sin(wave1 + i * 0.8f) * w * 0.035f
                    val sparkleAlpha = (0.28f + sparkle * 0.38f) * (if (i % 2 == 0) 1f else 0.58f)
                    drawCircle(
                        colours[i % colours.size].copy(alpha = sparkleAlpha),
                        radius = 1.8f + sin(wave2 + i) * 0.5f,
                        center = Offset(cx + cos(angle) * dist, cy + sin(angle) * dist)
                    )
                }
            }
            if (equippedAccessories.contains("stardust")) {
                for (i in 0 until 12) {
                    val angle = (i * 30f + wave2 * 11f) * 3.1415927f / 180f
                    val dist = w * (0.46f + sin(wave1 + i * 0.55f) * 0.1f)
                    val dustAlpha = (sparkle * (0.52f + sin(wave2 + i * 0.9f) * 0.24f)).coerceIn(0f, 0.8f)
                    drawCircle(
                        Color(0xFFFFD700).copy(alpha = dustAlpha),
                        radius = 1.1f + sin(wave1 + i) * 0.4f,
                        center = Offset(cx + cos(angle) * dist, cy + sin(angle) * dist)
                    )
                }
            }
        }

        // Celebration sparkles at 99%+
        if (fill >= 99f) {
            for (i in 0 until 8) {
                val angle = (i * 45f + wave1 * 36f) * 3.1415927f / 180f
                val dist = 20f + sin(wave1 * 1.4f + i) * 10f
                val colours = listOf(theme.accent, theme.accent2, theme.accent3, Color(0xFFFFD700), Color.White)
                drawCircle(
                    colours[i % colours.size].copy(alpha = 0.36f * sparkle),
                    radius = 1.6f + sin(wave1 + i) * 0.8f,
                    center = Offset(cx + cos(angle) * dist * 3f, cy + sin(angle) * dist * 2f)
                )
            }
        }
    }
}

private fun DrawScope.drawFace(
    cx: Float,
    cy: Float,
    w: Float,
    h: Float,
    hydraShape: String,
    mood: HydraViewModel.HydraMood,
    theme: HydraTheme,
    eyeOpen: Boolean,
    level: Int,
    showNecklace: Boolean,
    sparkle: Float,
    equippedAccessories: Set<String> = emptySet()
) {
    val fs = when (hydraShape) {
        "body_f", "body_m" -> 0.5f
        "bottle" -> 0.6f
        "bra" -> 0.65f
        "cup" -> 0.7f
        "droplet" -> 0.85f
        else -> 0.95f
    }
    val fcy = when (hydraShape) {
        "body_f", "body_m" -> 0.1f * h
        "bottle" -> 0.28f * h
        "bra" -> 0.72f * cy
        "cup" -> 0.58f * cy
        "droplet" -> 0.7f * cy
        else -> 0.72f * cy
    }

    // Cheeks
    drawCircle(theme.accent.copy(alpha = 0.1f), fs * 10f, Offset(cx - 35f * fs, fcy + fs * 12f))
    drawCircle(theme.accent.copy(alpha = 0.1f), fs * 10f, Offset(cx + 35f * fs, fcy + fs * 12f))

    when (mood) {
        HydraViewModel.HydraMood.ECSTATIC, HydraViewModel.HydraMood.HAPPY -> {
            if (eyeOpen) {
                happyEye(cx - fs * 20f, fcy, fs * 14f, theme.textPrimary, fs * 2.5f)
                happyEye(cx + fs * 20f, fcy, fs * 14f, theme.textPrimary, fs * 2.5f)
            } else {
                drawLine(
                    theme.textPrimary, Offset(cx - 27f * fs, fcy), Offset(cx - 13f * fs, fcy),
                    fs * 2.5f, StrokeCap.Round
                )
                drawLine(
                    theme.textPrimary, Offset(cx + 13f * fs, fcy), Offset(cx + 27f * fs, fcy),
                    fs * 2.5f, StrokeCap.Round
                )
            }
            val smile = Path()
            smile.moveTo(cx - fs * 12f, fcy + fs * 14f)
            smile.quadraticBezierTo(cx, fcy + 26f * fs, cx + fs * 12f, fcy + fs * 14f)
            drawPath(smile, theme.textPrimary, style = Stroke(width = fs * 2.2f, cap = StrokeCap.Round))
            if (mood == HydraViewModel.HydraMood.ECSTATIC) {
                drawCircle(Color(0xFFFFD700).copy(alpha = sparkle * 0.7f), 2f, Offset(cx - 30f * fs, fcy - fs * 6f))
                drawCircle(Color(0xFFFFD700).copy(alpha = sparkle * 0.5f), 1.6f, Offset(cx + 30f * fs, fcy - fs * 5f))
            }
        }
        HydraViewModel.HydraMood.CONTENT -> {
            if (eyeOpen) {
                drawCircle(theme.textPrimary, fs * 4f, Offset(cx - fs * 20f, fcy))
                drawCircle(theme.textPrimary, fs * 4f, Offset(cx + fs * 20f, fcy))
            } else {
                drawLine(
                    theme.textPrimary, Offset(cx - fs * 24f, fcy), Offset(cx - fs * 16f, fcy),
                    fs * 2f, StrokeCap.Round
                )
                drawLine(
                    theme.textPrimary, Offset(cx + 16f * fs, fcy), Offset(cx + fs * 24f, fcy),
                    fs * 2f, StrokeCap.Round
                )
            }
            val smile = Path()
            smile.moveTo(cx - fs * 9f, fcy + fs * 14f)
            smile.quadraticBezierTo(cx, fcy + 22f * fs, cx + fs * 9f, fcy + fs * 14f)
            drawPath(smile, theme.textPrimary, style = Stroke(width = fs * 1.8f, cap = StrokeCap.Round))
        }
        HydraViewModel.HydraMood.NEUTRAL -> {
            if (eyeOpen) {
                drawCircle(theme.textPrimary, fs * 4f, Offset(cx - fs * 20f, fcy))
                drawCircle(theme.textPrimary, fs * 4f, Offset(cx + fs * 20f, fcy))
            } else {
                drawLine(
                    theme.textPrimary, Offset(cx - fs * 24f, fcy), Offset(cx - fs * 16f, fcy),
                    fs * 2f, StrokeCap.Round
                )
                drawLine(
                    theme.textPrimary, Offset(cx + fs * 16f, fcy), Offset(cx + fs * 24f, fcy),
                    fs * 2f, StrokeCap.Round
                )
            }
            drawLine(
                theme.textPrimary, Offset(cx - fs * 7f, fcy + fs * 16f), Offset(cx + fs * 7f, fcy + 16f * fs),
                fs * 1.8f, StrokeCap.Round
            )
        }
        HydraViewModel.HydraMood.CONCERNED -> {
            if (eyeOpen) {
                drawCircle(theme.textPrimary, fs * 4.5f, Offset(cx - fs * 20f, fcy + fs * 1f))
                drawCircle(theme.textPrimary, fs * 4.5f, Offset(cx + fs * 20f, fcy + fs * 1f))
                val lidL = Path()
                lidL.moveTo(cx - 25f * fs, fcy - fs * 1f)
                lidL.quadraticBezierTo(cx - fs * 20f, fcy - 5.5f * fs, cx - 15f * fs, fcy - fs * 1f)
                drawPath(
                    lidL, theme.textPrimary.copy(alpha = 0.44f),
                    style = Stroke(width = fs * 1.5f, cap = StrokeCap.Round)
                )
                val lidR = Path()
                lidR.moveTo(cx + 15f * fs, fcy - fs * 1f)
                lidR.quadraticBezierTo(cx + fs * 20f, fcy - 5.5f * fs, cx + 25f * fs, fcy - 1f * fs)
                drawPath(
                    lidR, theme.textPrimary.copy(alpha = 0.44f),
                    style = Stroke(width = fs * 1.5f, cap = StrokeCap.Round)
                )
            } else {
                drawLine(
                    theme.textPrimary, Offset(cx - 24.5f * fs, fcy + fs * 1f), Offset(cx - 15.5f * fs, fcy + fs * 1f),
                    fs * 2f, StrokeCap.Round
                )
                drawLine(
                    theme.textPrimary, Offset(cx + 15.5f * fs, fcy + fs * 1f), Offset(cx + 24.5f * fs, fcy + 1f * fs),
                    fs * 2f, StrokeCap.Round
                )
            }
            drawLine(
                theme.textPrimary, Offset(cx - fs * 28f, fcy - fs * 7f), Offset(cx - fs * 12f, fcy - fs * 4f),
                fs * 1.3f, StrokeCap.Round
            )
            drawLine(
                theme.textPrimary, Offset(cx + fs * 28f, fcy - fs * 7f), Offset(cx + fs * 12f, fcy - fs * 4f),
                fs * 1.3f, StrokeCap.Round
            )
            val frown = Path()
            frown.moveTo(cx - fs * 8f, fcy + 18f * fs)
            frown.quadraticBezierTo(cx, fcy + fs * 12f, cx + 8f * fs, fcy + 18f * fs)
            drawPath(frown, theme.textPrimary, style = Stroke(width = fs * 1.8f, cap = StrokeCap.Round))
        }
        HydraViewModel.HydraMood.WORRIED -> {
            drawLine(
                theme.textPrimary, Offset(cx - 26f * fs, fcy + fs * 1f), Offset(cx - fs * 12f, fcy + fs * 1f),
                fs * 2.2f, StrokeCap.Round
            )
            drawLine(
                theme.textPrimary, Offset(cx + fs * 12f, fcy + fs * 1f), Offset(cx + 26f * fs, fcy + 1f * fs),
                fs * 2.2f, StrokeCap.Round
            )
            val frown = Path()
            frown.moveTo(cx - fs * 8f, fcy + fs * 20f)
            frown.quadraticBezierTo(cx, fcy + fs * 12f, cx + 8f * fs, fcy + fs * 20f)
            drawPath(frown, theme.textPrimary, style = Stroke(width = fs * 1.8f, cap = StrokeCap.Round))
        }
    }

    if (showNecklace && level >= 2) {
        val ny = fcy + fs * 28f
        val nw = 32f * fs
        val np = Path()
        np.moveTo(cx - nw, ny)
        np.quadraticBezierTo(cx, ny + fs * 12f, cx + nw, ny)
        drawPath(np, Color(0xFFFFD700).copy(alpha = 0.55f), style = Stroke(width = 1.8f, cap = StrokeCap.Round))
        drawCircle(Color(0xFFFFD700).copy(alpha = 0.7f), fs * 3f, Offset(cx, ny + 11f * fs))
        drawCircle(Color(0xFFFF69B4).copy(alpha = 0.6f), fs * 1.5f, Offset(cx, ny + 11f * fs))
    }

    if (equippedAccessories.contains("crown")) {
        val crY = fcy - fs * 24f
        val crown = Path().apply {
            moveTo(cx - fs * 14f, crY + 9f)
            lineTo(cx - fs * 14f, crY + 2f)
            lineTo(cx - fs * 7f, crY + 6f)
            lineTo(cx, crY - 3f)
            lineTo(cx + fs * 7f, crY + 6f)
            lineTo(cx + fs * 14f, crY + 2f)
            lineTo(cx + 14f * fs, crY + 9f)
            close()
        }
        drawPath(crown, Color(0xFFFFD700).copy(alpha = 0.72f))
        drawCircle(Color(0xFFFF4444).copy(alpha = 0.8f), fs * 1.8f, Offset(cx, crY))
        drawCircle(Color(0xFF4444FF).copy(alpha = 0.8f), fs * 1.5f, Offset(cx - fs * 7f, crY + 5f))
        drawCircle(Color(0xFF4444FF).copy(alpha = 0.8f), fs * 1.5f, Offset(cx + fs * 7f, crY + 5f))
    }

    if (equippedAccessories.contains("flower")) {
        val flx = cx - 28f * fs
        val fly = fcy + 22f * fs
        for (i in 0 until 5) {
            val a = i * 72f * 3.1415927f / 180f
            drawCircle(
                Color(0xFFFF69B4).copy(alpha = 0.72f),
                fs * 4f,
                Offset(flx + cos(a) * 5f * fs, fly + sin(a) * 5f * fs)
            )
        }
        drawCircle(Color(0xFFFFEB3B).copy(alpha = 0.9f), fs * 2.5f, Offset(flx, fly))
    }

    if (equippedAccessories.contains("bracelet")) {
        val by = fcy + 50f * fs
        for (i in -4..4) {
            val bx = cx + i * 7.5f * fs
            drawCircle(Color(0xFFC8C8C8).copy(alpha = 0.72f), fs * 2.2f, Offset(bx, by))
            drawCircle(Color.White.copy(alpha = 0.42f), fs * 0.9f, Offset(bx - 0.6f, by - 0.6f))
        }
    }

    if (equippedAccessories.contains("pearl_ring")) {
        val prx = cx + fs * 24f
        val pry = fcy + 24f * fs
        for (i in 0 until 3) {
            val ox = prx + (i - 1) * 6f * fs
            drawCircle(Color(0xFFF2F2F2).copy(alpha = 0.9f), fs * 2.8f, Offset(ox, pry))
            drawCircle(Color.White.copy(alpha = 0.65f), fs * 1.1f, Offset(ox - 0.8f, pry - 0.8f))
        }
    }

    if (equippedAccessories.contains("ribbon")) {
        val rbx = cx + 32f * fs
        val rby = fcy + fs * 12f
        val rL = Path()
        rL.moveTo(rbx, rby)
        rL.cubicTo(rbx - fs * 9f, rby - fs * 7f, rbx - fs * 9f, rby + fs * 7f, rbx, rby)
        drawPath(rL, Color(0xFFFF69B4).copy(alpha = 0.76f))
        val rR = Path()
        rR.moveTo(rbx, rby)
        rR.cubicTo(rbx + fs * 9f, rby - fs * 7f, rbx + 9f * fs, rby + 7f * fs, rbx, rby)
        drawPath(rR, Color(0xFFFF69B4).copy(alpha = 0.76f))
        drawCircle(Color(0xFFFF1493).copy(alpha = 0.88f), fs * 2.2f, Offset(rbx, rby))
    }

    if (equippedAccessories.contains("rose")) {
        val rox = cx + 20f * fs
        val roy = fcy + 32f * fs
        drawCircle(Color(0xFFCC0000).copy(alpha = 0.62f), fs * 6.5f, Offset(rox, roy))
        drawCircle(Color(0xFFFF2222).copy(alpha = 0.62f), fs * 4.5f, Offset(rox - fs * 1.5f, roy - 1.5f * fs))
        drawCircle(Color(0xFFFF5555).copy(alpha = 0.6f), fs * 3f, Offset(rox - 0.8f * fs, roy - 2.5f * fs))
        val leaf = Path()
        leaf.moveTo(rox - fs * 6f, roy + 4f * fs)
        leaf.quadraticBezierTo(rox - 11f * fs, roy, rox - 6f * fs, roy - fs * 3f)
        drawPath(leaf, Color(0xFF2E7D32).copy(alpha = 0.72f), style = Stroke(width = fs * 1.6f, cap = StrokeCap.Round))
    }
}

private fun DrawScope.happyEye(cx: Float, cy: Float, w: Float, c: Color, sw: Float) {
    val p = Path()
    p.moveTo(cx - w / 2f, cy)
    p.quadraticBezierTo(cx, cy - 0.55f * w, cx + w / 2f, cy)
    drawPath(p, c, style = Stroke(width = sw, cap = StrokeCap.Round))
}

private fun heartPath(w: Float, h: Float): Path {
    return Path().apply {
        val cx = w / 2f
        val edge = w * 0.04f
        moveTo(cx, h * 0.88f)
        cubicTo(w * 0.1f, h * 0.62f, edge, h * 0.38f, 0.07f * w, h * 0.25f)
        cubicTo(0.12f * w, h * 0.1f, w * 0.3f, h * 0.08f, 0.4f * w, h * 0.14f)
        cubicTo(0.46f * w, h * 0.18f, cx - w * 0.02f, h * 0.24f, cx, h * 0.3f)
        cubicTo(cx + 0.02f * w, h * 0.24f, 0.54f * w, h * 0.18f, 0.6f * w, h * 0.14f)
        cubicTo(0.7f * w, h * 0.08f, w * 0.88f, h * 0.1f, 0.93f * w, h * 0.25f)
        cubicTo(w - edge, h * 0.38f, 0.9f * w, h * 0.62f, cx, h * 0.88f)
        close()
    }
}

private fun dropletPath(w: Float, h: Float): Path {
    return Path().apply {
        val cx = w / 2f
        moveTo(cx, h * 0.08f)
        cubicTo(cx + w * 0.05f, h * 0.2f, w * 0.85f, h * 0.45f, w * 0.85f, h * 0.62f)
        cubicTo(w * 0.85f, h * 0.82f, 0.7f * w, h * 0.93f, cx, h * 0.93f)
        cubicTo(0.3f * w, h * 0.93f, w * 0.15f, h * 0.82f, w * 0.15f, h * 0.62f)
        cubicTo(w * 0.15f, h * 0.45f, cx - 0.05f * w, h * 0.2f, cx, h * 0.08f)
        close()
    }
}

private fun bottlePath(w: Float, h: Float): Path {
    return Path().apply {
        val cx = w / 2f
        val bw = 0.28f * w
        moveTo(cx - bw * 0.35f, h * 0.06f)
        lineTo(cx + bw * 0.35f, 0.06f * h)
        lineTo(cx + bw * 0.35f, h * 0.12f)
        quadraticBezierTo(cx + bw * 0.35f, h * 0.15f, cx + bw * 0.55f, h * 0.19f)
        quadraticBezierTo(cx + bw, h * 0.23f, cx + bw, h * 0.27f)
        lineTo(cx + bw, h * 0.88f)
        quadraticBezierTo(cx + bw, h * 0.93f, cx + bw * 0.7f, h * 0.93f)
        lineTo(cx - 0.7f * bw, h * 0.93f)
        quadraticBezierTo(cx - bw, 0.93f * h, cx - bw, 0.88f * h)
        lineTo(cx - bw, 0.27f * h)
        quadraticBezierTo(cx - bw, 0.23f * h, cx - 0.55f * bw, 0.19f * h)
        quadraticBezierTo(cx - bw * 0.35f, 0.15f * h, cx - bw * 0.35f, h * 0.12f)
        close()
    }
}

private fun femalePath(w: Float, h: Float): Path {
    return Path().apply {
        val cx = w / 2f
        val sw = w * 0.22f
        addOval(Rect(cx - w * 0.08f, h * 0.03f, cx + w * 0.08f, h * 0.12f))
        val body = Path()
        body.moveTo(cx - w * 0.04f, h * 0.12f)
        body.lineTo(cx - w * 0.04f, h * 0.16f)
        body.cubicTo(cx - w * 0.18f, h * 0.17f, cx - sw, h * 0.19f, cx - sw, h * 0.24f)
        body.cubicTo(cx - w * 0.24f, h * 0.3f, cx - w * 0.23f, h * 0.34f, cx - w * 0.21f, h * 0.37f)
        body.cubicTo(cx - w * 0.17f, h * 0.42f, cx - w * 0.14f, h * 0.46f, cx - w * 0.13f, 0.5f * h)
        body.cubicTo(cx - w * 0.14f, 0.55f * h, cx - w * 0.24f, 0.58f * h, cx - 0.25f * w, h * 0.64f)
        body.cubicTo(cx - w * 0.24f, h * 0.72f, cx - 0.2f * w, 0.8f * h, cx - w * 0.17f, h * 0.92f)
        body.lineTo(cx - w * 0.08f, h * 0.92f)
        body.cubicTo(cx - 0.06f * w, 0.82f * h, cx - w * 0.03f, h * 0.72f, cx - 0.02f * w, h * 0.64f)
        body.lineTo(cx + 0.02f * w, h * 0.64f)
        body.cubicTo(cx + 0.03f * w, h * 0.72f, cx + 0.06f * w, h * 0.82f, cx + 0.08f * w, h * 0.92f)
        body.lineTo(cx + w * 0.17f, h * 0.92f)
        body.cubicTo(cx + 0.2f * w, h * 0.8f, cx + w * 0.24f, h * 0.72f, cx + 0.25f * w, h * 0.64f)
        body.cubicTo(cx + w * 0.24f, h * 0.58f, cx + w * 0.14f, h * 0.55f, cx + 0.13f * w, h * 0.5f)
        body.cubicTo(cx + 0.14f * w, h * 0.46f, cx + w * 0.17f, h * 0.42f, cx + 0.21f * w, h * 0.37f)
        body.cubicTo(cx + 0.23f * w, h * 0.34f, cx + w * 0.24f, h * 0.3f, cx + sw, h * 0.24f)
        body.cubicTo(cx + sw, h * 0.19f, cx + 0.18f * w, h * 0.17f, cx + w * 0.04f, h * 0.16f)
        body.lineTo(cx + 0.04f * w, 0.12f * h)
        body.close()
        addPath(body)
    }
}

private fun malePath(w: Float, h: Float): Path {
    return Path().apply {
        val cx = w / 2f
        val sw = w * 0.28f
        addOval(Rect(cx - w * 0.08f, h * 0.03f, cx + 0.08f * w, h * 0.12f))
        val body = Path()
        body.moveTo(cx - w * 0.05f, h * 0.12f)
        body.lineTo(cx - w * 0.05f, h * 0.16f)
        body.cubicTo(cx - w * 0.22f, h * 0.17f, cx - sw, h * 0.19f, cx - sw, h * 0.25f)
        body.cubicTo(cx - w * 0.26f, h * 0.34f, cx - w * 0.22f, h * 0.42f, cx - w * 0.19f, h * 0.5f)
        body.cubicTo(cx - w * 0.18f, h * 0.56f, cx - w * 0.18f, h * 0.6f, cx - w * 0.19f, h * 0.64f)
        body.cubicTo(cx - w * 0.19f, h * 0.72f, cx - w * 0.17f, h * 0.82f, cx - w * 0.15f, h * 0.92f)
        body.lineTo(cx - 0.06f * w, h * 0.92f)
        body.cubicTo(cx - 0.04f * w, h * 0.82f, cx - w * 0.03f, h * 0.72f, cx - 0.02f * w, 0.62f * h)
        body.lineTo(cx + 0.02f * w, 0.62f * h)
        body.cubicTo(cx + 0.03f * w, h * 0.72f, cx + 0.04f * w, h * 0.82f, cx + 0.06f * w, h * 0.92f)
        body.lineTo(cx + 0.15f * w, h * 0.92f)
        body.cubicTo(cx + w * 0.17f, h * 0.82f, cx + w * 0.19f, h * 0.72f, cx + w * 0.19f, h * 0.64f)
        body.cubicTo(cx + w * 0.18f, h * 0.6f, cx + 0.18f * w, h * 0.56f, cx + w * 0.19f, h * 0.5f)
        body.cubicTo(cx + w * 0.22f, h * 0.42f, cx + 0.26f * w, h * 0.34f, cx + sw, h * 0.25f)
        body.cubicTo(cx + sw, h * 0.19f, cx + 0.22f * w, h * 0.17f, cx + w * 0.05f, h * 0.16f)
        body.lineTo(cx + 0.05f * w, 0.12f * h)
        body.close()
        addPath(body)
    }
}

private fun braPath(w: Float, h: Float): Path {
    return Path().apply {
        val cx = w / 2f
        val cupW = w * 0.28f
        val cupH = h * 0.3f
        val top = h * 0.22f
        // straps
        moveTo(cx - cupW * 0.6f, top - h * 0.1f)
        lineTo(cx - cupW * 0.5f, top)
        moveTo(cx + cupW * 0.6f, top - h * 0.1f)
        lineTo(cx + 0.5f * cupW, top)
        // cups
        moveTo(cx - w * 0.04f, top + cupH * 0.15f)
        cubicTo(cx - w * 0.1f, top - cupH * 0.05f, cx - cupW - w * 0.05f, top, cx - cupW - w * 0.04f, top + cupH * 0.45f)
        cubicTo(cx - cupW - w * 0.03f, top + cupH * 0.85f, cx - w * 0.12f, top + cupH, cx - w * 0.04f, top + cupH * 0.85f)
        lineTo(cx + w * 0.04f, top + cupH * 0.85f)
        cubicTo(cx + 0.12f * w, top + cupH, cx + cupW + 0.03f * w, top + 0.85f * cupH, cx + cupW + w * 0.04f, top + 0.45f * cupH)
        cubicTo(cx + cupW + w * 0.05f, top, cx + w * 0.1f, top - 0.05f * cupH, cx + w * 0.04f, top + 0.15f * cupH)
        close()
        // band
        moveTo(cx - cupW - w * 0.06f, top + cupH * 0.6f)
        lineTo(cx - cupW - w * 0.1f, top + cupH * 0.65f)
        lineTo(cx - cupW - w * 0.1f, top + cupH * 0.75f)
        cubicTo(
            cx - cupW - w * 0.08f, top + cupH + h * 0.04f,
            cx + cupW + 0.08f * w, top + cupH + 0.04f * h,
            cx + cupW + w * 0.1f, top + 0.75f * cupH
        )
        lineTo(cx + cupW + 0.1f * w, top + 0.65f * cupH)
        lineTo(cx + cupW + 0.06f * w, top + 0.6f * cupH)
    }
}

private fun cupPath(w: Float, h: Float): Path {
    return Path().apply {
        val cx = w / 2f
        val topW = 0.4f * w
        val botW = 0.28f * w
        moveTo(cx - topW, h * 0.14f)
        lineTo(cx + topW, 0.14f * h)
        lineTo(cx + botW, h * 0.86f)
        quadraticBezierTo(cx + botW, h * 0.93f, cx + botW * 0.75f, h * 0.93f)
        lineTo(cx - 0.75f * botW, h * 0.93f)
        quadraticBezierTo(cx - botW, 0.93f * h, cx - botW, 0.86f * h)
        close()
    }
}
