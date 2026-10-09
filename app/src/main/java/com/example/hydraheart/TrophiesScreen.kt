package com.example.hydraheart

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydraheart.ui.theme.AppFont
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private val TrophyDeep = Color(0xFF081229)
private val TrophyBlue = Color(0xFF4E80FF)
private val TrophyPanelTop = Color(0xFF2A3D69)
private val TrophyPanelMid = Color(0xFF162653)
private val TrophyPanelBottom = Color(0xFF0E1C42)
private val TrophyText = Color(0xFFEAF0FF)
private val TrophyMuted = Color(0xFFA8B8E3)
private val LegendaryGold = Color(0xFFFFD629)
private val EpicPurple = Color(0xFFB15CFF)
private val RareBlue = Color(0xFF62A7FF)
private val UncommonGreen = Color(0xFF4CDF87)
private val CommonSilver = Color(0xFFC6D0EA)

private enum class TrophyRarity(
    val label: String,
    val color: Color
) {
    Legendary("Legendary", LegendaryGold),
    Epic("Epic", EpicPurple),
    Rare("Rare", RareBlue),
    Uncommon("Uncommon", UncommonGreen),
    Common("Common", CommonSilver)
}

private data class TrophyEntry(
    val icon: String,
    val title: String,
    val subtitle: String,
    val rarity: TrophyRarity,
    val unlocked: Boolean = false,
    val progress: Float? = null,
    val progressText: String? = null
)

