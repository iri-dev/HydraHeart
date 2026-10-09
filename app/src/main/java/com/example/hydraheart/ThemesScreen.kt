package com.example.hydraheart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont

private val ThAccent = Color(0xFF6E9BFF)
private val ThLavender = Color(0xFFEAF0FF)
private val ThMuted = Color(0xFFB8C6EE)
private val ThLocked = Color(0xFF8C9BC8)

private val thRowBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF26345E).copy(alpha = 0.30f),
        Color(0xFF131F3D).copy(alpha = 0.55f)
    )
)

private val thRowBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.20f),
        Color.White.copy(alpha = 0.05f),
        ThAccent.copy(alpha = 0.05f)
    )
)

private data class ThemeEntry(
    val name: String,
    val emoji: String,
    val swatchTop: Color,
    val swatchBottom: Color,
    val dotColor: Color,
    val unlocked: Boolean = false,
    val unlockNote: String = ""
)

private val yourCollection = listOf(
    ThemeEntry("Sky Light", "☀️", Color(0xFFF7FBFF), Color(0xFFE5EEFC), Color(0xFF6BA8FF), unlocked = true),
    ThemeEntry("Obsidian Flow", "🖤", Color(0xFF1B2034), Color(0xFF0A0F22), Color(0xFF6E9BFF), unlocked = true),
    ThemeEntry("Pure Water", "🫧", Color(0xFFE6F2FB), Color(0xFFBFDEF4), Color(0xFF6BA8FF), unlocked = true),
    ThemeEntry("Rose", "🌹", Color(0xFF4C1717), Color(0xFF230A0A), Color(0xFFD55C6F), unlocked = true),
    ThemeEntry("Cherry Blossom", "🌸", Color(0xFFFDE3EA), Color(0xFFF6BFCD), Color(0xFFE65C8F), unlocked = true),
    ThemeEntry("Matcha", "🍵", Color(0xFFE6F4DA), Color(0xFFC8E5AE), Color(0xFF7BB060), unlocked = true),
    ThemeEntry("Ember Glow", "🔥", Color(0xFF3F1404), Color(0xFF1F0902), Color(0xFFFF7A3D), unlocked = true)
)

private val missionCollection = listOf(
    ThemeEntry("Barbie", "💗", Color(0xFFFBD8E6), Color(0xFFF6B6CE), Color(0xFFE65C8F), unlockNote = "Complete a perfect hydration day"),
    ThemeEntry("Neon Pulse", "⚡", Color(0xFF1C1334), Color(0xFF0A081C), Color(0xFFB070FF), unlockNote = "Maintain a 7-day hydration streak"),
    ThemeEntry("Mint Choc", "🍫", Color(0xFFE3F4E0), Color(0xFFB7DAB4), Color(0xFF5BB484), unlockNote = "Log 5 different drink types")
)

private data class LevelTier(val level: Int, val themes: List<ThemeEntry>)

private val levelCollections = listOf(
    LevelTier(
        2,
        listOf(
            ThemeEntry("Pearls", "🤍", Color(0xFFE8E8EE), Color(0xFFC6C6CE), Color(0xFFA9A9B4)),
            ThemeEntry("Cloud Nine", "☁️", Color(0xFFC9CFE4), Color(0xFFA6AECE), Color(0xFF8BA0E0)),
            ThemeEntry("Strawberry Milk", "🍓", Color(0xFFE8D2D6), Color(0xFFD5A8AF), Color(0xFFE08D9C)),
            ThemeEntry("Icy Morning", "❄️", Color(0xFFB7CADD), Color(0xFF8AA1BC), Color(0xFF6BA8FF)),
            ThemeEntry("Oat Latte", "☕", Color(0xFFDDC8B8), Color(0xFFB89F8B), Color(0xFFA88D78))
        )
    ),
    LevelTier(
        3,
        listOf(
            ThemeEntry("Honey Glow", "🍯", Color(0xFFE5C783), Color(0xFFC59A4D), Color(0xFFAF8536)),
            ThemeEntry("Glazed", "🍩", Color(0xFFD9B7B0), Color(0xFFB4847D), Color(0xFFA06860)),
            ThemeEntry("Sunset Hour", "🌅", Color(0xFFE3B49F), Color(0xFFC58370), Color(0xFFE05F4A)),
            ThemeEntry("Botanical Garden", "🌿", Color(0xFFCEDDC4), Color(0xFFA3B89A), Color(0xFF6FA66A)),
            ThemeEntry("Berry Cream", "🫐", Color(0xFFD7BCC6), Color(0xFFB58CA0), Color(0xFFA770B0))
        )
    ),
    LevelTier(
        4,
        listOf(
            ThemeEntry("Midnight Glam", "🌙", Color(0xFF1F1830), Color(0xFF0F0A1C), Color(0xFFA070FF)),
            ThemeEntry("Champagne", "🥂", Color(0xFFE6D5B0), Color(0xFFB69A65), Color(0xFFA88D55)),
            ThemeEntry("Ocean Depths", "🌊", Color(0xFF12343A), Color(0xFF071A1E), Color(0xFF3DBFC8)),
            ThemeEntry("Afterglow", "🌇", Color(0xFF4A1E1F), Color(0xFF210C0C), Color(0xFFE56B71)),
            ThemeEntry("Sugar Plum", "🧁", Color(0xFFD9C2E8), Color(0xFFAF8DC8), Color(0xFFAA6FE0))
        )
    ),
    LevelTier(
        5,
        listOf(
            ThemeEntry("Aura Flow", "🌈", Color(0xFF2A1F38), Color(0xFF120A1C), Color(0xFFB070FF)),
            ThemeEntry("Starlight", "🌌", Color(0xFF1A1B36), Color(0xFF080820), Color(0xFFA0B0FF)),
            ThemeEntry("Cosmic Girl", "🔮", Color(0xFF291E3F), Color(0xFF120A22), Color(0xFFB070FF)),
            ThemeEntry("Dark Academia", "📚", Color(0xFF1B1A22), Color(0xFF09080F), Color(0xFFBA8E5F)),
            ThemeEntry("Sapphire Night", "💎", Color(0xFF0F1A40), Color(0xFF050B20), Color(0xFF3A6BE0))
        )
    )
)

