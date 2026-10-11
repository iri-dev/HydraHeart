package com.hydraheart2.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
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
import com.hydraheart2.app.R
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.data.TrophyData
import com.hydraheart2.app.data.TrophyRarity
import com.hydraheart2.app.ui.components.AddictiveButton
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel

private fun trophyCurrentProgress(id: String, vm: HydraViewModel): Int = when (id) {
    "three_day", "five_day", "week_streak", "two_week",
    "thirty_streak", "sixty_streak",
    -> vm.streak
    "committed" -> vm.loggedDaysInARow()
    "ten_drinks", "twenty_five_drinks", "fifty_drinks", "hundred_drinks",
    "two_fifty", "five_hundred", "seven_fifty", "thousand_drinks" -> vm.totalDrinksAllTime
    "level5", "level10" -> vm.levelInfo.level
    "hydration_five" -> minOf(vm.hitDaysInARow(), 5)
    "perfect_week" -> minOf(vm.hitDaysInARow(), 7)
    else -> 0
}

private data class RarityXpEntry(val label: String, val xp: Int, val colour: Color)

private val rarityXpEntries = listOf(
    RarityXpEntry("Legendary", 550, Color(0xFFFFD700)),
    RarityXpEntry("Epic", 300, Color(0xFFA855F7)),
    RarityXpEntry("Rare", 150, Color(0xFF60A5FA)),
    RarityXpEntry("Uncommon", 50, Color(0xFF4ADE80)),
    RarityXpEntry("Common", 25, Color(0xFFA0B0C0)),
)

