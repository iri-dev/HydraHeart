package com.hydraheart2.app.ui.screens

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.hydraheart2.app.R
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.data.models.ActivityLevel
import com.hydraheart2.app.data.models.Climate
import com.hydraheart2.app.data.models.CustomReminder
import com.hydraheart2.app.data.models.Gender
import com.hydraheart2.app.data.models.Personality
import com.hydraheart2.app.ui.components.AddictiveButton
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.ui.components.HydraHeartCanvas
import com.hydraheart2.app.ui.components.HydraSpeechBubble
import com.hydraheart2.app.ui.components.HydraTimePicker
import com.hydraheart2.app.ui.navigation.pageTransition
import com.hydraheart2.app.util.AppLocale
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.util.NotificationHelper
import com.hydraheart2.app.viewmodel.HydraViewModel
import java.util.Calendar
import kotlin.math.abs
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: HydraViewModel,
    onBack: () -> Unit,
    onNavigateTo: (String) -> Unit = {}
) {
    val theme = viewModel.currentTheme
    val ctx = LocalContext.current
    var sec by remember { mutableStateOf(viewModel.pendingSettingsSection.also { viewModel.pendingSettingsSection = null }) }
    var newMsg by remember { mutableStateOf("") }

    BackHandler(enabled = sec != null) { sec = null }

    Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            ) {
                IconButton(onClick = {
                    HapticEngine.tick(ctx)
                    if (sec != null) sec = null else onBack()
                }) {
                    Text("←", fontSize = 24.sp, color = theme.accent, fontWeight = FontWeight.Light)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    when (sec) {
                        null -> stringResource(R.string.settings_title)
                        "profile" -> stringResource(R.string.settings_title_profile)
                        "personality" -> stringResource(R.string.settings_title_personality)
                        "hydra" -> stringResource(R.string.settings_title_hydra)
                        "goals" -> stringResource(R.string.settings_title_goals)
                        "calendar" -> stringResource(R.string.settings_title_calendar)
                        "reminders" -> stringResource(R.string.settings_title_reminders)
                        "theme" -> stringResource(R.string.settings_title_theme)
                        "units" -> stringResource(R.string.settings_title_units)
                        "howwefeel" -> stringResource(R.string.settings_title_how_we_feel)
                        "general" -> stringResource(R.string.settings_title_general)
                        else -> sec?.replaceFirstChar { it.uppercase() } ?: ""
                    },
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = theme.accent
                )
            }

            AnimatedContent(
                targetState = sec,
                transitionSpec = { pageTransition() },
                label = "settings_nav"
            ) { s ->
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    when (s) {
                        null -> MainSettingsMenu(onSelect = { id -> if (id == "about") onNavigateTo("about") else sec = id }, theme = theme)
                        "profile" -> ProfileSettingsSection(viewModel, theme)
                        "personality" -> PersonalitySettingsSection(viewModel, theme)
                        "hydra" -> HydraLookSettingsSection(viewModel, theme)
                        "calendar" -> CalendarSettingsSection(viewModel, theme)
                        "reminders" -> RemindersSettingsSection(viewModel, theme, newMsg, onMsgChange = { newMsg = it })
                        "theme" -> ThemeSettingsSection(viewModel, theme)
                        "units" -> UnitSettingsSection(viewModel, theme)
                        "howwefeel" -> EmotionalJourneySection(viewModel, theme, onNavigateTo)
                        "goals" -> GoalsSettingsSection(viewModel, theme)
                        "general" -> GeneralSettingsSection(viewModel, theme)
                    }
                }
            }
        }
    }
}

@Composable
private fun MainSettingsMenu(onSelect: (String) -> Unit, theme: HydraTheme) {
    val groups = listOf(
        stringResource(R.string.menu_group_profile) to listOf(
            "profile" to ("👤" to stringResource(R.string.menu_profile)),
            "howwefeel" to ("💖" to stringResource(R.string.menu_how_we_feel))
        ),
        stringResource(R.string.menu_group_hydra) to listOf(
            "personality" to ("💬" to stringResource(R.string.menu_personality)),
            "hydra" to ("💕" to stringResource(R.string.menu_hydra_look))
        ),
        stringResource(R.string.menu_group_tracking) to listOf(
            "goals" to ("🎯" to stringResource(R.string.menu_goals)),
            "calendar" to ("📅" to stringResource(R.string.menu_calendar)),
            "reminders" to ("🔔" to stringResource(R.string.menu_reminders))
        ),
        stringResource(R.string.menu_group_app) to listOf(
            "theme" to ("🎨" to stringResource(R.string.menu_themes)),
            "units" to ("📐" to stringResource(R.string.menu_units)),
            "general" to ("⚙️" to stringResource(R.string.menu_general)),
            "about" to ("ℹ️" to stringResource(R.string.menu_about))
        )
    )

    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        groups.forEachIndexed { groupIndex, (groupLabel, items) ->
            if (groupIndex > 0) Spacer(Modifier.height(26.dp)) else Spacer(Modifier.height(12.dp))

            MenuGroupHeader(label = groupLabel, theme = theme)
            Spacer(Modifier.height(6.dp))

            FrostedGlass(
                theme = theme,
                padding = PaddingValues(0.dp),
                isProminent = groupIndex == 0
            ) {
                items.forEachIndexed { itemIndex, (id, data) ->
                    val isFirst = itemIndex == 0
                    val isLast = itemIndex == items.lastIndex

                    SettingsMenuRow(
                        icon = data.first,
                        label = data.second,
                        theme = theme,
                        isFirst = isFirst,
                        isLast = isLast,
                        onClick = { onSelect(id) }
                    )

                    if (!isLast) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .height(0.5.dp)
                                .background(theme.accent.copy(alpha = 0.06f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsMenuRow(
    icon: String,
    label: String,
    theme: HydraTheme,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: () -> Unit
) {
    val ctx = LocalContext.current
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()

    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "rowScale")
    val bgAlpha by animateFloatAsState(if (pressed) 0.08f else 0f, tween(120), label = "rowBg")

    val topR = if (isFirst) 22.dp else 0.dp
    val bottomR = if (isLast) 22.dp else 0.dp
    val shape = RoundedCornerShape(topR, topR, bottomR, bottomR)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(shape)
            .background(theme.accent.copy(alpha = bgAlpha))
            .clickable(interactionSource = source, indication = null) {
                HapticEngine.tick(ctx)
                onClick()
            }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlassIconBox(icon = icon, theme = theme)
        Spacer(Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = theme.textPrimary, modifier = Modifier.weight(1f))
        Text("›", fontSize = 22.sp, color = theme.accent.copy(alpha = 0.4f), fontWeight = FontWeight.Light)
    }
}

@Composable
private fun GlassIconBox(icon: String, theme: HydraTheme) {
    val isLight = theme.isLight
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        theme.accent.copy(alpha = if (isLight) 0.18f else 0.24f),
                        theme.accent.copy(alpha = if (isLight) 0.08f else 0.12f)
                    )
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = if (isLight) 0.4f else 0.2f),
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(icon, fontSize = 18.sp)
    }
}

@Composable
private fun MenuGroupHeader(label: String, theme: HydraTheme) {
    Text(
        label.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp, fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
        color = theme.accent.copy(alpha = 0.6f),
        modifier = Modifier.padding(start = 14.dp)
    )
}

@Composable
private fun ProfileSettingsSection(viewModel: HydraViewModel, theme: HydraTheme) {
    val ctx = LocalContext.current
    var loaded by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showBirthdayPicker by remember { mutableStateOf(false) }
    var showHeightPicker by remember { mutableStateOf(false) }
    var showWeightPicker by remember { mutableStateOf(false) }
    var showActivitySheet by remember { mutableStateOf(false) }
    var showClimateSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { loaded = true }

    val inf = rememberInfiniteTransition(label = "hydra_anim")
    val floatY by inf.animateFloat(0f, -8f, infiniteRepeatable(tween(3000, easing = EaseInOutSine), RepeatMode.Reverse), label = "fy")
    val breathScale by inf.animateFloat(1f, 1.015f, infiniteRepeatable(tween(3800, easing = EaseInOutSine), RepeatMode.Reverse), label = "breath")
    val glowAlpha by inf.animateFloat(0.08f, 0.22f, infiniteRepeatable(tween(3400, easing = EaseInOutSine), RepeatMode.Reverse), label = "glow")

    val emotionIntensity = (viewModel.waterPercent / 100f).coerceIn(0f, 1f)
    val animatedIntensity by animateFloatAsState(emotionIntensity, tween(1200, easing = EaseInOutSine), label = "ei")
    var tapped by remember { mutableStateOf(false) }
    val tapScale by animateFloatAsState(if (tapped) 1.08f else 1f, spring(0.5f, 400f), label = "tapScale")

    LaunchedEffect(tapped) { if (tapped) { kotlinx.coroutines.delay(200); tapped = false } }

    if (showBirthdayPicker) BirthdayPickerSheet(current = viewModel.profile.birthDate, theme = theme,
        onConfirm = { viewModel.profile = viewModel.profile.copy(birthDate = it); showBirthdayPicker = false },
        onDismiss = { showBirthdayPicker = false })
    if (showHeightPicker) WheelPickerSheet(title = "Height", emoji = "📏", value = viewModel.profile.heightCm,
        min = 100, max = 250, unit = "cm", theme = theme,
        onConfirm = { viewModel.profile = viewModel.profile.copy(heightCm = it); showHeightPicker = false },
        onDismiss = { showHeightPicker = false })
    if (showWeightPicker) WheelPickerSheet(title = "Weight", emoji = "⚖️", value = viewModel.profile.weightKg.toInt(),
        min = 30, max = 250, unit = "kg", theme = theme,
        onConfirm = { viewModel.profile = viewModel.profile.copy(weightKg = it.toFloat()); showWeightPicker = false },
        onDismiss = { showWeightPicker = false })
    if (showActivitySheet) ActivitySelectorSheet(current = viewModel.profile.activityLevel, theme = theme,
        onSelect = { viewModel.profile = viewModel.profile.copy(activityLevel = it); showActivitySheet = false },
        onDismiss = { showActivitySheet = false })
    if (showClimateSheet) ClimateSelectorSheet(current = viewModel.profile.climate, theme = theme,
        onSelect = { viewModel.profile = viewModel.profile.copy(climate = it); showClimateSheet = false },
        onDismiss = { showClimateSheet = false })
    if (showResetDialog) ResetHydraDialog(
        theme = theme,
        onConfirm = {
            showResetDialog = false
            HapticEngine.click(ctx)
            ctx.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit().putBoolean("has_seen_onboarding", false).apply()
            viewModel.resetAllData(ctx)
        },
        onDismiss = { showResetDialog = false }
    )

    val calculatedAge = remember(viewModel.profile.birthDate) { calculateAgeFromBirthDate(viewModel.profile.birthDate) }

    Column {
        AnimatedVisibility(loaded, enter = fadeIn(tween(600)) + scaleIn(tween(600), 0.92f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 20.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(180.dp).drawBehind {
                        drawCircle(
                            Brush.radialGradient(
                                listOf(theme.accent.copy(alpha = glowAlpha * (animatedIntensity * 0.5f + 0.5f)), Color.Transparent),
                                radius = size.minDimension * 0.45f
                            )
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier.size(110.dp)
                            .graphicsLayer { translationY = floatY; scaleX = breathScale * tapScale; scaleY = breathScale * tapScale }
                            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                                HapticEngine.tick(ctx)
                                tapped = true
                            }
                    ) {
                        HydraHeartCanvas(
                            fillPercent = viewModel.waterPercent,
                            mood = viewModel.hydraMood,
                            theme = theme,
                            showFace = viewModel.showFace,
                            hydraShape = viewModel.hydraShape,
                            drinkColours = viewModel.drinkColourPortions,
                            level = viewModel.levelInfo.level,
                            showRainbowShimmer = viewModel.showRainbowShimmer,
                            showButterfly = viewModel.showButterfly,
                            showGoldenGlow = viewModel.showGoldenGlow,
                            showNecklace = viewModel.showNecklace,
                            size = 110.dp
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("Your Profile", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = theme.accent)
                Text("Hydra uses your profile for smarter targets", style = MaterialTheme.typography.bodySmall, color = theme.textSoft)
            }
        }

        ProfileSectionCard(theme = theme, label = "IDENTITY", delay = 60, loaded = loaded, isProminent = true) {
            OutlinedTextField(value = viewModel.profile.name, onValueChange = { viewModel.profile = viewModel.profile.copy(name = it) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true,
                placeholder = { Text("Your name", color = theme.textMuted) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = theme.accent.copy(alpha = 0.35f),
                    unfocusedBorderColor = theme.accent.copy(alpha = 0.12f),
                    focusedContainerColor = theme.accent.copy(alpha = 0.08f),
                    unfocusedContainerColor = theme.accent.copy(alpha = 0.04f),
                    cursorColor = theme.accent,
                    focusedTextColor = theme.textPrimary,
                    unfocusedTextColor = theme.textPrimary
                )
            )
            Spacer(Modifier.height(12.dp))
            InfoPill(icon = "🎂", label = "Birthday", value = viewModel.profile.birthDate.ifBlank { "Choose birthday" }, theme = theme, onClick = { showBirthdayPicker = true })
            Spacer(Modifier.height(10.dp))
            InfoPill(icon = "🎀", label = "Age", value = if (calculatedAge != null) "$calculatedAge years old" else "Set birthday above", theme = theme, isReadOnly = true, onClick = {})
            Spacer(Modifier.height(16.dp))
            SectionLabel("Gender", theme); Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Gender.entries.forEach { g ->
                    GenderPill(text = g.label, selected = viewModel.profile.gender == g, theme = theme, modifier = Modifier.weight(1f)) { viewModel.profile = viewModel.profile.copy(gender = g) }
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        ProfileSectionCard(theme = theme, label = "BODY", delay = 120, loaded = loaded) {
            InfoPill("📏", "Height", "${viewModel.profile.heightCm} cm", theme) { showHeightPicker = true }
            Spacer(Modifier.height(10.dp))
            InfoPill("⚖️", "Weight", "${viewModel.profile.weightKg.toInt()} kg", theme) { showWeightPicker = true }
        }
        Spacer(Modifier.height(12.dp))

        ProfileSectionCard(theme = theme, label = "LIFESTYLE", delay = 180, loaded = loaded) {
            SummaryPill(viewModel.profile.activityLevel.icon, "Activity Level", viewModel.profile.activityLevel.label,
                when (viewModel.profile.activityLevel) {
                    ActivityLevel.SEDENTARY -> "Minimal movement"
                    ActivityLevel.LIGHTLY_ACTIVE -> "Light daily movement"
                    ActivityLevel.MODERATE -> "Regular movement, active days"
                    ActivityLevel.VERY_ACTIVE -> "Intense training, high output"
                }, theme) { showActivitySheet = true }
            Spacer(Modifier.height(10.dp))
            SummaryPill(viewModel.profile.climate.icon, "Climate", viewModel.profile.climate.label,
                when (viewModel.profile.climate) {
                    Climate.COLD -> "Cool / Cold weather"
                    Climate.MODERATE -> "Comfortable conditions"
                    Climate.HOT -> "Warm / Tropical"
                }, theme) { showClimateSheet = true }
        }
        Spacer(Modifier.height(24.dp))

        AnimatedVisibility(loaded, enter = fadeIn(tween(400, 260)) + slideInVertically(tween(400, 260)) { 10 }) {
            AddictiveButton(
                text = "💕  Recalculate with Hydra",
                onClick = {
                    HapticEngine.click(ctx)
                    ctx.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit().putBoolean("has_seen_onboarding", false).apply()
                    viewModel.onboardingComplete = false
                },
                modifier = Modifier.fillMaxWidth(),
                theme = theme,
                colour = theme.accent,
                isPrimary = true
            )
        }

        Spacer(Modifier.height(32.dp))
        Box(Modifier.fillMaxWidth().height(0.5.dp).background(theme.accent.copy(alpha = 0.07f)))
        Spacer(Modifier.height(24.dp))

        val roseRed = Color(0xFFC0525E)
        AnimatedVisibility(loaded, enter = fadeIn(tween(400, 320)) + slideInVertically(tween(400, 320)) { 8 }) {
            FrostedGlass(modifier = Modifier.fillMaxWidth(), theme = theme, padding = PaddingValues(16.dp)) {
                Text(
                    "DATA & RESET",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
                    color = roseRed.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    "Permanently erase all of Hydra's progress and memories — HP, streaks, history, unlocked personalities, and all customisations. The app will return to a fresh install as if opened for the very first time.",
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.textSoft,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                AddictiveButton(
                    text = "Reset Hydra",
                    onClick = { showResetDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    theme = theme,
                    colour = roseRed,
                    isPrimary = false
                )
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun ProfileSectionCard(theme: HydraTheme, label: String, delay: Int, loaded: Boolean, isProminent: Boolean = false, content: @Composable ColumnScope.() -> Unit) {
    AnimatedVisibility(loaded, enter = fadeIn(tween(400, delay)) + slideInVertically(tween(400, delay)) { 8 }) {
        FrostedGlass(modifier = Modifier.fillMaxWidth(), theme = theme, padding = PaddingValues(16.dp), isProminent = isProminent) {
            Text(label, style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp), color = theme.accent.copy(alpha = 0.6f), fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 12.dp))
            content()
        }
    }
}

@Composable
private fun InfoPill(icon: String, label: String, value: String, theme: HydraTheme, isReadOnly: Boolean = false, onClick: () -> Unit) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed && !isReadOnly) 0.98f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "pillScale")

    Row(modifier = Modifier.fillMaxWidth().graphicsLayer { scaleX = scale; scaleY = scale }
        .clip(RoundedCornerShape(16.dp))
        .background(theme.accent.copy(alpha = if (theme.isLight) 0.06f else 0.1f))
        .border(0.5.dp, theme.accent.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
        .clickable(interactionSource = source, indication = null, enabled = !isReadOnly, onClick = onClick)
        .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 20.sp); Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = theme.accent.copy(alpha = 0.7f)); Spacer(Modifier.height(1.dp))
            Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isReadOnly) FontWeight.Normal else FontWeight.Medium), color = if (isReadOnly) theme.textSoft else theme.textPrimary)
        }
        if (!isReadOnly) Text("›", fontSize = 18.sp, color = theme.accent.copy(0.35f))
    }
}

@Composable
private fun SummaryPill(icon: String, label: String, value: String, desc: String, theme: HydraTheme, onClick: () -> Unit) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "spScale")

    Row(modifier = Modifier.fillMaxWidth().graphicsLayer { scaleX = scale; scaleY = scale }
        .clip(RoundedCornerShape(16.dp))
        .background(theme.accent.copy(alpha = if (theme.isLight) 0.06f else 0.1f))
        .border(0.5.dp, theme.accent.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
        .clickable(interactionSource = source, indication = null, onClick = onClick)
        .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 22.sp); Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = theme.accent.copy(alpha = 0.7f)); Spacer(Modifier.height(1.dp))
            Text(value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal), color = theme.textPrimary)
            Text(desc, style = MaterialTheme.typography.bodySmall, color = theme.textSoft, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text("›", fontSize = 18.sp, color = theme.accent.copy(0.35f))
    }
}

