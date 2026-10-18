package com.hydraheart2.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.R
import com.hydraheart2.app.data.DrinkDatabase
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.data.models.DrinkCategory
import com.hydraheart2.app.ui.components.AddictiveButton
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.ui.navigation.pageTransition
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel

@Composable
fun LogDrinkScreen(viewModel: HydraViewModel, onNavigateHome: () -> Unit) {
    val theme = viewModel.currentTheme
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf<DrinkCategory?>(null) }
    BackHandler(enabled = selectedCategory != null) { selectedCategory = null }

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
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                        color = theme.accent3
                    )
                }
                if (selectedCategory == null) {
                    Text(
                        "What are you drinking right now?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.textSoft.copy(alpha = 0.8f),
                        modifier = Modifier.padding(start = if (selectedCategory != null) 48.dp else 4.dp, top = 2.dp)
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
                                            "${drink.sizeMl}${viewModel.unitLabel()}",
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
                                                    listOf(category.colour.copy(alpha = 0.15f), Color.Transparent),
                                                    radius = size.width * 0.8f
                                                )
                                            )
                                        }
                                    ) {
                                        AddictiveButton(
                                            text = stringResource(R.string.log_drink_add),
                                            onClick = {
                                                HapticEngine.heavyClick(context)
                                                viewModel.logDrink(drink)
                                                onNavigateHome()
                                            },
                                            theme = theme,
                                            colour = category.colour,
                                            cornerRadius = 14.dp
                                        )
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
