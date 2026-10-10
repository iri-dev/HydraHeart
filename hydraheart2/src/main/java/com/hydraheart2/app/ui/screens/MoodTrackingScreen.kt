package com.hydraheart2.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.ui.components.AddictiveButton
import com.hydraheart2.app.ui.components.HydraHeartCanvas
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel

private val MOODS = listOf(
    Triple("Happy", "😊", Color(0xFFFFB7C5)),
    Triple("Calm", "😌", Color(0xFFB8E6C1)),
    Triple("Okay", "😐", Color(0xFFCDCDCD)),
    Triple("Tired", "😴", Color(0xFFADD8E6)),
    Triple("Stressed", "😣", Color(0xFFD4B3E8))
)

private fun moodToHydra(mood: String?): HydraViewModel.HydraMood = when (mood) {
    "Happy" -> HydraViewModel.HydraMood.ECSTATIC
    "Calm" -> HydraViewModel.HydraMood.CONTENT
    "Okay" -> HydraViewModel.HydraMood.NEUTRAL
    "Tired" -> HydraViewModel.HydraMood.CONCERNED
    "Stressed" -> HydraViewModel.HydraMood.WORRIED
    else -> HydraViewModel.HydraMood.NEUTRAL
}

private fun moodToFill(mood: String?): Float = when (mood) {
    "Happy" -> 100f
    "Calm" -> 72f
    "Okay" -> 50f
    "Tired" -> 28f
    "Stressed" -> 18f
    else -> 50f
}

private fun moodToColor(mood: String?): Color? = when (mood) {
    "Happy" -> Color(0xFFFFB7C5)
    "Calm" -> Color(0xFFB8E6C1)
    "Okay" -> Color(0xFFCDCDCD)
    "Tired" -> Color(0xFFADD8E6)
    "Stressed" -> Color(0xFFD4B3E8)
    else -> null
}

