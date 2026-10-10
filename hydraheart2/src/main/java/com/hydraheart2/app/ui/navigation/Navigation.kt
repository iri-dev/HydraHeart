package com.hydraheart2.app.ui.navigation

import android.app.Activity
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.hydraheart2.app.ui.components.NavBar
import com.hydraheart2.app.ui.screens.AboutScreen
import com.hydraheart2.app.ui.screens.DeveloperProfileScreen
import com.hydraheart2.app.ui.screens.DiaryScreen
import com.hydraheart2.app.ui.screens.EducationScreen
import com.hydraheart2.app.ui.screens.HistoryScreen
import com.hydraheart2.app.ui.screens.HomeScreen
import com.hydraheart2.app.ui.screens.LegalContentScreen
import com.hydraheart2.app.ui.screens.LevelControlScreen
import com.hydraheart2.app.ui.screens.LogDrinkScreen
import com.hydraheart2.app.ui.screens.MoodCalendarScreen
import com.hydraheart2.app.ui.screens.MoodTrackingScreen
import com.hydraheart2.app.ui.screens.SettingsScreen
import com.hydraheart2.app.ui.screens.TrophiesScreen
import com.hydraheart2.app.ui.screens.WhatsNewScreen
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel

@Composable
fun HydraNavigation(viewModel: HydraViewModel) {
    var currentScreen by rememberSaveable { mutableStateOf("home") }
    val backStack = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { mutableStateListOf(*it.toTypedArray()) }
        )
    ) { mutableStateListOf<String>() }

    fun navigateTo(screen: String) {
        if (screen != currentScreen) {
            backStack.add(currentScreen)
            currentScreen = screen
        }
    }

    LaunchedEffect(viewModel.hapticsEnabled) {
        HapticEngine.isEnabled = viewModel.hapticsEnabled
    }

    val view = LocalView.current
    DisposableEffect(viewModel.keepScreenOn) {
        val window = (view.context as? Activity)?.window
        if (viewModel.keepScreenOn) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    BackHandler(enabled = backStack.isNotEmpty()) {
        currentScreen = backStack.removeAt(backStack.lastIndex)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentScreen,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(100)) },
            label = "screenTransition"
        ) { screen ->
            when (screen) {
                "home" -> HomeScreen(viewModel, onNavigate = { navigateTo(it) })
                "log" -> LogDrinkScreen(viewModel, onBack = { navigateTo("home") })
                "history" -> HistoryScreen(viewModel, onNavigate = { navigateTo(it) })
                "education" -> EducationScreen(viewModel)
                "trophies" -> TrophiesScreen(viewModel)
                "settings" -> SettingsScreen(viewModel, onBack = { navigateTo("home") }, onNavigate = { navigateTo(it) })
                "diary" -> DiaryScreen(viewModel, onBack = { navigateTo("home") })
                "mood_tracking" -> MoodTrackingScreen(viewModel, onBack = { navigateTo("settings") })
                "mood_calendar" -> MoodCalendarScreen(viewModel, onBack = { navigateTo("settings") })
                "about" -> AboutScreen(viewModel, onBack = { navigateTo("settings") }, onNavigate = { route -> navigateTo(route) })
                "dev_profile" -> DeveloperProfileScreen(
                    viewModel,
                    onBack = { navigateTo("about") },
                    onNavigate = { route -> navigateTo(route) }
                )
                "privacy" -> LegalContentScreen(viewModel, "Privacy Policy", "privacy", onBack = { navigateTo("about") })
                "terms" -> LegalContentScreen(viewModel, "Terms of Service", "terms", onBack = { navigateTo("about") })
                "whats_new" -> WhatsNewScreen(viewModel, onBack = { navigateTo("about") })
                "dev_level_control" -> LevelControlScreen(viewModel, onBack = { navigateTo("about") })
            }
        }

        val hideNavBar = listOf(
            "settings", "diary", "mood_tracking", "mood_calendar", "about",
            "dev_profile", "privacy", "terms", "whats_new", "dev_level_control"
        ).contains(currentScreen)
        if (!hideNavBar) {
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                NavBar(currentScreen, viewModel.currentTheme, onNavigate = { navigateTo(it) })
            }
        }
    }
}
