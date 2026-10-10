package com.hydraheart2.app

import android.content.SharedPreferences
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import com.hydraheart2.app.ui.navigation.HydraNavigation
import com.hydraheart2.app.ui.screens.OnboardingScreen
import com.hydraheart2.app.ui.theme.HydraHeart2Theme
import com.hydraheart2.app.util.AppLocale
import com.hydraheart2.app.util.NotificationHelper
import com.hydraheart2.app.viewmodel.HydraViewModel

@Composable
fun HydraApp(viewModel: HydraViewModel, prefs: SharedPreferences) {
    val theme = viewModel.currentTheme
    val context = LocalContext.current

    LaunchedEffect(
        viewModel.isLoaded, viewModel.profile, viewModel.todayDrinks.size, viewModel.hp,
        viewModel.themeId, viewModel.streak, viewModel.earnedTrophyIds.size,
        viewModel.onboardingComplete, viewModel.reminderSound
    ) {
        if (viewModel.isLoaded) viewModel.save(context)
    }

    LaunchedEffect(
        viewModel.isLoaded, viewModel.showFace, viewModel.showSpeechBubble, viewModel.hydraShape,
        viewModel.showNecklace, viewModel.unitSystem, viewModel.customGoalEnabled, viewModel.customWaterGoalMl,
        viewModel.calDayFirst, viewModel.calShowDayName, viewModel.calShowTime, viewModel.cal12Hour,
        viewModel.calWeekStartMonday, viewModel.calDayResetHour, viewModel.customSpeechEnabled,
        viewModel.speechBubbleStyleId, viewModel.equippedAccessoryIds.toList(), viewModel.customSpeechLines.toList()
    ) {
        if (viewModel.isLoaded) viewModel.save(context)
    }

    LaunchedEffect(
        viewModel.isLoaded, viewModel.hapticsEnabled, viewModel.soundEffectsEnabled, viewModel.visualEffectsEnabled,
        viewModel.performanceMode, viewModel.keepScreenOn, viewModel.appLanguage, viewModel.interactionIntensity,
        viewModel.reduceMotion, viewModel.batteryAwareMode, viewModel.autoPerformance, viewModel.particlesEnabled,
        viewModel.animationsEnabled, viewModel.fullGlowEnabled
    ) {
        if (viewModel.isLoaded) viewModel.save(context)
    }

    LaunchedEffect(viewModel.isLoaded, viewModel.appLanguage) {
        if (viewModel.isLoaded) AppLocale.applyAppLanguage(viewModel.appLanguage)
    }

    val reminderTimes = viewModel.customReminders.map { it.time }
    val customMessages = viewModel.reminderMessages.toList()
    LaunchedEffect(
        viewModel.isLoaded, viewModel.remindersEnabled, reminderTimes,
        customMessages, viewModel.reminderSound
    ) {
        if (!viewModel.isLoaded) return@LaunchedEffect
        val allMessages = viewModel.allReminderMessages
        if (viewModel.remindersEnabled) {
            val reminders = viewModel.customReminders.map {
                val parts = it.time.split(":")
                val hour = parts[0].toIntOrNull() ?: 8
                val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
                hour to minute
            }
            NotificationHelper.rescheduleAll(context, reminders, allMessages, viewModel.reminderSound)
        } else {
            for (i in 0 until 20) {
                NotificationHelper.cancelReminder(context, i)
                NotificationHelper.cancelReminder(context, i + 1000)
            }
        }
        viewModel.save(context)
        NotificationHelper.saveBootData(
            context,
            viewModel.remindersEnabled,
            viewModel.customReminders.toList(),
            allMessages,
            viewModel.reminderSound
        )
    }

    HydraHeart2Theme(theme) {
        if (!viewModel.isLoaded) {
            Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3))))
        } else {
            AnimatedContent(
                targetState = viewModel.onboardingComplete,
                transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(300)) },
                modifier = Modifier.fillMaxSize(),
                label = "appState"
            ) { onboarded ->
                if (!onboarded) {
                    OnboardingScreen(viewModel) {
                        prefs.edit { putBoolean("has_seen_onboarding", true) }
                        viewModel.onboardingComplete = true
                    }
                } else {
                    HydraNavigation(viewModel)
                }
            }
        }
    }
}