@Composable
fun TrophiesScreen(viewModel: HydraViewModel) {
    val theme = viewModel.currentTheme
    val earned = viewModel.earnedTrophyIds.toSet()
    val gender = viewModel.profile.gender
    val totalHpFromTrophies = earned.sumOf { id -> TrophyData.trophies.find { it.id == id }?.hpReward ?: 25 }
    val context = LocalContext.current
    val royal = stringResource(gender.royalRes())

    var showGuide by remember { mutableStateOf(false) }

    val rarityOrder = remember {
        listOf(TrophyRarity.LEGENDARY, TrophyRarity.EPIC, TrophyRarity.RARE, TrophyRarity.UNCOMMON, TrophyRarity.COMMON)
    }
    val trophyGroups = remember(earned) {
        rarityOrder.map { rarity -> rarity to TrophyData.trophies.filter { it.rarity == rarity } }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.trophies_title),
                        style = MaterialTheme.typography.headlineLarge,
                        color = theme.accent3,
                        fontWeight = FontWeight.Bold,
                    )
                    HydraGuideButton(theme) {
                        HapticEngine.tick(context)
                        showGuide = true
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        value = "${earned.size}/${TrophyData.trophies.size}",
                        label = stringResource(R.string.trophies_earned_label),
                        icon = "🏆",
                        bg = theme.bg2,
                        accent = theme.accent,
                        label2 = theme.textSoft,
                    )
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        value = "$totalHpFromTrophies HP",
                        label = stringResource(R.string.trophies_from_trophies),
                        icon = "✨",
                        bg = theme.bg2,
                        accent = theme.accent,
                        label2 = theme.textSoft,
                    )
                }
                Spacer(Modifier.height(20.dp))
            }

            trophyGroups.forEach { (rarity, trophies) ->
                val rarityColor = Color(rarity.colour)
                item(key = "header_${rarity.name}") {
                    val rarityLabel = stringResource(rarity.labelRes)
                    Text(
                        rarityLabel,
                        style = MaterialTheme.typography.titleSmall,
                        color = rarityColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                    )
                }
                items(trophies, key = { it.id }) { trophy ->
                    val rarityLabel = stringResource(rarity.labelRes)
                    val isEarned = trophy.id in earned
                    val trophyName = stringResource(trophy.nameRes).replace("{royal}", royal)
                    val trophyDesc = stringResource(trophy.descriptionRes)
                    val curProgress = if (!isEarned && trophy.progressTarget > 0) trophyCurrentProgress(trophy.id, viewModel) else 0
                    val showBar = !isEarned && trophy.progressTarget > 0 && curProgress > 0
                    val fraction = if (showBar) (curProgress.toFloat() / trophy.progressTarget.toFloat()).coerceIn(0f, 1f) else 0f

                    FrostedGlass(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                            .alpha(if (isEarned) 1f else 0.45f),
                        theme = theme
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(rarityColor.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(trophy.icon, fontSize = 24.sp)
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    trophyName,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isEarned) theme.textPrimary else theme.textMuted,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(trophyDesc, style = MaterialTheme.typography.bodySmall, color = theme.textSoft)
                                Spacer(Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(rarityColor.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(rarityLabel, color = rarityColor, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                }
                                if (showBar) {
                                    Spacer(Modifier.height(7.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(1.5.dp)
                                                .clip(RoundedCornerShape(1.dp))
                                                .background(rarityColor.copy(alpha = 0.14f))
                                        ) {
                                            Box(
                                                Modifier
                                                    .fillMaxWidth(fraction)
                                                    .fillMaxHeight()
                                                    .background(
                                                        Brush.horizontalGradient(
                                                            listOf(rarityColor.copy(alpha = 0.65f), rarityColor.copy(alpha = 0.36f))
                                                        )
                                                    )
                                            )
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            "$curProgress / ${trophy.progressTarget}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                            color = rarityColor.copy(alpha = 0.55f),
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            if (isEarned) {
                                Text("✓", color = rarityColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Text("🔒", fontSize = 18.sp)
                            }
                        }
                    }
                }
                item(key = "spacer_${rarity.name}") {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }

    if (showGuide) {
        HydraGuideSheet(theme) { showGuide = false }
    }
}

@Composable
private fun HydraGuideButton(theme: HydraTheme, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val inf = rememberInfiniteTransition(label = "guideBtn")
    val breathScale by inf.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "breathScale"
    )
    val breathGlow by inf.animateFloat(
        initialValue = 0.16f,
        targetValue = 0.34f,
        animationSpec = infiniteRepeatable(tween(2800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "breathGlow"
    )
    val shimmerPhase by inf.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Restart),
        label = "shimmerPhase"
    )
    val shimmerX = if (shimmerPhase < 0.22f) -1.4f + (shimmerPhase / 0.22f) * 3.2f else 2.4f

    val pressScale by animateFloatAsState(
        if (isPressed) 0.86f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 1500f),
        label = "pressScale"
    )
    val pressGlow by animateFloatAsState(if (isPressed) 0.5f else 0f, tween(120), label = "pressGlow")

    Box(
        modifier = Modifier
            .size(42.dp)
            .graphicsLayer {
                val s = breathScale * pressScale
                scaleX = s
                scaleY = s
            }
            .drawBehind {
                val r = size.minDimension * 0.88f
                drawCircle(
                    Brush.radialGradient(
                        listOf(theme.accent.copy(alpha = breathGlow + pressGlow * 0.3f), Color.Transparent),
                        center = center,
                        radius = r
                    ),
                    radius = r
                )
            }
            .clip(CircleShape)
            .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.3f), theme.accent.copy(alpha = 0.16f))))
            .drawWithContent {
                drawContent()
                drawRect(
                    Brush.linearGradient(
                        listOf(Color.Transparent, Color.White.copy(alpha = 0.4f), Color.Transparent),
                        start = Offset(size.width * shimmerX - size.width * 0.3f, 0f),
                        end = Offset(size.width * shimmerX + size.width * 0.3f, size.height)
                    )
                )
                if (pressGlow > 0f) {
                    drawCircle(theme.accent.copy(alpha = 0.28f * pressGlow), radius = size.minDimension / 2f)
                }
            }
            .border(
                1.dp,
                Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.7f), theme.accent.copy(alpha = 0.28f))),
                CircleShape
            )
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text("✨", fontSize = 17.sp)
    }
}