private val trophySections = listOf(
    TrophyRarity.Legendary to listOf(
        TrophyEntry("👑", "30 Day Legend", "Hit your goal for 30 days straight", TrophyRarity.Legendary),
        TrophyEntry("✴️", "500 Club", "Log 500 total drinks", TrophyRarity.Legendary, progress = 42f / 500f, progressText = "42 / 500"),
        TrophyEntry("✨", "Hydra Master", "Reach Level 10 with Hydra", TrophyRarity.Legendary, unlocked = true),
        TrophyEntry("🌙", "60 Day Legend", "Hit your goal for 60 days straight", TrophyRarity.Legendary),
        TrophyEntry("💎", "1000 Club", "Log 1000 total drinks", TrophyRarity.Legendary, progress = 42f / 1000f, progressText = "42 / 1000"),
        TrophyEntry("🔮", "30 Day Ritual", "Log at least one drink every day for 30 days", TrophyRarity.Legendary),
        TrophyEntry("👑", "Hydra Loyalist", "Keep your hydration streak going for 60 days", TrophyRarity.Legendary),
        TrophyEntry("🏆", "750 Club", "Log 750 total drinks", TrophyRarity.Legendary, progress = 42f / 750f, progressText = "42 / 750"),
        TrophyEntry("🏅", "Completionist", "Earn every other trophy", TrophyRarity.Legendary)
    ),
    TrophyRarity.Epic to listOf(
        TrophyEntry("⭐", "Perfect Day", "Meet all targets without going over any limits", TrophyRarity.Epic),
        TrophyEntry("🛡️", "14 Day Warrior", "Hit your goal for 14 days straight", TrophyRarity.Epic),
        TrophyEntry("🎯", "Centurion", "Log 100 total drinks", TrophyRarity.Epic, progress = 42f / 100f, progressText = "42 / 100"),
        TrophyEntry("🌙", "Rising Star", "Reach Level 5 with Hydra", TrophyRarity.Epic, unlocked = true),
        TrophyEntry("⭐", "Perfect Week", "Hit your goal every day for 7 days straight", TrophyRarity.Epic, progress = 1f / 7f, progressText = "1 / 7"),
        TrophyEntry("✴️", "7 Day Devotion", "Use the app every day for 7 days", TrophyRarity.Epic),
        TrophyEntry("💫", "Perfect Flow", "Log water to hit your daily goal precisely", TrophyRarity.Epic),
        TrophyEntry("🚀", "Overachiever", "Exceed your daily water goal by 20% or more", TrophyRarity.Epic),
        TrophyEntry("✨", "Glow Up Queen", "Miss a day, then hit your goal the next 3 days", TrophyRarity.Epic)
    ),
    TrophyRarity.Rare to listOf(
        TrophyEntry("👑", "Variety Queen", "Log 5 different drink types in one day", TrophyRarity.Rare),
        TrophyEntry("🥛", "Calcium Queen", "Hit your daily calcium goal", TrophyRarity.Rare),
        TrophyEntry("💎", "7 Day Streak", "Hit your goal for 7 days running", TrophyRarity.Rare),
        TrophyEntry("🍃", "Green Queen", "Only drink water and tea for an entire day", TrophyRarity.Rare),
        TrophyEntry("⚡", "Electrolyte Pro", "Drink something with all electrolytes", TrophyRarity.Rare),
        TrophyEntry("📊", "50 Drinks", "Log 50 total drinks", TrophyRarity.Rare, progress = 42f / 50f, progressText = "42 / 50"),
        TrophyEntry("📊", "250 Club", "Log 250 total drinks", TrophyRarity.Rare, progress = 42f / 250f, progressText = "42 / 250"),
        TrophyEntry("💎", "5-Star Week", "Hit your goal 5 days out of the last 5", TrophyRarity.Rare),
        TrophyEntry("⏰", "Steady Rhythm", "Space 3+ drinks at least 6 hours apart in a day", TrophyRarity.Rare),
        TrophyEntry("🎯", "Committed Queen", "Log at least one drink every day for 7 days", TrophyRarity.Rare),
        TrophyEntry("🎯", "Hydration Focus", "Hit your daily goal without going over any limits", TrophyRarity.Rare),
        TrophyEntry("📅", "Routine Builder", "Log drinks at similar times across 3 days", TrophyRarity.Rare),
        TrophyEntry("🌕", "Close Enough", "Reach at least 90% of your daily goal", TrophyRarity.Rare, unlocked = true)
    ),
    TrophyRarity.Uncommon to listOf(
        TrophyEntry("🌊", "Fully Hydrated", "Reach your daily water goal", TrophyRarity.Uncommon, unlocked = true),
        TrophyEntry("🏆", "2L Legend", "Log over 2 litres of water in a day", TrophyRarity.Uncommon, unlocked = true),
        TrophyEntry("🍷", "Responsible Sip", "Log alcohol but stay under your daily limit", TrophyRarity.Uncommon, unlocked = true),
        TrophyEntry("😌", "Caffeine Free", "Go a whole day without any caffeine", TrophyRarity.Uncommon),
        TrophyEntry("🦷", "Sugar Free Day", "Log no sugary drinks for an entire day", TrophyRarity.Uncommon),
        TrophyEntry("🔥", "3 Day Streak", "Hit your goal for 3 days running", TrophyRarity.Uncommon),
        TrophyEntry("🔥", "5 Day Streak", "Hit your goal for 5 days running", TrophyRarity.Uncommon),
        TrophyEntry("⚖️", "Balanced Day", "Log water, one other drink type, and no alcohol", TrophyRarity.Uncommon),
        TrophyEntry("💪", "Comeback Queen", "Miss a day and bounce back the very next day", TrophyRarity.Uncommon),
        TrophyEntry("💧", "Water Rescue", "Log water immediately after logging an alcoholic drink", TrophyRarity.Uncommon),
        TrophyEntry("🔟", "Double Digits", "Log 10 or more drinks in a single day", TrophyRarity.Uncommon),
        TrophyEntry("🌈", "All Day Flow", "Log drinks in the morning, afternoon, and evening", TrophyRarity.Uncommon),
        TrophyEntry("⏱️", "Steady Sipper", "Log at least 5 drinks spread across the day", TrophyRarity.Uncommon),
        TrophyEntry("🌅", "Early Hydration", "Log a drink within 30 minutes of waking up", TrophyRarity.Uncommon),
        TrophyEntry("💧", "Hydration Priority", "Log water before any other drink today", TrophyRarity.Uncommon),
        TrophyEntry("💡", "In Sync", "Log a drink shortly after opening the app", TrophyRarity.Uncommon)
    ),
    TrophyRarity.Common to listOf(
        TrophyEntry("💧", "First Sip", "Log your very first drink", TrophyRarity.Common, unlocked = true),
        TrophyEntry("🌅", "Early Bird", "Log a drink before 7am", TrophyRarity.Common, unlocked = true),
        TrophyEntry("🦉", "Night Owl", "Log a drink after 10pm", TrophyRarity.Common),
        TrophyEntry("☕", "Coffee Lover", "Log your first coffee or tea", TrophyRarity.Common, unlocked = true),
        TrophyEntry("💦", "Litre Club", "Log over 1 litre of water in a day", TrophyRarity.Common, unlocked = true),
        TrophyEntry("🧠", "Self Aware", "Log your mood for the first time", TrophyRarity.Common, unlocked = true),
        TrophyEntry("📊", "10 Drinks", "Log 10 total drinks", TrophyRarity.Common, progress = 1f, progressText = "42 / 10"),
        TrophyEntry("📊", "25 Drinks", "Log 25 total drinks", TrophyRarity.Common, progress = 1f, progressText = "42 / 25"),
        TrophyEntry("🌤️", "Morning Starter", "Log a drink before 9am", TrophyRarity.Common),
        TrophyEntry("💧", "Water First", "Log water as your very first drink of the day", TrophyRarity.Common),
        TrophyEntry("☀️", "Midday Check-In", "Log a drink between noon and 2pm", TrophyRarity.Common),
        TrophyEntry("🌊", "Triple Pour", "Log 3 or more drinks in a single day", TrophyRarity.Common),
        TrophyEntry("🔋", "Steady Flow", "Log at least 5 drinks across the day", TrophyRarity.Common, unlocked = true)
    )
)