@Composable
private fun GenderPill(text: String, selected: Boolean, theme: HydraTheme, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val ctx = LocalContext.current
    val interSource = remember { MutableInteractionSource() }
    val pressed by interSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "gpScale")
    val bgAlpha by animateFloatAsState(if (selected) 0.18f else 0.05f, tween(150))

    Box(modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale }
        .clip(RoundedCornerShape(14.dp))
        .background(theme.accent.copy(alpha = bgAlpha))
        .border(0.5.dp, theme.accent.copy(alpha = if (selected) 0.35f else 0.1f), RoundedCornerShape(14.dp))
        .clickable(interactionSource = interSource, indication = null) { HapticEngine.tick(ctx); onClick() }
        .padding(horizontal = 4.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium),
            color = if (selected) theme.accent else theme.textSoft,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

private fun previewLines(p: Personality, name: String): List<String> {
    val n = name.ifBlank { "lovely" }
    return when (p) {
        Personality.BESTIE -> listOf("I am SO here for you, $n! 💕", "You literally make this app worth it 🤍", "We're doing this TOGETHER — it's going to be amazing! ✨", "I believe in you more than you believe in yourself 💕", "Hydration day with you is my absolute favourite 💦")
        Personality.SLAY_QUEEN -> listOf("Hydration? We're making it a whole moment, darling. 👑", "You are THAT girl, $n. Act accordingly. 💅", "Being iconic requires being hydrated. Non-negotiable. ✨", "The glow doesn't lie. Drink up. 👑", "Main character energy requires main character hydration. 💅")
        Personality.SOFT_GIRL -> listOf("I'm so happy we get to take care of you together 🌸", "Every sip is an act of love for yourself, $n 🥹", "You deserve to feel your very best, always 🌷", "I'm just really proud of you for being here 🌸", "Taking care of you is the most important thing we do 💕")
        Personality.VILLAIN_ERA -> listOf("Fine. I'll help you hydrate. Don't make it weird. 😈", "You need water. I need you to stop arguing. Deal? 💢", "Helping you is my villain origin story. But here we are. 👀", "I only care about your hydration. Don't read into it. 😤", "Every time you drink I become slightly less chaotic. 😈")
        Personality.ZEN_MASTER -> listOf("Water is the beginning of all balance. Let us begin 🌊", "In stillness, we find thirst. In water, we find peace 🌿", "Each sip is a return to yourself, $n 🧘‍♀️", "The body that receives water receives life 🌙", "Breathe. Drink. Return to the present moment 🌊")
        Personality.COACH -> listOf("Hydration protocol active. Let's lock in, $n. 💪", "Every elite performer hydrates. You're no different. 🎯", "Your body is an asset. Treat it like one. 💪", "Consistency beats motivation. Show up for hydration. 📊", "Champions don't wait to feel like it. They execute. 🎯")
        Personality.HEARTBREAK_ERA -> listOf("I'm just glad you're here, $n 💔", "You don't have to be okay. I'll be here either way 🤍", "Every small step counts. I see every single one 🥀", "Whatever today holds — I'm with you, softly 💔", "You are so worth taking care of, $n 🌹")
        Personality.COLD_CEO -> listOf("Standards exist for a reason. Meet them. 🖤", "Discipline is a choice you make before motivation arrives.", "You've earned this mode. Now execute it. 🖤", "Consistency isn't a feeling — it's a decision. Make it.", "No excuses. No shortcuts. Just results. 🖤")
    }
}

@Composable
private fun PersonalitySettingsSection(viewModel: HydraViewModel, theme: HydraTheme) {
    var preview by remember { mutableStateOf(viewModel.profile.personality) }
    var lineIdx by remember { mutableStateOf(0) }
    var pulseKey by remember { mutableStateOf(0) }
    val hasChange = preview != viewModel.profile.personality

    val pulseScale = remember { Animatable(1f) }
    LaunchedEffect(pulseKey) {
        if (pulseKey > 0) {
            pulseScale.animateTo(1.08f, tween(110, easing = EaseOutSine))
            pulseScale.animateTo(1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium))
        }
    }

    Text(
        "Who is Hydra to you?",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal),
        color = theme.accent
    )
    Text(
        "Choose how she speaks, encourages, and shows up for you",
        style = MaterialTheme.typography.bodySmall,
        color = theme.textSoft,
        modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
    )

    FrostedGlass(theme = theme, padding = PaddingValues(16.dp, 14.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(88.dp).graphicsLayer { scaleX = pulseScale.value; scaleY = pulseScale.value }) {
                HydraHeartCanvas(
                    fillPercent = viewModel.waterPercent,
                    mood = viewModel.hydraMood,
                    theme = theme,
                    showFace = viewModel.showFace,
                    hydraShape = viewModel.hydraShape,
                    drinkColours = viewModel.drinkColourPortions,
                    level = viewModel.levelInfo.level,
                    showRainbowShimmer = viewModel.showRainbowShimmer,
                    showButterfly = viewModel.showButterfly,
                    showGoldenGlow = viewModel.showGoldenGlow,
                    showNecklace = viewModel.showNecklace,
                    size = 88.dp
                )
            }
            Spacer(Modifier.width(14.dp))
            AnimatedContent(
                targetState = preview to lineIdx,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    (fadeIn(tween(260)) + slideInVertically(tween(260)) { -it / 3 }) togetherWith
                        (fadeOut(tween(140)) + slideOutVertically(tween(140)) { it / 3 })
                },
                label = "hydra_speech"
            ) { (p, idx) ->
                val lines = previewLines(p, viewModel.profile.name)
                Text(
                    lines[idx % lines.size],
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    color = theme.textPrimary
                )
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    val hiddenPersonalities = setOf(Personality.HEARTBREAK_ERA, Personality.COLD_CEO)
    val visiblePersonalities = Personality.entries.filter { it !in hiddenPersonalities || it in viewModel.unlockedPersonalities }

    visiblePersonalities.chunked(2).forEach { row ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        ) {
            row.forEach { p ->
                PersonalityCard(
                    personality = p,
                    selected = preview == p,
                    theme = theme,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (p == preview) {
                            lineIdx++
                        } else {
                            preview = p
                            lineIdx = 0
                            pulseKey++
                        }
                    }
                )
            }
            if (row.size == 1) Spacer(Modifier.weight(1f))
        }
    }

    val lockedCount = hiddenPersonalities.count { it !in viewModel.unlockedPersonalities }
    if (lockedCount > 0) {
        Spacer(Modifier.height(4.dp))
        FrostedGlass(theme = theme, padding = PaddingValues(14.dp, 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔒", fontSize = 18.sp)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        if (lockedCount == 2) "2 hidden personalities await" else "1 hidden personality awaits",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = theme.textSoft
                    )
                    Text(
                        "Hydra reveals them through your journey",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = theme.textSoft.copy(alpha = 0.6f)
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
    }

    AnimatedVisibility(
        visible = hasChange,
        enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 2 },
        exit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { it / 2 }
    ) {
        val inf = rememberInfiniteTransition(label = "btnGlow")
        val glow by inf.animateFloat(0.55f, 0.85f, infiniteRepeatable(tween(1400, easing = EaseInOutSine), RepeatMode.Reverse), label = "glowAlpha")
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .drawBehind {
                    drawRoundRect(theme.accent.copy(alpha = 0.22f * glow), cornerRadius = CornerRadius(18.dp.toPx()), size = Size(size.width, size.height))
                }
        ) {
            AddictiveButton(
                text = "Lock in this vibe ✨",
                onClick = { viewModel.profile = viewModel.profile.copy(personality = preview) },
                modifier = Modifier.fillMaxWidth(),
                theme = theme
            )
        }
    }
}

@Composable
private fun PersonalityCard(personality: Personality, selected: Boolean, theme: HydraTheme, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val ctx = LocalContext.current
    val interSource = remember { MutableInteractionSource() }
    val isPressed by interSource.collectIsPressedAsState()

    val pressScale by animateFloatAsState(if (isPressed) 0.95f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium), label = "ps")
    val selScale by animateFloatAsState(if (selected) 1.045f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "ss")
    val cardAlpha by animateFloatAsState(if (selected) 1f else 0.68f, tween(220), label = "ca")
    val bgAlpha by animateFloatAsState(if (selected) 0.15f else if (theme.isLight) 0.03f else 0.05f, tween(220), label = "ba")
    val borderAlpha by animateFloatAsState(if (selected) 0.42f else 0.09f, tween(220), label = "bra")
    val borderWidth = if (selected) 1.5.dp else 0.5.dp
    val iconScale by animateFloatAsState(if (selected) 1.22f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium), label = "is")

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = pressScale * selScale
                scaleY = pressScale * selScale
                alpha = cardAlpha
            }
            .drawBehind {
                if (selected) drawRoundRect(theme.accent.copy(alpha = 0.13f), cornerRadius = CornerRadius(18.dp.toPx()), size = Size(size.width, size.height))
            }
            .clip(RoundedCornerShape(18.dp))
            .background(theme.accent.copy(alpha = bgAlpha))
            .border(borderWidth, theme.accent.copy(alpha = borderAlpha), RoundedCornerShape(18.dp))
            .clickable(interactionSource = interSource, indication = null) {
                HapticEngine.tick(ctx)
                onClick()
            }
            .padding(14.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column {
            Text(personality.icon, fontSize = 28.sp, modifier = Modifier.graphicsLayer { scaleX = iconScale; scaleY = iconScale })
            Spacer(Modifier.height(8.dp))
            Text(
                personality.label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal),
                color = if (selected) theme.accent else theme.textPrimary
            )
            Spacer(Modifier.height(3.dp))
            Text(
                personality.desc,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = theme.textSoft,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private enum class AccessoryCategory(val label: String, val icon: String) {
    JEWELLERY("Jewellery", "💎"),
    HEAD_FACE("Head & Face", "👑"),
    EFFECTS("Visual Effects", "✨"),
    DECORATIVE("Decorative", "🌸")
}

private data class AccessoryItem(
    val id: String,
    val name: String,
    val emoji: String,
    val flavour: String,
    val category: AccessoryCategory,
    val unlockLevel: Int = 0,
    val unlockTrophy: String? = null
)

private val ALL_ACCESSORIES = listOf(
    AccessoryItem("necklace", "Gold Necklace", "💎", "A delicate chain with a golden rose", AccessoryCategory.JEWELLERY, 2),
    AccessoryItem("bracelet", "Crystal Bracelet", "📿", "Soft beads that catch the light", AccessoryCategory.JEWELLERY, 5),
    AccessoryItem("pearl_ring", "Pearl Ring", "💍", "Timeless elegance on every sip", AccessoryCategory.JEWELLERY, 8),
    AccessoryItem("crown", "Mini Crown", "👑", "Because you're royalty, obviously", AccessoryCategory.HEAD_FACE, 6),
    AccessoryItem("bow", "Silk Bow", "🎀", "Sweet and perfectly placed", AccessoryCategory.HEAD_FACE, 5),
    AccessoryItem("halo", "Golden Halo", "😇", "Earned through pure devotion", AccessoryCategory.HEAD_FACE, unlockTrophy = "week_streak"),
    AccessoryItem("sparkles", "Sparkles", "✨", "A sprinkle of magic around her", AccessoryCategory.EFFECTS, 3),
    AccessoryItem("aura", "Rose Aura", "🌸", "A warm glow that follows her", AccessoryCategory.EFFECTS, 7),
    AccessoryItem("stardust", "Stardust", "⭐", "Traces of light wherever she goes", AccessoryCategory.EFFECTS, 10),
    AccessoryItem("flower", "Tiny Flower", "🌷", "Pinned gently to her side", AccessoryCategory.DECORATIVE, 7),
    AccessoryItem("ribbon", "Satin Ribbon", "🪢", "Tied with care around her shape", AccessoryCategory.DECORATIVE, 4),
    AccessoryItem("rose", "Red Rose", "🌹", "For those who've truly blossomed", AccessoryCategory.DECORATIVE, 9)
)

private fun isAccessoryUnlocked(a: AccessoryItem, vm: HydraViewModel): Boolean =
    vm.levelInfo.level >= a.unlockLevel && (a.unlockTrophy == null || a.unlockTrophy in vm.earnedTrophyIds)

private fun isAccessoryEquipped(a: AccessoryItem, vm: HydraViewModel): Boolean =
    if (a.id == "necklace") vm.showNecklace else a.id in vm.equippedAccessoryIds

private fun toggleAccessory(a: AccessoryItem, vm: HydraViewModel) {
    if (a.id == "necklace") {
        vm.showNecklace = !vm.showNecklace
    } else if (a.id in vm.equippedAccessoryIds) {
        vm.equippedAccessoryIds.remove(a.id)
    } else {
        vm.equippedAccessoryIds.add(a.id)
    }
}

private fun unlockRequirement(a: AccessoryItem): String = when {
    a.unlockTrophy == "week_streak" -> "7-day streak"
    a.unlockTrophy != null -> "Trophy"
    else -> "Lvl ${a.unlockLevel}"
}

private data class BubbleStyleItem(val id: String, val label: String, val emoji: String, val unlockLevel: Int)

private val ALL_BUBBLE_STYLES = listOf(
    BubbleStyleItem("classic", "Classic", "🫧", 1),
    BubbleStyleItem("outlined", "Outlined", "◻️", 1),
    BubbleStyleItem("minimal", "Minimal", "🌫️", 2),
    BubbleStyleItem("solid", "Solid", "🟣", 4),
    BubbleStyleItem("gradient", "Gradient", "🌈", 4),
    BubbleStyleItem("dark", "Dark", "🌑", 5),
    BubbleStyleItem("glow", "Glow", "✨", 6),
    BubbleStyleItem("neon", "Neon", "💜", 8)
)

@Composable
private fun HydraLookSettingsSection(viewModel: HydraViewModel, theme: HydraTheme) {
    val ctx = LocalContext.current
    var newSpeechLine by remember { mutableStateOf("") }

    SectionHeader("Hydra's Look", "Personalise your companion", theme)

    FrostedGlass(
        theme = theme,
        padding = PaddingValues(0.dp),
        modifier = Modifier.fillMaxWidth(),
        isProminent = true
    ) {
        AnimatedVisibility(
            visible = viewModel.showFace && viewModel.showSpeechBubble,
            enter = fadeIn(tween(200)) + expandVertically(),
            exit = fadeOut(tween(160)) + shrinkVertically()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 12.dp)
            ) {
                HydraSpeechBubble(
                    text = viewModel.hydraSpeech.let { if (it.length > 72) it.take(69) + "…" else it },
                    styleId = viewModel.speechBubbleStyleId,
                    theme = theme
                )
            }
        }

        val canvasH = if (viewModel.showFace && viewModel.showSpeechBubble) 148.dp else 190.dp
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().height(canvasH)
        ) {
            Box(
                Modifier.size(140.dp).drawBehind {
                    drawCircle(Brush.radialGradient(listOf(theme.accent.copy(alpha = 0.15f), Color.Transparent), radius = size.minDimension * 0.5f))
                }
            )
            Box(modifier = Modifier.size(140.dp)) {
                HydraHeartCanvas(
                    fillPercent = viewModel.waterPercent,
                    mood = viewModel.hydraMood,
                    theme = theme,
                    showFace = viewModel.showFace,
                    hydraShape = viewModel.hydraShape,
                    drinkColours = viewModel.drinkColourPortions,
                    level = viewModel.levelInfo.level,
                    showRainbowShimmer = viewModel.showRainbowShimmer,
                    showButterfly = viewModel.showButterfly,
                    showGoldenGlow = viewModel.showGoldenGlow,
                    showNecklace = viewModel.showNecklace,
                    equippedAccessories = viewModel.equippedAccessoryIds.toSet(),
                    size = 140.dp
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Text(
                "LIVE PREVIEW",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
                color = theme.textMuted
            )
        }
    }

    Spacer(Modifier.height(24.dp))
    SectionLabel("Display", theme)
    Spacer(Modifier.height(8.dp))
    FrostedGlass(theme = theme, padding = PaddingValues(0.dp)) {
        LookToggleRow(
            label = "Show Hydra's Face",
            microcopy = "Lets Hydra express emotions and mood",
            checked = viewModel.showFace,
            enabled = true,
            theme = theme,
            onToggle = {
                viewModel.showFace = it
                if (!it) viewModel.showSpeechBubble = false
            }
        )
        Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(0.5.dp).background(theme.accent.copy(alpha = 0.06f)))
        LookToggleRow(
            label = "Enable Speech",
            microcopy = if (viewModel.showFace) "Hydra shares thoughts when she can see you" else "Turn on Hydra's face to enable speech",
            checked = viewModel.showSpeechBubble,
            enabled = viewModel.showFace,
            theme = theme,
            onToggle = { viewModel.showSpeechBubble = it }
        )
    }

    Spacer(Modifier.height(24.dp))
    SectionLabel("Level Rewards", theme)
    Spacer(Modifier.height(8.dp))
    val level = viewModel.levelInfo.level
    FrostedGlass(theme = theme, padding = PaddingValues(0.dp)) {
        LookToggleRow(
            label = "Rainbow Shimmer",
            microcopy = if (level >= 8) "A rainbow sweeps across her glass every few seconds" else "Unlocks at Level 8",
            checked = viewModel.showRainbowShimmer,
            enabled = level >= 8,
            theme = theme,
            onToggle = { viewModel.showRainbowShimmer = it }
        )
        Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(0.5.dp).background(theme.accent.copy(alpha = 0.06f)))
        LookToggleRow(
            label = "Butterfly",
            microcopy = if (level >= 9) "A little butterfly keeps her company" else "Unlocks at Level 9",
            checked = viewModel.showButterfly,
            enabled = level >= 9,
            theme = theme,
            onToggle = { viewModel.showButterfly = it }
        )
        Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(0.5.dp).background(theme.accent.copy(alpha = 0.06f)))
        LookToggleRow(
            label = "Golden Glow",
            microcopy = if (level >= 10) "Her legendary golden glow and rim" else "Unlocks at Level 10",
            checked = viewModel.showGoldenGlow,
            enabled = level >= 10,
            theme = theme,
            onToggle = { viewModel.showGoldenGlow = it }
        )
    }

    Spacer(Modifier.height(24.dp))
    SectionLabel("Shape", theme)
    Spacer(Modifier.height(10.dp))
    val shapes = listOf(
        "heart" to ("💕" to "Heart"),
        "droplet" to ("💧" to "Droplet"),
        "bottle" to ("🍶" to "Bottle"),
        "cup" to ("☕" to "Cup"),
        "body_f" to ("👗" to "Woman"),
        "bra" to ("👙" to "Bra")
    )
    shapes.chunked(3).forEach { row ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            row.forEach { (id, pair) ->
                EnhancedShapeTile(
                    emoji = pair.first,
                    label = pair.second,
                    selected = viewModel.hydraShape == id,
                    theme = theme,
                    modifier = Modifier.weight(1f)
                ) {
                    HapticEngine.tick(ctx)
                    viewModel.hydraShape = id
                }
            }
            repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
        }
    }

    Spacer(Modifier.height(24.dp))
    SectionLabel("Accessories", theme)
    Spacer(Modifier.height(4.dp))
    Text(
        "Collect items as you grow with Hydra",
        style = MaterialTheme.typography.bodySmall,
        color = theme.textSoft,
        modifier = Modifier.padding(bottom = 14.dp)
    )
    AccessoryCategory.entries.forEach { category ->
        AccessoryCategorySection(category, ALL_ACCESSORIES.filter { it.category == category }, viewModel, theme, ctx)
        Spacer(Modifier.height(18.dp))
    }

    SectionLabel("Speech Bubbles", theme)
    Spacer(Modifier.height(4.dp))
    Text(
        "Pick a bubble design for Hydra's speech",
        style = MaterialTheme.typography.bodySmall,
        color = theme.textSoft,
        modifier = Modifier.padding(bottom = 14.dp)
    )
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ALL_BUBBLE_STYLES.forEach { style ->
            val unlocked = viewModel.levelInfo.level >= style.unlockLevel
            BubbleStyleTile(
                style = style,
                selected = viewModel.speechBubbleStyleId == style.id,
                unlocked = unlocked,
                theme = theme
            ) {
                if (unlocked) {
                    HapticEngine.tick(ctx)
                    viewModel.speechBubbleStyleId = style.id
                }
            }
        }
    }

    Spacer(Modifier.height(20.dp))
    Text(
        "CUSTOM MESSAGES",
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
        color = theme.accent.copy(alpha = 0.45f),
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
        "Write your own messages for Hydra to say",
        style = MaterialTheme.typography.bodySmall,
        color = theme.textSoft,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    FrostedGlass(theme = theme, padding = PaddingValues(0.dp)) {
        LookToggleRow(
            label = "Use Custom Messages",
            microcopy = "Your words replace Hydra's default speech",
            checked = viewModel.customSpeechEnabled,
            enabled = true,
            theme = theme,
            onToggle = { viewModel.customSpeechEnabled = it }
        )
    }

    if (viewModel.customSpeechLines.isNotEmpty()) {
        Spacer(Modifier.height(10.dp))
        FrostedGlass(theme = theme, padding = PaddingValues(0.dp)) {
            viewModel.customSpeechLines.forEachIndexed { i, line ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text("💬", fontSize = 14.sp)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        line,
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.textPrimary,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(onClick = { viewModel.customSpeechLines.removeAt(i) }) {
                        Text("✕", fontSize = 14.sp, color = theme.textMuted)
                    }
                }
                if (i < viewModel.customSpeechLines.lastIndex) {
                    Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(0.5.dp).background(theme.accent.copy(alpha = 0.06f)))
                }
            }
        }
    }

    Spacer(Modifier.height(10.dp))
    OutlinedTextField(
        value = newSpeechLine,
        onValueChange = { newSpeechLine = it },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = theme.accent.copy(alpha = 0.25f),
            unfocusedBorderColor = theme.accent.copy(alpha = 0.1f),
            focusedTextColor = theme.textPrimary,
            unfocusedTextColor = theme.textPrimary,
            cursorColor = theme.accent
        ),
        placeholder = { Text("Add a message for Hydra…", color = theme.textMuted, fontSize = 14.sp) },
        trailingIcon = {
            IconButton(onClick = {
                if (newSpeechLine.isNotBlank()) {
                    HapticEngine.tick(ctx)
                    viewModel.customSpeechLines.add(newSpeechLine.trim())
                    newSpeechLine = ""
                }
            }) {
                Text("➕", color = theme.accent, fontSize = 16.sp)
            }
        },
        singleLine = true
    )
    Spacer(Modifier.height(4.dp))
}