@Composable
fun ThemesScreen(onBack: () -> Unit = {}) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F22))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF18203F),
                        Color(0xFF121A33),
                        Color(0xFF0C1228),
                        Color(0xFF070B1C)
                    ),
                    startY = 0f,
                    endY = size.height
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 22.dp)
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            ThHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(28.dp))
            BasicText(
                text = "Appearance",
                style = TextStyle(
                    color = ThAccent,
                    fontSize = 30.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Unlock new aesthetics as you grow",
                style = TextStyle(
                    color = ThLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )

            Spacer(modifier = Modifier.height(18.dp))
            ThSectionLabel("YOUR COLLECTION")
            Spacer(modifier = Modifier.height(10.dp))
            ThemeList(yourCollection)

            Spacer(modifier = Modifier.height(22.dp))
            Row(
                modifier = Modifier.padding(start = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicText(
                    text = "MISSION COLLECTION",
                    style = TextStyle(
                        color = ThAccent.copy(alpha = 0.72f),
                        fontSize = 13.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.0.sp
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(width = 26.dp, height = 22.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(ThAccent.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = "✦",
                        style = TextStyle(
                            color = ThAccent,
                            fontSize = 14.sp
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Special unlocks through your actions",
                modifier = Modifier.padding(start = 6.dp),
                style = TextStyle(
                    color = ThLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            ThemeList(missionCollection)

            Spacer(modifier = Modifier.height(22.dp))
            ThSectionLabel("LEVEL COLLECTIONS")
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "New tiers unlock as you level up",
                modifier = Modifier.padding(start = 6.dp),
                style = TextStyle(
                    color = ThLavender.copy(alpha = 0.88f),
                    fontSize = 15.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            )

            levelCollections.forEach { tier ->
                Spacer(modifier = Modifier.height(14.dp))
                LevelBadge(level = tier.level)
                Spacer(modifier = Modifier.height(8.dp))
                ThemeList(tier.themes, asLevelTier = true, levelLabel = "Level ${tier.level}")
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun ThHeader(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(36.dp)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(22.dp)) {
                val w = size.width
                val h = size.height
                drawLine(
                    color = ThAccent,
                    start = Offset(w * 0.78f, h * 0.50f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ThAccent,
                    start = Offset(w * 0.36f, h * 0.30f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ThAccent,
                    start = Offset(w * 0.36f, h * 0.70f),
                    end = Offset(w * 0.18f, h * 0.50f),
                    strokeWidth = 1.7.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        BasicText(
            text = "Themes",
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = ThAccent,
                fontSize = 26.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun ThSectionLabel(label: String) {
    BasicText(
        text = label,
        modifier = Modifier.padding(start = 6.dp),
        style = TextStyle(
            color = ThAccent.copy(alpha = 0.72f),
            fontSize = 13.sp,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.0.sp
        )
    )
}

@Composable
private fun LevelBadge(level: Int) {
    Box(
        modifier = Modifier
            .padding(start = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(ThAccent.copy(alpha = 0.10f))
            .border(
                width = 0.8.dp,
                color = ThAccent.copy(alpha = 0.30f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        BasicText(
            text = "Level $level",
            style = TextStyle(
                color = ThAccent,
                fontSize = 14.sp,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun ThemeList(
    items: List<ThemeEntry>,
    asLevelTier: Boolean = false,
    levelLabel: String = ""
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEach { item ->
            ThemeRow(item = item, asLevelTier = asLevelTier, levelLabel = levelLabel)
        }
    }
}

@Composable
private fun ThemeRow(item: ThemeEntry, asLevelTier: Boolean, levelLabel: String) {
    val dim = !item.unlocked && asLevelTier
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(thRowBrush)
            .border(
                brush = thRowBorder,
                width = 0.85.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {}
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Swatch(
            top = item.swatchTop,
            bottom = item.swatchBottom,
            dot = item.dotColor,
            dim = dim
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicText(
                    text = item.emoji,
                    style = TextStyle(fontSize = 22.sp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                BasicText(
                    text = item.name,
                    style = TextStyle(
                        color = if (dim) ThLocked.copy(alpha = 0.78f) else ThLavender,
                        fontSize = 22.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            if (item.unlockNote.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = item.unlockNote,
                    style = TextStyle(
                        color = ThAccent.copy(alpha = 0.75f),
                        fontSize = 14.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
            } else if (asLevelTier) {
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = levelLabel,
                    style = TextStyle(
                        color = ThLocked.copy(alpha = 0.55f),
                        fontSize = 14.sp,
                        fontFamily = AppFont,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
        if (!item.unlocked) {
            BasicText(
                text = "🔒",
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }
}

@Composable
private fun Swatch(top: Color, bottom: Color, dot: Color, dim: Boolean) {
    val alpha = if (dim) 0.40f else 1.0f
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        top.copy(alpha = top.alpha * alpha),
                        bottom.copy(alpha = bottom.alpha * alpha)
                    )
                )
            )
            .border(
                width = 0.8.dp,
                color = Color.White.copy(alpha = 0.10f * if (dim) 0.5f else 1f),
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 8.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(dot.copy(alpha = dot.alpha * alpha))
        )
    }
}
