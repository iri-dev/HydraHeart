package com.hydraheart2.app.ui.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

// How every page changes: the new one gently fades in with a small drift from the right while the old one fades out to the left.
const val PAGE_FADE_IN_MS = 270
const val PAGE_FADE_OUT_MS = 170
private const val PAGE_DRIFT_MS = 360

val PageEnter: EnterTransition = fadeIn(tween(PAGE_FADE_IN_MS, easing = EaseOutSine)) +
    slideInHorizontally(tween(PAGE_DRIFT_MS, easing = EaseOutCubic)) { it / 17 }
val PageExit: ExitTransition = fadeOut(tween(PAGE_FADE_OUT_MS, easing = EaseInOutSine)) +
    slideOutHorizontally(tween(PAGE_DRIFT_MS, easing = EaseOutCubic)) { -it / 17 }

fun pageTransition(): ContentTransform = PageEnter togetherWith PageExit

// Hydra fades in and grows a little as her page opens, the same way as on Your Profile.
fun Modifier.heartFadeIn(): Modifier = composed {
    val shown = remember { Animatable(0f) }
    LaunchedEffect(Unit) { shown.animateTo(1f, tween(600)) }
    graphicsLayer {
        alpha = shown.value
        scaleX = 0.92f + 0.08f * shown.value
        scaleY = scaleX
    }
}
