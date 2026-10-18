package com.hydraheart2.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.R
import com.hydraheart2.app.data.EducationData
import com.hydraheart2.app.data.EducationTopic
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.data.MythFact
import com.hydraheart2.app.data.TopicCategory
import com.hydraheart2.app.data.models.Gender
import com.hydraheart2.app.data.models.Profile
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.ui.components.HydraSpeechBubble
import com.hydraheart2.app.ui.navigation.PageEnter
import com.hydraheart2.app.ui.navigation.PageExit
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel
import java.util.Random

private val CATEGORY_INSIGHTS = mapOf(
    TopicCategory.HYDRATION to "Even 1% dehydration can affect your focus and reaction time",
    TopicCategory.ELECTROLYTES to "Water alone isn't always enough to keep your body balanced",
    TopicCategory.CAFFEINE to "Caffeine has a half-life of 5–6 hours — timing matters more than amount",
    TopicCategory.SUGAR to "Liquid sugar is absorbed faster than your body can regulate it",
    TopicCategory.ALCOHOL to "Alcohol makes you lose more fluid than you actually drink",
    TopicCategory.ENERGY_DRINKS to "Energy drinks are engineered for a spike, not for sustained energy",
    TopicCategory.BEAUTY to "Skincare works at the surface — hydration works from within",
    TopicCategory.WELLBEING to "Mood shifts are often the first signal that you need to drink more",
    TopicCategory.HABITS to "People who drink consistently have designed better habits, not more willpower",
)

private val SEARCH_SUGGESTIONS = listOf(
    "Why am I tired?",
    "Best time for coffee",
    "How much water do I need?",
    "Caffeine and sleep",
    "Signs of dehydration",
)

private fun selectFeaturedTopic(topics: List<EducationTopic>): EducationTopic {
    val dayEpoch = (System.currentTimeMillis() - 3_600_000L) / 86_400_000L
    val rng = Random(dayEpoch)
    return topics[rng.nextInt(topics.size)]
}

private fun EducationTopic.resolvedSummary(profile: Profile): String =
    if (profile.gender == Gender.MALE && summaryMale != null) summaryMale else summary

private fun EducationTopic.resolvedKeyFacts(profile: Profile): List<String> =
    if (profile.gender == Gender.MALE && keyFactsMale != null) keyFactsMale else keyFacts

private fun EducationTopic.resolvedWhatThisMeansForYou(profile: Profile): String =
    if (profile.gender == Gender.MALE && whatThisMeansForYouMale != null) whatThisMeansForYouMale
    else whatThisMeansForYou