@Composable
private fun HydraGuideSheet(theme: HydraTheme, onDismiss: () -> Unit) {
    val context = LocalContext.current
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        var contentVisible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { contentVisible = true }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(remember { MutableInteractionSource() }, null) {
                    HapticEngine.tick(context)
                    onDismiss()
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = contentVisible,
                enter = slideInVertically(tween(380, easing = FastOutSlowInEasing)) { it } + fadeIn(tween(200)),
                exit = slideOutVertically(tween(260, easing = FastOutSlowInEasing)) { it } + fadeOut(tween(180))
            ) {
                val sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .shadow(
                            elevation = 24.dp,
                            shape = sheetShape,
                            ambientColor = theme.accent.copy(alpha = 0.1f),
                            spotColor = theme.accent.copy(alpha = 0.06f),
                        )
                        .clip(sheetShape)
                        .background(Brush.verticalGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                Brush.verticalGradient(
                                    listOf(Color.White.copy(alpha = if (theme.isLight) 0.7f else 0.28f), Color.Transparent),
                                    startY = 0f,
                                    endY = 2.dp.toPx()
                                )
                            )
                            drawCircle(
                                Brush.radialGradient(
                                    listOf(theme.accent.copy(alpha = if (theme.isLight) 0.08f else 0.14f), Color.Transparent),
                                    center = Offset(size.width / 2f, 0f),
                                    radius = size.width * 0.68f
                                ),
                                radius = size.width * 0.68f,
                                center = Offset(size.width / 2f, 0f)
                            )
                        }
                        .border(
                            0.5.dp,
                            Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(alpha = if (theme.isLight) 0.7f else 0.38f),
                                    theme.cardBorder.copy(alpha = 0.08f),
                                )
                            ),
                            sheetShape
                        )
                        .clickable(remember { MutableInteractionSource() }, null) { }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp, bottom = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            Modifier
                                .width(36.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(theme.cardBorder.copy(alpha = 0.35f))
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 22.dp)
                            .padding(top = 4.dp, bottom = 16.dp)
                    ) {
                        HydraHeroSection(theme)

                        Spacer(Modifier.height(20.dp))
                        HorizontalDivider(thickness = 0.5.dp, color = theme.accent.copy(alpha = 0.12f))
                        Spacer(Modifier.height(20.dp))

                        SectionLabel("🏆", "Trophies", theme)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Every good habit earns a trophy.",
                            style = MaterialTheme.typography.bodySmall,
                            color = theme.textSoft,
                            lineHeight = 20.sp,
                        )
                        Text(
                            "That's my way of saying — I noticed. 🌟",
                            style = MaterialTheme.typography.bodySmall,
                            color = theme.textSoft,
                            lineHeight = 20.sp,
                        )

                        Spacer(Modifier.height(22.dp))
                        SectionDivider(theme)
                        Spacer(Modifier.height(22.dp))

                        SectionLabel("✨", "Trophy Rewards", theme)
                        Spacer(Modifier.height(3.dp))
                        Text("Rarer trophies reward more XP.", style = MaterialTheme.typography.bodySmall, color = theme.textMuted)
                        Spacer(Modifier.height(10.dp))
                        rarityXpEntries.forEachIndexed { index, entry ->
                            RarityXpRow(entry, theme)
                            if (index < rarityXpEntries.lastIndex) {
                                HorizontalDivider(thickness = 0.5.dp, color = theme.accent.copy(alpha = 0.07f))
                            }
                        }

                        Spacer(Modifier.height(22.dp))
                        SectionDivider(theme)
                        Spacer(Modifier.height(22.dp))

                        SectionLabel("💧", "Daily Actions", theme)
                        Spacer(Modifier.height(3.dp))
                        Text("Every time you show up, I reward it.", style = MaterialTheme.typography.bodySmall, color = theme.textMuted)
                        Spacer(Modifier.height(10.dp))
                        XpRow("Log a drink", "+5 XP", theme)
                        HorizontalDivider(thickness = 0.5.dp, color = theme.accent.copy(alpha = 0.07f))
                        XpRow("Open the app", "+2 XP", theme)
                        HorizontalDivider(thickness = 0.5.dp, color = theme.accent.copy(alpha = 0.07f))
                        XpRow("Log your mood", "+3 XP", theme)

                        Spacer(Modifier.height(22.dp))
                        SectionDivider(theme)
                        Spacer(Modifier.height(22.dp))

                        SectionLabel("🎯", "Milestones", theme)
                        Spacer(Modifier.height(10.dp))
                        XpRow("Hit your daily goal", "+50 XP", theme)
                        HorizontalDivider(thickness = 0.5.dp, color = theme.accent.copy(alpha = 0.07f))
                        XpRow("Earn a trophy", "25–550 XP", theme)

                        Spacer(Modifier.height(28.dp))
                        HorizontalDivider(thickness = 0.5.dp, color = theme.accent.copy(alpha = 0.09f))
                        Spacer(Modifier.height(20.dp))

                        Text(
                            "Just keep showing up.",
                            style = MaterialTheme.typography.bodySmall,
                            color = theme.textSoft,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            "Every sip, every log, every day.",
                            style = MaterialTheme.typography.bodySmall,
                            color = theme.textSoft,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "I'm cheering for you 💙",
                            style = MaterialTheme.typography.bodySmall,
                            color = theme.textMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(Modifier.height(28.dp))
                        AddictiveButton(
                            text = "Got it! 💕",
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth(),
                            theme = theme
                        )
                        Spacer(Modifier.height(12.dp))
                        Spacer(Modifier.navigationBarsPadding())
                    }
                }
            }
        }
    }
}