@Composable
fun TrophiesScreen(state: HydraState) {
    var showGuide by remember { mutableStateOf(false) }
    val dynamicSections = trophySections.map { (rarity, entries) ->
        rarity to entries.map { it.withHydraState(state) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TrophyDeep)
    ) {
        TrophiesBackground()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(18.dp))
                    TrophiesTopHeader(onInfoClick = { showGuide = true })
                    Spacer(modifier = Modifier.height(15.dp))
                    TrophySummaryCards(state)
                    Spacer(modifier = Modifier.height(26.dp))
                }
            }
            dynamicSections.forEachIndexed { index, (rarity, entries) ->
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        TrophyCopy(
                            text = rarity.label,
                            color = rarity.color,
                            fontSize = 16,
                            lineHeight = 20,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
                itemsIndexed(entries) { i, entry ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (i > 0) Spacer(modifier = Modifier.height(8.dp))
                        TrophyAchievementCard(entry = entry)
                    }
                }
                if (index != trophySections.lastIndex) {
                    item { Spacer(modifier = Modifier.height(26.dp)) }
                }
            }
            item { Spacer(modifier = Modifier.height(158.dp)) }
        }

        if (showGuide) {
            TrophyGuideOverlay(onDismiss = { showGuide = false })
        }
    }
}

@Composable
private fun TrophiesBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF122044),
                    Color(0xFF101D3F),
                    Color(0xFF0D1936),
                    Color(0xFF081126)
                ),
                startY = 0f,
                endY = size.height
            )
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF335EC9).copy(alpha = 0.16f),
                    Color(0xFF1E397F).copy(alpha = 0.07f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.68f, size.height * 0.12f),
                radius = size.width * 0.86f
            ),
            center = Offset(size.width * 0.68f, size.height * 0.12f),
            radius = size.width * 0.86f
        )
    }
}

@Composable
private fun TrophiesTopHeader(onInfoClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TrophyCopy(
            text = "Trophies",
            color = TrophyText,
            fontSize = 27,
            lineHeight = 33,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
        InfoOrb(onClick = onInfoClick)
    }
}

