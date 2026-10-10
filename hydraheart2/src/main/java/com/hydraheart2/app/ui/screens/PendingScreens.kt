package com.hydraheart2.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hydraheart2.app.viewmodel.HydraViewModel

// Temporary placeholders for screens that are still being built.

@Composable
private fun Pending(name: String, viewModel: HydraViewModel, onBack: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = onBack != null) { onBack?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Text("$name — coming soon", color = viewModel.currentTheme.textSoft)
    }
}

@Composable
fun EducationScreen(viewModel: HydraViewModel) = Pending("Learn", viewModel)

@Composable
fun SettingsScreen(viewModel: HydraViewModel, onBack: () -> Unit, onNavigate: (String) -> Unit) =
    Pending("Settings", viewModel, onBack)

@Composable
fun AboutScreen(viewModel: HydraViewModel, onBack: () -> Unit, onNavigate: (String) -> Unit) =
    Pending("About", viewModel, onBack)

@Composable
fun DeveloperProfileScreen(viewModel: HydraViewModel, onBack: () -> Unit, onNavigate: (String) -> Unit) =
    Pending("Developer profile", viewModel, onBack)

@Composable
fun LegalContentScreen(viewModel: HydraViewModel, title: String, type: String, onBack: () -> Unit) =
    Pending(title, viewModel, onBack)

@Composable
fun WhatsNewScreen(viewModel: HydraViewModel, onBack: () -> Unit) = Pending("What's new", viewModel, onBack)

@Composable
fun LevelControlScreen(viewModel: HydraViewModel, onBack: () -> Unit) = Pending("Level control", viewModel, onBack)

@Composable
fun OnboardingScreen(viewModel: HydraViewModel, onComplete: () -> Unit) = Pending("Onboarding", viewModel, onComplete)
