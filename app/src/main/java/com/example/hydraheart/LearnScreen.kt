package com.example.hydraheart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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

private val LearnDeep = Color(0xFF09132D)
private val LearnBlue = Color(0xFF4F82FF)
private val LearnText = Color(0xFFE4ECFF)
private val LearnMuted = Color(0xFFA9B6DE)
private val LearnFaint = Color(0xFF7484B4)
private val LearnPanelTop = Color(0xFF2B3E6D)
private val LearnPanelMid = Color(0xFF162653)
private val LearnPanelBottom = Color(0xFF0E1B3D)

private data class LearnArticle(
    val icon: String,
    val title: String,
    val subtitle: String
)

private data class LearnSection(
    val name: String,
    val note: String,
    val articles: List<LearnArticle>
)

private val learnSections = listOf(
    LearnSection(
        name = "HYDRATION",
        note = "Even 1% dehydration can affect your focus and reaction time",
        articles = listOf(
            LearnArticle("💧", "Why Water Matters", "You're probably slightly dehydrated right now..."),
            LearnArticle("🌡️", "Signs of Mild Dehydration", "You'll feel it in your mood before you ever feel thirsty"),
            LearnArticle("⚠️", "Signs of Severe Dehydration", "Know the difference before it becomes urgent"),
            LearnArticle("🌊", "Can You Drink Too Much Water?", "Can you actually drink too much water?"),
            LearnArticle("🌅", "Morning vs Evening Hydration", "The most important glass of water is the one you time well"),
            LearnArticle("🤒", "Why Dehydration Causes Headaches", "Most headaches respond to water faster than you think"),
            LearnArticle("😴", "Dehydration & Fatigue", "Fix your energy levels by fixing this one habit")
        )
    ),
    LearnSection(
        name = "ELECTROLYTES",
        note = "Water alone isn't always enough to keep your body balanced",
        articles = listOf(
            LearnArticle("⚡", "Your Electrolyte Balance", "Your heartbeat depends on minerals most people ignore"),
            LearnArticle("🧂", "When You Need More Than Just Water", "Plain water isn't always the right answer"),
            LearnArticle("📉", "Signs Your Electrolytes Are Low", "Cramps, poor sleep, anxiety — they could all start here"),
            LearnArticle("🥥", "Natural Electrolyte Sources", "Skip the sports drinks — your kitchen already has options")
        )
    ),
    LearnSection(
        name = "CAFFEINE",
        note = "Caffeine has a half-life of 5-6 hours — timing matters more than amount",
        articles = listOf(
            LearnArticle("☕", "Caffeine: Friend & Foe", "You're probably not using caffeine as effectively as you could"),
            LearnArticle("⏰", "The Best Time to Drink Caffeine", "The best time for your first coffee isn't first thing"),
            LearnArticle("😰", "Caffeine & Anxiety", "That second coffee might be feeding your anxiety"),
            LearnArticle("🌙", "Caffeine & Your Sleep", "Caffeine is still in your system at midnight even when you feel fine"),
            LearnArticle("⚡", "How to Avoid Caffeine Crashes", "The 3pm slump has a very specific biological cause")
        )
    ),
    LearnSection(
        name = "SUGAR",
        note = "Liquid sugar is absorbed faster than your body can regulate it",
        articles = listOf(
            LearnArticle("🍬", "Understanding Sugar", "One can of cola is almost your entire daily sugar limit"),
            LearnArticle("👀", "Hidden Sugar in Drinks", "The drinks labelled healthy often have the most sugar"),
            LearnArticle("📊", "Why Sugar Crashes Happen", "Your afternoon energy crash is entirely predictable"),
            LearnArticle("🔬", "Artificial Sweeteners Explained", "Sweeteners aren't the villain they're made out to be")
        )
    ),
    LearnSection(
        name = "ALCOHOL",
        note = "Alcohol makes you lose more fluid than you actually drink",
        articles = listOf(
            LearnArticle("🍷", "Alcohol & Dehydration", "Alcohol takes more fluid from your body than it gives back"),
            LearnArticle("🛏️", "Alcohol & Sleep Quality", "That nightcap is costing you deep sleep you wanted"),
            LearnArticle("🥴", "Why Hangovers Happen", "A hangover is three different problems happening at once"),
            LearnArticle("🫧", "Staying Hydrated While Drinking", "Two simple habits that make a real difference tomorrow")
        )
    ),
    LearnSection(
        name = "ENERGY DRINKS",
        note = "Energy drinks are engineered for a spike, not for sustained energy",
        articles = listOf(
            LearnArticle("🔋", "Energy Drinks Decoded", "Energy drinks are engineered for a spike, not calm focus"),
            LearnArticle("⚡", "Why Energy Drinks Hit Harder Than Coffee", "The same caffeine hits very differently when it is loaded"),
            LearnArticle("📉", "The Crash After an Energy Drink, Explained", "The harder the spike, the harder the fall — the pattern is built in"),
            LearnArticle("🍹", "Mixing Energy Drinks With Alcohol", "It makes you feel sober while your blood alcohol keeps climbing"),
            LearnArticle("🏷️", "Are Sugar-Free Energy Drinks Actually Better?", "Sugar-free fixes the sugar problem, but everything else still matters")
        )
    ),
    LearnSection(
        name = "BEAUTY & SKIN",
        note = "Skincare works at the surface — hydration works from within",
        articles = listOf(
            LearnArticle("✨", "Hydration & Your Skin", "No serum works as well as drinking enough water consistently"),
            LearnArticle("💋", "Why Your Lips Get Dry", "Reach for water before you reach for lip balm"),
            LearnArticle("👁️", "Dehydration & Dark Circles", "Dehydration makes dark circles visibly worse for many people"),
            LearnArticle("💇", "Hydration & Hair Health", "Healthy hair starts from inside, not from what you put on it")
        )
    ),
    LearnSection(
        name = "WELLBEING",
        note = "Mood shifts are often the first signal that you need to drink more",
        articles = listOf(
            LearnArticle("🦴", "Calcium for Strong Bones", "Calcium matters long after you stop growing"),
            LearnArticle("🧠", "Brain Fog & Dehydration", "Brain fog is usually a symptom, not a condition"),
            LearnArticle("🌸", "Mood & Hydration", "Your mood is more sensitive to dehydration than you think"),
            LearnArticle("🌙", "Hydration & Sleep Quality", "Hydration and sleep are quietly affecting each other")
        )
    ),
    LearnSection(
        name = "HABITS",
        note = "People who drink consistently have designed better habits, not more willpower",
        articles = listOf(
            LearnArticle("✅", "Building a Hydration Habit", "People who drink consistently haven't got more discipline — they built cues"),
            LearnArticle("🔔", "Why You Keep Forgetting to Drink", "Forgetting to drink isn't a lack of discipline — it's a missing trigger"),
            LearnArticle("🔄", "Recovering from a Low-Intake Day", "Trying to catch up on water in the evening does not need to feel chaotic")
        )
    )
)