@Composable
private fun InfoOrb(onClick: () -> Unit) {
    val motion = rememberInfiniteTransition(label = "trophyInfoMotion")
    val glowBreath = motion.animateFloat(
        initialValue = 0.72f,
        targetValue = 1.16f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "trophyInfoGlow"
    ).value
    val glintProgress = motion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "trophyInfoGlintOrbit"
    ).value

    Box(
        modifier = Modifier
            .size(52.dp)
            .offset(y = 6.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val orbRadius = size.width * 0.40f
            val orbCenter = Offset(size.width * 0.51f, size.height * 0.52f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFB9CCFF).copy(alpha = 0.16f * glowBreath),
                        Color(0xFF5D82EA).copy(alpha = 0.08f * glowBreath),
                        Color.Transparent
                    ),
                    center = orbCenter,
                    radius = size.width * 0.58f
                ),
                radius = size.width * 0.58f,
                center = orbCenter
            )
            fun orbit(angleDegrees: Float): Offset {
                val radians = angleDegrees * PI / 180.0
                return Offset(
                    x = orbCenter.x + cos(radians).toFloat() * orbRadius,
                    y = orbCenter.y + sin(radians).toFloat() * orbRadius
                )
            }
            fun glint(center: Offset, radius: Float, alpha: Float) {
                drawLine(
                    color = Color.White.copy(alpha = alpha),
                    start = Offset(center.x - radius, center.y),
                    end = Offset(center.x + radius, center.y),
                    strokeWidth = 0.9.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.White.copy(alpha = alpha),
                    start = Offset(center.x, center.y - radius),
                    end = Offset(center.x, center.y + radius),
                    strokeWidth = 0.9.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            val travelSpan = 0.84f
            val travelProgress = (glintProgress / travelSpan).coerceIn(0f, 1f)
            val headAngle = -90f + travelProgress * 360f
            val fadeIn = (glintProgress / 0.05f).coerceIn(0f, 1f)
            val remaining = (1f - travelProgress).coerceIn(0f, 1f)
            val fadeOut = remaining * remaining
            val visible = if (glintProgress >= travelSpan) 0f else fadeIn * fadeOut
            val baseAlpha = (0.62f + 0.28f * glowBreath) * visible
            glint(
                center = orbit(headAngle - 16f),
                radius = orbRadius * 0.10f,
                alpha = baseAlpha * 0.70f
            )
            glint(
                center = orbit(headAngle + 8f),
                radius = orbRadius * 0.13f,
                alpha = baseAlpha
            )
        }
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .border(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.67f),
                            Color(0xFFC4D6FF).copy(alpha = 0.41f),
                            Color(0xFF4E80FF).copy(alpha = 0.21f)
                        )
                    ),
                    width = 1.dp,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF829BD4).copy(alpha = 0.68f),
                            Color(0xFF334F99).copy(alpha = 0.76f),
                            Color(0xFF112B63).copy(alpha = 0.94f),
                            Color(0xFF071844).copy(alpha = 0.98f)
                        ),
                        center = Offset(size.width * 0.34f, size.height * 0.30f),
                        radius = size.width * 0.88f
                    ),
                    radius = size.width / 2f,
                    center = center
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFEAF1FF).copy(alpha = 0.21f),
                            Color(0xFFAFC7FF).copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.43f, size.height * 0.36f),
                        radius = size.width * 0.40f
                    ),
                    radius = size.width * 0.40f,
                    center = Offset(size.width * 0.43f, size.height * 0.36f)
                )
                drawCircle(
                    color = Color(0xFFBFD0FF).copy(alpha = 0.22f),
                    radius = size.width * 0.46f,
                    center = center,
                    style = Stroke(width = 0.7.dp.toPx())
                )
            }
            BasicText(
                text = "i",
                modifier = Modifier.offset(y = (-1).dp),
                style = TextStyle(
                    color = Color(0xFFD8E0EE).copy(alpha = 0.86f),
                    fontSize = 36.sp,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun TrophyGuideOverlay(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF020713).copy(alpha = 0.68f))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            event.changes.forEach { it.consume() }
                        }
                    }
                }
        )

        val sheetShape = RoundedCornerShape(
            topStart = 34.dp,
            topEnd = 34.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.86f)
                .clip(sheetShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF20366E).copy(alpha = 0.97f),
                            Color(0xFF172C61).copy(alpha = 0.98f),
                            Color(0xFF102653).copy(alpha = 0.99f)
                        )
                    )
                )
                .border(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.60f),
                            Color.White.copy(alpha = 0.16f),
                            TrophyBlue.copy(alpha = 0.10f)
                        )
                    ),
                    width = 1.2.dp,
                    shape = sheetShape
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 30.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(52.dp)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(TrophyBlue.copy(alpha = 0.20f))
            )

            Spacer(modifier = Modifier.height(38.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(66.dp)
                    .clip(CircleShape)
                    .background(TrophyBlue.copy(alpha = 0.22f))
                    .border(
                        color = Color.White.copy(alpha = 0.30f),
                        width = 1.dp,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                BasicText(text = "💧", style = TextStyle(fontSize = 34.sp))
            }

            Spacer(modifier = Modifier.height(28.dp))
            TrophyCopy(
                text = "Hydra's Guide",
                color = TrophyText,
                fontSize = 31,
                lineHeight = 38,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            TrophyCopy(
                text = "Let me walk you through it",
                color = TrophyMuted.copy(alpha = 0.86f),
                fontSize = 17,
                lineHeight = 23,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            GuideDivider(top = 34.dp, bottom = 28.dp)

            GuideSectionTitle(icon = "🏆", title = "Trophies")
            Spacer(modifier = Modifier.height(14.dp))
            TrophyCopy(
                text = "Every good habit earns a trophy.\nThat's my way of saying - I noticed. 🌟",
                color = TrophyMuted.copy(alpha = 0.92f),
                fontSize = 17,
                lineHeight = 23,
                fontStyle = FontStyle.Italic
            )

            GuideDivider(top = 34.dp, bottom = 28.dp)

            GuideSectionTitle(icon = "✨", title = "Trophy Rewards")
            Spacer(modifier = Modifier.height(14.dp))
            TrophyCopy(
                text = "Rarer trophies reward more XP.",
                color = TrophyMuted.copy(alpha = 0.82f),
                fontSize = 17,
                lineHeight = 23,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(24.dp))
            RewardValueRow(color = LegendaryGold, label = "Legendary", value = "+550 XP")
            RewardValueRow(color = EpicPurple, label = "Epic", value = "+300 XP")
            RewardValueRow(color = RareBlue, label = "Rare", value = "+150 XP")
            RewardValueRow(color = UncommonGreen, label = "Uncommon", value = "+50 XP")
            RewardValueRow(color = CommonSilver, label = "Common", value = "+25 XP", divider = false)

            GuideDivider(top = 34.dp, bottom = 28.dp)

            GuideSectionTitle(icon = "💧", title = "Daily Actions")
            Spacer(modifier = Modifier.height(14.dp))
            TrophyCopy(
                text = "Every time you show up, I reward it.",
                color = TrophyMuted.copy(alpha = 0.82f),
                fontSize = 17,
                lineHeight = 23,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(24.dp))
            GuideValueRow(label = "Log a drink", value = "+5 XP")
            GuideValueRow(label = "Open the app", value = "+2 XP")
            GuideValueRow(label = "Log your mood", value = "+3 XP", divider = false)

            GuideDivider(top = 34.dp, bottom = 28.dp)

            GuideSectionTitle(icon = "🎯", title = "Milestones")
            Spacer(modifier = Modifier.height(28.dp))
            GuideValueRow(label = "Hit your daily goal", value = "+50 XP")
            GuideValueRow(label = "Earn a trophy", value = "25-550 XP", divider = false)

            GuideDivider(top = 34.dp, bottom = 28.dp)

            TrophyCopy(
                text = "Just keep showing up.\nEvery sip, every log, every day.\nI'm cheering for you 💙",
                color = TrophyMuted.copy(alpha = 0.88f),
                fontSize = 17,
                lineHeight = 24,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(38.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF4E80FF), Color(0xFF366EF2))
                        )
                    )
                    .clickable(onClick = onDismiss)
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                TrophyCopy(
                    text = "Got it 💕",
                    color = Color(0xFF07142F),
                    fontSize = 24,
                    lineHeight = 28,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(168.dp))
        }
    }
}