@Composable
fun EducationScreen(viewModel: HydraViewModel) {
    val theme = viewModel.currentTheme
    val profile = viewModel.profile

    var selectedTopic by remember { mutableStateOf<EducationTopic?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val viewedTopics = remember { mutableStateListOf<Int>() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val topicTitles = remember {
        EducationData.topics.associateWith { context.getString(it.titleRes) }
    }
    val featured = remember { selectFeaturedTopic(EducationData.topics) }

    val isFiltering by remember { derivedStateOf { searchQuery.isNotBlank() } }
    val filteredTopics by remember {
        derivedStateOf {
            if (searchQuery.isNotBlank()) {
                val q = searchQuery.trim().lowercase()
                EducationData.topics.filter { topic ->
                    topicTitles[topic]?.lowercase()?.contains(q) == true ||
                        topic.previewText.lowercase().contains(q) ||
                        topic.category.label.lowercase().contains(q)
                }
            } else {
                emptyList()
            }
        }
    }

    val groupedRemaining = remember(featured) {
        EducationData.topics.filter { it !== featured }.groupBy { it.category }
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
                .padding(bottom = 100.dp)
        ) {
            Text(
                stringResource(R.string.learn_title),
                style = MaterialTheme.typography.headlineLarge,
                color = theme.textPrimary.copy(alpha = if (theme.isLight) 0.88f else 0.92f),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp, bottom = 4.dp),
            )
            Text(
                stringResource(R.string.learn_hydra_teaser),
                style = MaterialTheme.typography.bodySmall,
                color = theme.textMuted.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 14.dp),
            )

            LearnSearchBar(
                query = searchQuery,
                onQuery = { q -> searchQuery = q },
                onClear = {
                    searchQuery = ""
                    focusManager.clearFocus()
                },
                theme = theme
            )

            if (!isFiltering) {
                Spacer(Modifier.height(28.dp))
                FeaturedCard(
                    topic = featured,
                    theme = theme,
                    onClick = {
                        if (featured.titleRes !in viewedTopics) viewedTopics += featured.titleRes
                        selectedTopic = featured
                    }
                )
                Spacer(Modifier.height(40.dp))

                TopicCategory.entries.forEach { category ->
                    val categoryTopics = groupedRemaining[category] ?: return@forEach
                    CategorySectionHeader(category = category, theme = theme)
                    CATEGORY_INSIGHTS[category]?.let { insight ->
                        InsightStrip(text = insight, theme = theme)
                        Spacer(Modifier.height(5.dp))
                    }
                    categoryTopics.forEach { topic ->
                        CompactTopicCard(
                            topic = topic,
                            theme = theme,
                            isViewed = topic.titleRes in viewedTopics,
                            onClick = {
                                if (topic.titleRes !in viewedTopics) viewedTopics += topic.titleRes
                                selectedTopic = topic
                            }
                        )
                    }
                    Spacer(Modifier.height(26.dp))
                }
            } else {
                Spacer(Modifier.height(12.dp))
                FilteredResultsSection(
                    topics = filteredTopics,
                    searchQuery = searchQuery,
                    viewedTopics = viewedTopics,
                    theme = theme,
                    onTap = { topic ->
                        if (topic.titleRes !in viewedTopics) viewedTopics += topic.titleRes
                        selectedTopic = topic
                    }
                )
                Spacer(Modifier.height(22.dp))
            }
        }

        AnimatedVisibility(
            visible = selectedTopic != null,
            enter = PageEnter,
            exit = PageExit
        ) {
            selectedTopic?.let { topic ->
                BackHandler { selectedTopic = null }
                ArticleView(topic, profile, theme, viewModel) { selectedTopic = null }
            }
        }
    }
}