@Composable
private fun LookToggleRow(label: String, microcopy: String, checked: Boolean, enabled: Boolean, theme: HydraTheme, onToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = theme.textPrimary)
            Text(microcopy, style = MaterialTheme.typography.bodySmall, color = theme.textSoft)
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = theme.accent,
                uncheckedThumbColor = theme.textMuted,
                uncheckedTrackColor = theme.accent.copy(alpha = 0.12f)
            )
        )
    }
}

@Composable
private fun EnhancedShapeTile(emoji: String, label: String, selected: Boolean, theme: HydraTheme, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "shapeScale")
    val glowAlpha by animateFloatAsState(if (selected) 0.28f else 0f, tween(200), label = "shapeGlow")
    val bgAlpha by animateFloatAsState(if (selected) 0.15f else 0.05f, tween(150), label = "shapeBg")

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(16.dp))
            .drawBehind {
                if (glowAlpha > 0f) {
                    drawRoundRect(
                        Brush.radialGradient(
                            listOf(theme.accent.copy(alpha = glowAlpha), Color.Transparent),
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = size.minDimension * 0.9f
                        ),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )
                }
            }
            .background(theme.accent.copy(alpha = bgAlpha))
            .border(if (selected) 1.5.dp else 0.5.dp, theme.accent.copy(alpha = if (selected) 0.5f else 0.1f), RoundedCornerShape(16.dp))
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 22.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                color = if (selected) theme.accent else theme.textSoft,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun BubbleStyleTile(style: BubbleStyleItem, selected: Boolean, unlocked: Boolean, theme: HydraTheme, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed && unlocked) 0.94f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "bsScale")
    val bgAlpha by animateFloatAsState(if (selected && unlocked) 0.15f else 0.05f, tween(150), label = "bsBg")

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp, 84.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(14.dp))
            .background(theme.accent.copy(alpha = bgAlpha))
            .border(
                if (selected && unlocked) 1.5.dp else 0.5.dp,
                theme.accent.copy(alpha = if (selected && unlocked) 0.5f else if (!unlocked) 0.07f else 0.12f),
                RoundedCornerShape(14.dp)
            )
            .alpha(if (!unlocked) 0.45f else 1f)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Text(style.emoji, fontSize = 20.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                style.label,
                color = if (selected && unlocked) theme.accent else theme.textSoft,
                fontWeight = if (selected && unlocked) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(2.dp))
            Text(
                if (!unlocked) "Lvl ${style.unlockLevel}" else "",
                color = theme.textMuted,
                fontSize = 9.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AccessoryCategorySection(category: AccessoryCategory, items: List<AccessoryItem>, viewModel: HydraViewModel, theme: HydraTheme, ctx: Context) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
        Text(category.icon, fontSize = 13.sp)
        Spacer(Modifier.width(6.dp))
        Text(
            category.label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.0.sp),
            color = theme.accent.copy(alpha = 0.7f),
            fontWeight = FontWeight.SemiBold
        )
    }
    items.chunked(3).forEach { row ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            row.forEach { accessory ->
                val unlocked = isAccessoryUnlocked(accessory, viewModel)
                AccessoryTile(
                    accessory = accessory,
                    unlocked = unlocked,
                    equipped = isAccessoryEquipped(accessory, viewModel),
                    theme = theme,
                    modifier = Modifier.weight(1f)
                ) {
                    if (unlocked) {
                        HapticEngine.tick(ctx)
                        toggleAccessory(accessory, viewModel)
                    }
                }
            }
            repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
        }
    }
}

@Composable
private fun AccessoryTile(accessory: AccessoryItem, unlocked: Boolean, equipped: Boolean, theme: HydraTheme, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed && unlocked) 0.94f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "accScale")
    val glowAlpha by animateFloatAsState(if (equipped && unlocked) 0.26f else 0f, tween(200), label = "accGlow")
    val bgAlpha by animateFloatAsState(if (equipped && unlocked) 0.15f else if (!unlocked) 0.04f else 0.06f, tween(150), label = "accBg")
    val borderAlpha by animateFloatAsState(if (equipped && unlocked) 0.5f else 0.08f, tween(150), label = "accBorder")

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(16.dp))
            .drawBehind {
                if (glowAlpha > 0f) {
                    drawRoundRect(
                        Brush.radialGradient(
                            listOf(theme.accent.copy(alpha = glowAlpha), Color.Transparent),
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = size.minDimension * 0.9f
                        ),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )
                }
            }
            .background(theme.accent.copy(alpha = bgAlpha))
            .border(if (equipped && unlocked) 1.5.dp else 0.5.dp, theme.accent.copy(alpha = borderAlpha), RoundedCornerShape(16.dp))
            .alpha(if (unlocked) 1f else 0.55f)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.TopEnd) {
                Text(if (unlocked) accessory.emoji else "🔒", fontSize = 22.sp)
                if (equipped && unlocked) {
                    Box(
                        modifier = Modifier.offset(x = 4.dp, y = (-4).dp).size(14.dp).background(theme.accent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✓", fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(5.dp))
            Text(
                accessory.name,
                color = if (equipped && unlocked) theme.accent else theme.textSoft,
                fontWeight = if (equipped && unlocked) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                if (!unlocked) unlockRequirement(accessory) else if (equipped) "Equipped" else "Tap to equip",
                color = if (equipped && unlocked) theme.accent.copy(alpha = 0.7f) else theme.textMuted,
                fontSize = 9.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun CalendarSettingsSection(viewModel: HydraViewModel, theme: HydraTheme) {
    val ctx = LocalContext.current
    var showResetPicker by remember { mutableStateOf(false) }

    if (showResetPicker) WheelPickerSheet(title = "Day resets at", emoji = "🌙", value = viewModel.calDayResetHour,
        min = 0, max = 23, unit = "h", theme = theme,
        onConfirm = { viewModel.calDayResetHour = it; showResetPicker = false },
        onDismiss = { showResetPicker = false })

    SectionHeader("Date & Time", "Customise how time appears throughout the app", theme)
    Spacer(Modifier.height(10.dp))

    val previewParts = buildList {
        if (viewModel.calShowDayName) add("Wed")
        add(if (viewModel.calDayFirst) "29 Mar" else "Mar 29")
        if (viewModel.calShowTime) add(if (viewModel.cal12Hour) "2:45 PM" else "14:45")
    }
    val previewText = previewParts.joinToString(" · ")

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        AnimatedContent(
            targetState = previewText,
            transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(180)) },
            label = "calPreview"
        ) { text ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(22.dp),
                        ambientColor = theme.accent.copy(alpha = 0.1f),
                        spotColor = theme.accent.copy(alpha = 0.08f)
                    )
                    .clip(RoundedCornerShape(22.dp))
                    .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.07f), theme.accent.copy(alpha = 0.04f))))
                    .border(
                        0.5.dp,
                        Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.28f), theme.accent.copy(alpha = 0.07f))),
                        RoundedCornerShape(22.dp)
                    )
                    .padding(horizontal = 28.dp, vertical = 14.dp)
            ) {
                Text(
                    text,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                    color = theme.textPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    Spacer(Modifier.height(30.dp))
    SectionLabel("Date Order", theme)
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        DateFormatCard(example = "29 Mar", label = "Day First", selected = viewModel.calDayFirst, theme = theme, modifier = Modifier.weight(1f)) {
            HapticEngine.tick(ctx); viewModel.calDayFirst = true
        }
        DateFormatCard(example = "Mar 29", label = "Month First", selected = !viewModel.calDayFirst, theme = theme, modifier = Modifier.weight(1f)) {
            HapticEngine.tick(ctx); viewModel.calDayFirst = false
        }
    }

    Spacer(Modifier.height(26.dp))
    SectionLabel("Time Format", theme)
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        DateFormatCard(example = "2:45 PM", label = "12-Hour", selected = viewModel.cal12Hour, theme = theme, modifier = Modifier.weight(1f)) {
            HapticEngine.tick(ctx); viewModel.cal12Hour = true
        }
        DateFormatCard(example = "14:45", label = "24-Hour", selected = !viewModel.cal12Hour, theme = theme, modifier = Modifier.weight(1f)) {
            HapticEngine.tick(ctx); viewModel.cal12Hour = false
        }
    }

    Spacer(Modifier.height(30.dp))
    SectionLabel("Display Details", theme)
    Spacer(Modifier.height(2.dp))
    CalBareToggleRow("Day Name", "Mon, Tue, Wed beside the date", viewModel.calShowDayName, theme) { viewModel.calShowDayName = it }
    Box(Modifier.fillMaxWidth().height(0.5.dp).background(theme.accent.copy(alpha = 0.06f)))
    CalBareToggleRow("Time", "Time shown in history entries", viewModel.calShowTime, theme) { viewModel.calShowTime = it }

    Spacer(Modifier.height(30.dp))
    SectionLabel("Week & Tracking", theme)
    Spacer(Modifier.height(2.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text("Week Starts On", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = theme.textPrimary)
            Text("Used in weekly summaries", style = MaterialTheme.typography.bodySmall, color = theme.textSoft)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            SelectionSegment("Mon", viewModel.calWeekStartMonday, theme, Modifier.width(52.dp)) {
                HapticEngine.tick(ctx); viewModel.calWeekStartMonday = true
            }
            SelectionSegment("Sun", !viewModel.calWeekStartMonday, theme, Modifier.width(52.dp)) {
                HapticEngine.tick(ctx); viewModel.calWeekStartMonday = false
            }
        }
    }
    Box(Modifier.fillMaxWidth().height(0.5.dp).background(theme.accent.copy(alpha = 0.06f)))

    val resetLabel = if (viewModel.cal12Hour) {
        val h = viewModel.calDayResetHour
        when {
            h == 0 -> "12:00 AM"
            h < 12 -> "$h:00 AM"
            h == 12 -> "12:00 PM"
            else -> "${h - 12}:00 PM"
        }
    } else {
        "%02d:00".format(viewModel.calDayResetHour)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                HapticEngine.tick(ctx)
                showResetPicker = true
            }
            .padding(vertical = 10.dp)
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text("Day Resets At", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = theme.textPrimary)
            Text("When your daily tracking starts fresh", style = MaterialTheme.typography.bodySmall, color = theme.textSoft)
        }
        Text(resetLabel, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = theme.accent)
        Spacer(Modifier.width(4.dp))
        Text("›", fontSize = 18.sp, color = theme.accent.copy(alpha = 0.4f))
    }
}

@Composable
private fun CalBareToggleRow(label: String, microcopy: String, checked: Boolean, theme: HydraTheme, onToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = theme.textPrimary)
            Text(microcopy, style = MaterialTheme.typography.bodySmall, color = theme.textSoft)
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = theme.accent,
                uncheckedThumbColor = theme.textMuted,
                uncheckedTrackColor = theme.accent.copy(alpha = 0.12f)
            )
        )
    }
}

@Composable
private fun DateFormatCard(example: String, label: String, selected: Boolean, theme: HydraTheme, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "dfcScale")
    val glowAlpha by animateFloatAsState(if (selected) 0.14f else 0f, tween(220), label = "dfcGlow")
    val bgAlpha by animateFloatAsState(if (selected) 0.09f else 0.02f, tween(220), label = "dfcBg")
    val borderAlpha by animateFloatAsState(if (selected) 0.38f else 0.07f, tween(220), label = "dfcBorder")
    val shadowElev by animateDpAsState(if (selected) 6.dp else 0.dp, tween(220), label = "dfcShadow")

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(88.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(
                elevation = shadowElev,
                shape = RoundedCornerShape(18.dp),
                ambientColor = theme.accent.copy(alpha = 0.18f),
                spotColor = theme.accent.copy(alpha = 0.14f)
            )
            .clip(RoundedCornerShape(18.dp))
            .drawBehind {
                if (glowAlpha > 0f) {
                    drawRoundRect(
                        Brush.radialGradient(
                            listOf(theme.accent.copy(alpha = glowAlpha), Color.Transparent),
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = size.minDimension * 0.85f
                        ),
                        cornerRadius = CornerRadius(18.dp.toPx())
                    )
                }
            }
            .background(theme.accent.copy(alpha = bgAlpha))
            .border(if (selected) 1.5.dp else 0.5.dp, theme.accent.copy(alpha = borderAlpha), RoundedCornerShape(18.dp))
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                example,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (selected) theme.accent else theme.textPrimary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = if (selected) theme.accent.copy(alpha = 0.75f) else theme.textMuted
            )
        }
    }
}

private enum class FlowQuality { BALANCED, GOOD, STRETCHED, SPARSE }

private fun evaluateFlowQuality(reminders: List<CustomReminder>): FlowQuality {
    if (reminders.size < 2) return FlowQuality.SPARSE
    val minutes = reminders.mapNotNull { r ->
        val h = r.time.split(":")[0].toIntOrNull() ?: return@mapNotNull null
        val m = r.time.split(":").getOrNull(1)?.toIntOrNull() ?: 0
        h * 60 + m
    }.sorted()
    val maxGap = minutes.zipWithNext { a, b -> b - a }.maxOrNull() ?: 0
    val avgGap = if (minutes.size > 1) minutes.zipWithNext { a, b -> b - a }.average() else 0.0
    return when {
        maxGap > 300 -> FlowQuality.STRETCHED
        reminders.size < 3 -> FlowQuality.SPARSE
        maxGap <= 150 && avgGap in 60.0..180.0 -> FlowQuality.BALANCED
        else -> FlowQuality.GOOD
    }
}

private enum class TimeOfDay { MORNING, AFTERNOON, EVENING }

private fun currentTimeOfDay(): TimeOfDay {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5 until 12 -> TimeOfDay.MORNING
        in 12 until 18 -> TimeOfDay.AFTERNOON
        else -> TimeOfDay.EVENING
    }
}