@Composable
private fun GuideSectionTitle(icon: String, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicText(text = icon, style = TextStyle(fontSize = 25.sp))
        Spacer(modifier = Modifier.width(10.dp))
        TrophyCopy(
            text = title,
            color = TrophyBlue,
            fontSize = 21,
            lineHeight = 25,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun RewardValueRow(
    color: Color,
    label: String,
    value: String,
    divider: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(11.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.76f))
        )
        Spacer(modifier = Modifier.width(20.dp))
        TrophyCopy(
            text = label,
            color = TrophyText.copy(alpha = 0.86f),
            fontSize = 18,
            lineHeight = 22,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.weight(1f)
        )
        TrophyCopy(
            text = value,
            color = TrophyBlue,
            fontSize = 18,
            lineHeight = 22,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.End
        )
    }
    if (divider) {
        GuideHairline()
    }
}

@Composable
private fun GuideValueRow(
    label: String,
    value: String,
    divider: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TrophyCopy(
            text = label,
            color = TrophyText.copy(alpha = 0.86f),
            fontSize = 18,
            lineHeight = 22,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.weight(1f)
        )
        TrophyCopy(
            text = value,
            color = TrophyBlue,
            fontSize = 18,
            lineHeight = 22,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.End
        )
    }
    if (divider) {
        GuideHairline()
    }
}

