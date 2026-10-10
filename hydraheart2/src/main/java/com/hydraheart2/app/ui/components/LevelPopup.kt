package com.hydraheart2.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.util.HapticEngine
import java.text.NumberFormat

data class LevelDisplay(
    val level: Int,
    val hpRequired: Int,
    val reward: String,
    val rewardIcon: String
)

val ALL_LEVELS = listOf(
    LevelDisplay(1, 0, "Welcome to HydraHeart2!", "💧"),
    LevelDisplay(2, 500, "Cute necklace for Hydra", "💎"),
    LevelDisplay(3, 1200, "Sparkle particle effect", "✨"),
    LevelDisplay(4, 2000, "New speech bubble styles", "🎨"),
    LevelDisplay(5, 3200, "Hydra's Diary unlocked", "📖"),
    LevelDisplay(6, 4800, "Crown accessory for Hydra", "👑"),
    LevelDisplay(7, 7000, "Flower particle effects", "🌸"),
    LevelDisplay(8, 10000, "Rainbow shimmer effect", "💫"),
    LevelDisplay(9, 14000, "Butterfly idle animation", "🦋"),
    LevelDisplay(10, 20000, "Legendary Hydra form", "🏆")
)

@Composable
fun LevelPopup(
    visible: Boolean,
    currentLevel: Int,
    currentHp: Int,
    totalHp: Int,
    theme: HydraTheme,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)) + scaleIn(spring(dampingRatio = 0.7f, stiffness = 400f), initialScale = 0.85f),
        exit = fadeOut(tween(150)) + scaleOut(tween(150), targetScale = 0.9f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                    HapticEngine.tick(context)
                    onDismiss()
                },
            contentAlignment = Alignment.Center
        ) {
            FrostedGlass(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.75f)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { },
                theme = theme,
                cornerRadius = 24.dp,
                blurRadius = 30.dp,
                padding = PaddingValues(24.dp)
            ) {
                Text(
                    "Hydra Levels",
                    modifier = Modifier.fillMaxWidth(),
                    color = theme.accent3,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Total HP earned: $totalHp",
                    modifier = Modifier.fillMaxWidth(),
                    color = theme.textSoft,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ALL_LEVELS.forEach { lvl ->
                        val unlocked = currentLevel >= lvl.level
                        val isCurrent = currentLevel == lvl.level
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isCurrent) theme.accent.copy(alpha = 0.15f)
                                    else if (unlocked) theme.accent.copy(alpha = 0.06f)
                                    else Color.Transparent
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (unlocked) theme.accent.copy(alpha = 0.2f)
                                        else theme.cardBorder.copy(alpha = 0.3f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(if (unlocked) lvl.rewardIcon else "🔒", fontSize = 20.sp)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "Level ${lvl.level}",
                                        color = if (unlocked) theme.accent3 else theme.textMuted,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    if (isCurrent) {
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            "← YOU",
                                            color = theme.accent,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                                Text(
                                    lvl.reward,
                                    color = if (unlocked) theme.textSoft else theme.textMuted,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                if (lvl.level > 1) {
                                    Text(
                                        NumberFormat.getNumberInstance().format(lvl.hpRequired) + " HP required",
                                        color = theme.textMuted,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
                                    )
                                }
                            }
                            if (unlocked) {
                                Text("✓", color = theme.accent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                AddictiveButton(
                    text = "Got it! 💕",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    theme = theme
                )
            }
        }
    }
}