@Composable
private fun RemindersSettingsSection(viewModel: HydraViewModel, theme: HydraTheme, newMsg: String, onMsgChange: (String) -> Unit) {
    var showTimePicker by remember { mutableStateOf(false) }
    var pickerId by remember { mutableStateOf("") }
    var pickerHour by remember { mutableIntStateOf(8) }
    var pickerMin by remember { mutableIntStateOf(0) }

    var deletingIds by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(deletingIds) {
        if (deletingIds.isNotEmpty()) {
            val snapshot = deletingIds.toSet()
            kotlinx.coroutines.delay(360)
            snapshot.forEach { id -> viewModel.removeReminder(id) }
            deletingIds = deletingIds - snapshot
        }
    }

    if (showTimePicker) {
        HydraTimePicker(
            initialHour = pickerHour, initialMinute = pickerMin, theme = theme,
            onConfirm = { h, m ->
                viewModel.updateReminderTime(pickerId, "%02d:%02d".format(h, m))
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    val timeOfDay = remember { currentTimeOfDay() }

    UnifiedReminderHero(viewModel = viewModel, theme = theme, timeOfDay = timeOfDay)

    AnimatedVisibility(
        visible = viewModel.remindersEnabled,
        enter = fadeIn(tween(320)) + expandVertically(tween(420)),
        exit = fadeOut(tween(200)) + shrinkVertically(tween(280))
    ) {
        Column {
            Spacer(Modifier.height(28.dp))

            val quality = evaluateFlowQuality(viewModel.customReminders.toList())
            FlowQualityBar(quality = quality, theme = theme)
            if (quality == FlowQuality.STRETCHED || quality == FlowQuality.SPARSE) {
                Spacer(Modifier.height(10.dp))
                SmartSuggestionStrip(quality = quality, theme = theme)
            }

            Spacer(Modifier.height(24.dp))
            Text(
                stringResource(R.string.reminders_section_your_flow),
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.4.sp, fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
                color = theme.accent.copy(alpha = 0.52f),
                modifier = Modifier.padding(start = 6.dp)
            )
            Spacer(Modifier.height(16.dp))

            val flowInf = rememberInfiniteTransition(label = "reminderFlow")
            val flowY by flowInf.animateFloat(0f, 1f, infiniteRepeatable(tween(9000, easing = LinearEasing)), label = "flowY")

            val reminderList = viewModel.customReminders.sortedBy { r ->
                val p = r.time.split(":")
                (p[0].toIntOrNull() ?: 0) * 60 + (p.getOrNull(1)?.toIntOrNull() ?: 0)
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Canvas(Modifier.matchParentSize()) {
                    val bandH = size.height * 0.55f
                    val yOff = (size.height + bandH) * flowY - bandH
                    drawRect(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, theme.accent.copy(alpha = 0.022f), theme.glow.copy(alpha = 0.016f), Color.Transparent),
                            startY = yOff,
                            endY = yOff + bandH
                        )
                    )
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    reminderList.forEachIndexed { i, rem ->
                        key(rem.id) {
                            AnimatedVisibility(
                                visible = rem.id !in deletingIds,
                                exit = fadeOut(tween(300)) + shrinkVertically(tween(320))
                            ) {
                                ReminderTimelineItem(
                                    reminder = rem,
                                    isFirst = i == 0,
                                    isLast = i == reminderList.lastIndex,
                                    isPast = isReminderPast(rem.time),
                                    canDelete = reminderList.size > 1,
                                    theme = theme,
                                    onTap = {
                                        val p = rem.time.split(":")
                                        pickerHour = p[0].toIntOrNull() ?: 8
                                        pickerMin = p.getOrNull(1)?.toIntOrNull() ?: 0
                                        pickerId = rem.id
                                        showTimePicker = true
                                    },
                                    onDelete = { deletingIds = deletingIds + rem.id }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            ReminderAddButton(theme = theme) { viewModel.addReminder() }

            Spacer(Modifier.height(36.dp))
            ReminderSoundSection(viewModel, theme)

            Spacer(Modifier.height(36.dp))
            ReminderMessagesSection(viewModel, theme, newMsg, onMsgChange)

            Spacer(Modifier.height(36.dp))
            AdvancedRemindersSection(viewModel, theme)

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun UnifiedReminderHero(viewModel: HydraViewModel, theme: HydraTheme, timeOfDay: TimeOfDay) {
    val ctx = LocalContext.current
    val quality = if (viewModel.remindersEnabled) evaluateFlowQuality(viewModel.customReminders.toList()) else FlowQuality.SPARSE

    val heroMood = when {
        !viewModel.remindersEnabled -> HydraViewModel.HydraMood.NEUTRAL
        quality == FlowQuality.BALANCED -> HydraViewModel.HydraMood.HAPPY
        quality == FlowQuality.GOOD -> HydraViewModel.HydraMood.CONTENT
        quality == FlowQuality.STRETCHED -> HydraViewModel.HydraMood.CONCERNED
        else -> HydraViewModel.HydraMood.NEUTRAL
    }
    val heroMessage = when {
        !viewModel.remindersEnabled -> stringResource(R.string.reminders_msg_disabled)
        quality == FlowQuality.BALANCED -> stringResource(R.string.reminders_msg_balanced)
        quality == FlowQuality.GOOD -> stringResource(R.string.reminders_msg_good)
        quality == FlowQuality.STRETCHED -> stringResource(R.string.reminders_msg_stretched)
        else -> stringResource(R.string.reminders_msg_sparse)
    }

    val glowInf = rememberInfiniteTransition(label = "heroGlow")
    val glowAlpha by glowInf.animateFloat(
        0.18f, 0.42f,
        infiniteRepeatable(tween(3200, easing = EaseInOutSine), RepeatMode.Reverse), label = "heroGA"
    )
    val glowScale by glowInf.animateFloat(
        0.85f, 1.15f,
        infiniteRepeatable(tween(3200, easing = EaseInOutSine), RepeatMode.Reverse), label = "heroGS"
    )
    val ringAlpha by glowInf.animateFloat(
        0.06f, 0.18f,
        infiniteRepeatable(tween(4800, easing = EaseInOutSine), RepeatMode.Reverse), label = "heroRA"
    )

    val todTint = when (timeOfDay) {
        TimeOfDay.MORNING -> theme.accent.copy(alpha = 0.07f)
        TimeOfDay.AFTERNOON -> theme.accent2.copy(alpha = 0.05f)
        TimeOfDay.EVENING -> theme.glow.copy(alpha = 0.09f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0f to Color.Transparent,
                    0.25f to todTint,
                    0.6f to theme.accent.copy(alpha = 0.07f),
                    1f to Color.Transparent
                )
            )
            .padding(top = 8.dp, bottom = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.reminders_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal),
                        color = theme.accent
                    )
                    Text(
                        stringResource(R.string.reminders_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.textSoft.copy(alpha = 0.65f),
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
                Box(modifier = Modifier.graphicsLayer { scaleX = 0.88f; scaleY = 0.88f }) {
                    Switch(
                        checked = viewModel.remindersEnabled,
                        onCheckedChange = {
                            HapticEngine.tick(ctx)
                            viewModel.remindersEnabled = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = theme.accent,
                            uncheckedThumbColor = theme.textMuted.copy(alpha = 0.7f),
                            uncheckedTrackColor = theme.accent.copy(alpha = 0.1f)
                        )
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            Box(contentAlignment = Alignment.Center) {
                Canvas(Modifier.size(180.dp)) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    drawCircle(
                        Brush.radialGradient(
                            listOf(theme.glow.copy(alpha = ringAlpha * 0.6f), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = size.width * 0.5f * glowScale
                        ),
                        radius = size.width * 0.5f * glowScale,
                        center = Offset(cx, cy)
                    )
                    drawCircle(
                        Brush.radialGradient(
                            listOf(theme.accent.copy(alpha = glowAlpha), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = size.width * 0.38f
                        ),
                        radius = size.width * 0.38f,
                        center = Offset(cx, cy)
                    )
                }
                Box(modifier = Modifier.size(120.dp)) {
                    HydraHeartCanvas(
                        fillPercent = viewModel.waterPercent,
                        mood = heroMood,
                        theme = theme,
                        showFace = viewModel.showFace,
                        hydraShape = viewModel.hydraShape,
                        drinkColours = viewModel.drinkColourPortions,
                        level = viewModel.levelInfo.level,
                        showRainbowShimmer = viewModel.showRainbowShimmer,
                        showButterfly = viewModel.showButterfly,
                        showGoldenGlow = viewModel.showGoldenGlow,
                        showNecklace = false,
                        equippedAccessories = emptySet(),
                        size = 120.dp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            Text(
                heroMessage,
                style = MaterialTheme.typography.bodySmall.copy(letterSpacing = 0.2.sp),
                color = theme.textSoft.copy(alpha = 0.82f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
private fun ReminderTimelineItem(
    reminder: CustomReminder,
    isFirst: Boolean,
    isLast: Boolean,
    isPast: Boolean,
    canDelete: Boolean,
    theme: HydraTheme,
    onTap: () -> Unit,
    onDelete: () -> Unit
) {
    val ctx = LocalContext.current
    val pulseInf = rememberInfiniteTransition(label = "rimPulse")
    val pulseAlpha by pulseInf.animateFloat(0.16f, 0.42f, infiniteRepeatable(tween(2100, easing = EaseInOutSine), RepeatMode.Reverse), label = "pA")
    val pulseScale by pulseInf.animateFloat(1f, 1.55f, infiniteRepeatable(tween(2100, easing = EaseInOutSine), RepeatMode.Reverse), label = "pS")
    val effectivePulseAlpha = if (!isPast) pulseAlpha else 0f
    val effectivePulseScale = if (!isPast) pulseScale else 1f

    val source = remember { MutableInteractionSource() }
    val isPressed by source.collectIsPressedAsState()
    val pressScale by animateFloatAsState(if (isPressed) 0.975f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "rimPress")

    val dotColor = if (isPast) theme.textMuted.copy(alpha = 0.55f) else theme.accent
    val timeColor = if (isPast) theme.textSoft.copy(alpha = 0.55f) else theme.accent
    val rowAlpha = if (isPast) 0.58f else 1f
    val label = reminderTimeLabelString(reminder.time)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(rowAlpha)
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(Modifier.width(36.dp).height(76.dp)) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val dotR = 5.2.dp.toPx()
            val lineW = 1.4.dp.toPx()

            if (!isFirst) {
                drawRect(
                    Brush.verticalGradient(listOf(theme.accent.copy(alpha = 0.09f), theme.accent.copy(alpha = 0.28f)), startY = 0f, endY = cy - dotR - 1f),
                    topLeft = Offset(cx - lineW / 2f, 0f),
                    size = Size(lineW, cy - dotR - 1f)
                )
            }
            if (!isLast) {
                drawRect(
                    Brush.verticalGradient(listOf(theme.accent.copy(alpha = 0.28f), theme.accent.copy(alpha = 0.07f)), startY = cy + dotR + 1f, endY = size.height),
                    topLeft = Offset(cx - lineW / 2f, cy + dotR + 1f),
                    size = Size(lineW, size.height - cy - dotR - 1f)
                )
            }
            if (effectivePulseAlpha > 0f) {
                drawCircle(
                    Brush.radialGradient(
                        listOf(theme.accent.copy(alpha = effectivePulseAlpha), Color.Transparent),
                        center = Offset(cx, cy),
                        radius = dotR * 3.2f * effectivePulseScale
                    ),
                    radius = 3.2f * dotR * effectivePulseScale,
                    center = Offset(cx, cy)
                )
            }
            drawCircle(
                Brush.radialGradient(
                    listOf(Color.White.copy(alpha = 0.94f), dotColor.copy(alpha = 0.88f), dotColor),
                    center = Offset(cx - dotR * 0.32f, cy - dotR * 0.32f),
                    radius = 2.2f * dotR
                ),
                radius = dotR,
                center = Offset(cx, cy)
            )
        }

        Spacer(Modifier.width(6.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable(interactionSource = source, indication = null) {
                    HapticEngine.tick(ctx)
                    onTap()
                }
                .padding(vertical = 12.dp)
        ) {
            Text(
                reminder.time,
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 30.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-1f).sp),
                color = timeColor
            )
            Text(
                label,
                style = MaterialTheme.typography.bodySmall.copy(letterSpacing = 0.3.sp),
                color = theme.textMuted.copy(alpha = if (isPast) 0.55f else 0.8f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .alpha(if (canDelete) 1f else 0f)
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, enabled = canDelete) {
                    HapticEngine.tick(ctx)
                    onDelete()
                },
            contentAlignment = Alignment.Center
        ) {
            Text("×", fontSize = 22.sp, color = theme.textMuted.copy(alpha = 0.38f), fontWeight = FontWeight.Light)
        }
    }
}

@Composable
private fun ReminderAddButton(theme: HydraTheme, onClick: () -> Unit) {
    val ctx = LocalContext.current
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "addScale")
    val fillAlpha by animateFloatAsState(if (pressed) 0.1f else 0.04f, tween(150), label = "addFill")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(20.dp))
            .background(theme.accent.copy(alpha = fillAlpha))
            .border(1.dp, theme.accent.copy(alpha = if (pressed) 0.3f else 0.18f), RoundedCornerShape(20.dp))
            .clickable(interactionSource = source, indication = null) {
                HapticEngine.tick(ctx)
                onClick()
            }
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("+", fontSize = 18.sp, color = theme.accent.copy(alpha = 0.75f), fontWeight = FontWeight.Light)
            Text(
                stringResource(R.string.reminders_add_reminder),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium, letterSpacing = 0.2.sp),
                color = theme.accent.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun LiquidSectionHeader(title: String, theme: HydraTheme, subtitle: String? = null, subtitleBottomSpace: Dp = 0.dp) {
    Text(
        title,
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.4.sp, fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
        color = theme.accent.copy(alpha = 0.52f),
        modifier = Modifier.padding(start = 6.dp)
    )
    if (subtitle != null) {
        Spacer(Modifier.height(3.dp))
        Text(
            subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = theme.textSoft.copy(alpha = 0.55f),
            modifier = Modifier.padding(start = 6.dp, bottom = subtitleBottomSpace)
        )
    }
}

@Composable
private fun ReminderStylePillChip(label: String, selected: Boolean, theme: HydraTheme, modifier: Modifier = Modifier, leadingGlyph: String, onClick: () -> Unit) {
    val bgAlpha by animateFloatAsState(if (selected) 0.14f else 0.04f, tween(200), label = "pillBg")
    val borderAlpha by animateFloatAsState(if (selected) 0.45f else 0.1f, tween(200), label = "pillBd")
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, spring(dampingRatio = 0.5f), label = "pillSc")

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(16.dp))
            .background(theme.accent.copy(alpha = bgAlpha))
            .border(if (selected) 1.dp else 0.5.dp, theme.accent.copy(alpha = borderAlpha), RoundedCornerShape(16.dp))
            .clickable(interactionSource = source, indication = null, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(leadingGlyph, fontSize = 13.sp, color = if (selected) theme.accent else theme.textMuted.copy(alpha = 0.65f))
            Spacer(Modifier.width(7.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal),
                color = if (selected) theme.accent else theme.textSoft.copy(alpha = 0.72f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun notificationSoundLabel(soundId: String): String = when (soundId) {
    "silent" -> stringResource(R.string.reminders_sound_silent)
    "default" -> stringResource(R.string.reminders_sound_default)
    "hydra_chime" -> stringResource(R.string.sound_hydra_chime)
    "hydra_drop" -> stringResource(R.string.sound_hydra_drop)
    "hydra_crystal" -> stringResource(R.string.sound_hydra_crystal)
    "hydra_bowl" -> stringResource(R.string.sound_hydra_bowl)
    "hydra_shimmer" -> stringResource(R.string.sound_hydra_shimmer)
    "hydra_bell" -> stringResource(R.string.sound_hydra_bell)
    else -> soundId
}

private fun soundChipGlyph(soundId: String): String = when (soundId) {
    "silent" -> "○"
    "default" -> "◎"
    "hydra_chime" -> "♫"
    "hydra_drop" -> "💧"
    "hydra_crystal" -> "◇"
    "hydra_bowl" -> "⊙"
    "hydra_shimmer" -> "✦"
    "hydra_bell" -> "◉"
    else -> "♪"
}

@Composable
private fun FlowQualityBar(quality: FlowQuality, theme: HydraTheme) {
    val label = when (quality) {
        FlowQuality.BALANCED -> stringResource(R.string.reminders_flow_balanced)
        FlowQuality.GOOD -> stringResource(R.string.reminders_flow_good)
        FlowQuality.STRETCHED -> stringResource(R.string.reminders_flow_stretched)
        FlowQuality.SPARSE -> stringResource(R.string.reminders_flow_sparse)
    }
    val desc = when (quality) {
        FlowQuality.BALANCED -> stringResource(R.string.reminders_flow_desc_balanced)
        FlowQuality.GOOD -> stringResource(R.string.reminders_flow_desc_good)
        FlowQuality.STRETCHED -> stringResource(R.string.reminders_flow_desc_stretched)
        FlowQuality.SPARSE -> stringResource(R.string.reminders_flow_desc_sparse)
    }
    val barFraction = when (quality) {
        FlowQuality.BALANCED -> 1f
        FlowQuality.GOOD -> 0.72f
        FlowQuality.STRETCHED -> 0.38f
        FlowQuality.SPARSE -> 0.22f
    }
    val barColor = when (quality) {
        FlowQuality.BALANCED -> theme.accent
        FlowQuality.GOOD -> theme.accent2
        FlowQuality.STRETCHED -> theme.glow
        FlowQuality.SPARSE -> theme.textMuted
    }
    val animFraction by animateFloatAsState(barFraction, tween(800, easing = EaseInOutSine), label = "flowBar")

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(R.string.reminders_flow_quality_label),
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp, fontWeight = FontWeight.SemiBold, fontSize = 10.sp),
                color = theme.accent.copy(alpha = 0.5f)
            )
            Spacer(Modifier.weight(1f))
            Text(label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp), color = barColor.copy(alpha = 0.85f))
        }
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(50))
                .background(theme.accent.copy(alpha = 0.08f))
        ) {
            Box(
                Modifier
                    .fillMaxWidth(animFraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(Brush.horizontalGradient(listOf(barColor.copy(alpha = 0.55f), barColor)))
            )
        }
        Spacer(Modifier.height(5.dp))
        Text(desc, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = theme.textMuted.copy(alpha = 0.65f))
    }
}

@Composable
private fun SmartSuggestionStrip(quality: FlowQuality, theme: HydraTheme) {
    val suggestion = when (quality) {
        FlowQuality.STRETCHED -> stringResource(R.string.reminders_suggestion_stretched)
        FlowQuality.SPARSE -> stringResource(R.string.reminders_suggestion_sparse)
        else -> return
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(theme.glow.copy(alpha = 0.07f))
            .border(0.5.dp, theme.glow.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Text("✦", fontSize = 11.sp, color = theme.glow.copy(alpha = 0.7f))
        Spacer(Modifier.width(10.dp))
        Text(
            stringResource(R.string.reminders_suggestion_format, suggestion),
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = theme.textSoft.copy(alpha = 0.78f)
        )
    }
}

@Composable
private fun ReminderSoundSection(viewModel: HydraViewModel, theme: HydraTheme) {
    val ctx = LocalContext.current

    LiquidSectionHeader(
        title = stringResource(R.string.reminders_sound_section),
        theme = theme,
        subtitle = stringResource(R.string.reminders_sound_hint),
        subtitleBottomSpace = 14.dp
    )

    val soundOptions = listOf(
        "silent" to stringResource(R.string.reminders_sound_silent),
        "default" to stringResource(R.string.reminders_sound_default),
    ) + NotificationHelper.CURATED_SOUNDS.map { (id, _) -> id to notificationSoundLabel(id) }

    val activePlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        soundOptions.chunked(2).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { (id, name) ->
                    SoundOptionChip(
                        id = id,
                        name = name,
                        selected = viewModel.reminderSound == id,
                        theme = theme,
                        modifier = Modifier.weight(1f)
                    ) {
                        HapticEngine.tick(ctx)
                        viewModel.reminderSound = id
                        activePlayer.value?.stop()
                        activePlayer.value?.release()
                        activePlayer.value = null
                        when {
                            id == "silent" -> {}
                            id.startsWith("hydra_") -> {
                                val resId = ctx.resources.getIdentifier(id, "raw", ctx.packageName)
                                if (resId != 0) {
                                    activePlayer.value = MediaPlayer.create(ctx, resId)?.also { it.start() }
                                } else {
                                    val slot = NotificationHelper.CURATED_SOUNDS.indexOfFirst { it.first == id }.coerceAtLeast(0)
                                    val uri = NotificationHelper.getSystemNotificationUri(ctx, slot)
                                    try { RingtoneManager.getRingtone(ctx, uri)?.play() } catch (_: Exception) {}
                                }
                            }
                            else -> {
                                val uri = NotificationHelper.getSoundUri(ctx, id) ?: return@SoundOptionChip
                                try { RingtoneManager.getRingtone(ctx, uri)?.play() } catch (_: Exception) {}
                            }
                        }
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { activePlayer.value?.stop(); activePlayer.value?.release() }
    }
}

@Composable
private fun SoundOptionChip(
    id: String,
    name: String,
    selected: Boolean,
    theme: HydraTheme,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ReminderStylePillChip(
        label = name,
        selected = selected,
        theme = theme,
        modifier = modifier,
        leadingGlyph = soundChipGlyph(id),
        onClick = onClick
    )
}

@Composable
private fun ReminderMessagesSection(
    viewModel: HydraViewModel,
    theme: HydraTheme,
    newMsg: String,
    onMsgChange: (String) -> Unit
) {
    val ctx = LocalContext.current
    var showPresetLibrary by remember { mutableStateOf(false) }

    LiquidSectionHeader(
        title = stringResource(R.string.reminders_voice_section),
        theme = theme,
        subtitle = stringResource(R.string.reminders_voice_hint),
        subtitleBottomSpace = 14.dp
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(theme.accent.copy(alpha = 0.04f))
            .border(0.5.dp, theme.accent.copy(alpha = 0.09f), RoundedCornerShape(14.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                HapticEngine.tick(ctx); showPresetLibrary = !showPresetLibrary
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                stringResource(R.string.reminders_voice_presets_title),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = theme.textPrimary.copy(alpha = 0.85f)
            )
            Text(
                stringResource(R.string.reminders_voice_presets_sub),
                style = MaterialTheme.typography.bodySmall,
                color = theme.textMuted.copy(alpha = 0.65f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Text(
            if (showPresetLibrary) "−" else "+",
            fontSize = 20.sp,
            color = theme.accent.copy(alpha = 0.55f),
            fontWeight = FontWeight.Light
        )
    }

    AnimatedVisibility(
        visible = showPresetLibrary,
        enter = fadeIn(tween(200)) + expandVertically(tween(280)),
        exit = fadeOut(tween(160)) + shrinkVertically(tween(220))
    ) {
        Column(modifier = Modifier.padding(top = 8.dp)) {
            AnimatedVisibility(
                visible = viewModel.disabledPresetIndices.isNotEmpty(),
                enter = fadeIn(tween(200)) + expandVertically(tween(240)),
                exit = fadeOut(tween(160)) + shrinkVertically(tween(200))
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.End) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(theme.accent.copy(alpha = 0.07f))
                            .border(0.5.dp, theme.accent.copy(alpha = 0.18f), RoundedCornerShape(12.dp))
                            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                                HapticEngine.tick(ctx); viewModel.resetPresetMessages()
                            }
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            stringResource(R.string.reminders_voice_reset_all),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = theme.accent.copy(alpha = 0.75f)
                        )
                    }
                }
            }

            HydraViewModel.PRESET_REMINDER_MESSAGES.forEachIndexed { i, msg ->
                if (i !in viewModel.disabledPresetIndices) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp, horizontal = 4.dp)
                    ) {
                        Box(Modifier.padding(top = 5.dp).size(4.dp).background(theme.accent.copy(alpha = 0.25f), CircleShape))
                        Spacer(Modifier.width(12.dp))
                        Text(
                            msg,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = theme.textPrimary.copy(alpha = 0.65f),
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                                    HapticEngine.tick(ctx); viewModel.disablePresetMessage(i)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("×", fontSize = 16.sp, color = theme.textMuted.copy(alpha = 0.35f), fontWeight = FontWeight.Light)
                        }
                    }
                    if (i < HydraViewModel.PRESET_REMINDER_MESSAGES.lastIndex) {
                        Box(Modifier.fillMaxWidth().padding(start = 16.dp).height(0.5.dp).background(theme.accent.copy(alpha = 0.04f)))
                    }
                }
            }
        }
    }

    if (viewModel.reminderMessages.isNotEmpty()) {
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(R.string.reminders_your_messages),
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp, fontWeight = FontWeight.SemiBold, fontSize = 10.sp),
            color = theme.accent.copy(alpha = 0.45f),
            modifier = Modifier.padding(start = 6.dp, bottom = 8.dp)
        )
        viewModel.reminderMessages.forEachIndexed { i, msg ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
            ) {
                Box(Modifier.size(5.dp).background(theme.accent.copy(alpha = 0.5f), CircleShape))
                Spacer(Modifier.width(12.dp))
                Text(msg, style = MaterialTheme.typography.bodySmall, color = theme.textPrimary.copy(alpha = 0.88f), modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                            HapticEngine.tick(ctx); viewModel.removeReminderMessage(i)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("×", fontSize = 17.sp, color = theme.textMuted.copy(alpha = 0.36f), fontWeight = FontWeight.Light)
                }
            }
            if (i < viewModel.reminderMessages.lastIndex) {
                Box(Modifier.fillMaxWidth().padding(start = 17.dp).height(0.5.dp).background(theme.accent.copy(alpha = 0.05f)))
            }
        }
    }

    Spacer(Modifier.height(14.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(theme.accent.copy(alpha = 0.04f))
            .border(0.5.dp, theme.accent.copy(alpha = 0.11f), RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        BasicTextField(
            value = newMsg,
            onValueChange = onMsgChange,
            modifier = Modifier.weight(1f).padding(vertical = 10.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = theme.textPrimary),
            singleLine = true,
            decorationBox = { inner ->
                Box {
                    if (newMsg.isEmpty()) Text(stringResource(R.string.reminders_voice_add_placeholder), style = MaterialTheme.typography.bodyMedium, color = theme.textMuted.copy(alpha = 0.55f))
                    inner()
                }
            }
        )
        AnimatedVisibility(visible = newMsg.isNotBlank()) {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(theme.accent.copy(alpha = 0.14f))
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                        HapticEngine.tick(ctx)
                        viewModel.addReminderMessage(newMsg)
                        onMsgChange("")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("+", fontSize = 20.sp, color = theme.accent, fontWeight = FontWeight.Light)
            }
        }
    }
}

@Composable
private fun AdvancedRemindersSection(viewModel: HydraViewModel, theme: HydraTheme) {
    val ctx = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f, tween(250), label = "advArrow")

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                    HapticEngine.tick(ctx); expanded = !expanded
                }
                .padding(vertical = 4.dp, horizontal = 2.dp)
        ) {
            Text(
                stringResource(R.string.reminders_advanced_section),
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.4.sp, fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
                color = theme.accent.copy(alpha = 0.52f),
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            )
            Text(
                "∨",
                fontSize = 14.sp,
                color = theme.accent.copy(alpha = 0.4f),
                modifier = Modifier.graphicsLayer { rotationZ = arrowRotation }
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(200)) + expandVertically(tween(280)),
            exit = fadeOut(tween(160)) + shrinkVertically(tween(220))
        ) {
            Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                AdvancedToggleRow(
                    label = stringResource(R.string.reminders_advanced_stop_goal),
                    sublabel = stringResource(R.string.reminders_advanced_stop_goal_sub),
                    checked = viewModel.reminderStopAtGoal,
                    theme = theme,
                    onToggle = { HapticEngine.tick(ctx); viewModel.reminderStopAtGoal = it }
                )
                AdvancedToggleRow(
                    label = stringResource(R.string.reminders_advanced_snooze),
                    sublabel = stringResource(R.string.reminders_advanced_snooze_sub),
                    checked = viewModel.reminderSmartSnooze,
                    theme = theme,
                    onToggle = { HapticEngine.tick(ctx); viewModel.reminderSmartSnooze = it }
                )
                AdvancedToggleRow(
                    label = stringResource(R.string.reminders_advanced_summary),
                    sublabel = stringResource(R.string.reminders_advanced_summary_sub),
                    checked = viewModel.reminderDailySummary,
                    theme = theme,
                    onToggle = { HapticEngine.tick(ctx); viewModel.reminderDailySummary = it }
                )
            }
        }
    }
}

@Composable
private fun AdvancedToggleRow(
    label: String,
    sublabel: String,
    checked: Boolean,
    theme: HydraTheme,
    onToggle: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(theme.accent.copy(alpha = 0.035f))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = theme.textPrimary)
            Text(sublabel, style = MaterialTheme.typography.bodySmall, color = theme.textMuted.copy(alpha = 0.68f), modifier = Modifier.padding(top = 2.dp))
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = theme.accent,
                uncheckedThumbColor = theme.textMuted,
                uncheckedTrackColor = theme.accent.copy(alpha = 0.12f)
            )
        )
    }
}