@Composable
private fun GuideDivider(top: Dp, bottom: Dp) {
    Spacer(modifier = Modifier.height(top))
    GuideHairline()
    Spacer(modifier = Modifier.height(bottom))
}

@Composable
private fun GuideHairline() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.7.dp)
            .background(Color.White.copy(alpha = 0.045f))
    )
}

@Composable
private fun TrophySummaryCards(state: HydraState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TrophySummaryCard(
            label = "Earned",
            value = "${state.earnedAchievements.size}/60",
            icon = "🏆",
            modifier = Modifier.weight(1f)
        )
        TrophySummaryCard(
            label = "From Trophies",
            value = "${state.earnedAchievements.size * 25} HP",
            icon = "✨",
            modifier = Modifier.weight(1f)
        )
    }
}

private fun TrophyEntry.withHydraState(state: HydraState): TrophyEntry {
    val unlocked = when (title) {
        "First Sip" -> "FIRST_SIP" in state.earnedAchievements
        "Early Bird" -> "EARLY_BIRD" in state.earnedAchievements
        "Night Owl" -> "NIGHT_OWL" in state.earnedAchievements
        "Fully Hydrated" -> "HYDRATED_DAY" in state.earnedAchievements
        "Calcium Queen" -> "CALCIUM_HERO" in state.earnedAchievements
        "Variety Queen" -> "VARIETY_PACK" in state.earnedAchievements
        "Centurion" -> "MILESTONE_100" in state.earnedAchievements
        else -> false
    }
    val total = state.logs.size
    val threshold = when {
        title == "10 Drinks" -> 10
        title == "25 Drinks" -> 25
        title == "50 Drinks" -> 50
        title == "100 total drinks" || title == "Centurion" -> 100
        title == "250 Club" -> 250
        title == "500 Club" -> 500
        title == "750 Club" -> 750
        title == "1000 Club" -> 1_000
        else -> null
    }
    return copy(
        unlocked = unlocked,
        progress = threshold?.let { (total.toFloat() / it).coerceIn(0f, 1f) } ?: progress,
        progressText = threshold?.let { "$total / $it" } ?: progressText
    )
}

@Composable
private fun TrophySummaryCard(
    label: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(90.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A2E67).copy(alpha = 0.88f),
                        Color(0xFF132656).copy(alpha = 0.94f)
                    )
                )
            )
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        TrophyCopy(
            text = label,
            color = TrophyMuted.copy(alpha = 0.84f),
            fontSize = 14,
            lineHeight = 17,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.TopStart)
        )
        BasicText(
            text = icon,
            modifier = Modifier.align(Alignment.TopEnd),
            style = TextStyle(fontSize = 25.sp)
        )
        TrophyCopy(
            text = value,
            color = TrophyBlue,
            fontSize = 22,
            lineHeight = 26,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
private fun TrophyAchievementCard(entry: TrophyEntry) {
    val contentAlpha = if (entry.unlocked) 1f else 0.34f
    val borderAlpha = if (entry.unlocked) 0.68f else 0.30f
    val longSubtitle = entry.subtitle.length > 38
    val hasProgress = entry.progress != null && entry.progressText != null
    val rowHeight = when {
        hasProgress && longSubtitle -> 124.dp
        hasProgress -> 112.dp
        longSubtitle -> 112.dp
        else -> 98.dp
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        TrophyPanelTop.copy(alpha = if (entry.unlocked) 0.70f else 0.42f),
                        TrophyPanelMid.copy(alpha = if (entry.unlocked) 0.86f else 0.56f),
                        TrophyPanelBottom.copy(alpha = if (entry.unlocked) 0.92f else 0.62f)
                    )
                )
            )
            .border(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = borderAlpha),
                        Color.White.copy(alpha = borderAlpha * 0.22f),
                        TrophyBlue.copy(alpha = borderAlpha * 0.08f)
                    )
                ),
                width = 1.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(start = 20.dp, end = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TrophyIconBox(entry = entry, alpha = contentAlpha)
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
            TrophyCopy(
                text = entry.title,
                color = TrophyText.copy(alpha = 0.98f * contentAlpha),
                fontSize = 16,
                lineHeight = 20,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                TrophyCopy(
                text = entry.subtitle,
                color = TrophyMuted.copy(alpha = 0.88f * contentAlpha),
                fontSize = 13,
                lineHeight = 17,
                fontStyle = FontStyle.Italic,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                RarityBadge(rarity = entry.rarity, alpha = contentAlpha)
                if (hasProgress) {
                    Spacer(modifier = Modifier.height(10.dp))
                    TrophyProgress(entry = entry, alpha = contentAlpha)
                }
            }
            TrophyStateIcon(entry = entry, alpha = contentAlpha)
        }
    }
}

