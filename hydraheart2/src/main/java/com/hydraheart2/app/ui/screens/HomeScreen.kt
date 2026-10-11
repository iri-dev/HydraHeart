package com.hydraheart2.app.ui.screens

import android.media.AudioAttributes
import android.media.SoundPool
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.hydraheart2.app.R
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.data.models.Personality
import com.hydraheart2.app.ui.components.AddictiveButton
import com.hydraheart2.app.ui.components.AnimatedCounter
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.ui.components.HydraHeartCanvas
import com.hydraheart2.app.ui.components.HydraSpeechBubble
import com.hydraheart2.app.ui.components.LevelPopup
import com.hydraheart2.app.ui.components.StatPill
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun HomeScreen(viewModel: HydraViewModel, onNavigate: (String) -> Unit) {
    val theme = viewModel.currentTheme
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val inf = rememberInfiniteTransition(label = "amb")
    val particlePhase by inf.animateFloat(
        0f, 100f,
        infiniteRepeatable(tween(10000, easing = LinearEasing), RepeatMode.Restart),
        label = "pp"
    )
    val shimmerX by inf.animateFloat(
        -300f, 800f,
        infiniteRepeatable(tween(3500, easing = LinearEasing), RepeatMode.Restart),
        label = "sh"
    )
    var showCalPopover by remember { mutableStateOf(false) }
    var showFloating by remember { mutableStateOf(false) }
    var floatText by remember { mutableStateOf("") }
    var floatColour by remember { mutableStateOf(Color.White) }

    LaunchedEffect(viewModel.showDrinkAnimation) {
        if (viewModel.showDrinkAnimation) {
            val amt = viewModel.convertMl(viewModel.lastLoggedAmount); val u = viewModel.unitLabel()
            if (viewModel.lastLoggedIsDehydrating) { floatText = "+$amt$u ⚠️"; floatColour = Color(0xFFEF4444) } else { floatText = "+$amt$u"; floatColour = theme.accent }
            showFloating = true; HapticEngine.heavyClick(context); delay(150); HapticEngine.liquidPour(context); delay(1200); showFloating = false; viewModel.dismissDrinkAnimation()
        }
    }

    // Haptic milestones as the heart fills
    val prevPct = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(viewModel.waterPercent) {
        val p = prevPct.floatValue
        val c = viewModel.waterPercent
        if (p < 25f && c >= 25f) HapticEngine.click(context)
        if (p < 50f && c >= 50f) HapticEngine.doubleTap(context)
        if (p < 75f && c >= 75f) HapticEngine.doubleTap(context)
        if (p < 100f && c >= 100f) HapticEngine.celebration(context)
        prevPct.floatValue = c
    }

    LaunchedEffect(viewModel.justHitGoal) {
        if (viewModel.justHitGoal) {
            try {
                val soundPool = SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .build()
                val soundId = soundPool.load(context, Settings.System.DEFAULT_NOTIFICATION_URI.hashCode(), 1)
                soundPool.setOnLoadCompleteListener { _, _, _ ->
                    soundPool.play(soundId, 0.7f, 0.7f, 1, 0, 1f)
                }
            } catch (e: Exception) {
            }
            delay(3000)
            viewModel.dismissCelebration()
        }
    }

    viewModel.pendingUnlockPersonality?.let { unlocked ->
        PersonalityUnlockDialog(
            personality = unlocked,
            theme = theme,
            onSwitchTo = {
                viewModel.pendingUnlockPersonality = null
                viewModel.profile = viewModel.profile.copy(personality = unlocked)
            },
            onKeepCurrent = { viewModel.pendingUnlockPersonality = null }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listOf(theme.bg1, theme.bg2, theme.bg3),
                    start = Offset(0f, 0f),
                    end = Offset(200f, 1200f)
                )
            )
    ) {
        // Drifting ambient particles
        Canvas(Modifier.fillMaxSize()) {
            for (i in 0 until 8) {
                drawCircle(
                    theme.accent.copy(alpha = 0.05f + sin(particlePhase * 0.04f + i) * 0.02f),
                    3f + sin(particlePhase * 0.06f + i) * 2.5f,
                    Offset(
                        size.width * (0.08f + i * 0.12f) + sin(particlePhase * 0.018f + i * 1.3f) * 25f,
                        size.height * (0.06f + i * 0.11f) + sin(particlePhase * 0.025f + i * 2.1f) * 35f
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp)
        ) {
            // Top bar: date card, level badge, settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box {
                    FrostedGlass(
                        theme = theme,
                        cornerRadius = 16.dp,
                        onClick = {
                            if (viewModel.calendarClickAction == "history") {
                                onNavigate("history")
                            } else {
                                showCalPopover = !showCalPopover
                            }
                        },
                        padding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            viewModel.formattedCalDate(),
                            color = theme.accent,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                val levelInteraction = remember { MutableInteractionSource() }
                val levelPressed by levelInteraction.collectIsPressedAsState()
                val lvlS by animateFloatAsState(
                    if (levelPressed) 0.9f else 1f,
                    spring(dampingRatio = 0.5f, stiffness = 1200f),
                    label = "ls"
                )
                Column(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = lvlS
                            scaleY = lvlS
                        }
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(interactionSource = levelInteraction, indication = null) {
                            HapticEngine.click(context)
                            viewModel.showLevelPopup = true
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.home_level, viewModel.levelInfo.level),
                        color = theme.textMuted,
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp)
                    )
                    // Fix: at the top level there is no next threshold (requiredHp is 0), which used
                    // to read "1251148/0 HP". Show the HP total with MAX instead.
                    Text(
                        if (viewModel.levelInfo.requiredHp > 0) {
                            stringResource(R.string.home_hp, viewModel.levelInfo.currentHp, viewModel.levelInfo.requiredHp)
                        } else {
                            stringResource(R.string.home_hp_max, viewModel.levelInfo.totalHp)
                        },
                        color = theme.accent,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(theme.cardBorder.copy(alpha = 0.3f))
                    ) {
                        Box(
                            Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(
                                    if (viewModel.levelInfo.requiredHp > 0) {
                                        viewModel.levelInfo.currentHp.toFloat() / viewModel.levelInfo.requiredHp.toFloat()
                                    } else {
                                        1f
                                    }
                                )
                                .clip(RoundedCornerShape(2.dp))
                                .background(theme.accent)
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        stringResource(R.string.home_tap_to_view),
                        color = theme.textMuted,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp)
                    )
                }

                FrostedGlass(
                    theme = theme,
                    cornerRadius = 14.dp,
                    onClick = { onNavigate("settings") },
                    padding = PaddingValues(12.dp)
                ) {
                    Text("⚙️", fontSize = 20.sp)
                }
            }

            // Hydra
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.showFace && viewModel.showSpeechBubble) {
                    HydraSpeechBubble(
                        text = viewModel.hydraSpeech,
                        styleId = viewModel.speechBubbleStyleId,
                        theme = theme,
                        modifier = Modifier.widthIn(max = 310.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.9f)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HydraHeartCanvas(
                        fillPercent = viewModel.waterPercent,
                        mood = viewModel.hydraMood,
                        theme = theme,
                        showFace = viewModel.showFace,
                        hydraShape = viewModel.hydraShape,
                        drinkColours = viewModel.drinkColourPortions,
                        level = viewModel.levelInfo.level,
                        showNecklace = viewModel.showNecklace,
                        equippedAccessories = viewModel.equippedAccessoryIds.toSet()
                    )
                    // Fully qualified: the ColumnScope overload is not usable from inside this Box.
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showFloating,
                        enter = fadeIn(tween(100)) + slideInVertically(initialOffsetY = { it / 2 }),
                        exit = fadeOut(tween(600)) + slideOutVertically(targetOffsetY = { -it })
                    ) {
                        Text(floatText, color = floatColour, style = MaterialTheme.typography.displaySmall)
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    AnimatedCounter(
                        targetValue = viewModel.convertMl(viewModel.effectiveWater).toIntOrNull()
                            ?: viewModel.effectiveWater,
                        style = MaterialTheme.typography.displayMedium.copy(color = theme.accent3)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "/ " + viewModel.convertMl(viewModel.goals.waterMl) + viewModel.unitLabel(),
                        modifier = Modifier.padding(bottom = 6.dp),
                        color = theme.textSoft,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                if (viewModel.totalDehydrationPenalty > 0) {
                    Spacer(Modifier.height(4.dp))
                    FrostedGlass(
                        theme = theme,
                        cornerRadius = 12.dp,
                        padding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚠️", fontSize = 16.sp)
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(
                                    stringResource(
                                        R.string.home_dehydration_lost,
                                        viewModel.convertMl(viewModel.totalDehydrationPenalty) + viewModel.unitLabel()
                                    ),
                                    color = Color(0xFFEF4444),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    stringResource(R.string.home_dehydration_hint),
                                    color = Color(0xFFEF4444).copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(theme.cardBg.copy(alpha = 0.15f))
                    .border(0.5.dp, theme.cardBorder.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                val progress by animateFloatAsState(
                    viewModel.waterPercent / 100f,
                    spring(dampingRatio = 0.65f, stiffness = 180f),
                    label = "pf"
                )
                Box(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                if (viewModel.waterPercent >= 100f) {
                                    listOf(Color(0xFF4ADE80), Color(0xFF22C55E))
                                } else {
                                    listOf(theme.accent2, theme.accent, theme.accent3)
                                }
                            )
                        )
                        .drawBehind {
                            drawRect(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color.White.copy(alpha = 0.18f), Color.Transparent),
                                    startX = shimmerX,
                                    endX = shimmerX + 250f
                                )
                            )
                        }
                )
                Text(
                    "${viewModel.waterPercent.toInt()}%",
                    color = theme.textPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(Modifier.height(16.dp))
            FrostedGlass(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                theme = theme
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        StatPill(
                            label = "Caffeine",
                            value = viewModel.totalCaffeine.toFloat(),
                            max = viewModel.goals.caffeineMg.toFloat(),
                            unit = "mg",
                            colour = Color(0xFFA0724A),
                            theme = theme,
                            modifier = Modifier.weight(1f)
                        )
                        StatPill(
                            label = "Sugar",
                            value = viewModel.totalSugar,
                            max = viewModel.goals.sugarG.toFloat(),
                            unit = "g",
                            colour = Color(0xFFF472B6),
                            theme = theme,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        StatPill(
                            label = "Calcium",
                            value = viewModel.totalCalcium.toFloat(),
                            max = viewModel.goals.calciumMg.toFloat(),
                            unit = "mg",
                            colour = Color(0xFFFBBF24),
                            theme = theme,
                            modifier = Modifier.weight(1f)
                        )
                        StatPill(
                            label = "Alcohol",
                            value = viewModel.totalAlcohol,
                            max = viewModel.goals.alcoholG.toFloat(),
                            unit = "g",
                            colour = Color(0xFFA855F7),
                            theme = theme,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            FrostedGlass(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                theme = theme
            ) {
                Column {
                    Text(
                        stringResource(R.string.home_electrolytes),
                        color = theme.textSoft,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(Modifier.height(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StatPill(
                                label = "Sodium",
                                value = viewModel.totalSodium.toFloat(),
                                max = viewModel.goals.sodiumMg.toFloat(),
                                unit = "mg",
                                colour = Color(0xFFFB923C),
                                theme = theme,
                                modifier = Modifier.weight(1f)
                            )
                            StatPill(
                                label = "Potassium",
                                value = viewModel.totalPotassium.toFloat(),
                                max = viewModel.goals.potassiumMg.toFloat(),
                                unit = "mg",
                                colour = Color(0xFF4ADE80),
                                theme = theme,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        StatPill(
                            label = "Magnesium",
                            value = viewModel.totalMagnesium.toFloat(),
                            max = viewModel.goals.magnesiumMg.toFloat(),
                            unit = "mg",
                            colour = Color(0xFF60A5FA),
                            theme = theme,
                            modifier = Modifier.fillMaxWidth(0.48f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (viewModel.levelInfo.level >= 5) {
                    FrostedGlass(
                        modifier = Modifier.weight(1f),
                        theme = theme,
                        onClick = { onNavigate("diary") },
                        padding = PaddingValues(horizontal = 10.dp, vertical = 14.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📖", fontSize = 20.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                stringResource(R.string.home_diary),
                                color = theme.textSoft,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                FrostedGlass(
                    modifier = Modifier.weight(1f),
                    theme = theme,
                    padding = PaddingValues(horizontal = 10.dp, vertical = 14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔥", fontSize = 20.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            stringResource(R.string.home_streak, viewModel.streak),
                            color = theme.textSoft,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                FrostedGlass(
                    modifier = Modifier.weight(1f),
                    theme = theme,
                    onClick = { onNavigate("trophies") },
                    padding = PaddingValues(horizontal = 10.dp, vertical = 14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🏆", fontSize = 20.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            stringResource(R.string.home_trophies_count, viewModel.earnedTrophyIds.size),
                            color = theme.textSoft,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            if (viewModel.levelInfo.level < 10) {
                FrostedGlass(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    theme = theme
                ) {
                    Column {
                        Text(
                            stringResource(R.string.home_next_reward, viewModel.levelInfo.level + 1),
                            color = theme.textSoft,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(viewModel.levelReward, color = theme.accent3, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            if (viewModel.todayDrinks.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.home_recent),
                        color = theme.textSoft,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        stringResource(R.string.home_undo),
                        style = MaterialTheme.typography.labelSmall,
                        color = theme.textSoft.copy(alpha = 0.55f),
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { viewModel.undoLastDrink() }
                    )
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(viewModel.todayDrinks.reversed().take(8)) { l ->
                        FrostedGlass(
                            theme = theme,
                            padding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(l.categoryIcon, fontSize = 22.sp)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    l.drink.name,
                                    color = theme.textPrimary,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    "${l.drink.sizeMl}${viewModel.unitLabel()}",
                                    color = theme.textSoft,
                                    style = MaterialTheme.typography.labelSmall
                                )
                                if (l.drink.dehydrationFactor > 0.05f) {
                                    Text("⚠️", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }

        // Calendar tap popover
        if (showCalPopover) {
            Box(
                Modifier
                    .fillMaxSize()
                    .zIndex(50f)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                        showCalPopover = false
                    }
            )
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 20.dp, top = 90.dp)
                    .zIndex(51f)
            ) {
                FrostedGlass(modifier = Modifier.width(220.dp), theme = theme, cornerRadius = 16.dp) {
                    Column {
                        Text(
                            stringResource(R.string.home_calendar_tap_action),
                            color = theme.textPrimary,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(Modifier.height(10.dp))
                        AddictiveButton(
                            text = stringResource(R.string.home_calendar_edit),
                            onClick = {
                                viewModel.pendingSettingsSection = "calendar"
                                showCalPopover = false
                                onNavigate("settings")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp),
                            theme = theme,
                            isPrimary = viewModel.calendarClickAction == "edit",
                            cornerRadius = 12.dp
                        )
                        AddictiveButton(
                            text = stringResource(R.string.home_calendar_history),
                            onClick = {
                                viewModel.calendarClickAction = "history"
                                showCalPopover = false
                                onNavigate("history")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            theme = theme,
                            isPrimary = viewModel.calendarClickAction == "history",
                            cornerRadius = 12.dp
                        )
                    }
                }
            }
        }

        // Goal reached celebration
        AnimatedVisibility(
            visible = viewModel.justHitGoal,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(60f),
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(1000))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉", fontSize = 64.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.home_goal_reached_title),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.home_goal_reached_body),
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        LevelPopup(
            visible = viewModel.showLevelPopup,
            currentLevel = viewModel.levelInfo.level,
            currentHp = viewModel.levelInfo.currentHp,
            totalHp = viewModel.levelInfo.totalHp,
            theme = theme,
            onDismiss = { viewModel.showLevelPopup = false }
        )
    }
}

@Composable
private fun PersonalityUnlockDialog(
    personality: Personality,
    theme: HydraTheme,
    onSwitchTo: () -> Unit,
    onKeepCurrent: () -> Unit
) {
    val ctx = LocalContext.current
    val (acknowledgment, reveal) = when (personality) {
        Personality.HEARTBREAK_ERA -> Pair(
            "I've been watching over you lately, and I see how hard things have been. You've kept showing up, even when it felt like too much. That means everything to me.",
            "Something in me has shifted. I want to be softer with you — more honest, more present. I think you need that right now, and I want to be that for you. 💔"
        )
        Personality.COLD_CEO -> Pair(
            "I've watched you show up, day after day. Consistent. Disciplined. Refusing to let the pattern slip. That's not luck — that's who you're becoming.",
            "You've earned something new. A version of me that matches your standard. No softness. No excuses. Just the highest version of both of us. 🖤"
        )
        else -> Pair("", "")
    }
    var phase by remember { mutableIntStateOf(0) }
    val inf = rememberInfiniteTransition(label = "unlockGlow")
    val glowAlpha by inf.animateFloat(
        0.12f, 0.32f,
        infiniteRepeatable(tween(2000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "ua"
    )
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.verticalGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))
                .drawBehind {
                    drawRoundRect(
                        theme.accent.copy(alpha = glowAlpha),
                        size = size,
                        cornerRadius = CornerRadius(24.dp.toPx())
                    )
                }
                .padding(28.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val iconScale = remember { Animatable(0.4f) }
                LaunchedEffect(Unit) {
                    // EaseOutBack throws on the last float before 1 (Compose 1.7), which crashed this dialog
                    iconScale.animateTo(1.12f, tween(500, easing = { if (it < 0.9999f) EaseOutBack.transform(it) else 1f }))
                    iconScale.animateTo(1f, spring(dampingRatio = 0.5f, stiffness = 400f))
                }
                Text(
                    personality.icon,
                    modifier = Modifier.graphicsLayer {
                        scaleX = iconScale.value
                        scaleY = iconScale.value
                    },
                    fontSize = 56.sp
                )
                Spacer(Modifier.height(16.dp))
                AnimatedContent(
                    targetState = phase,
                    transitionSpec = {
                        (fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 4 }) togetherWith fadeOut(tween(250))
                    },
                    label = "unlockPhase"
                ) { p ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (p == 0) {
                            Text(
                                acknowledgment,
                                style = MaterialTheme.typography.bodyMedium,
                                color = theme.textSoft,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                        } else {
                            Text(
                                "✨ " + personality.label + " unlocked",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = theme.accent,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                reveal,
                                style = MaterialTheme.typography.bodyMedium,
                                color = theme.textSoft,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                personality.desc,
                                style = MaterialTheme.typography.bodySmall,
                                color = theme.accent.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                AddictiveButton(
                    text = if (phase == 0) "I hear you" else "Switch to " + personality.label,
                    onClick = {
                        HapticEngine.click(ctx)
                        if (phase == 0) {
                            phase = 1
                        } else {
                            onSwitchTo()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    theme = theme,
                    isPrimary = true
                )
                if (phase == 1) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Keep current personality",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            HapticEngine.tick(ctx)
                            onKeepCurrent()
                        },
                        color = theme.textMuted
                    )
                }
            }
        }
    }
}