@Composable
private fun reminderTimeLabelString(time: String): String {
    val hour = time.split(":").firstOrNull()?.toIntOrNull() ?: 12
    val resId = when (hour) {
        in 0 until 5 -> R.string.reminder_time_before_dawn
        in 5 until 9 -> R.string.reminder_time_morning
        in 9 until 12 -> R.string.reminder_time_late_morning
        in 12 until 14 -> R.string.reminder_time_midday
        in 14 until 17 -> R.string.reminder_time_afternoon
        in 17 until 20 -> R.string.reminder_time_evening
        in 20 until 22 -> R.string.reminder_time_night
        else -> R.string.reminder_time_late_night
    }
    return stringResource(resId)
}

private fun isReminderPast(time: String): Boolean {
    val now = Calendar.getInstance()
    val hour = time.split(":").firstOrNull()?.toIntOrNull() ?: 0
    val min = time.split(":").getOrNull(1)?.toIntOrNull() ?: 0
    val nowH = now.get(Calendar.HOUR_OF_DAY)
    val nowM = now.get(Calendar.MINUTE)
    return hour < nowH || (hour == nowH && min <= nowM)
}

@Composable
private fun ThemeSettingsSection(viewModel: HydraViewModel, theme: HydraTheme) {
    SectionHeader("Appearance", "Unlock new aesthetics as you grow", theme)

    val allThemes = viewModel.allThemes
    val unlocked = allThemes.filter { viewModel.isThemeUnlocked(it.id) }
    val missionLocked = allThemes.filter { it.unlockAchievement != null && !viewModel.isThemeUnlocked(it.id) }
    val levelGroups = allThemes
        .filter { it.unlockLevel > 0 && it.unlockAchievement == null && !viewModel.isThemeUnlocked(it.id) }
        .groupBy { it.unlockLevel }
        .entries
        .sortedBy { it.key }

    if (unlocked.isNotEmpty()) {
        ThemeCollectionHeader("YOUR COLLECTION", null, theme)
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            unlocked.forEach { th ->
                ThemeProgressRow(
                    th = th, selected = viewModel.themeId == th.id, locked = false, sublabel = null, isMission = false, theme = theme,
                    onClick = { viewModel.themeId = th.id }
                )
            }
        }
        Spacer(Modifier.height(28.dp))
    }

    if (missionLocked.isNotEmpty()) {
        ThemeCollectionHeader("MISSION COLLECTION", "Special unlocks through your actions", theme, isMission = true)
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            missionLocked.forEach { th ->
                ThemeProgressRow(
                    th = th, selected = false, locked = true, sublabel = missionHint(th.unlockAchievement), isMission = true, theme = theme,
                    onClick = {}
                )
            }
        }
        Spacer(Modifier.height(28.dp))
    }

    if (levelGroups.isNotEmpty()) {
        ThemeCollectionHeader("LEVEL COLLECTIONS", "New tiers unlock as you level up", theme)
        levelGroups.forEachIndexed { gi, (level, levelThemes) ->
            Spacer(Modifier.height(if (gi == 0) 10.dp else 20.dp))
            Box(
                modifier = Modifier
                    .padding(start = 2.dp, bottom = 7.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(theme.accent.copy(alpha = 0.09f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    "Level $level",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp),
                    color = theme.accent.copy(alpha = 0.7f)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                levelThemes.forEach { th ->
                    ThemeProgressRow(
                        th = th, selected = false, locked = true, sublabel = "Level $level", isMission = false, theme = theme,
                        onClick = {}
                    )
                }
            }
        }
    }
}

private fun missionHint(achievement: String?): String = when (achievement) {
    "perfect_day" -> "Complete a perfect hydration day"
    "variety" -> "Log 5 different drink types"
    "two_litres" -> "Reach your goal 3 days in a row"
    "week_streak" -> "Maintain a 7-day hydration streak"
    else -> "Complete a special challenge"
}

@Composable
private fun ThemeCollectionHeader(label: String, subtitle: String?, theme: HydraTheme, isMission: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.4.sp, fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
            color = theme.accent.copy(alpha = if (isMission) 0.72f else 0.52f)
        )
        if (isMission) {
            Spacer(Modifier.width(7.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(theme.accent.copy(alpha = 0.09f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("✦", fontSize = 8.sp, color = theme.accent.copy(alpha = 0.65f))
            }
        }
    }
    if (subtitle != null) {
        Spacer(Modifier.height(2.dp))
        Text(
            subtitle,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = theme.textSoft.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun ThemeProgressRow(
    th: HydraTheme,
    selected: Boolean,
    locked: Boolean,
    sublabel: String?,
    isMission: Boolean,
    theme: HydraTheme,
    onClick: () -> Unit
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && !locked) 0.985f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "thRowSc"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale; scaleY = scale
                alpha = if (locked && !isMission) 0.58f else 1f
            }
            .clip(RoundedCornerShape(16.dp))
            .then(
                when {
                    selected -> Modifier.background(th.accent.copy(alpha = 0.07f))
                    isMission -> Modifier.background(Brush.horizontalGradient(listOf(theme.accent.copy(alpha = 0.04f), theme.accent2.copy(alpha = 0.02f))))
                    locked -> Modifier.background(Brush.horizontalGradient(listOf(th.accent.copy(alpha = 0.04f), Color.Transparent)))
                    else -> Modifier
                }
            )
            .border(
                if (selected) 1.dp else 0.5.dp,
                when {
                    selected -> th.accent.copy(alpha = 0.35f)
                    isMission -> theme.accent.copy(alpha = 0.09f)
                    locked -> theme.accent.copy(alpha = 0.05f)
                    else -> theme.accent.copy(alpha = 0.07f)
                },
                RoundedCornerShape(16.dp)
            )
            .clickable(interactionSource = source, indication = null, enabled = !locked) { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(52.dp, 42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.verticalGradient(listOf(th.bg1, th.bg2)))
                    .border(
                        1.dp,
                        th.accent.copy(alpha = if (selected) 0.55f else 0.12f),
                        RoundedCornerShape(10.dp)
                    )
            ) {
                Box(
                    Modifier
                        .padding(6.dp)
                        .size(7.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(th.accent)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(th.emoji, fontSize = 14.sp)
                    Text(
                        th.name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal),
                        color = theme.textPrimary.copy(alpha = if (selected) 1f else 0.88f)
                    )
                }
                if (sublabel != null) {
                    Text(
                        sublabel,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = if (isMission) theme.accent.copy(alpha = 0.62f) else theme.textMuted.copy(alpha = 0.5f),
                        modifier = Modifier.padding(top = 2.dp, start = 20.dp)
                    )
                }
            }

            if (selected) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(th.accent.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = th.accent, modifier = Modifier.size(18.dp))
                }
            } else if (locked) {
                Text(
                    "🔒", fontSize = 11.sp,
                    modifier = Modifier.alpha(if (isMission) 0.85f else 0.38f)
                )
            }
        }
    }
}