@Composable
private fun LearnSearchBar(query: String, onQuery: (String) -> Unit, onClear: () -> Unit, theme: HydraTheme) {
    val shape = RoundedCornerShape(12.dp)
    var isFocused by remember { mutableStateOf(false) }
    val showSuggestions = isFocused && query.isEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(theme.cardBg.copy(alpha = if (isFocused) 0.22f else 0.14f))
                .border(0.75.dp, theme.cardBorder.copy(alpha = if (isFocused) 0.22f else 0.12f), shape)
                .padding(horizontal = 13.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🔍", fontSize = 13.sp, color = theme.textMuted.copy(alpha = if (isFocused) 0.7f else 0.5f))
            Spacer(Modifier.width(9.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        stringResource(R.string.learn_search_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.textMuted.copy(alpha = 0.44f),
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQuery,
                    textStyle = MaterialTheme.typography.bodySmall.copy(color = theme.textPrimary),
                    cursorBrush = SolidColor(theme.accent),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onClear() }),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isFocused = it.isFocused }
                )
            }
            if (query.isNotEmpty()) {
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(theme.textMuted.copy(alpha = 0.12f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClear
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "×",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = theme.textMuted.copy(alpha = 0.7f),
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showSuggestions,
            enter = fadeIn(tween(160)) + expandVertically(tween(180)),
            exit = fadeOut(tween(100)) + shrinkVertically(tween(120))
        ) {
            Column {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SEARCH_SUGGESTIONS.forEach { suggestion ->
                        val chipShape = RoundedCornerShape(20.dp)
                        Box(
                            modifier = Modifier
                                .clip(chipShape)
                                .background(theme.cardBg.copy(alpha = 0.14f))
                                .border(0.75.dp, theme.cardBorder.copy(alpha = 0.13f), chipShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { onQuery(suggestion) }
                                .padding(horizontal = 11.dp, vertical = 5.dp)
                        ) {
                            Text(
                                suggestion,
                                style = MaterialTheme.typography.bodySmall,
                                color = theme.textMuted.copy(alpha = 0.72f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategorySectionHeader(category: TopicCategory, theme: HydraTheme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 4.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            category.label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.8.sp, fontWeight = FontWeight.Medium),
            color = theme.textMuted.copy(alpha = if (theme.isLight) 0.46f else 0.38f),
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = theme.cardBorder.copy(alpha = 0.13f),
            thickness = 0.5.dp
        )
    }
}

@Composable
private fun InsightStrip(text: String, theme: HydraTheme) {
    Text(
        text,
        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
        color = theme.textMuted.copy(alpha = if (theme.isLight) 0.48f else 0.4f),
        lineHeight = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 2.dp),
    )
}

@Composable
private fun FeaturedCard(topic: EducationTopic, theme: HydraTheme, onClick: () -> Unit) {
    FrostedGlass(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        theme = theme,
        cornerRadius = 22.dp,
        onClick = onClick,
        padding = PaddingValues(horizontal = 22.dp, vertical = 22.dp),
        borderAlpha = 0.06f,
        isProminent = true
    ) {
        Text(
            "${stringResource(R.string.learn_today_badge)}  ·  ${stringResource(R.string.learn_refreshes_daily)}",
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.7.sp),
            color = theme.textMuted.copy(alpha = 0.44f),
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(topic.titleRes),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Normal),
                    color = theme.textPrimary.copy(alpha = if (theme.isLight) 0.9f else 0.95f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(9.dp))
                Text(
                    topic.previewText,
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.textSoft.copy(alpha = 0.72f),
                    lineHeight = 19.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(
                                topic.colour.copy(alpha = if (theme.isLight) 0.22f else 0.28f),
                                topic.colour.copy(alpha = if (theme.isLight) 0.08f else 0.1f),
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(topic.icon, fontSize = 24.sp)
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(22.dp))
                .background(topic.colour.copy(alpha = if (theme.isLight) 0.09f else 0.15f))
                .padding(horizontal = 16.dp, vertical = 7.dp)
        ) {
            Text(
                stringResource(R.string.learn_read_article),
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 0.3.sp),
                color = topic.colour.copy(alpha = if (theme.isLight) 0.9f else 0.82f),
            )
        }
    }
}

@Composable
private fun CompactTopicCard(topic: EducationTopic, theme: HydraTheme, isViewed: Boolean = false, onClick: () -> Unit) {
    FrostedGlass(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 3.dp),
        theme = theme,
        cornerRadius = 14.dp,
        onClick = onClick,
        padding = PaddingValues(horizontal = 13.dp, vertical = 10.dp),
        borderAlpha = 0.06f
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(
                                topic.colour.copy(alpha = if (theme.isLight) 0.18f else 0.22f),
                                topic.colour.copy(alpha = if (theme.isLight) 0.06f else 0.08f),
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(topic.icon, fontSize = 17.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        stringResource(topic.titleRes),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal, fontSize = 13.5.sp),
                        color = theme.textPrimary.copy(
                            alpha = if (theme.isLight) {
                                if (isViewed) 0.46f else 0.78f
                            } else {
                                if (isViewed) 0.42f else 0.84f
                            }
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (isViewed) {
                        Text("✓", style = MaterialTheme.typography.labelSmall, color = theme.textMuted.copy(alpha = 0.32f))
                    }
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    topic.previewText,
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.textSoft.copy(alpha = if (isViewed) 0.44f else 0.68f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.width(6.dp))
            Text("›", style = MaterialTheme.typography.headlineSmall, color = theme.textMuted.copy(alpha = 0.32f))
        }
    }
}

@Composable
private fun FilteredResultsSection(
    topics: List<EducationTopic>,
    searchQuery: String,
    viewedTopics: List<Int>,
    theme: HydraTheme,
    onTap: (EducationTopic) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp)
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "\"${searchQuery.trim()}\"",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = theme.textPrimary,
                modifier = Modifier.weight(1f),
            )
        }
        if (topics.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 36.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        stringResource(R.string.learn_no_results_title),
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 15.sp),
                        color = theme.textSoft,
                    )
                    Text(
                        stringResource(R.string.learn_no_results_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.textMuted,
                    )
                }
            }
        } else {
            topics.forEach { topic ->
                CompactTopicCard(
                    topic = topic,
                    theme = theme,
                    isViewed = topic.titleRes in viewedTopics,
                    onClick = { onTap(topic) }
                )
            }
        }
    }
}

