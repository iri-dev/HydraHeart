package com.hydraheart2.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel

@Composable
fun DeveloperProfileScreen(viewModel: HydraViewModel, onBack: () -> Unit, onNavigateTo: (String) -> Unit) {
    val theme = viewModel.currentTheme
    val ctx = LocalContext.current

    var tapCount by remember { mutableIntStateOf(0) }
    var lastTapTime by remember { mutableLongStateOf(0L) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            ) {
                IconButton(onClick = {
                    HapticEngine.tick(ctx)
                    onBack()
                }) {
                    Text("←", fontSize = 24.sp, color = theme.accent, fontWeight = FontWeight.Light)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Developer Profile",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = theme.accent
                )
            }

            FrostedGlass(theme = theme, isProminent = true) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "iri.dev",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
                        color = theme.accent,
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                val now = System.currentTimeMillis()
                                if (now - lastTapTime < 500) tapCount++ else tapCount = 1
                                lastTapTime = now
                                if (tapCount >= 20) {
                                    onNavigateTo("dev_level_control")
                                    tapCount = 0
                                }
                            })
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "I am an independent developer dedicated to crafting high-quality, intentional digital experiences that respect the user and enhance daily life.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = theme.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        "HydraHeart was born from a desire to make self-care feel beautiful rather than mechanical. Every animation, sound, and word is chosen to make your hydration journey feel personal, calming, and rewarding.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.textSoft,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(32.dp))
                    DeveloperActionRow(
                        icon = "💻",
                        label = "GitHub",
                        theme = theme,
                        onClick = {
                            runCatching { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/iri-dev"))) }
                        }
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
            Text(
                "Thank you for being part of this journey. 🤍",
                style = MaterialTheme.typography.bodySmall,
                color = theme.textMuted,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DeveloperActionRow(icon: String, label: String, theme: HydraTheme, onClick: () -> Unit) {
    val ctx = LocalContext.current
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (pressed) 0.98f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "devRowScale"
    )
    val bgAlpha by animateFloatAsState(if (pressed) 0.08f else 0.04f, tween(120), label = "devRowBg")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(theme.accent.copy(alpha = bgAlpha))
            .clickable(interactionSource = source, indication = null) {
                HapticEngine.tick(ctx)
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(icon, fontSize = 18.sp)
        Spacer(Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = theme.accent)
    }
}

@Composable
fun LegalContentScreen(viewModel: HydraViewModel, title: String, type: String, onBack: () -> Unit) {
    val theme = viewModel.currentTheme
    val ctx = LocalContext.current
    val content = if (type == "privacy") {
        "At HydraHeart, your privacy is a core feature, not an afterthought.\n\n" +
            "1. Data Collection: We do not collect, store, or share any personal information. All your hydration data, profile settings, and mood logs are stored locally on your device.\n\n" +
            "2. Third Parties: No third-party tracking or analytics are used. We believe your habits should stay yours.\n\n" +
            "3. Permissions: We only ask for permissions necessary to run the app (like notifications for reminders).\n\n" +
            "4. Transparency: If you delete the app, all your data is permanently removed from your device. We have no way to recover it because we never had it."
    } else {
        "By using HydraHeart, you agree to the following terms:\n\n" +
            "1. Purpose: HydraHeart is a hydration tracking tool. It is not a medical device and should not be used as professional medical advice.\n\n" +
            "2. Ownership: All design elements, code, and content are owned by iri.dev.\n\n" +
            "3. Responsibility: You are responsible for how you use the app and for setting hydration goals that are safe and appropriate for your health.\n\n" +
            "4. Updates: We may update these terms occasionally to reflect changes in the app. Your continued use of the app signifies acceptance of any changes."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            ) {
                IconButton(onClick = {
                    HapticEngine.tick(ctx)
                    onBack()
                }) {
                    Text("←", fontSize = 24.sp, color = theme.accent, fontWeight = FontWeight.Light)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = theme.accent
                )
            }

            FrostedGlass(theme = theme) {
                Text(
                    content,
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                    color = theme.textPrimary,
                    modifier = Modifier.padding(24.dp)
                )
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
fun WhatsNewScreen(viewModel: HydraViewModel, onBack: () -> Unit) {
    val theme = viewModel.currentTheme
    val ctx = LocalContext.current
    val changes = listOf(
        "✨ Fully native in-app legal screens for a seamless experience.",
        "💎 Refined developer identity with the new iri.dev profile.",
        "🌊 Dynamic hydration status in the About screen.",
        "📧 Direct contact support integration.",
        "⚡ Enhanced micro-interactions and tactile feedback.",
        "🎨 Visual polish across all system cards.",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            ) {
                IconButton(onClick = {
                    HapticEngine.tick(ctx)
                    onBack()
                }) {
                    Text("←", fontSize = 24.sp, color = theme.accent, fontWeight = FontWeight.Light)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "What's New",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = theme.accent
                )
            }

            FrostedGlass(theme = theme, isProminent = true) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "Version 1.0.0 (Latest)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = theme.accent
                    )
                    Spacer(Modifier.height(16.dp))
                    changes.forEach { change ->
                        Row(modifier = Modifier.padding(vertical = 6.dp)) {
                            Text(change, style = MaterialTheme.typography.bodyLarge, color = theme.textPrimary)
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    HapticEngine.tick(ctx)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = theme.accent)
            ) {
                Text("Great!", color = theme.bg1, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LevelControlScreen(viewModel: HydraViewModel, onBack: () -> Unit) {
    val theme = viewModel.currentTheme
    val ctx = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            ) {
                IconButton(onClick = {
                    HapticEngine.tick(ctx)
                    onBack()
                }) {
                    Text("←", fontSize = 24.sp, color = theme.accent, fontWeight = FontWeight.Light)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Level Control",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = theme.accent
                )
            }

            Text(
                "Developer Access Granted",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                color = theme.accent.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
            )

            FrostedGlass(theme = theme, padding = PaddingValues(0.dp)) {
                Column {
                    (1..10).forEach { level ->
                        val isFirst = level == 1
                        val isLast = level == 10
                        val isCurrent = viewModel.levelInfo.level == level
                        val shape = when {
                            isFirst && isLast -> RoundedCornerShape(22.dp)
                            isFirst -> RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
                            isLast -> RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)
                            else -> RoundedCornerShape(0.dp)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (isCurrent) theme.accent.copy(alpha = 0.1f) else Color.Transparent, shape)
                                .clickable {
                                    HapticEngine.tick(ctx)
                                    var targetHp = 0
                                    var required = 500
                                    for (i in 1 until level) {
                                        targetHp += required
                                        required = (required * 2.5).toInt()
                                    }
                                    viewModel.hp = targetHp
                                }
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Level $level",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isCurrent) theme.accent else theme.textPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            if (isCurrent) {
                                Text("Active", style = MaterialTheme.typography.labelSmall, color = theme.accent)
                            }
                        }
                        if (!isLast) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(0.5.dp)
                                    .padding(horizontal = 20.dp)
                                    .background(theme.accent.copy(alpha = 0.06f))
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