@Composable
private fun TrophyIconBox(entry: TrophyEntry, alpha: Float) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        entry.rarity.color.copy(alpha = 0.18f * alpha + 0.05f),
                        Color(0xFF233861).copy(alpha = 0.78f * alpha + 0.10f),
                        Color(0xFF17264E).copy(alpha = 0.84f * alpha + 0.10f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = entry.icon,
            style = TextStyle(fontSize = 26.sp)
        )
    }
}

@Composable
private fun RarityBadge(rarity: TrophyRarity, alpha: Float, compact: Boolean = false) {
    Box(
        modifier = Modifier
            .height(if (compact) 26.dp else 30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(rarity.color.copy(alpha = 0.16f * alpha + 0.08f))
            .padding(horizontal = if (compact) 10.dp else 12.dp),
        contentAlignment = Alignment.Center
    ) {
        TrophyCopy(
            text = rarity.label,
            color = rarity.color.copy(alpha = 0.94f * alpha + 0.10f),
            fontSize = if (compact) 11 else 12,
            lineHeight = if (compact) 13 else 14,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun TrophyProgress(entry: TrophyEntry, alpha: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .clip(CircleShape)
                .background(Color(0xFF29407C).copy(alpha = 0.42f * alpha + 0.12f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(entry.progress?.coerceIn(0f, 1f) ?: 0f)
                    .height(2.dp)
                    .clip(CircleShape)
                    .background(entry.rarity.color.copy(alpha = 0.78f * alpha + 0.12f))
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        TrophyCopy(
            text = entry.progressText.orEmpty(),
            color = TrophyMuted.copy(alpha = 0.72f * alpha),
            fontSize = 12,
            lineHeight = 14,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun TrophyStateIcon(entry: TrophyEntry, alpha: Float) {
    if (entry.unlocked) {
        TrophyCopy(
            text = "✓",
            color = entry.rarity.color.copy(alpha = 0.98f),
            fontSize = 32,
            lineHeight = 34,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(28.dp)
        )
    } else {
        TrophyLockIcon(tint = entry.rarity.color)
    }
}

@Composable
private fun TrophyLockIcon(tint: Color) {
    Canvas(modifier = Modifier.size(width = 28.dp, height = 28.dp)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f

        val bodyLeft = w * 0.18f
        val bodyRight = w * 0.82f
        val bodyTop = h * 0.46f
        val bodyBottom = h * 0.93f
        val bodyCornerR = w * 0.13f
        val bodyW = bodyRight - bodyLeft
        val bodyH = bodyBottom - bodyTop

        val shackleStrokeW = w * 0.115f
        val shackleHalfW = w * 0.215f
        val shackleTopY = h * 0.10f
        val shackleBottomY = bodyTop + shackleStrokeW * 0.38f
        val shackleArcDiameter = shackleHalfW * 2f

        val shacklePath = Path().apply {
            moveTo(cx - shackleHalfW, shackleBottomY)
            lineTo(cx - shackleHalfW, shackleTopY + shackleHalfW)
            arcTo(
                rect = Rect(
                    cx - shackleHalfW,
                    shackleTopY,
                    cx + shackleHalfW,
                    shackleTopY + shackleArcDiameter
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            lineTo(cx + shackleHalfW, shackleBottomY)
        }

        drawPath(
            path = shacklePath,
            color = Color(0xFF120902).copy(alpha = 0.62f),
            style = Stroke(width = shackleStrokeW + 1.2.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            path = shacklePath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    tint.copy(alpha = 1f).lighten(0.35f),
                    tint,
                    tint.darken(0.40f)
                ),
                startY = shackleTopY,
                endY = shackleBottomY
            ),
            style = Stroke(width = shackleStrokeW, cap = StrokeCap.Round)
        )
        drawPath(
            path = shacklePath,
            color = Color.White.copy(alpha = 0.34f),
            style = Stroke(width = shackleStrokeW * 0.22f, cap = StrokeCap.Round)
        )

        drawRoundRect(
            color = Color(0xFF120902).copy(alpha = 0.46f),
            topLeft = Offset(bodyLeft, bodyTop + 0.8.dp.toPx()),
            size = Size(bodyW, bodyH),
            cornerRadius = CornerRadius(bodyCornerR, bodyCornerR)
        )
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    tint.lighten(0.45f),
                    tint.lighten(0.10f),
                    tint.darken(0.20f),
                    tint.darken(0.55f)
                ),
                startY = bodyTop,
                endY = bodyBottom
            ),
            topLeft = Offset(bodyLeft, bodyTop),
            size = Size(bodyW, bodyH),
            cornerRadius = CornerRadius(bodyCornerR, bodyCornerR)
        )
        drawRoundRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.52f),
                    Color.White.copy(alpha = 0.12f),
                    Color.Transparent
                ),
                center = Offset(bodyLeft + bodyW * 0.32f, bodyTop + bodyH * 0.22f),
                radius = bodyW * 0.58f
            ),
            topLeft = Offset(bodyLeft, bodyTop),
            size = Size(bodyW, bodyH),
            cornerRadius = CornerRadius(bodyCornerR, bodyCornerR)
        )
        drawRoundRect(
            color = tint.lighten(0.55f).copy(alpha = 0.65f),
            topLeft = Offset(bodyLeft, bodyTop),
            size = Size(bodyW, bodyH),
            cornerRadius = CornerRadius(bodyCornerR, bodyCornerR),
            style = Stroke(width = 0.75.dp.toPx())
        )

        val keyholeCx = cx
        val keyholeCy = bodyTop + bodyH * 0.40f
        val keyholeR = w * 0.075f
        val slotBottomY = bodyTop + bodyH * 0.76f
        val slotHalfWAtBottom = w * 0.052f

        val keyholePath = Path().apply {
            addOval(Rect(keyholeCx - keyholeR, keyholeCy - keyholeR, keyholeCx + keyholeR, keyholeCy + keyholeR))
            moveTo(keyholeCx - keyholeR * 0.55f, keyholeCy + keyholeR * 0.55f)
            lineTo(keyholeCx - slotHalfWAtBottom, slotBottomY)
            lineTo(keyholeCx + slotHalfWAtBottom, slotBottomY)
            lineTo(keyholeCx + keyholeR * 0.55f, keyholeCy + keyholeR * 0.55f)
            close()
        }

        drawPath(
            path = keyholePath,
            color = Color(0xFF1B0F02).copy(alpha = 0.94f)
        )
    }
}

private fun Color.lighten(amount: Float): Color {
    val a = amount.coerceIn(0f, 1f)
    return Color(
        red = red + (1f - red) * a,
        green = green + (1f - green) * a,
        blue = blue + (1f - blue) * a,
        alpha = alpha
    )
}

private fun Color.darken(amount: Float): Color {
    val a = amount.coerceIn(0f, 1f)
    return Color(
        red = red * (1f - a),
        green = green * (1f - a),
        blue = blue * (1f - a),
        alpha = alpha
    )
}

@Composable
private fun TrophyCopy(
    text: String,
    color: Color,
    fontSize: Int,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = AppFont,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    lineHeight: Int = fontSize + 4,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    BasicText(
        text = text,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
        style = TextStyle(
            color = color,
            fontSize = fontSize.sp,
            lineHeight = lineHeight.sp,
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            textAlign = textAlign
        )
    )
}