@Composable
private fun UnitSettingsSection(viewModel: HydraViewModel, theme: HydraTheme) {
    val breathInf = rememberInfiniteTransition(label = "unitBreath")
    val breathScale by breathInf.animateFloat(
        0.96f, 1.04f,
        infiniteRepeatable(tween(2600, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "breathSc"
    )
    val heroGlow by breathInf.animateFloat(
        0.13f, 0.28f,
        infiniteRepeatable(tween(2600, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "unitGlA"
    )

    val unitMessage = when (viewModel.unitSystem) {
        "oz" -> "US standard — I'll keep up with you ✨"
        "L" -> "Thinking big — I love that about you 🌊"
        else -> "Precise and personal — just how I like it 💧"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0f to theme.accent.copy(alpha = 0f),
                    0.12f to theme.accent.copy(alpha = 0.018f),
                    0.32f to theme.accent.copy(alpha = 0.048f),
                    0.55f to theme.glow.copy(alpha = 0.038f),
                    0.8f to theme.accent.copy(alpha = 0.018f),
                    1f to theme.accent.copy(alpha = 0f)
                )
            )
            .padding(top = 4.dp, bottom = 22.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        "Measurement",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal),
                        color = theme.accent
                    )
                    Text(
                        "Your way, your world",
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.textSoft.copy(alpha = 0.65f),
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            Box(contentAlignment = Alignment.Center) {
                Canvas(Modifier.size(130.dp)) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val r = 0.54f * size.width
                    drawCircle(
                        Brush.radialGradient(
                            0f to theme.accent.copy(alpha = heroGlow * 0.46f),
                            0.35f to theme.accent.copy(alpha = heroGlow * 0.24f),
                            0.65f to theme.accent.copy(alpha = heroGlow * 0.07f),
                            0.88f to theme.accent.copy(alpha = heroGlow * 0.02f),
                            1f to theme.accent.copy(alpha = 0f),
                            center = Offset(cx, cy),
                            radius = r
                        ),
                        radius = r,
                        center = Offset(cx, cy)
                    )
                }
                Box(Modifier.size(88.dp).graphicsLayer { scaleX = breathScale; scaleY = breathScale }) {
                    HydraHeartCanvas(
                        fillPercent = viewModel.waterPercent,
                        mood = HydraViewModel.HydraMood.HAPPY,
                        theme = theme,
                        showFace = viewModel.showFace,
                        hydraShape = viewModel.hydraShape,
                        drinkColours = viewModel.drinkColourPortions,
                        level = viewModel.levelInfo.level,
                        showRainbowShimmer = viewModel.showRainbowShimmer,
                        showButterfly = viewModel.showButterfly,
                        showGoldenGlow = viewModel.showGoldenGlow,
                        showNecklace = false,
                        equippedAccessories = emptySet(),
                        size = 88.dp
                    )
                }
            }

            Spacer(Modifier.height(13.dp))

            Crossfade(targetState = unitMessage, animationSpec = tween(260), label = "unitMsg") { msg ->
                Text(
                    msg,
                    style = MaterialTheme.typography.bodySmall.copy(letterSpacing = 0.2.sp),
                    color = theme.textSoft.copy(alpha = 0.78f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
                )
            }
        }
    }

    Spacer(Modifier.height(4.dp))
    Text(
        "VOLUME",
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.4.sp, fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
        color = theme.accent.copy(alpha = 0.52f),
        modifier = Modifier.padding(start = 6.dp, bottom = 10.dp)
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
    ) {
        UnitCard("ml", "Millilitres", "Most common worldwide", viewModel.unitSystem == "ml", theme, Modifier.weight(1f).fillMaxHeight()) {
            viewModel.unitSystem = "ml"
        }
        UnitCard("oz", "Fluid Ounces", "US & UK standard", viewModel.unitSystem == "oz", theme, Modifier.weight(1f).fillMaxHeight()) {
            viewModel.unitSystem = "oz"
        }
        UnitCard("L", "Litres", "Larger volume tracking", viewModel.unitSystem == "L", theme, Modifier.weight(1f).fillMaxHeight()) {
            viewModel.unitSystem = "L"
        }
    }

    Spacer(Modifier.height(14.dp))
    UnitLivePreview(selected = viewModel.unitSystem, theme = theme)
}

@Composable
private fun UnitCard(
    symbol: String,
    label: String,
    desc: String,
    selected: Boolean,
    theme: HydraTheme,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val ctx = LocalContext.current
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()

    val bgAlpha by animateFloatAsState(if (selected) 0.13f else 0.04f, tween(260), label = "ucBg")
    val borderAlpha by animateFloatAsState(if (selected) 0.38f else 0.07f, tween(260), label = "ucBd")
    val glowAlpha by animateFloatAsState(if (selected) 0.22f else 0f, tween(320), label = "ucGl")
    val lift by animateFloatAsState(if (selected) -7f else 0f, tween(240), label = "ucLt")
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "ucSc"
    )

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale; translationY = lift }
            .drawBehind {
                val r = size.width * 0.8f
                drawCircle(
                    Brush.radialGradient(
                        0f to theme.accent.copy(alpha = glowAlpha),
                        0.42f to theme.accent.copy(alpha = 0.4f * glowAlpha),
                        0.75f to theme.accent.copy(alpha = 0.09f * glowAlpha),
                        1f to theme.accent.copy(alpha = 0f),
                        radius = r
                    ),
                    radius = r
                )
            }
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    0f to theme.accent.copy(alpha = bgAlpha * 1.2f),
                    0.5f to theme.accent.copy(alpha = bgAlpha),
                    1f to theme.accent.copy(alpha = bgAlpha * 0.55f)
                )
            )
            .border(1.dp, theme.accent.copy(alpha = borderAlpha), RoundedCornerShape(20.dp))
            .clickable(interactionSource = source, indication = null) {
                HapticEngine.tick(ctx); onClick()
            }
            .padding(horizontal = 8.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                symbol,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, fontSize = 22.sp),
                color = if (selected) theme.accent else theme.textSoft.copy(alpha = 0.68f),
                maxLines = 1
            )
            Spacer(Modifier.height(3.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                color = if (selected) theme.accent.copy(alpha = 0.72f) else theme.textMuted.copy(alpha = 0.5f),
                maxLines = 1
            )
            Spacer(Modifier.height(6.dp))
            Text(
                desc,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = theme.textMuted.copy(alpha = if (selected) 0.58f else 0.36f),
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp)
            )
        }
    }
}

@Composable
private fun UnitLivePreview(selected: String, theme: HydraTheme) {
    val items = listOf("ml" to "500 ml", "oz" to "16.9 oz", "L" to "0.5 L")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.verticalGradient(listOf(theme.accent.copy(alpha = 0.05f), theme.accent.copy(alpha = 0.025f))))
            .border(0.5.dp, theme.accent.copy(alpha = 0.08f), RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column {
            Text(
                "LIVE PREVIEW",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold),
                color = theme.accent.copy(alpha = 0.42f)
            )
            Spacer(Modifier.height(9.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Water bottle",
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.textSoft.copy(alpha = 0.58f),
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    items.forEachIndexed { i, (id, value) ->
                        val isSel = id == selected
                        val textAlpha by animateFloatAsState(if (isSel) 1f else 0.36f, tween(220), label = "pvA$i")
                        Text(
                            value,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (isSel) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = if (isSel) 13.sp else 11.sp
                            ),
                            color = if (isSel) theme.accent else theme.textMuted.copy(alpha = textAlpha)
                        )
                        if (i < items.lastIndex) Text("·", fontSize = 9.sp, color = theme.textMuted.copy(alpha = 0.22f))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmotionalJourneySection(viewModel: HydraViewModel, theme: HydraTheme, onNavigate: (String) -> Unit) {
    SectionHeader("How We Feel", "Track your emotional wellness", theme)
    FrostedGlass(theme = theme, padding = PaddingValues(0.dp)) {
        listOf(
            "mood_tracking" to ("🌸" to "Log Today's Mood"),
            "mood_calendar" to ("🗓️" to "Emotional History")
        ).forEachIndexed { i, (route, data) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(route) }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                GlassIconBox(icon = data.first, theme = theme); Spacer(Modifier.width(16.dp))
                Text(data.second, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal), modifier = Modifier.weight(1f), color = theme.textPrimary)
                Text("›", fontSize = 22.sp, color = theme.accent.copy(alpha = 0.4f), fontWeight = FontWeight.Light)
            }
            if (i == 0) Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(0.5.dp).background(theme.accent.copy(alpha = 0.06f)))
        }
    }
}

@Composable
private fun GoalsSettingsSection(viewModel: HydraViewModel, theme: HydraTheme) {
    val ctx = LocalContext.current
    val mem = viewModel.hydraMemory
    val calcGoals = viewModel.calculatedGoals

    var showWhyThis by remember { mutableStateOf(false) }
    var confirmText by remember { mutableStateOf<String?>(null) }
    var expandedBalance by remember { mutableStateOf<String?>(null) }
    var inputText by remember {
        mutableStateOf(
            (if (viewModel.customGoalEnabled && viewModel.customWaterGoalMl != null) viewModel.customWaterGoalMl!!
            else calcGoals.waterMl).toString()
        )
    }

    val displayGoalMl = if (viewModel.customGoalEnabled && viewModel.customWaterGoalMl != null) viewModel.customWaterGoalMl!! else calcGoals.waterMl
    val animatedGoal by animateIntAsState(displayGoalMl, tween(600), label = "goalAnim")
    val shimmer = rememberInfiniteTransition(label = "shimmer")
    val shimmerX by shimmer.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing)),
        label = "shimX"
    )

    val identity = flowIdentityLabel(mem)
    val microcopy = goalMicrocopy(mem)
    val nudge = smartNudge(viewModel, mem)

    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Daily Flow", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal), color = theme.accent)
            Text("Set your hydration intention", style = MaterialTheme.typography.bodySmall, color = theme.textSoft, modifier = Modifier.padding(top = 2.dp))
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(theme.accent.copy(alpha = 0.12f))
                .border(0.5.dp, theme.accent.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(identity, style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.5.sp), color = theme.accent, fontWeight = FontWeight.SemiBold)
        }
    }

    Spacer(Modifier.height(10.dp))

    val glowColor = theme.accent
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
            .drawBehind {
                drawCircle(
                    Brush.radialGradient(
                        listOf(glowColor.copy(alpha = 0.2f), Color.Transparent),
                        radius = size.width * 0.65f,
                        center = Offset(size.width / 2f, size.height * 0.3f)
                    ),
                    radius = size.width * 0.65f,
                    center = Offset(size.width / 2f, size.height * 0.3f)
                )
            }
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.verticalGradient(listOf(theme.accent.copy(alpha = 0.13f), theme.accent.copy(alpha = 0.06f), theme.bg2.copy(alpha = 0.4f))))
            .border(1.dp, Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.18f), theme.accent.copy(alpha = 0.08f))), RoundedCornerShape(24.dp))
            .drawWithContent {
                drawContent()
                drawRect(
                    Brush.linearGradient(
                        listOf(Color.Transparent, Color.White.copy(alpha = 0.06f), Color.Transparent),
                        start = Offset(shimmerX * size.width, 0f),
                        end = Offset(shimmerX * size.width + size.width * 0.4f, size.height)
                    )
                )
            }
            .padding(horizontal = 24.dp, vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    viewModel.convertMl(animatedGoal),
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold, fontSize = 58.sp),
                    color = theme.accent
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    viewModel.unitLabel(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                    color = theme.accent.copy(alpha = 0.65f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(microcopy, style = MaterialTheme.typography.bodySmall, color = theme.textSoft, textAlign = TextAlign.Center)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(theme.accent.copy(alpha = 0.06f))
            .border(0.5.dp, theme.accent.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listOf(false to "Auto", true to "Custom").forEach { (isCustom, label) ->
            val selected = viewModel.customGoalEnabled == isCustom
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) theme.accent.copy(alpha = 0.16f) else Color.Transparent)
                    .border(0.5.dp, if (selected) theme.accent.copy(alpha = 0.35f) else Color.Transparent, RoundedCornerShape(12.dp))
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                        HapticEngine.tick(ctx)
                        viewModel.customGoalEnabled = isCustom
                        if (isCustom && viewModel.customWaterGoalMl == null) {
                            viewModel.customWaterGoalMl = calcGoals.waterMl
                            inputText = calcGoals.waterMl.toString()
                        }
                        confirmText = null
                    }
                    .padding(vertical = 11.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium), color = if (selected) theme.accent else theme.textSoft)
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    val recommendedMl = listOf(1500, 2000, 2500, 3000).minByOrNull { abs(it - calcGoals.waterMl) } ?: 2000
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf(1500 to "1.5L", 2000 to "2L", 2500 to "2.5L", 3000 to "3L").forEach { (ml, lbl) ->
            FlowPresetChip(
                label = lbl,
                isSelected = if (viewModel.customGoalEnabled) viewModel.customWaterGoalMl == ml else calcGoals.waterMl == ml,
                isRecommended = ml == recommendedMl,
                theme = theme,
                modifier = Modifier.weight(1f)
            ) {
                if (!viewModel.customGoalEnabled) viewModel.customGoalEnabled = true
                viewModel.customWaterGoalMl = ml
                inputText = ml.toString()
                confirmText = null
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    AnimatedVisibility(visible = viewModel.customGoalEnabled, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
        Column {
            OutlinedTextField(
                value = inputText,
                onValueChange = { v ->
                    inputText = v
                    v.toIntOrNull()?.let { viewModel.customWaterGoalMl = it.coerceIn(500, 8000) }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Custom amount", color = theme.textSoft) },
                shape = RoundedCornerShape(16.dp),
                suffix = { Text(viewModel.unitLabel(), color = theme.textSoft) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = theme.accent.copy(alpha = 0.35f),
                    unfocusedBorderColor = theme.accent.copy(alpha = 0.12f),
                    focusedTextColor = theme.textPrimary,
                    unfocusedTextColor = theme.textPrimary,
                    cursorColor = theme.accent
                )
            )
            Spacer(Modifier.height(12.dp))
        }
    }

    AnimatedVisibility(visible = nudge != null, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
        if (nudge != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(theme.accent.copy(alpha = 0.07f))
                    .border(0.5.dp, theme.accent.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Text("💡 $nudge", style = MaterialTheme.typography.bodySmall, color = theme.textPrimary, lineHeight = 18.sp)
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                HapticEngine.tick(ctx); showWhyThis = !showWhyThis
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Why this goal?", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium), color = theme.accent.copy(0.7f))
        Spacer(Modifier.width(4.dp))
        Text(if (showWhyThis) "▲" else "▼", fontSize = 10.sp, color = theme.accent.copy(0.45f))
    }

    AnimatedVisibility(visible = showWhyThis, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(theme.accent.copy(alpha = 0.05f))
                .border(0.5.dp, theme.accent.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Text(whyThisText(viewModel), style = MaterialTheme.typography.bodySmall, color = theme.textPrimary, lineHeight = 18.sp)
        }
    }

    Spacer(Modifier.height(8.dp))

    AnimatedVisibility(visible = confirmText != null, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
        if (confirmText != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(theme.accent.copy(alpha = 0.1f), theme.accent.copy(alpha = 0.06f))))
                    .border(0.5.dp, theme.accent.copy(alpha = 0.22f), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text("✨ ${confirmText!!}", style = MaterialTheme.typography.bodySmall, color = theme.textPrimary, lineHeight = 18.sp)
            }
        }
    }

    var ctaDown by remember { mutableStateOf(false) }
    val ctaScale by animateFloatAsState(
        targetValue = if (ctaDown) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "ctaScale"
    )
    val ctaOffsetY by animateDpAsState(
        targetValue = if (ctaDown) 5.dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "ctaOffsetY"
    )
    val ctaDarken by animateFloatAsState(
        targetValue = if (ctaDown) 0.22f else 0f,
        animationSpec = tween(60),
        label = "ctaDarken"
    )
    val ctaShadow by animateDpAsState(
        targetValue = if (ctaDown) 1.dp else 14.dp,
        animationSpec = tween(60),
        label = "ctaShadow"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 22.dp)
            .offset(y = ctaOffsetY)
            .graphicsLayer { scaleX = ctaScale; scaleY = ctaScale }
            .shadow(
                elevation = ctaShadow,
                shape = RoundedCornerShape(18.dp),
                ambientColor = theme.accent.copy(alpha = 0.45f),
                spotColor = theme.accent.copy(alpha = 0.45f)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.horizontalGradient(listOf(theme.accent, theme.accent.copy(alpha = 0.78f))))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        ctaDown = true
                        val released = tryAwaitRelease()
                        ctaDown = false
                        if (released) {
                            HapticEngine.heavyClick(ctx)
                            confirmText = flowConfirmText(viewModel, displayGoalMl)
                        }
                    }
                )
            }
            .padding(vertical = 17.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Set Daily Flow", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold), color = Color.White)
        if (ctaDarken > 0f) {
            Box(Modifier.matchParentSize().background(Color.Black.copy(alpha = ctaDarken)))
        }
    }

    val goals = viewModel.goals
    val balanceItems = listOf(
        BalanceItem("caffeine", "☕", "Caffeine", "${goals.caffeineMg}mg", "Daily caffeine limit based on your age and health profile. Keeping within this range helps protect sleep quality and heart rhythm."),
        BalanceItem("sugar", "🍬", "Sugar", "${goals.sugarG}g", "Recommended daily free-sugar intake aligned with WHO guidelines. This includes sugars added to food and drinks."),
        BalanceItem("calcium", "🥛", "Calcium", "${goals.calciumMg}mg", "Calcium target adjusted for your age and gender. Essential for bone density, muscle function, and nerve signalling."),
        BalanceItem("alcohol", "🍷", "Alcohol", "${goals.alcoholG}g", "UK Chief Medical Officers' low-risk drinking guideline. Staying within this limit significantly reduces long-term health risks.")
    )

    Text(
        "Balance Targets".uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp),
        color = theme.accent.copy(alpha = 0.6f),
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 6.dp)
    )
    FrostedGlass(modifier = Modifier.fillMaxWidth(), theme = theme, padding = PaddingValues(0.dp)) {
        Column(Modifier.padding(vertical = 4.dp)) {
            balanceItems.forEachIndexed { i, item ->
                BalanceTargetRow(
                    item = item,
                    isExpanded = expandedBalance == item.id,
                    theme = theme,
                    onToggle = { HapticEngine.tick(ctx); expandedBalance = if (expandedBalance == item.id) null else item.id }
                )
                if (i < balanceItems.lastIndex) {
                    HorizontalDivider(color = theme.accent.copy(0.05f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

private fun flowIdentityLabel(mem: HydraViewModel.HydraMemory): String = when {
    mem.consecutiveHit >= 7 -> "Hydration Goddess ✨"
    mem.consecutiveHit >= 3 -> "Flow State 💧"
    mem.above85Count14Days >= 10 -> "Consistent 🌊"
    mem.trend == HydraViewModel.Trend.IMPROVING -> "Rising 📈"
    mem.below60Count7Days >= 4 -> "Getting Started 🌱"
    mem.trend == HydraViewModel.Trend.DECLINING -> "Rebuilding 🔄"
    else -> "In Progress 💫"
}

private fun goalMicrocopy(mem: HydraViewModel.HydraMemory): String = when {
    mem.consecutiveHit >= 7 -> "7-day streak — you're unstoppable"
    mem.consecutiveHit >= 3 -> "${mem.consecutiveHit} days in a row — keep flowing"
    mem.trend == HydraViewModel.Trend.IMPROVING -> "Trending up — your body thanks you"
    mem.below60Count7Days >= 3 -> "A gentle reset — every sip counts"
    mem.avg7Days >= 90f -> "Almost perfect average this week"
    mem.avg7Days >= 75f -> "Good momentum — stay consistent"
    else -> "Based on your body and lifestyle"
}

private fun smartNudge(vm: HydraViewModel, mem: HydraViewModel.HydraMemory): String? = when {
    mem.consecutiveMissed >= 3 -> "You've missed your goal ${mem.consecutiveMissed} days running. A slightly smaller target might help rebuild the habit."
    mem.below60Count7Days >= 4 -> "Only ${7 - mem.below60Count7Days} strong days this week. A smaller goal today might give you the win you need."
    mem.consecutiveHit >= 5 && vm.calculatedGoals.waterMl > 2500 -> "5-day streak and going strong — you might be ready to push the goal higher."
    mem.trend == HydraViewModel.Trend.DECLINING && mem.avg7Days < 70f -> "Your average has been dipping lately. Tap Auto to recalibrate to your smart target."
    else -> null
}

private fun whyThisText(vm: HydraViewModel): String {
    val goals = vm.calculatedGoals
    val p = vm.profile
    return buildString {
        append("Your smart goal of ${vm.convertDisplay(goals.waterMl)} is calculated from ")
        append("your weight (${p.weightKg.toInt()}kg × 35 ml/kg), ")
        append("activity level (${p.activityLevel.label.lowercase()}), ")
        append("and climate (${p.climate.label.lowercase()}). ")
        if (p.age >= 60) append("An age adjustment has been applied. ")
        append("This is your body's optimum — not the minimum.")
    }
}

private fun flowConfirmText(vm: HydraViewModel, goalMl: Int): String {
    val display = vm.convertDisplay(goalMl)
    val name = vm.profile.name.ifBlank { "lovely" }
    return when (vm.profile.personality) {
        Personality.BESTIE -> "Okay YES! $display is SO your vibe, $name — let's do this bestie! 💕"
        Personality.SLAY_QUEEN -> "Darling, $display? That's ICONIC. Your hydration era starts NOW. 👑"
        Personality.SOFT_GIRL -> "That's perfect, $name 🧸 $display feels so right for you. I believe in you."
        Personality.VILLAIN_ERA -> "Fine, $display it is. Don't make me regret trusting you. 😈"
        Personality.ZEN_MASTER -> "$display. A mindful choice. Let the flow carry you forward. 🧘‍♀️"
        Personality.COACH -> "Locked in. $display is your target — no excuses, just execution. 💪"
        Personality.HEARTBREAK_ERA -> "I set it to $display for us. Let's take care of ourselves, okay? 💔"
        Personality.COLD_CEO -> "$display. Logged. Expectations set. Deliver. 🖤"
    }
}

@Composable
private fun FlowPresetChip(
    label: String,
    isSelected: Boolean,
    isRecommended: Boolean,
    theme: HydraTheme,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val ctx = LocalContext.current
    val bgAlpha by animateFloatAsState(if (isSelected) 0.22f else 0.06f, tween(150), label = "chipBg")
    val borderAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0.5f else if (isRecommended) 0.3f else 0.12f,
        animationSpec = tween(150),
        label = "chipBorder"
    )

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(theme.accent.copy(alpha = bgAlpha))
            .border(0.5.dp, theme.accent.copy(alpha = borderAlpha), RoundedCornerShape(14.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { HapticEngine.tick(ctx); onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium),
            color = if (isSelected) theme.accent else theme.textSoft,
            textAlign = TextAlign.Center
        )
        if (isRecommended) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 5.dp, end = 5.dp)
                    .size(5.dp)
                    .background(theme.accent.copy(alpha = 0.75f), CircleShape)
            )
        }
    }
}

@Composable
private fun BalanceTargetRow(item: BalanceItem, isExpanded: Boolean, theme: HydraTheme, onToggle: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onToggle() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(item.emoji, fontSize = 18.sp, modifier = Modifier.width(30.dp))
            Text(item.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = theme.textPrimary, modifier = Modifier.weight(1f))
            Text(item.value, color = theme.accent, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(8.dp))
            Text(if (isExpanded) "▲" else "▼", fontSize = 10.sp, color = theme.accent.copy(0.4f))
        }
        AnimatedVisibility(visible = isExpanded, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
            Text(item.explanation, style = MaterialTheme.typography.bodySmall, color = theme.textSoft, modifier = Modifier.padding(top = 6.dp, start = 30.dp), lineHeight = 17.sp)
        }
    }
}

