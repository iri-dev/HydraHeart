package com.hydraheart2.app.ui.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

// How every page changes: the new one gently fades in with a small drift from the right while the old one fades out to the left.
const val PAGE_FADE_IN_MS = 270
const val PAGE_FADE_OUT_MS = 170
private const val PAGE_DRIFT_MS = 360

val PageEnter: EnterTransition = fadeIn(tween(PAGE_FADE_IN_MS, easing = EaseOutSine)) +
    slideInHorizontally(tween(PAGE_DRIFT_MS, easing = EaseOutCubic)) { it / 17 }
val PageExit: ExitTransition = fadeOut(tween(PAGE_FADE_OUT_MS, easing = EaseInOutSine)) +
    slideOutHorizontally(tween(PAGE_DRIFT_MS, easing = EaseOutCubic)) { -it / 17 }

fun pageTransition(): ContentTransform = PageEnter togetherWith PageExit