@Composable
private fun ArticleView(
    topic: EducationTopic,
    profile: Profile,
    theme: HydraTheme,
    viewModel: HydraViewModel,
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
        ) {
            ArticleHeroSection(topic, profile, theme, onDismiss)

            Spacer(Modifier.height(32.dp))
            ArticleSectionHeader(stringResource(R.string.edu_article_key_facts), topic.colour, theme)
            Spacer(Modifier.height(14.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                val facts = topic.resolvedKeyFacts(profile)
                facts.forEachIndexed { i, fact ->
                    KeyFactBlock(fact, topic, theme)
                    if (i < facts.lastIndex) Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(34.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                HydraSpeechBubble(topic.hydraComment, viewModel.speechBubbleStyleId, theme, Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(34.dp))
            ArticleSectionHeader(stringResource(R.string.edu_article_how_it_works), topic.colour, theme)
            Spacer(Modifier.height(14.dp))
            Text(
                topic.content,
                style = MaterialTheme.typography.bodyMedium,
                color = theme.textSoft,
                lineHeight = 26.sp,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            topic.insightCallout?.let { callout ->
                Spacer(Modifier.height(26.dp))
                InsightCallout(callout, topic, theme)
            }

            Spacer(Modifier.height(36.dp))
            ArticleSectionHeader(stringResource(R.string.edu_article_what_goes_wrong), topic.colour, theme)
            Spacer(Modifier.height(14.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                topic.whatGoesWrong.forEachIndexed { i, item ->
                    WhatGoesWrongRow(item, topic, theme)
                    if (i < topic.whatGoesWrong.lastIndex) Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(36.dp))
            ArticleSectionHeader(stringResource(R.string.edu_article_what_this_means), topic.colour, theme)
            Spacer(Modifier.height(14.dp))
            WhatThisMeansCard(topic.resolvedWhatThisMeansForYou(profile), topic, theme)

            Spacer(Modifier.height(36.dp))
            topic.mythVsFact?.let { myths ->
                ArticleSectionHeader(stringResource(R.string.edu_article_myth_vs_fact), topic.colour, theme)
                Spacer(Modifier.height(14.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    myths.forEachIndexed { i, mythFact ->
                        MythVsFactCard(mythFact, topic, theme)
                        if (i < myths.lastIndex) Spacer(Modifier.height(16.dp))
                    }
                }
                Spacer(Modifier.height(36.dp))
            }

            ArticleSectionHeader(stringResource(R.string.edu_article_practical_tips), topic.colour, theme)
            Spacer(Modifier.height(14.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                topic.practicalTips.forEachIndexed { i, tip ->
                    PracticalTipRow(i + 1, tip, topic, theme)
                    if (i < topic.practicalTips.lastIndex) Spacer(Modifier.height(16.dp))
                }
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun ArticleHeroSection(topic: EducationTopic, profile: Profile, theme: HydraTheme, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val backInteraction = remember { MutableInteractionSource() }
    val backPressed by backInteraction.collectIsPressedAsState()
    val backScale by animateFloatAsState(
        if (backPressed) 0.94f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 1500f),
        label = "backScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    Brush.verticalGradient(
                        listOf(
                            topic.colour.copy(alpha = if (theme.isLight) 0.14f else 0.18f),
                            topic.colour.copy(alpha = if (theme.isLight) 0.03f else 0.05f),
                            Color.Transparent,
                        ),
                        endY = size.height * 1.05f
                    )
                )
            }
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 28.dp)
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = backScale
                        scaleY = backScale
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(topic.colour.copy(alpha = if (theme.isLight) 0.1f else 0.14f))
                    .clickable(interactionSource = backInteraction, indication = null) {
                        HapticEngine.tick(context)
                        onDismiss()
                    }
                    .padding(horizontal = 13.dp, vertical = 6.dp)
            ) {
                Text(
                    stringResource(R.string.learn_article_back),
                    style = MaterialTheme.typography.labelMedium,
                    color = topic.colour.copy(alpha = if (theme.isLight) 1f else 0.92f),
                )
            }

            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(topic.colour.copy(alpha = if (theme.isLight) 0.14f else 0.2f), RoundedCornerShape(18.dp))
                        .border(
                            0.5.dp,
                            topic.colour.copy(alpha = if (theme.isLight) 0.14f else 0.2f),
                            RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(topic.icon, fontSize = 28.sp)
                }
                Spacer(Modifier.width(16.dp))
                Text(
                    stringResource(topic.titleRes),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Normal),
                    color = theme.textPrimary,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                topic.resolvedSummary(profile),
                style = MaterialTheme.typography.bodyLarge,
                color = theme.textSoft,
                lineHeight = 27.sp,
            )
        }
    }
}

@Composable
private fun ArticleSectionHeader(title: String, topicColour: Color, theme: HydraTheme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .width(3.dp)
                .height(18.dp)
                .background(topicColour.copy(alpha = if (theme.isLight) 0.8f else 0.72f), RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.width(10.dp))
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 15.sp),
            color = theme.textPrimary,
        )
    }
}