@Composable
fun LearnScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LearnDeep)
    ) {
        LearnBackground()

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
                    Spacer(modifier = Modifier.height(16.dp))
                    LearnHeader()
                    Spacer(modifier = Modifier.height(10.dp))
                    SearchPill()
                    Spacer(modifier = Modifier.height(36.dp))
                    TodayLessonCard()
                    Spacer(modifier = Modifier.height(45.dp))
                }
            }
            learnSections.forEachIndexed { index, section ->
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SectionLabel(name = section.name)
                        Spacer(modifier = Modifier.height(10.dp))
                        LearnCopy(
                            text = section.note,
                            color = LearnMuted.copy(alpha = 0.45f),
                            fontSize = 12,
                            lineHeight = 17,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                itemsIndexed(section.articles) { i, article ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (i > 0) Spacer(modifier = Modifier.height(8.dp))
                        LearnArticleRow(article = article)
                    }
                }
                if (index != learnSections.lastIndex) {
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                }
            }
            item { Spacer(modifier = Modifier.height(154.dp)) }
        }
    }
}

@Composable
private fun LearnBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF142145),
                    Color(0xFF101C3E),
                    Color(0xFF0B1734),
                    Color(0xFF081126)
                ),
                startY = 0f,
                endY = size.height
            )
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF345BC0).copy(alpha = 0.15f),
                    Color(0xFF1E397F).copy(alpha = 0.07f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.56f, size.height * 0.15f),
                radius = size.width * 0.92f
            ),
            center = Offset(size.width * 0.56f, size.height * 0.15f),
            radius = size.width * 0.92f
        )
    }
}