@Composable
fun MoodTrackingScreen(viewModel: HydraViewModel, onBack: () -> Unit) {
    val theme = viewModel.currentTheme
    val ctx = LocalContext.current
    var selectedMood by remember { mutableStateOf(viewModel.currentMood) }
    var note by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }
    var loaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { loaded = true }
    val infiniteTransition = rememberInfiniteTransition(label = "moodGlow")
    val glowPulse by infiniteTransition.animateFloat(
        0.3f, 0.72f,
        infiniteRepeatable(tween(1800, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "glowPulse"
    )
    val targetHydraFill by animateFloatAsState(
        if (selectedMood != null) moodToFill(selectedMood) else viewModel.waterPercent,
        spring(dampingRatio = 0.5f, stiffness = 400f),
        label = "hydraFill"
    )
    val hydraMood = remember(selectedMood) {
        if (selectedMood != null) moodToHydra(selectedMood) else viewModel.hydraMood
    }
    val moodColor = moodToColor(selectedMood) ?: theme.accent

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
                .padding(horizontal = 20.dp)
                .padding(bottom = 80.dp)
        ) {
            AnimatedVisibility(visible = loaded, enter = fadeIn(tween(250))) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    IconButton(onClick = {
                        HapticEngine.tick(ctx)
                        onBack()
                    }) {
                        Text("←", fontSize = 22.sp, color = theme.accent)
                    }
                    Text("How We Feel", style = MaterialTheme.typography.headlineMedium, color = theme.accent3)
                }
            }

            AnimatedVisibility(
                visible = loaded,
                enter = fadeIn(tween(450, delayMillis = 60)) +
                    scaleIn(tween(450, delayMillis = 60), 0.88f, TransformOrigin(0.5f, 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(160.dp)) {
                        HydraHeartCanvas(
                            fillPercent = targetHydraFill,
                            mood = hydraMood,
                            theme = theme,
                            showFace = viewModel.showFace,
                            hydraShape = viewModel.hydraShape,
                            drinkColours = viewModel.drinkColourPortions,
                            level = viewModel.levelInfo.level,
                            showNecklace = viewModel.showNecklace,
                            size = 160.dp
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = loaded,
                enter = fadeIn(tween(380, delayMillis = 120)) +
                    slideInVertically(tween(350, delayMillis = 120)) { it / 3 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "How are we feeling today?",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = theme.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Just a quick check-in ✨",
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.textSoft,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(22.dp))
                }
            }

            MOODS.forEachIndexed { index, (mood, emoji, cardColor) ->
                AnimatedVisibility(
                    visible = loaded,
                    enter = fadeIn(tween(340, delayMillis = index * 55 + 180)) +
                        slideInVertically(tween(300, delayMillis = index * 55 + 180)) { it / 4 }
                ) {
                    Column {
                        MoodCard(
                            mood = mood,
                            emoji = emoji,
                            moodColor = cardColor,
                            theme = theme,
                            isSelected = selectedMood == mood,
                            glowPulse = glowPulse,
                            onClick = {
                                HapticEngine.click(ctx)
                                selectedMood = mood
                                saved = false
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }

            AnimatedVisibility(
                visible = selectedMood != null,
                enter = fadeIn(tween(420)) + slideInVertically(tween(380)) { it / 2 }
            ) {
                val response = remember(selectedMood, viewModel.moodEntries.size) {
                    selectedMood?.let { viewModel.moodResponseFor(it) } ?: ""
                }
                Column {
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = moodColor.copy(alpha = 0.18f),
                                spotColor = moodColor.copy(alpha = 0.12f)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        moodColor.copy(alpha = 0.2f),
                                        moodColor.copy(alpha = 0.1f),
                                        theme.bg2.copy(alpha = 0.55f)
                                    )
                                )
                            )
                            .border(0.75.dp, moodColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        Text(
                            response,
                            style = MaterialTheme.typography.bodyMedium,
                            color = theme.textPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 22.sp
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "What's on your mind? 🌸",
                                color = theme.textMuted,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = moodColor,
                            unfocusedBorderColor = theme.cardBorder,
                            focusedTextColor = theme.textPrimary,
                            unfocusedTextColor = theme.textPrimary,
                            cursorColor = moodColor
                        ),
                        maxLines = 3
                    )
                    Spacer(Modifier.height(16.dp))
                    AnimatedContent(
                        targetState = saved,
                        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
                        label = "saveState"
                    ) { isSaved ->
                        if (isSaved) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(moodColor.copy(alpha = 0.15f))
                                    .border(0.75.dp, moodColor.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Saved 💧",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = moodColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            AddictiveButton(
                                text = "Save Check-In",
                                onClick = {
                                    HapticEngine.heavyClick(ctx)
                                    selectedMood?.let { viewModel.logMood(it, note) }
                                    saved = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                theme = theme,
                                colour = moodColor,
                                isPrimary = true,
                                cornerRadius = 16.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodCard(
    mood: String,
    emoji: String,
    moodColor: Color,
    theme: HydraTheme,
    isSelected: Boolean,
    glowPulse: Float,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (isSelected) 1.035f else if (isPressed) 0.967f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 1500f),
        label = "cardScale"
    )
    val borderAlpha by animateFloatAsState(if (isSelected) glowPulse else 0.14f, tween(280), label = "borderAlpha")
    val bgAlpha by animateFloatAsState(if (isSelected) 0.25f else 0.08f, tween(280), label = "bgAlpha")
    val elevation by animateFloatAsState(if (isSelected) 12f else 3f, tween(250), label = "elevation")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = moodColor.copy(alpha = if (isSelected) 0.28f else 0.05f),
                spotColor = moodColor.copy(alpha = if (isSelected) 0.22f else 0.03f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        moodColor.copy(alpha = bgAlpha),
                        moodColor.copy(alpha = bgAlpha * 0.5f),
                        theme.bg2.copy(alpha = 0.55f)
                    )
                )
            )
            .drawWithContent {
                drawContent()
                if (isSelected && glowPulse > 0f) {
                    drawRoundRect(
                        moodColor.copy(alpha = glowPulse * 0.1f),
                        cornerRadius = CornerRadius(20.dp.toPx())
                    )
                }
            }
            .border(
                if (isSelected) 1.5.dp else 0.75.dp,
                moodColor.copy(alpha = borderAlpha),
                RoundedCornerShape(20.dp)
            )
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, fontSize = 36.sp)
        Spacer(Modifier.width(18.dp))
        Text(
            mood,
            style = MaterialTheme.typography.titleMedium,
            color = if (isSelected) theme.textPrimary else theme.textSoft,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn(tween(180)) + scaleIn(spring(dampingRatio = 0.5f, stiffness = 1500f), initialScale = 0.5f),
            exit = fadeOut(tween(130)) + scaleOut(tween(130))
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(moodColor.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = moodColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