@Composable
private fun KeyFactBlock(fact: String, topic: EducationTopic, theme: HydraTheme) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(topic.colour.copy(alpha = if (theme.isLight) 0.055f else 0.09f))
            .border(0.5.dp, topic.colour.copy(alpha = if (theme.isLight) 0.1f else 0.14f), shape)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(5.dp)
                .background(topic.colour.copy(alpha = if (theme.isLight) 0.55f else 0.45f), RoundedCornerShape(3.dp))
        )
        Spacer(Modifier.width(12.dp))
        Text(
            fact,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = theme.textPrimary,
            lineHeight = 23.sp,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun InsightCallout(text: String, topic: EducationTopic, theme: HydraTheme) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        topic.colour.copy(alpha = if (theme.isLight) 0.09f else 0.13f),
                        topic.colour.copy(alpha = if (theme.isLight) 0.05f else 0.08f),
                    )
                )
            )
            .border(0.5.dp, topic.colour.copy(alpha = if (theme.isLight) 0.09f else 0.13f), shape)
            .padding(horizontal = 22.dp, vertical = 22.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Text(
                "❝",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
                color = topic.colour.copy(alpha = if (theme.isLight) 0.65f else 0.52f),
            )
            Spacer(Modifier.width(14.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Normal),
                color = theme.textPrimary.copy(alpha = if (theme.isLight) 0.82f else 0.88f),
                lineHeight = 28.sp,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun WhatGoesWrongRow(text: String, topic: EducationTopic, theme: HydraTheme) {
    val warnColour = Color(0xFFC46060)
    val shape = RoundedCornerShape(11.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(warnColour.copy(alpha = if (theme.isLight) 0.04f else 0.06f))
            .border(0.5.dp, warnColour.copy(alpha = if (theme.isLight) 0.09f else 0.13f), shape)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(4.dp)
                .background(warnColour.copy(alpha = if (theme.isLight) 0.55f else 0.45f), RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = theme.textSoft,
            lineHeight = 21.sp,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun WhatThisMeansCard(text: String, topic: EducationTopic, theme: HydraTheme) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(shape)
            .background(topic.colour.copy(alpha = if (theme.isLight) 0.035f else 0.055f))
            .border(0.5.dp, topic.colour.copy(alpha = if (theme.isLight) 0.07f else 0.09f), shape)
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge, color = theme.textSoft, lineHeight = 27.sp)
    }
}

@Composable
private fun MythVsFactCard(mythFact: MythFact, topic: EducationTopic, theme: HydraTheme) {
    val mythWarnColour = Color(0xFFC46060)
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(0.5.dp, topic.colour.copy(alpha = if (theme.isLight) 0.09f else 0.12f), shape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(mythWarnColour.copy(alpha = if (theme.isLight) 0.04f else 0.07f))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                "MYTH",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.8.sp),
                color = mythWarnColour.copy(alpha = if (theme.isLight) 0.72f else 0.58f),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(42.dp),
            )
            Spacer(Modifier.width(12.dp))
            Text(
                mythFact.myth,
                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                color = theme.textSoft,
                lineHeight = 22.sp,
                modifier = Modifier.weight(1f),
            )
        }
        HorizontalDivider(color = topic.colour.copy(alpha = if (theme.isLight) 0.07f else 0.1f), thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(topic.colour.copy(alpha = if (theme.isLight) 0.04f else 0.07f))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                "FACT",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.8.sp),
                color = topic.colour.copy(alpha = if (theme.isLight) 0.78f else 0.65f),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(42.dp),
            )
            Spacer(Modifier.width(12.dp))
            Text(
                mythFact.fact,
                style = MaterialTheme.typography.bodySmall,
                color = theme.textSoft,
                lineHeight = 22.sp,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun PracticalTipRow(number: Int, tip: String, topic: EducationTopic, theme: HydraTheme) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(topic.colour.copy(alpha = if (theme.isLight) 0.12f else 0.18f), RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "$number",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = topic.colour.copy(alpha = if (theme.isLight) 1f else 0.9f),
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            tip,
            style = MaterialTheme.typography.bodyMedium,
            color = theme.textSoft,
            lineHeight = 22.sp,
            modifier = Modifier.weight(1f),
        )
    }
}
