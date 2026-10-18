package com.hydraheart2.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.R
import com.hydraheart2.app.data.DrinkDatabase
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.data.models.Drink
import com.hydraheart2.app.data.models.DrinkCategory
import com.hydraheart2.app.ui.components.AddictiveButton
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.ui.navigation.PAGE_FADE_IN_MS
import com.hydraheart2.app.ui.navigation.PAGE_FADE_OUT_MS
import com.hydraheart2.app.ui.navigation.pageTransition
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel
import java.text.Normalizer

@Composable
fun LogDrinkScreen(viewModel: HydraViewModel, onNavigateHome: () -> Unit) {
    val theme = viewModel.currentTheme
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf<DrinkCategory?>(null) }
    var searching by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }
    BackHandler(enabled = selectedCategory != null) { selectedCategory = null }
    BackHandler(enabled = searching) {
        searching = false
        query = ""
    }
    val addDrink: (Drink) -> Unit = { drink ->
        HapticEngine.heavyClick(context)
        viewModel.logDrink(drink)
        onNavigateHome()
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
                .imePadding()
                .padding(bottom = 100.dp)
        ) {
            AnimatedContent(
                targetState = searching,
                transitionSpec = { fadeIn(tween(PAGE_FADE_IN_MS)) togetherWith fadeOut(tween(PAGE_FADE_OUT_MS)) },
                label = "search"
            ) { isSearching ->
                if (isSearching) {
                    Column {
                        SearchBar(
                            query = query,
                            onQueryChange = { query = it },
                            onClose = {
                                searching = false
                                query = ""
                            },
                            theme = theme
                        )
                        SearchResults(query, viewModel, theme, onAdd = addDrink)
                    }
                } else {
                    Column {
                        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                if (selectedCategory != null) {
                                    IconButton(onClick = {
                                        HapticEngine.tick(context)
                                        selectedCategory = null
                                    }) {
                                        Text("←", fontSize = 22.sp, color = theme.accent)
                                    }
                                }
                                val titleText = selectedCategory?.let { stringResource(it.nameRes) }
                                    ?: stringResource(R.string.log_drink_title)
                                Text(
                                    titleText,
                                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                    color = theme.accent3,
                                    modifier = Modifier.weight(1f)
                                )
                                FrostedGlass(
                                    theme = theme,
                                    cornerRadius = 14.dp,
                                    onClick = { searching = true },
                                    padding = PaddingValues(11.dp)
                                ) {
                                    Icon(Icons.Rounded.Search, contentDescription = "Search drinks", tint = theme.accent, modifier = Modifier.size(22.dp))
                                }
                            }
                            if (selectedCategory == null) {
                                Text(
                                    "What are you drinking right now?",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = theme.textSoft.copy(alpha = 0.8f),
                                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                                )
                            }
                        }

                        AnimatedContent(
                            targetState = selectedCategory,
                            transitionSpec = { pageTransition() },
                            label = "categoryTransition"
                        ) { category ->
                            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                                if (category == null) {
                                    DrinkDatabase.categories.forEach { cat ->
                                        val isWater = cat.id == "water"
                                        val subtitle = when (cat.id) {
                                            "water" -> "Pure hydration"
                                            "coffee" -> "Warm and energising"
                                            "juice" -> "Natural vitamins"
                                            "milk" -> "Nourishing calcium"
                                            "energy" -> "A powerful boost"
                                            "soda" -> "Sweet and bubbly"
                                            "alcohol" -> "Occasional treats"
                                            else -> "Refreshment"
                                        }
                                        CategoryCard(
                                            cat = cat,
                                            subtitle = subtitle,
                                            theme = theme,
                                            isEmphasized = isWater,
                                            onClick = { selectedCategory = cat }
                                        )
                                    }
                                } else {
                                    category.drinks.forEach { drink ->
                                        DrinkRow(drink, category.colour, label = null, viewModel = viewModel, theme = theme) { addDrink(drink) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DrinkRow(drink: Drink, colour: Color, label: String?, viewModel: HydraViewModel, theme: HydraTheme, onAdd: () -> Unit) {
    FrostedGlass(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        theme = theme,
        blurRadius = 18.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    drink.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = theme.textPrimary
                )
                Text(
                    listOfNotNull(label, "${drink.sizeMl}${viewModel.unitLabel()}").joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.textSoft
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (drink.caffeineMg > 0) {
                        Pill(
                            "☕ ${drink.caffeineMg}mg",
                            Color(0xFFA0724A).copy(alpha = 0.15f),
                            Color(0xFFA0724A)
                        )
                    }
                    if (drink.sugarG > 0f) {
                        Pill(
                            "🍬 ${drink.sugarG.toInt()}g",
                            Color(0xFFF472B6).copy(alpha = 0.15f),
                            Color(0xFFF472B6)
                        )
                    }
                    if (drink.calciumMg > 0) {
                        Pill(
                            "🦴 ${drink.calciumMg}mg",
                            Color(0xFFFBBF24).copy(alpha = 0.15f),
                            Color(0xFFD4A017)
                        )
                    }
                    if (drink.alcoholG > 0f) {
                        Pill(
                            "🍷 ${drink.alcoholG.toInt()}g",
                            Color(0xFFA855F7).copy(alpha = 0.15f),
                            Color(0xFFA855F7)
                        )
                    }
                }
                if (drink.dehydrationFactor > 0.05f) {
                    Spacer(Modifier.height(6.dp))
                    val lostMl = (drink.sizeMl * drink.dehydrationFactor).toInt()
                    Text(
                        stringResource(
                            R.string.log_drink_dehydrating,
                            lostMl.toString(),
                            viewModel.unitLabel()
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFFEF4444)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier.drawBehind {
                    drawCircle(
                        Brush.radialGradient(
                            listOf(colour.copy(alpha = 0.15f), Color.Transparent),
                            radius = size.width * 0.8f
                        )
                    )
                }
            ) {
                AddictiveButton(
                    text = stringResource(R.string.log_drink_add),
                    onClick = onAdd,
                    theme = theme,
                    // Milk's cream colour is too pale for white text, so very light colours use the theme's accent.
                    colour = if (colour.luminance() > 0.75f) theme.accent else colour,
                    cornerRadius = 14.dp
                )
            }
        }
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit, onClose: () -> Unit, theme: HydraTheme) {
    val context = LocalContext.current
    val focus = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) { focus.requestFocus() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 8.dp, end = 20.dp, top = 16.dp, bottom = 14.dp)
    ) {
        IconButton(onClick = {
            HapticEngine.tick(context)
            onClose()
        }) {
            Text("←", fontSize = 22.sp, color = theme.accent)
        }
        FrostedGlass(
            theme = theme,
            cornerRadius = 18.dp,
            padding = PaddingValues(horizontal = 14.dp, vertical = 13.dp),
            modifier = Modifier.weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Search, contentDescription = null, tint = theme.accent, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = theme.textPrimary),
                    cursorBrush = SolidColor(theme.accent),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }),
                    modifier = Modifier.weight(1f).focusRequester(focus),
                    decorationBox = { field ->
                        Box {
                            if (query.isEmpty()) {
                                Text("Search drinks", style = MaterialTheme.typography.bodyLarge, color = theme.textMuted)
                            }
                            field()
                        }
                    }
                )
                if (query.isNotEmpty()) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Clear the search",
                        tint = theme.textMuted,
                        modifier = Modifier.size(20.dp).clip(CircleShape).clickable { onQueryChange("") }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResults(
    query: String,
    viewModel: HydraViewModel,
    theme: HydraTheme,
    onAdd: (Drink) -> Unit
) {
    val words = remember(query) { searchWords(query) }
    val everything = remember { DrinkDatabase.categories.flatMap { c -> c.drinks.map { it to c } } }
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        if (words.isEmpty()) {
            val recent = viewModel.todayDrinks.reversed().map { it.drink }.distinctBy { it.id }.take(5)
            if (recent.isNotEmpty()) {
                Text(
                    "HAD TODAY",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp, fontWeight = FontWeight.SemiBold),
                    color = theme.accent.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 6.dp, bottom = 8.dp)
                )
                recent.forEach { drink ->
                    val category = DrinkDatabase.getCategoryForDrink(drink.id)
                    DrinkRow(drink, category?.colour ?: theme.accent, category?.let { stringResource(it.nameRes) }, viewModel, theme) { onAdd(drink) }
                }
            } else {
                Text(
                    "Search all ${everything.size} drinks by name, like latte or cola.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = theme.textSoft,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        } else {
            val results = everything
                .mapNotNull { (drink, category) -> matchRank(drink.name, words)?.let { Triple(drink, category, it) } }
                .sortedBy { it.third }
            if (results.isEmpty()) {
                FrostedGlass(theme = theme, cornerRadius = 22.dp, padding = PaddingValues(20.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "No drinks match “${query.trim()}”",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = theme.textPrimary
                    )
                    Text(
                        "Try another word, or browse the drink categories.",
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.textSoft,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                results.forEach { (drink, category) ->
                    DrinkRow(drink, category.colour, stringResource(category.nameRes), viewModel, theme) { onAdd(drink) }
                }
            }
        }
    }
}

// Lower-case, without accents, in words: "Caffè Latte" becomes ["caffe", "latte"].
private fun searchWords(text: String): List<String> =
    Normalizer.normalize(text.lowercase(), Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .split(Regex("[^a-z0-9]+"))
        .filter { it.isNotEmpty() }
        .map { if (it == "coke") "cola" else it }

// Every typed word has to start one of the drink's words; drinks whose name starts with it come first.
private fun matchRank(name: String, words: List<String>): Int? {
    val nameWords = searchWords(name)
    if (!words.all { w -> nameWords.any { it.startsWith(w) } }) return null
    return when {
        nameWords.firstOrNull()?.startsWith(words.first()) == true -> 0
        else -> 1
    }
}

@Composable
private fun CategoryCard(
    cat: DrinkCategory,
    subtitle: String,
    theme: HydraTheme,
    isEmphasized: Boolean,
    onClick: () -> Unit
) {
    // Note: this interaction source isn't attached to anything, so the card never scales
    // itself; FrostedGlass's own press effect is what shows.
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (isPressed) 0.97f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "scale"
    )
    FrostedGlass(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        theme = theme,
        onClick = onClick,
        padding = PaddingValues(0.dp),
        isProminent = isEmphasized
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(cat.colour.copy(alpha = 0.22f), RoundedCornerShape(16.dp))
                    .drawBehind {
                        if (isEmphasized) {
                            drawCircle(
                                Brush.radialGradient(
                                    listOf(cat.colour.copy(alpha = 0.3f), Color.Transparent),
                                    radius = size.width * 0.9f
                                )
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(cat.icon, fontSize = 26.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(cat.nameRes),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isEmphasized) theme.accent else theme.textPrimary
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.textSoft.copy(alpha = 0.8f)
                )
                Text(
                    "${cat.drinks.size} drinks",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.5.sp),
                    color = theme.textMuted
                )
            }
            Text(
                "›",
                fontSize = 24.sp,
                color = if (isEmphasized) theme.accent.copy(alpha = 0.6f) else theme.textMuted,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
private fun Pill(t: String, bg: Color, fg: Color) {
    Text(
        t,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(bg, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        color = fg
    )
}