@Composable
private fun LearnHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        LearnCopy(
            text = "Learn",
            color = LearnText,
            fontSize = 32,
            lineHeight = 38,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(8.dp))
        LearnCopy(
            text = "Everything you need to know about what you drink.",
            color = LearnMuted.copy(alpha = 0.58f),
            fontSize = 15,
            lineHeight = 19,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun SearchPill() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF203D8B).copy(alpha = 0.76f),
                        Color(0xFF18326F).copy(alpha = 0.88f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = LearnBlue.copy(alpha = 0.10f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = "🔍",
            style = TextStyle(fontSize = 15.sp)
        )
        Spacer(modifier = Modifier.width(13.dp))
        LearnCopy(
            text = "Search hydration, sleep, caffeine...",
            color = LearnMuted.copy(alpha = 0.48f),
            fontSize = 14,
            lineHeight = 17,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun TodayLessonCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(229.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        LearnPanelTop.copy(alpha = 0.82f),
                        LearnPanelMid.copy(alpha = 0.86f),
                        LearnPanelBottom.copy(alpha = 0.92f)
                    )
                )
            )
            .border(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.58f),
                        Color.White.copy(alpha = 0.18f),
                        LearnBlue.copy(alpha = 0.09f)
                    )
                ),
                width = 1.dp,
                shape = RoundedCornerShape(25.dp)
            )
            .padding(start = 24.dp, top = 20.dp, end = 23.dp, bottom = 20.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.05f),
                        Color.Transparent
                    )
                ),
                topLeft = Offset(size.width * 0.09f, size.height * 0.10f),
                size = Size(size.width * 0.84f, size.height * 0.62f),
                cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(0.76f)
        ) {
            LearnCopy(
                text = "Today's Lesson  ·  Refreshes daily",
                color = LearnMuted.copy(alpha = 0.30f),
                fontSize = 11,
                lineHeight = 14,
                fontStyle = FontStyle.Italic,
                letterSpacing = 1
            )
            Spacer(modifier = Modifier.height(18.dp))
            LearnCopy(
                text = "How Much Water Do\nYou Need?",
                color = LearnText,
                fontSize = 21,
                lineHeight = 27,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(10.dp))
            LearnCopy(
                text = "Your perfect water intake isn't actually 2\nlitres",
                color = LearnMuted.copy(alpha = 0.66f),
                fontSize = 12,
                lineHeight = 17,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(12.dp))
            ReadArticleButton()
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(52.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF315F9A).copy(alpha = 0.70f),
                            Color(0xFF173D76).copy(alpha = 0.82f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "🥛",
                style = TextStyle(fontSize = 26.sp)
            )
        }
    }
}

@Composable
private fun ReadArticleButton() {
    Box(
        modifier = Modifier
            .width(108.dp)
            .height(29.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF245C90).copy(alpha = 0.92f),
                        Color(0xFF1F477B).copy(alpha = 0.92f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        LearnCopy(
            text = "Read article",
            color = Color(0xFF58CEF0).copy(alpha = 0.82f),
            fontSize = 14,
            lineHeight = 17,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SectionLabel(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LearnCopy(
            text = name,
            color = LearnFaint.copy(alpha = 0.34f),
            fontSize = 11,
            lineHeight = 14,
            fontStyle = FontStyle.Italic,
            letterSpacing = 4
        )
        Spacer(modifier = Modifier.width(15.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.035f))
        )
    }
}

@Composable
private fun LearnArticleRow(article: LearnArticle) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF273A68).copy(alpha = 0.88f),
                        Color(0xFF142655).copy(alpha = 0.91f),
                        Color(0xFF101D43).copy(alpha = 0.94f)
                    )
                )
            )
            .border(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.58f),
                        Color.White.copy(alpha = 0.16f),
                        LearnBlue.copy(alpha = 0.08f)
                    )
                ),
                width = 1.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { }
            .padding(start = 14.dp, end = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArticleIcon(icon = article.icon)
        Spacer(modifier = Modifier.width(13.dp))
        Column(modifier = Modifier.weight(1f)) {
            LearnCopy(
                text = article.title,
                color = LearnText.copy(alpha = 0.94f),
                fontSize = 14,
                lineHeight = 17,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            LearnCopy(
                text = article.subtitle,
                color = LearnMuted.copy(alpha = 0.70f),
                fontSize = 12,
                lineHeight = 15,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        LearnCopy(
            text = "›",
            color = LearnMuted.copy(alpha = 0.25f),
            fontSize = 28,
            lineHeight = 28,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ArticleIcon(icon: String) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF253D71).copy(alpha = 0.88f),
                        Color(0xFF1A2E62).copy(alpha = 0.92f),
                        Color(0xFF1F2759).copy(alpha = 0.92f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        LearnBlue.copy(alpha = 0.13f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.38f, size.height * 0.24f),
                    radius = size.width * 0.9f
                ),
                size = size,
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
            )
            drawRoundRect(
                color = Color.White.copy(alpha = 0.05f),
                size = size,
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                style = Stroke(width = 0.7.dp.toPx())
            )
        }
        BasicText(
            text = icon,
            style = TextStyle(fontSize = 19.sp)
        )
    }
}

@Composable
private fun LearnCopy(
    text: String,
    color: Color,
    fontSize: Int,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = AppFont,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    lineHeight: Int = fontSize + 4,
    textAlign: TextAlign = TextAlign.Start,
    letterSpacing: Int = 0,
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
            textAlign = textAlign,
            letterSpacing = letterSpacing.sp
        )
    )
}