private data class BalanceItem(val id: String, val emoji: String, val name: String, val value: String, val explanation: String)

@Composable
private fun SectionHeader(title: String, subtitle: String?, theme: HydraTheme) {
    Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal), color = theme.accent)
    if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodySmall, color = theme.textSoft, modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)) else Spacer(Modifier.height(10.dp))
}

@Composable
private fun SectionLabel(text: String, theme: HydraTheme) {
    Text(text.uppercase(), style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp), color = theme.accent.copy(alpha = 0.6f), fontWeight = FontWeight.SemiBold)
}

@Composable
private fun SelectionSegment(text: String, selected: Boolean, theme: HydraTheme, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val ctx = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "segScale")
    val bgAlpha by animateFloatAsState(if (selected) 0.16f else 0.05f, tween(150))

    Box(
        modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale }.clip(RoundedCornerShape(12.dp)).background(theme.accent.copy(alpha = bgAlpha)).border(0.5.dp, theme.accent.copy(alpha = if (selected) 0.35f else 0.1f), RoundedCornerShape(12.dp)).clickable(interactionSource = interactionSource, indication = null) { HapticEngine.tick(ctx); onClick() }
            .padding(horizontal = 4.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium), color = if (selected) theme.accent else theme.textSoft, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
    }
}

@Composable
private fun SettingsSwitchRow(label: String, checked: Boolean, theme: HydraTheme, enabled: Boolean = true, onToggle: (Boolean) -> Unit) {
    FrostedGlass(modifier = Modifier.fillMaxWidth().alpha(if (enabled) 1f else 0.5f), theme = theme, padding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = theme.textPrimary, modifier = Modifier.weight(1f))
            Switch(checked = checked, onCheckedChange = onToggle, enabled = enabled, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = theme.accent, uncheckedThumbColor = theme.textMuted, uncheckedTrackColor = theme.accent.copy(0.12f)))
        }
    }
}

@Composable
private fun SettingsSwitchRowInternal(label: String, checked: Boolean, theme: HydraTheme, enabled: Boolean = true, onToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().alpha(if (enabled) 1f else 0.5f).padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = theme.textPrimary, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onToggle, enabled = enabled, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = theme.accent, uncheckedThumbColor = theme.textMuted, uncheckedTrackColor = theme.accent.copy(0.12f)))
    }
}

@Composable
private fun PremiumModal(theme: HydraTheme, onDismiss: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect { window?.setDimAmount(0.4f) }
        Box(
            Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)).clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Column(Modifier.fillMaxWidth(0.9f).wrapContentHeight().clickable(enabled = false) {}.clip(RoundedCornerShape(28.dp)).background(theme.bg1).border(1.dp, Color.White.copy(0.2f), RoundedCornerShape(28.dp)).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, content = content)
        }
    }
}

private val ITEM_H = 44.dp
private const val SIDE_ITEMS = 2
private val PICKER_H = ITEM_H * (SIDE_ITEMS * 2 + 1)

@Composable
private fun WheelPickerSheet(title: String, emoji: String, value: Int, min: Int, max: Int, unit: String, theme: HydraTheme, onConfirm: (Int) -> Unit, onDismiss: () -> Unit) {
    val items = remember(min, max) { (min..max).map { it.toString() } }
    var selIdx by remember { mutableStateOf((value - min).coerceIn(0, items.lastIndex)) }
    PremiumModal(theme, onDismiss) {
        Text("$emoji  $title", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = theme.accent)
        Spacer(Modifier.height(20.dp))
        WheelPickerInternal(items, selIdx, theme) { selIdx = it }
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, theme.accent.copy(0.2f))) { Text("Cancel", color = theme.textSoft) }
            AddictiveButton("Confirm", { onConfirm(selIdx + min) }, modifier = Modifier.weight(2f), theme = theme, colour = theme.accent, isPrimary = true, cornerRadius = 14.dp)
        }
    }
}

@Composable
private fun BirthdayPickerSheet(current: String, theme: HydraTheme, onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    val parts = current.split("/")
    val years = remember { (1920..Calendar.getInstance().get(Calendar.YEAR)).map { it.toString() } }
    val months = remember { listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec") }
    val days = remember { (1..31).map { "%02d".format(it) } }
    var d by remember { mutableStateOf((parts.getOrNull(0)?.toIntOrNull() ?: 1) - 1) }
    var m by remember { mutableStateOf((parts.getOrNull(1)?.toIntOrNull() ?: 6) - 1) }
    var y by remember { mutableStateOf(((parts.getOrNull(2)?.toIntOrNull() ?: 2000) - 1920).coerceIn(0, years.lastIndex)) }

    PremiumModal(theme, onDismiss) {
        Text("🎂  Birthday", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = theme.accent)
        Spacer(Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) { Text("Day", style = MaterialTheme.typography.labelSmall, color = theme.accent.copy(0.6f)); Spacer(Modifier.height(6.dp)); WheelPickerInternal(days, d, theme) { d = it } }
            Column(Modifier.weight(1.3f), horizontalAlignment = Alignment.CenterHorizontally) { Text("Month", style = MaterialTheme.typography.labelSmall, color = theme.accent.copy(0.6f)); Spacer(Modifier.height(6.dp)); WheelPickerInternal(months, m, theme) { m = it } }
            Column(Modifier.weight(1.3f), horizontalAlignment = Alignment.CenterHorizontally) { Text("Year", style = MaterialTheme.typography.labelSmall, color = theme.accent.copy(0.6f)); Spacer(Modifier.height(6.dp)); WheelPickerInternal(years, y, theme) { y = it } }
        }
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, theme.accent.copy(0.2f))) { Text("Cancel", color = theme.textSoft) }
            AddictiveButton("Set Birthday", { onConfirm("%02d/%02d/%04d".format(d + 1, m + 1, y + 1920)) }, modifier = Modifier.weight(2f), theme = theme, colour = theme.accent, isPrimary = true, cornerRadius = 14.dp)
        }
    }
}

@Composable
private fun WheelPickerInternal(items: List<String>, startIndex: Int, theme: HydraTheme, onSelect: (Int) -> Unit) {
    val state = rememberLazyListState(initialFirstVisibleItemIndex = startIndex.coerceIn(0, items.lastIndex))
    val snap = rememberSnapFlingBehavior(state)
    val sel by remember { derivedStateOf { state.firstVisibleItemIndex.coerceIn(0, items.lastIndex) } }
    LaunchedEffect(sel) { if (sel in items.indices) onSelect(sel) }
    LaunchedEffect(state.isScrollInProgress) { if (!state.isScrollInProgress) state.animateScrollToItem(sel.coerceIn(0, items.lastIndex)) }

    Box(modifier = Modifier.height(PICKER_H).fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(Modifier.fillMaxWidth().height(ITEM_H).clip(RoundedCornerShape(14.dp)).background(theme.accent.copy(alpha = 0.1f)).border(1.dp, theme.accent.copy(alpha = 0.2f), RoundedCornerShape(14.dp)))
        LazyColumn(modifier = Modifier.fillMaxSize(), state = state, contentPadding = PaddingValues(vertical = ITEM_H * SIDE_ITEMS), horizontalAlignment = Alignment.CenterHorizontally, flingBehavior = snap) {
            itemsIndexed(items) { i, text ->
                val dist = abs(i - sel)
                Box(Modifier.height(ITEM_H).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text, style = when (dist) { 0 -> MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 18.sp); 1 -> MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium); else -> MaterialTheme.typography.bodySmall }, color = when (dist) { 0 -> theme.accent; 1 -> theme.textPrimary.copy(alpha = 0.6f); else -> theme.textSoft.copy(alpha = 0.3f) }, textAlign = TextAlign.Center)
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(ITEM_H * SIDE_ITEMS).align(Alignment.TopCenter).background(Brush.verticalGradient(listOf(theme.bg1.copy(alpha = 0.9f), Color.Transparent))))
        Box(Modifier.fillMaxWidth().height(ITEM_H * SIDE_ITEMS).align(Alignment.BottomCenter).background(Brush.verticalGradient(listOf(Color.Transparent, theme.bg1.copy(alpha = 0.9f)))))
    }
}

@Composable
private fun ActivitySelectorSheet(current: ActivityLevel, theme: HydraTheme, onSelect: (ActivityLevel) -> Unit, onDismiss: () -> Unit) {
    PremiumModal(theme, onDismiss) {
        Text("Activity Level", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = theme.accent)
        Spacer(Modifier.height(16.dp))
        ActivityLevel.entries.forEach { level ->
            OptionRow(level.icon, level.label, when (level) { ActivityLevel.SEDENTARY -> "Minimal movement"; ActivityLevel.LIGHTLY_ACTIVE -> "Light movement"; ActivityLevel.MODERATE -> "Regularly active"; ActivityLevel.VERY_ACTIVE -> "Intense training" }, current == level, theme) { onSelect(level) }
            if (level != ActivityLevel.entries.last()) Spacer(Modifier.height(6.dp))
        }
        Spacer(Modifier.height(14.dp)); OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, theme.accent.copy(0.2f))) { Text("Cancel", color = theme.textSoft) }
    }
}

@Composable
private fun ClimateSelectorSheet(current: Climate, theme: HydraTheme, onSelect: (Climate) -> Unit, onDismiss: () -> Unit) {
    PremiumModal(theme, onDismiss) {
        Text("Climate", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = theme.accent)
        Spacer(Modifier.height(16.dp))
        Climate.entries.forEach { c ->
            OptionRow(c.icon, c.label, when (c) { Climate.COLD -> "Cool / Cold"; Climate.MODERATE -> "Comfortable"; Climate.HOT -> "Warm / Hot" }, current == c, theme) { onSelect(c) }
            if (c != Climate.entries.last()) Spacer(Modifier.height(6.dp))
        }
        Spacer(Modifier.height(14.dp)); OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, theme.accent.copy(0.2f))) { Text("Cancel", color = theme.textSoft) }
    }
}

@Composable
private fun OptionRow(icon: String, label: String, desc: String, selected: Boolean, theme: HydraTheme, onClick: () -> Unit) {
    val source = remember { MutableInteractionSource() }
    val isPressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow), label = "optScale")
    val bgAlpha by animateFloatAsState(if (selected) 0.14f else 0.05f, tween(150))
    Row(modifier = Modifier.fillMaxWidth().graphicsLayer { scaleX = scale; scaleY = scale }.clip(RoundedCornerShape(16.dp)).background(theme.accent.copy(alpha = bgAlpha)).border(if (selected) 1.dp else 0.5.dp, theme.accent.copy(alpha = if (selected) 0.4f else 0.1f), RoundedCornerShape(16.dp)).clickable(interactionSource = source, indication = null, onClick = onClick).padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontSize = 20.sp); Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium), color = if (selected) theme.textPrimary else theme.textSoft)
            Text(desc, style = MaterialTheme.typography.bodySmall, color = theme.textSoft.copy(0.7f))
        }
        if (selected) Box(Modifier.size(20.dp).clip(CircleShape).background(theme.accent.copy(0.15f)), contentAlignment = Alignment.Center) { Text("✓", color = theme.accent, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
    }
}

private fun calculateAgeFromBirthDate(birthDate: String): Int? {
    if (birthDate.isBlank()) return null
    val parts = birthDate.split("/")
    if (parts.size != 3) return null
    val day = parts[0].toIntOrNull() ?: return null
    val month = parts[1].toIntOrNull() ?: return null
    val year = parts[2].toIntOrNull() ?: return null
    val today = Calendar.getInstance()
    val birth = Calendar.getInstance().apply { set(year, month - 1, day) }
    var age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) age--
    return age.coerceAtLeast(0)
}

