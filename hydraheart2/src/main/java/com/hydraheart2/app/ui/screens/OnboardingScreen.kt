package com.hydraheart2.app.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.data.models.ActivityLevel
import com.hydraheart2.app.data.models.Climate
import com.hydraheart2.app.data.models.Gender
import com.hydraheart2.app.ui.components.AddictiveButton
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.ui.components.HydraHeartCanvas
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel
import java.util.Calendar

@Composable
fun OnboardingScreen(viewModel: HydraViewModel, onComplete: () -> Unit) {
    val theme = viewModel.currentTheme
    val context = LocalContext.current
    var step by remember { mutableIntStateOf(0) }

    val notificationGranted = remember { mutableStateOf(false) }
    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        notificationGranted.value = granted
        if (granted) {
            viewModel.remindersEnabled = true
            HapticEngine.celebration(context)
        }
    }

    val inf = rememberInfiniteTransition(label = "float")
    val floatY by inf.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(tween(2500, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "y"
    )

    Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.size(if (step == 0) 180.dp else 130.dp).graphicsLayer { translationY = floatY }) {
                HydraHeartCanvas(
                    fillPercent = if (step >= 8) 100f else step * 8f + 25f,
                    mood = if (step >= 8) HydraViewModel.HydraMood.ECSTATIC else HydraViewModel.HydraMood.CONCERNED,
                    theme = theme,
                    showFace = true,
                    hydraShape = "heart",
                    drinkColours = listOf(HydraViewModel.DrinkColourPortion(theme.accent, 1f)),
                    level = 1
                )
            }

            Spacer(Modifier.height(24.dp))

            AnimatedContent(step, transitionSpec = {
                (fadeIn(tween(500)) + slideInVertically { it / 3 }).togetherWith(fadeOut(tween(250)) + slideOutVertically { -it / 3 })
            }, label = "speech") { s ->
                val hydraResponse = when (s) {
                    0 -> "Hey there! I'm Hydra 💕\n\nI'm your personal hydration companion and I'm SO happy to meet you! Let's get to know each other so I can take the best care of you."
                    1 -> "First things first — what should I call you? 😊"
                    2 -> "Ooh ${viewModel.profile.name}, I love that name! ✨\n\nWhen is your birthday? I want to make sure I know exactly how much your body needs."
                    3 -> "Got it! 🎂\n\nAnd how do you identify? This helps me refine my calculations just for you."
                    4 -> "Perfect. Now, let's talk about your height 📏\n\nIt helps me understand your body's surface area for hydration."
                    5 -> "And your weight? ⚖️\n\nThis is one of the most important things for me to know to give you a smart target."
                    6 -> "Nearly there! How active are you day to day? 🏃‍♀️\n\nMore movement means we need more water to stay glowing."
                    7 -> "Last one, I promise! What's the weather usually like where you live? ☀️"
                    8 -> {
                        val g = viewModel.goals
                        "All done, ${viewModel.profile.name}! 🎉\n\nBased on your profile, I've calculated your daily goal:\n\n💧 ${g.waterMl}ml\n\nIt's perfectly tailored just for you."
                    }
                    9 -> "One last thing — how would you like me to show up for you? 💕\n\nSome people love a chatty companion. Others prefer things clean and simple. There's no wrong answer, and you can always change it later."
                    10 -> "Ooh one more thing! 🔔\n\nCan I send you little reminders to drink water? I just want to look after you! I promise I won't be annoying 💕"
                    else -> ""
                }
                FrostedGlass(
                    theme = theme,
                    cornerRadius = 24.dp,
                    modifier = Modifier.fillMaxWidth(),
                    padding = PaddingValues(24.dp),
                    borderAlpha = 0.2f
                ) {
                    Text(
                        text = hydraResponse,
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                        color = theme.textPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            AnimatedContent(step, transitionSpec = {
                (fadeIn(tween(400)) + slideInHorizontally { it / 4 }).togetherWith(fadeOut(tween(200)))
            }, label = "input") { s ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    when (s) {
                        1 -> {
                            OutlinedTextField(
                                value = viewModel.profile.name,
                                onValueChange = { viewModel.profile = viewModel.profile.copy(name = it) },
                                placeholder = { Text("Your beautiful name...", color = theme.textMuted) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = theme.accent,
                                    unfocusedBorderColor = theme.cardBorder.copy(alpha = 0.5f),
                                    cursorColor = theme.accent,
                                    focusedTextColor = theme.textPrimary,
                                    unfocusedTextColor = theme.textPrimary,
                                    focusedContainerColor = theme.accent.copy(alpha = 0.05f)
                                )
                            )
                        }

                        2 -> {
                            OnboardingBirthdayPicker(
                                current = viewModel.profile.birthDate,
                                theme = theme,
                                onDateChange = { viewModel.profile = viewModel.profile.copy(birthDate = it) }
                            )
                        }

                        3 -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                Gender.entries.forEach { g ->
                                    val sel = viewModel.profile.gender == g
                                    OnboardingSelectionButton(g.label, sel, theme, Modifier.weight(1f)) {
                                        HapticEngine.click(context); viewModel.profile = viewModel.profile.copy(gender = g)
                                    }
                                }
                            }
                        }

                        4 -> {
                            OnboardingWheelPicker(
                                value = viewModel.profile.heightCm,
                                min = 100, max = 220, unit = "cm", theme = theme
                            ) { viewModel.profile = viewModel.profile.copy(heightCm = it) }
                        }

                        5 -> {
                            OnboardingWheelPicker(
                                value = viewModel.profile.weightKg.toInt(),
                                min = 30, max = 200, unit = "kg", theme = theme
                            ) { viewModel.profile = viewModel.profile.copy(weightKg = it.toFloat()) }
                        }

                        6 -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                ActivityLevel.entries.forEach { level ->
                                    val sel = viewModel.profile.activityLevel == level
                                    OnboardingSelectionButton("${level.icon}  ${level.label}", sel, theme, Modifier.fillMaxWidth()) {
                                        HapticEngine.click(context); viewModel.profile = viewModel.profile.copy(activityLevel = level)
                                    }
                                }
                            }
                        }

                        7 -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                Climate.entries.forEach { c ->
                                    val sel = viewModel.profile.climate == c
                                    OnboardingSelectionButton("${c.icon} ${c.label}", sel, theme, Modifier.weight(1f)) {
                                        HapticEngine.click(context); viewModel.profile = viewModel.profile.copy(climate = c)
                                    }
                                }
                            }
                        }

                        9 -> {
                            val isFull = viewModel.showFace && viewModel.showSpeechBubble
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                ExperienceModeCard(
                                    icon = "💕",
                                    title = "Full Hydra Experience",
                                    description = "Hydra speaks, reacts, and grows with you — a personal companion through every sip",
                                    selected = isFull,
                                    theme = theme,
                                    onClick = {
                                        HapticEngine.click(context)
                                        viewModel.showFace = true
                                        viewModel.showSpeechBubble = true
                                    }
                                )
                                ExperienceModeCard(
                                    icon = "🎯",
                                    title = "Focus Mode",
                                    description = "Clean and distraction-free — just your goal, your progress, and your data",
                                    selected = !isFull,
                                    theme = theme,
                                    onClick = {
                                        HapticEngine.click(context)
                                        viewModel.showFace = false
                                        viewModel.showSpeechBubble = false
                                    }
                                )
                            }
                        }

                        10 -> {
                            if (!notificationGranted.value) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    AddictiveButton(
                                        text = "🔔 Turn On Reminders",
                                        onClick = {
                                            HapticEngine.click(context)
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                            } else {
                                                notificationGranted.value = true
                                                viewModel.remindersEnabled = true
                                                HapticEngine.celebration(context)
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        theme = theme,
                                        isPrimary = true,
                                        cornerRadius = 18.dp
                                    )
                                    TextButton(onClick = { HapticEngine.tick(context); viewModel.onboardingComplete = true; onComplete() }) {
                                        Text("Maybe later", color = theme.textMuted)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step in 1 until 10) {
                    TextButton(onClick = { HapticEngine.tick(context); step-- }) {
                        Text("Back", color = theme.textMuted, fontWeight = FontWeight.Medium)
                    }
                }

                val canProceed = when (step) {
                    1 -> viewModel.profile.name.isNotBlank()
                    2 -> viewModel.profile.birthDate.isNotBlank()
                    else -> true
                }
                val nextLabel = when (step) {
                    0 -> "Let's Start! ✨"
                    8 -> "Continue ✨"
                    10 -> if (notificationGranted.value) "Ready! 🚀" else null
                    else -> "Next"
                }
                if (nextLabel != null) {
                    AddictiveButton(
                        text = nextLabel,
                        onClick = {
                            HapticEngine.click(context)
                            if (step < 10) step++ else { viewModel.onboardingComplete = true; onComplete() }
                        },
                        modifier = Modifier.weight(1f),
                        theme = theme,
                        colour = if (canProceed) theme.accent else theme.textMuted.copy(0.3f),
                        isPrimary = true,
                        enabled = canProceed,
                        cornerRadius = 20.dp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(11) { i ->
                    val active = i == step
                    Box(
                        Modifier
                            .size(if (active) 24.dp else 8.dp, 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (active) theme.accent else if (i < step) theme.accent.copy(0.4f) else theme.cardBorder.copy(0.5f))
                            .animateContentSize()
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingSelectionButton(text: String, selected: Boolean, theme: HydraTheme, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) theme.accent.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.1f),
        border = BorderStroke(1.5.dp, if (selected) theme.accent else theme.cardBorder.copy(alpha = 0.3f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) theme.accent else theme.textPrimary,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun OnboardingWheelPicker(value: Int, min: Int, max: Int, unit: String, theme: HydraTheme, onValueChange: (Int) -> Unit) {
    val items = remember(min, max) { (min..max).map { it.toString() } }
    val startIndex = (value - min).coerceIn(0, items.lastIndex)

    Box(
        modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(20.dp))
            .background(theme.accent.copy(alpha = 0.05f))
            .border(1.dp, theme.accent.copy(alpha = 0.1f), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(unit, style = MaterialTheme.typography.labelSmall, color = theme.accent)
            Spacer(Modifier.height(8.dp))
            WheelPickerInner(
                items = items,
                startIndex = startIndex,
                onItemSelected = { onValueChange(min + it) },
                theme = theme,
                itemHeight = 44.dp,
                visibleItems = 3
            )
        }
    }
}

@Composable
fun OnboardingBirthdayPicker(current: String, theme: HydraTheme, onDateChange: (String) -> Unit) {
    val parts = current.split("/")
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = remember { (1920..currentYear).map { it.toString() } }
    val months = remember { listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec") }
    val days = remember { (1..31).map { "%02d".format(it) } }

    var selDay by remember { mutableIntStateOf((parts.getOrNull(0)?.toIntOrNull() ?: 1) - 1) }
    var selMonth by remember { mutableIntStateOf((parts.getOrNull(1)?.toIntOrNull() ?: 6) - 1) }
    var selYear by remember { mutableIntStateOf((parts.getOrNull(2)?.toIntOrNull() ?: 2000) - 1920) }

    LaunchedEffect(selDay, selMonth, selYear) {
        onDateChange("%02d/%02d/%04d".format(selDay + 1, selMonth + 1, selYear + 1920))
    }

    Row(
        modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(20.dp))
            .background(theme.accent.copy(alpha = 0.05f))
            .border(1.dp, theme.accent.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.weight(1f)) {
            WheelPickerInner(days, selDay, { selDay = it }, theme, itemHeight = 44.dp, visibleItems = 3)
        }
        Box(Modifier.weight(1.2f)) {
            WheelPickerInner(months, selMonth, { selMonth = it }, theme, itemHeight = 44.dp, visibleItems = 3)
        }
        Box(Modifier.weight(1.2f)) {
            WheelPickerInner(years, selYear.coerceIn(0, years.lastIndex), { selYear = it }, theme, itemHeight = 44.dp, visibleItems = 3)
        }
    }
}

@Composable
private fun WheelPickerInner(
    items: List<String>,
    startIndex: Int,
    onItemSelected: (Int) -> Unit,
    theme: HydraTheme,
    itemHeight: Dp = 48.dp,
    visibleItems: Int = 3
) {
    val padding = visibleItems / 2
    val paddedItems = remember(items) { List(padding) { "" } + items + List(padding) { "" } }
    val state = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)
    val snap = rememberSnapFlingBehavior(state)
    val selected by remember { derivedStateOf { state.firstVisibleItemIndex } }

    LaunchedEffect(selected) {
        if (selected in items.indices) onItemSelected(selected)
    }

    LazyColumn(
        modifier = Modifier.height(itemHeight * visibleItems).fillMaxWidth(),
        state = state,
        flingBehavior = snap
    ) {
        itemsIndexed(paddedItems) { index, item ->
            val isSelected = index - padding == selected
            Box(
                modifier = Modifier.height(itemHeight).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) theme.accent else theme.textSoft.copy(0.5f),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.graphicsLayer { scaleX = if (isSelected) 1.1f else 0.9f; scaleY = if (isSelected) 1.1f else 0.9f }
                )
            }
        }
    }
}

@Composable
private fun ExperienceModeCard(
    icon: String,
    title: String,
    description: String,
    selected: Boolean,
    theme: HydraTheme,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (selected) theme.accent.copy(alpha = 0.13f) else Color.White.copy(alpha = 0.06f),
        border = BorderStroke(
            if (selected) 1.5.dp else 0.8.dp,
            if (selected) theme.accent.copy(alpha = 0.5f) else theme.cardBorder.copy(alpha = 0.25f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 26.sp)
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = if (selected) theme.accent else theme.textPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.textSoft
                )
            }
            if (selected) {
                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier.size(22.dp).clip(RoundedCornerShape(50)).background(theme.accent),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