@Composable
private fun HydraHeroSection(theme: HydraTheme) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .drawBehind {
                    drawCircle(
                        Brush.radialGradient(
                            listOf(theme.accent.copy(alpha = 0.22f), Color.Transparent),
                            center = center,
                            radius = size.minDimension * 0.88f
                        ),
                        radius = size.minDimension * 0.88f
                    )
                }
                .clip(CircleShape)
                .background(Brush.verticalGradient(listOf(theme.accent.copy(alpha = 0.2f), theme.accent2.copy(alpha = 0.1f))))
                .border(
                    0.5.dp,
                    Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.55f), theme.accent.copy(alpha = 0.18f))),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("💧", fontSize = 20.sp)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            "Hydra's Guide",
            style = MaterialTheme.typography.headlineMedium,
            color = theme.textPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(3.dp))
        Text(
            "let me walk you through it",
            style = MaterialTheme.typography.labelMedium,
            color = theme.textSoft,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SectionDivider(theme: HydraTheme) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Brush.horizontalGradient(listOf(Color.Transparent, theme.accent.copy(alpha = 0.14f), Color.Transparent)))
    )
}

@Composable
private fun SectionLabel(emoji: String, title: String, theme: HydraTheme) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 14.sp)
        Spacer(Modifier.width(6.dp))
        Text(title, style = MaterialTheme.typography.titleSmall, color = theme.accent, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun RarityXpRow(entry: RarityXpEntry, theme: HydraTheme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(entry.colour.copy(alpha = 0.55f))
            )
            Spacer(Modifier.width(10.dp))
            Text(entry.label, style = MaterialTheme.typography.bodySmall, color = theme.textSoft, fontWeight = FontWeight.Medium)
        }
        Text("+${entry.xp} XP", style = MaterialTheme.typography.bodySmall, color = theme.accent, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun XpRow(label: String, value: String, theme: HydraTheme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = theme.textSoft)
        Text(value, style = MaterialTheme.typography.bodySmall, color = theme.accent, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier,
    value: String,
    label: String,
    icon: String,
    bg: Color,
    accent: Color,
    label2: Color,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, color = label2, style = MaterialTheme.typography.bodySmall)
                Text(icon, fontSize = 20.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, color = accent, fontWeight = FontWeight.Bold)
        }
    }
}