@Composable
private fun ResetHydraDialog(theme: HydraTheme, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    var phase by remember { mutableIntStateOf(0) }
    val roseRed = Color(0xFFC0525E)

    PremiumModal(theme = theme, onDismiss = onDismiss) {
        AnimatedContent(
            targetState = phase,
            transitionSpec = {
                (fadeIn(tween(220)) + slideInHorizontally(tween(220)) { it / 8 }) togetherWith
                    (fadeOut(tween(140)) + slideOutHorizontally(tween(140)) { -it / 8 })
            },
            label = "resetPhase"
        ) { p ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (p == 0) {
                    Text("🌊", fontSize = 38.sp, modifier = Modifier.padding(bottom = 10.dp))
                    Text(
                        "Reset Hydra?",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = theme.accent,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "This will permanently erase everything — your HP, streaks, hydration history, logged drinks, tracked patterns, unlocked personalities, and all customisations. Hydra will wake up as if it's the very first day.",
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.textSoft,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    AddictiveButton(
                        text = "I Understand — Continue",
                        onClick = { phase = 1 },
                        modifier = Modifier.fillMaxWidth(),
                        theme = theme,
                        colour = roseRed,
                        isPrimary = false
                    )
                    Spacer(Modifier.height(8.dp))
                    AddictiveButton(text = "Keep My Progress", onClick = onDismiss, modifier = Modifier.fillMaxWidth(), theme = theme, isPrimary = false)
                } else {
                    Text("💔", fontSize = 38.sp, modifier = Modifier.padding(bottom = 10.dp))
                    Text(
                        "This Cannot Be Undone",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = roseRed,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Every sip, every streak, every moment you and Hydra have shared will be gone. Are you absolutely sure you want to start completely over?",
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.textSoft,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    AddictiveButton(
                        text = "Reset Hydra Forever",
                        onClick = onConfirm,
                        modifier = Modifier.fillMaxWidth(),
                        theme = theme,
                        colour = roseRed,
                        isPrimary = true
                    )
                    Spacer(Modifier.height(8.dp))
                    AddictiveButton(text = "Actually, Keep It", onClick = onDismiss, modifier = Modifier.fillMaxWidth(), theme = theme, isPrimary = false)
                }
            }
        }
    }
}

@Composable
private fun GeneralSettingsSection(viewModel: HydraViewModel, theme: HydraTheme) {
    var showLangSheet by remember { mutableStateOf(false) }
    var hydraReacting by remember { mutableStateOf(false) }
    var reactionTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(reactionTrigger) {
        if (reactionTrigger > 0) {
            hydraReacting = true
            kotlinx.coroutines.delay(1400)
            hydraReacting = false
        }
    }

    fun triggerReaction() { reactionTrigger++ }

    Column(modifier = Modifier.fillMaxWidth()) {
        GeneralHeroSection(
            viewModel = viewModel,
            theme = theme,
            reacting = hydraReacting
        )

        Spacer(Modifier.height(28.dp))
        GeneralInteractionStyleSection(viewModel, theme) { triggerReaction() }

        Spacer(Modifier.height(36.dp))
        GeneralExperienceSection(
            viewModel = viewModel,
            theme = theme,
            onChanged = { triggerReaction() }
        )

        Spacer(Modifier.height(36.dp))
        GeneralDeviceBehaviourSection(viewModel, theme) { triggerReaction() }

        Spacer(Modifier.height(36.dp))
        GeneralLanguageSection(viewModel, theme) { showLangSheet = true }

        if (showLangSheet) {
            LanguageBottomSheet(
                viewModel = viewModel,
                theme = theme,
                onDismiss = { showLangSheet = false },
                onChanged = { triggerReaction() }
            )
        }

        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun GeneralHeroSection(viewModel: HydraViewModel, theme: HydraTheme, reacting: Boolean) {
    val glowInf = rememberInfiniteTransition(label = "genHeroGlow")
    val glowAlpha by glowInf.animateFloat(
        0.18f, 0.42f,
        infiniteRepeatable(tween(3200, easing = EaseInOutSine), RepeatMode.Reverse), label = "ghGA"
    )
    val glowScale by glowInf.animateFloat(
        0.85f, 1.15f,
        infiniteRepeatable(tween(3200, easing = EaseInOutSine), RepeatMode.Reverse), label = "ghGS"
    )
    val ringAlpha by glowInf.animateFloat(
        0.06f, 0.18f,
        infiniteRepeatable(tween(4800, easing = EaseInOutSine), RepeatMode.Reverse), label = "ghRA"
    )

    val modeBase = when (viewModel.performanceMode) {
        "battery_saver" -> 0.3f
        "high_performance" -> 1f
        else -> 0.62f
    }
    val interactionBoost = (viewModel.interactionIntensity / 3f).coerceIn(0f, 1f)
    val visualMul = if (viewModel.visualEffectsEnabled) 1f else 0.38f
    val calmMul = if (viewModel.reduceMotion) 0.68f else 1f
    val reactBurst by animateFloatAsState(
        targetValue = if (reacting) 1f else 0f,
        animationSpec = tween(if (reacting) 120 else 720),
        label = "gReact"
    )
    val rawEnergy = (0.86f * modeBase * (0.6f * interactionBoost + 0.4f) * visualMul * calmMul + 0.14f).coerceIn(0.1f, 1.2f)
    val energy by animateFloatAsState(rawEnergy + reactBurst * 0.18f, spring(0.92f, 200f), label = "gEnergy")

    val displayMood = if (reacting) HydraViewModel.HydraMood.HAPPY else viewModel.hydraMood
    val heroMessage = when {
        reacting -> stringResource(R.string.general_hydra_msg_celebrate)
        viewModel.interactionIntensity == 0 -> stringResource(R.string.general_hydra_msg_off)
        viewModel.interactionIntensity == 1 -> stringResource(R.string.general_hydra_msg_soft)
        viewModel.interactionIntensity == 2 -> stringResource(R.string.general_hydra_msg_balanced)
        else -> stringResource(R.string.general_hydra_msg_full_intensity)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0f to Color.Transparent,
                    0.25f to theme.accent.copy(alpha = 0.07f),
                    0.6f to theme.accent.copy(alpha = 0.07f),
                    1f to Color.Transparent
                )
            )
            .padding(top = 8.dp, bottom = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.settings_title_general),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal),
                        color = theme.accent
                    )
                    Text(
                        stringResource(R.string.general_screen_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.textSoft.copy(alpha = 0.65f),
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            Box(contentAlignment = Alignment.Center) {
                Canvas(Modifier.size(180.dp)) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val pulse = energy.coerceIn(0f, 1.2f)
                    drawCircle(
                        Brush.radialGradient(
                            listOf(theme.glow.copy(alpha = ringAlpha * 0.6f * pulse), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = size.width * 0.5f * glowScale
                        ),
                        radius = size.width * 0.5f * glowScale,
                        center = Offset(cx, cy)
                    )
                    drawCircle(
                        Brush.radialGradient(
                            listOf(theme.accent.copy(alpha = glowAlpha * pulse), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = size.width * 0.38f
                        ),
                        radius = size.width * 0.38f,
                        center = Offset(cx, cy)
                    )
                }
                Box(modifier = Modifier.size(120.dp)) {
                    HydraHeartCanvas(
                        fillPercent = viewModel.waterPercent,
                        mood = displayMood,
                        theme = theme,
                        showFace = viewModel.showFace,
                        hydraShape = viewModel.hydraShape,
                        drinkColours = viewModel.drinkColourPortions,
                        level = viewModel.levelInfo.level,
                        showRainbowShimmer = viewModel.showRainbowShimmer,
                        showButterfly = viewModel.showButterfly,
                        showGoldenGlow = viewModel.showGoldenGlow,
                        showNecklace = false,
                        equippedAccessories = emptySet(),
                        size = 120.dp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            AnimatedContent(
                targetState = heroMessage,
                modifier = Modifier.fillMaxWidth(),
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
                label = "heroMsg"
            ) { msg ->
                Text(
                    msg,
                    style = MaterialTheme.typography.bodySmall.copy(letterSpacing = 0.2.sp),
                    color = theme.textSoft.copy(alpha = 0.82f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun GeneralInteractionStyleSection(viewModel: HydraViewModel, theme: HydraTheme, onChanged: () -> Unit) {
    val ctx = LocalContext.current
    val options = listOf(
        stringResource(R.string.general_interaction_off) to "○",
        stringResource(R.string.general_interaction_soft) to "◔",
        stringResource(R.string.general_interaction_balanced) to "◑",
        stringResource(R.string.general_interaction_full) to "●"
    )

    LiquidSectionHeader(
        title = stringResource(R.string.general_section_interaction),
        theme = theme,
        subtitle = stringResource(R.string.general_interaction_hint),
        subtitleBottomSpace = 14.dp
    )

    AnimatedContent(
        targetState = viewModel.interactionIntensity,
        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
        label = "intDesc"
    ) { level ->
        Text(
            when (level) {
                0 -> stringResource(R.string.general_interaction_desc_off)
                1 -> stringResource(R.string.general_interaction_desc_soft)
                2 -> stringResource(R.string.general_interaction_desc_balanced)
                else -> stringResource(R.string.general_interaction_desc_full)
            },
            style = MaterialTheme.typography.bodySmall.copy(letterSpacing = 0.sp, lineHeight = 18.sp),
            color = theme.textSoft.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 6.dp)
        )
    }

    Spacer(Modifier.height(14.dp))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.chunked(2).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { pair ->
                    val idx = options.indexOf(pair)
                    val (label, glyph) = pair
                    ReminderStylePillChip(
                        label = label,
                        selected = viewModel.interactionIntensity == idx,
                        theme = theme,
                        modifier = Modifier.weight(1f),
                        leadingGlyph = glyph,
                        onClick = {
                            HapticEngine.tick(ctx)
                            viewModel.interactionIntensity = idx
                            viewModel.hapticsEnabled = idx > 0
                            viewModel.soundEffectsEnabled = idx > 1
                            viewModel.visualEffectsEnabled = idx > 1
                            viewModel.fullGlowEnabled = idx == 3
                            onChanged()
                        }
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun GeneralExperienceSection(viewModel: HydraViewModel, theme: HydraTheme, onChanged: () -> Unit) {
    val ctx = LocalContext.current

    fun commitMode(mode: String) {
        viewModel.performanceMode = mode
        viewModel.particlesEnabled = mode != "battery_saver"
        viewModel.animationsEnabled = mode != "battery_saver"
        viewModel.fullGlowEnabled = mode == "high_performance"
        HapticEngine.tick(ctx)
        onChanged()
    }

    LiquidSectionHeader(
        title = stringResource(R.string.general_section_experience),
        theme = theme,
    )

    Spacer(Modifier.height(16.dp))
    AnimatedContent(
        targetState = viewModel.performanceMode,
        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
        label = "expDesc"
    ) { mode ->
        Column(Modifier.padding(start = 6.dp)) {
            Text(
                when (mode) {
                    "battery_saver" -> stringResource(R.string.general_experience_saver)
                    "high_performance" -> stringResource(R.string.general_experience_full)
                    else -> stringResource(R.string.general_experience_balanced)
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, letterSpacing = (-0.15).sp),
                color = theme.textPrimary
            )
            Text(
                when (mode) {
                    "battery_saver" -> stringResource(R.string.general_experience_desc_saver)
                    "high_performance" -> stringResource(R.string.general_experience_desc_full)
                    else -> stringResource(R.string.general_experience_desc_balanced)
                },
                style = MaterialTheme.typography.bodySmall.copy(letterSpacing = 0.sp, lineHeight = 18.sp),
                color = theme.textSoft.copy(alpha = 0.55f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }

    Spacer(Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            Triple("battery_saver", stringResource(R.string.general_experience_saver), "🔋"),
            Triple("standard", stringResource(R.string.general_experience_balanced), "⚖️"),
            Triple("high_performance", stringResource(R.string.general_experience_full), "✨")
        ).forEach { (mode, label, glyph) ->
            ReminderStylePillChip(
                label = label,
                selected = viewModel.performanceMode == mode,
                theme = theme,
                modifier = Modifier.weight(1f),
                leadingGlyph = glyph,
                onClick = { commitMode(mode) }
            )
        }
    }
}

@Composable
private fun GeneralDeviceBehaviourSection(viewModel: HydraViewModel, theme: HydraTheme, onChanged: () -> Unit) {
    val ctx = LocalContext.current
    LiquidSectionHeader(
        title = stringResource(R.string.general_section_device),
        theme = theme,
    )
    Spacer(Modifier.height(16.dp))
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        AdvancedToggleRow(
            label = stringResource(R.string.general_device_keep_awake_title),
            sublabel = stringResource(R.string.general_device_keep_awake_desc),
            checked = viewModel.keepScreenOn,
            theme = theme,
            onToggle = { HapticEngine.tick(ctx); viewModel.keepScreenOn = it; onChanged() }
        )
        AdvancedToggleRow(
            label = stringResource(R.string.general_device_reduce_motion_title),
            sublabel = stringResource(R.string.general_device_reduce_motion_desc),
            checked = viewModel.reduceMotion,
            theme = theme,
            onToggle = { HapticEngine.tick(ctx); viewModel.reduceMotion = it; onChanged() }
        )
        AdvancedToggleRow(
            label = stringResource(R.string.general_device_battery_title),
            sublabel = stringResource(R.string.general_device_battery_desc),
            checked = viewModel.batteryAwareMode,
            theme = theme,
            onToggle = { HapticEngine.tick(ctx); viewModel.batteryAwareMode = it; onChanged() }
        )
    }
}

@Composable
private fun GeneralLanguageSection(viewModel: HydraViewModel, theme: HydraTheme, onOpenSheet: () -> Unit) {
    val currentLabel = when (viewModel.appLanguage) {
        "en_uk" -> stringResource(R.string.lang_en_uk)
        "en_us" -> stringResource(R.string.lang_en_us)
        "es" -> stringResource(R.string.lang_es)
        "de" -> stringResource(R.string.lang_de)
        "system" -> stringResource(R.string.lang_system)
        else -> stringResource(R.string.lang_system)
    }

    LiquidSectionHeader(
        title = stringResource(R.string.general_section_language),
        theme = theme,
    )
    Spacer(Modifier.height(12.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(theme.accent.copy(alpha = 0.04f))
            .border(0.5.dp, theme.accent.copy(alpha = 0.09f), RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onOpenSheet
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                stringResource(R.string.general_language_row_title),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = theme.textPrimary.copy(alpha = 0.85f)
            )
            Spacer(Modifier.height(2.dp))
            AnimatedContent(
                targetState = currentLabel,
                transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(170)) },
                label = "langCurrent"
            ) { lbl ->
                Text(
                    lbl,
                    style = MaterialTheme.typography.bodySmall.copy(letterSpacing = 0.sp),
                    color = theme.textMuted.copy(alpha = 0.65f)
                )
            }
        }
        Text("›", fontSize = 22.sp, color = theme.accent.copy(alpha = 0.4f), fontWeight = FontWeight.Light)
    }
}

@Composable
private fun LanguageBottomSheet(viewModel: HydraViewModel, theme: HydraTheme, onDismiss: () -> Unit, onChanged: () -> Unit) {
    val ctx = LocalContext.current
    var visible by remember { mutableStateOf(true) }

    data class LangOption(val code: String, val flag: String, val label: String)
    val langOptions = listOf(
        LangOption("en_uk", "🇬🇧", stringResource(R.string.lang_en_uk)),
        LangOption("en_us", "🇺🇸", stringResource(R.string.lang_en_us)),
        LangOption("es", "🇪🇸", stringResource(R.string.lang_es)),
        LangOption("de", "🇩🇪", stringResource(R.string.lang_de)),
        LangOption("system", "🌐", stringResource(R.string.lang_system))
    )

    LaunchedEffect(visible) {
        if (!visible) {
            kotlinx.coroutines.delay(260)
            onDismiss()
        }
    }

    Dialog(
        onDismissRequest = { visible = false },
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            AnimatedVisibility(visible = visible, enter = fadeIn(tween(200)), exit = fadeOut(tween(260))) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.48f))
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { visible = false }
                )
            }

            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(tween(340, easing = FastOutSlowInEasing)) { it } + fadeIn(tween(260)),
                exit = slideOutVertically(tween(240)) { it } + fadeOut(tween(180))
            ) {
                val sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.78f)
                        .clip(sheetShape)
                        .background(Brush.verticalGradient(listOf(theme.bg1.copy(alpha = 0.99f), theme.bg2.copy(alpha = 0.99f))))
                        .border(0.6.dp, Brush.verticalGradient(listOf(theme.accent.copy(alpha = 0.28f), Color.Transparent)), sheetShape)
                        .navigationBarsPadding()
                ) {
                    Box(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 12.dp, bottom = 6.dp)
                            .size(40.dp, 4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(theme.accent.copy(alpha = 0.22f))
                    )

                    Column(Modifier.padding(start = 22.dp, top = 10.dp, end = 22.dp, bottom = 6.dp)) {
                        Text(
                            stringResource(R.string.general_language_sheet_title),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.3).sp),
                            color = theme.accent
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Choose how Hydra speaks to you",
                            style = MaterialTheme.typography.bodyMedium.copy(letterSpacing = 0.sp),
                            color = theme.textPrimary.copy(alpha = 0.52f)
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 18.dp)
                            .padding(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        langOptions.forEach { opt ->
                            val selected = viewModel.appLanguage == opt.code
                            LangOptionRow(opt.flag, opt.label, selected, theme) {
                                HapticEngine.click(ctx)
                                viewModel.appLanguage = opt.code
                                AppLocale.applyAppLanguage(opt.code)
                                onChanged()
                                visible = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LangOptionRow(flag: String, label: String, selected: Boolean, theme: HydraTheme, onClick: () -> Unit) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val pulse = remember { Animatable(0f) }
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = (if (pressed) 0.974f else 1f) * (pulse.value * 0.018f + 1f),
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 10000f),
        label = "langRowScale"
    )
    val bgAlpha by animateFloatAsState(if (selected) 0.18f else 0.05f, tween(260), label = "langRowBg")
    val borderAlpha by animateFloatAsState(if (selected) 0.6f else 0.14f, tween(260), label = "langRowBd")
    val borderWidth by animateFloatAsState(if (selected) 1.2f else 0.5f, tween(260), label = "langRowBw")
    val shadowElev by animateDpAsState(if (selected) 10.dp else 0.dp, tween(280), label = "langRowSh")
    val glowR = if (selected) pulse.value * 0.4f + 0.38f else pulse.value * 0.08f

    fun triggerPulse() {
        HapticEngine.click(ctx)
        scope.launch {
            pulse.snapTo(0f)
            pulse.animateTo(1f, tween(80, easing = FastOutSlowInEasing))
            pulse.animateTo(0f, tween(280, easing = LinearOutSlowInEasing))
        }
    }

    val rowShape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(
                elevation = shadowElev + (pulse.value * 8f).dp,
                shape = rowShape,
                ambientColor = theme.accent.copy(alpha = pulse.value * 0.14f + 0.12f),
                spotColor = theme.accent.copy(alpha = pulse.value * 0.22f + 0.2f)
            )
            .clip(rowShape)
            .drawBehind {
                if (glowR > 0.01f) {
                    drawRoundRect(
                        Brush.radialGradient(
                            listOf(theme.accent.copy(alpha = 0.18f * glowR), theme.glow.copy(alpha = glowR * 0.08f), Color.Transparent),
                            center = Offset(size.width * 0.12f, size.height * 0.5f),
                            radius = 0.9f * size.width
                        ),
                        cornerRadius = CornerRadius(20.dp.toPx())
                    )
                }
            }
            .background(theme.accent.copy(alpha = bgAlpha))
            .border(borderWidth.dp, theme.accent.copy(alpha = borderAlpha), rowShape)
            .clickable(interactionSource = source, indication = null) { triggerPulse(); onClick() }
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(flag, fontSize = 26.sp)
            Spacer(Modifier.width(16.dp))
            Text(
                label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium, fontSize = 16.sp),
                color = if (selected) theme.accent else theme.textPrimary.copy(alpha = 0.88f)
            )
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(tween(180)) + scaleIn(tween(200), initialScale = 0.7f),
                exit = fadeOut(tween(120)) + scaleOut(tween(120), targetScale = 0.85f)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(theme.accent.copy(alpha = 0.2f), CircleShape)
                        .border(1.dp, theme.accent.copy(alpha = 0.55f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = theme.accent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
