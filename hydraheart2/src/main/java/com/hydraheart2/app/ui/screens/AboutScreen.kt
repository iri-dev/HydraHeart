package com.hydraheart2.app.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel

@Composable
fun AboutScreen(viewModel: HydraViewModel, onBack: () -> Unit, onNavigateTo: (String) -> Unit) {
    val theme = viewModel.currentTheme
    val ctx = LocalContext.current
    val versionName = remember {
        try {
            ctx.packageManager.getPackageInfo(ctx.packageName, 0).versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
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
                .padding(horizontal = 20.dp)
                .padding(bottom = 48.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            ) {
                IconButton(onClick = {
                    HapticEngine.tick(ctx)
                    onBack()
                }) {
                    Text("←", fontSize = 24.sp, color = theme.accent, fontWeight = FontWeight.Light)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "About",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = theme.accent
                )
            }
            Spacer(Modifier.height(4.dp))

            AboutHeroCard(
                theme = theme,
                versionName = versionName,
                waterPercent = viewModel.waterPercent,
                onVersionClick = { onNavigateTo("whats_new") }
            )

            Spacer(Modifier.height(28.dp))
            AboutSectionLabel("ABOUT", theme)
            Spacer(Modifier.height(6.dp))
            FrostedGlass(theme = theme, padding = PaddingValues(0.dp), isProminent = true) {
                AboutInfoRow(
                    title = "HydraHeart",
                    subtitle = "A gentle hydration companion designed to make consistency feel effortless and rewarding.",
                    theme = theme,
                    isFirst = true,
                    isLast = false
                )
                Divider(theme)
                DeveloperSignatureRow(theme = theme, onClick = { onNavigateTo("dev_profile") })
            }

            Spacer(Modifier.height(26.dp))
            AboutSectionLabel("LEGAL", theme)
            Spacer(Modifier.height(6.dp))
            FrostedGlass(theme = theme, padding = PaddingValues(0.dp)) {
                AboutActionRow(
                    icon = "📄",
                    label = "Privacy Policy",
                    theme = theme,
                    isFirst = true,
                    isLast = false,
                    onClick = { onNavigateTo("privacy") }
                )
                Divider(theme)
                AboutActionRow(
                    icon = "📋",
                    label = "Terms of Service",
                    theme = theme,
                    isFirst = false,
                    isLast = true,
                    onClick = { onNavigateTo("terms") }
                )
            }

            Spacer(Modifier.height(26.dp))
            AboutSectionLabel("SUPPORT", theme)
            Spacer(Modifier.height(6.dp))
            FrostedGlass(theme = theme, padding = PaddingValues(0.dp)) {
                AboutActionRow(
                    icon = "⭐",
                    label = "Rate HydraHeart",
                    theme = theme,
                    isFirst = true,
                    isLast = false,
                    onClick = {
                        val pkg = ctx.packageName
                        runCatching {
                            ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg")))
                        }.onFailure {
                            runCatching {
                                ctx.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$pkg"))
                                )
                            }
                        }
                    }
                )
                Divider(theme)
                AboutActionRow(
                    icon = "✉️",
                    label = "Contact Support",
                    theme = theme,
                    isFirst = false,
                    isLast = false,
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:iri.devsupport@gmail.com")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("iri.devsupport@gmail.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "HydraHeart Support")
                            putExtra(Intent.EXTRA_TEXT, "\n\n---\nVersion: $versionName")
                        }
                        runCatching { ctx.startActivity(Intent.createChooser(intent, "Contact via Email")) }
                    }
                )
                Divider(theme)
                AboutActionRow(
                    icon = "🔗",
                    label = "Share with a friend",
                    theme = theme,
                    isFirst = false,
                    isLast = true,
                    onClick = {
                        val pkg = ctx.packageName
                        val share = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "HydraHeart — Hydrate beautifully.")
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "I've been using HydraHeart to stay hydrated — it's beautiful! ✨\nhttps://play.google.com/store/apps/details?id=$pkg"
                            )
                        }
                        ctx.startActivity(Intent.createChooser(share, "Share HydraHeart"))
                    }
                )
            }

            Spacer(Modifier.height(44.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Crafted with love and intention 💗",
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.textMuted,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "HydraHeart © 2026",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = theme.textMuted.copy(alpha = 0.55f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun AboutHeroCard(theme: HydraTheme, versionName: String, waterPercent: Float, onVersionClick: () -> Unit) {
    val ctx = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val dynamicMessage = when {
        waterPercent >= 100f -> "Goal reached! Your body is grateful. 💗"
        waterPercent >= 75f -> "So close to your goal! Finish strong. ✨"
        waterPercent >= 25f -> "You're doing great, keep that momentum. 🌊"
        else -> "A fresh start is just one glass away. 💧"
    }

    val iconBitmap = remember {
        try {
            val drawable = ctx.packageManager.getApplicationIcon(ctx.packageName)
            val size = 192
            val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bmp)
            drawable.setBounds(0, 0, size, size)
            drawable.draw(canvas)
            bmp.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    val inf = rememberInfiniteTransition(label = "hero_inf")
    val breathGlow by inf.animateFloat(
        initialValue = 0.16f,
        targetValue = 0.34f,
        animationSpec = infiniteRepeatable(tween(3200, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "breathGlow"
    )
    val breathScale by inf.animateFloat(
        initialValue = 1f,
        targetValue = 1.01f,
        animationSpec = infiniteRepeatable(tween(3800, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "breathScale"
    )
    val pressScale by animateFloatAsState(
        if (isPressed) 0.972f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "heroPress"
    )
    val pressGlowBoost by animateFloatAsState(if (isPressed) 0.5f else 0f, tween(140), label = "pressGlowBoost")
    val activeGlow = breathGlow + pressGlowBoost
    val shape = RoundedCornerShape(28.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = pressScale * breathScale
                scaleY = pressScale * breathScale
            }
            .shadow(
                elevation = 24.dp,
                shape = shape,
                ambientColor = theme.accent.copy(alpha = 0.22f),
                spotColor = theme.accent.copy(alpha = 0.16f)
            )
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.13f),
                        Color.White.copy(alpha = 0.04f),
                        theme.bg2.copy(alpha = 0.28f)
                    )
                )
            )
            .drawWithContent {
                drawContent()
                drawRoundRect(
                    Brush.radialGradient(
                        listOf(theme.accent.copy(alpha = 0.3f * activeGlow), Color.Transparent),
                        center = Offset(size.width / 2f, size.height * 0.36f),
                        radius = size.width * 0.7f
                    ),
                    cornerRadius = CornerRadius(28.dp.toPx()),
                    size = Size(size.width, size.height)
                )
                drawRoundRect(
                    Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.88f), Color.Transparent),
                        startY = 0f,
                        endY = 2.5.dp.toPx()
                    ),
                    cornerRadius = CornerRadius(28.dp.toPx()),
                    size = Size(size.width, size.height)
                )
            }
            .border(
                1.25.dp,
                Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.55f), theme.cardBorder.copy(alpha = 0.2f))),
                shape
            )
            .clickable(interactionSource = interactionSource, indication = null) { HapticEngine.tick(ctx) }
            .padding(horizontal = 24.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(120.dp)
                    .drawBehind {
                        drawCircle(
                            Brush.radialGradient(
                                listOf(
                                    theme.accent.copy(alpha = 0.7f * activeGlow),
                                    theme.glow.copy(alpha = activeGlow * 0.35f),
                                    Color.Transparent
                                ),
                                radius = size.width * 0.68f,
                                center = center
                            )
                        )
                    }
            )
            val iconModifier = Modifier
                .size(88.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = CircleShape,
                    ambientColor = theme.accent.copy(alpha = 0.28f),
                    spotColor = theme.accent.copy(alpha = 0.2f)
                )
                .clip(CircleShape)
                .border(1.5.dp, Color.White.copy(alpha = 0.3f), CircleShape)
            if (iconBitmap != null) {
                Image(
                    bitmap = iconBitmap,
                    contentDescription = "HydraHeart",
                    modifier = iconModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = iconModifier.background(
                        Brush.linearGradient(listOf(theme.accent.copy(alpha = 0.92f), theme.accent2.copy(alpha = 0.82f)))
                    )
                ) {
                    Text("💧", fontSize = 34.sp)
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "HydraHeart",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
            color = theme.textPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            dynamicMessage,
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
            color = theme.textSoft,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Surface(
            color = Color.Transparent,
            shape = RoundedCornerShape(8.dp),
            onClick = {
                HapticEngine.tick(ctx)
                onVersionClick()
            }
        ) {
            Text(
                "Version $versionName",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.6.sp),
                color = theme.textMuted.copy(alpha = 0.65f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun DeveloperSignatureRow(theme: HydraTheme, onClick: () -> Unit) {
    val ctx = LocalContext.current
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (pressed) 0.985f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "sigScale"
    )
    val bgAlpha by animateFloatAsState(if (pressed) 0.08f else 0f, tween(120), label = "sigBg")
    val inf = rememberInfiniteTransition(label = "sig_glow")
    val glowAlpha by inf.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(2800, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "sigGlow"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(bottomEnd = 22.dp, bottomStart = 22.dp))
            .background(theme.accent.copy(alpha = bgAlpha))
            .clickable(interactionSource = source, indication = null) {
                HapticEngine.tick(ctx)
                onClick()
            }
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Text(
            "Crafted by",
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.8.sp, fontWeight = FontWeight.Medium),
            color = theme.textMuted.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(5.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "iri.dev",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.3).sp),
                color = theme.accent.copy(alpha = glowAlpha),
                modifier = Modifier.weight(1f)
            )
            Text("›", fontSize = 22.sp, color = theme.accent.copy(alpha = 0.38f), fontWeight = FontWeight.Light)
        }
        Spacer(Modifier.height(3.dp))
        Text(
            "Independently designed and built with care",
            style = MaterialTheme.typography.bodySmall,
            color = theme.textSoft.copy(alpha = 0.65f)
        )
    }
}

@Composable
private fun Divider(theme: HydraTheme) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(0.5.dp)
            .background(theme.accent.copy(alpha = 0.06f))
    )
}

@Composable
private fun AboutSectionLabel(label: String, theme: HydraTheme) {
    Text(
        label,
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.2.sp, fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
        color = theme.accent.copy(alpha = 0.6f),
        modifier = Modifier.padding(start = 14.dp)
    )
}

@Composable
private fun AboutInfoRow(
    title: String,
    subtitle: String,
    theme: HydraTheme,
    isFirst: Boolean,
    isLast: Boolean,
    value: String? = null,
) {
    val topR = if (isFirst) 22.dp else 0.dp
    val bottomR = if (isLast) 22.dp else 0.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topR, topR, bottomR, bottomR))
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = theme.textPrimary,
                modifier = Modifier.weight(1f)
            )
            if (value != null) {
                Text(value, style = MaterialTheme.typography.bodyMedium, color = theme.accent.copy(alpha = 0.75f))
            }
        }
        Spacer(Modifier.height(3.dp))
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = theme.textSoft.copy(alpha = 0.75f))
    }
}

@Composable
private fun AboutActionRow(
    icon: String,
    label: String,
    theme: HydraTheme,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: () -> Unit,
) {
    val ctx = LocalContext.current
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (pressed) 0.98f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "rowScale"
    )
    val bgAlpha by animateFloatAsState(if (pressed) 0.08f else 0f, tween(120), label = "rowBg")
    val topR = if (isFirst) 22.dp else 0.dp
    val bottomR = if (isLast) 22.dp else 0.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(topR, topR, bottomR, bottomR))
            .background(theme.accent.copy(alpha = bgAlpha))
            .clickable(interactionSource = source, indication = null) {
                HapticEngine.tick(ctx)
                onClick()
            }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AboutGlassIcon(icon, theme)
        Spacer(Modifier.width(16.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = theme.textPrimary,
            modifier = Modifier.weight(1f)
        )
        Text("›", fontSize = 22.sp, color = theme.accent.copy(alpha = 0.4f), fontWeight = FontWeight.Light)
    }
}

@Composable
private fun AboutGlassIcon(icon: String, theme: HydraTheme) {
    val isLight = theme.isLight
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        theme.accent.copy(alpha = if (isLight) 0.18f else 0.24f),
                        theme.accent.copy(alpha = if (isLight) 0.08f else 0.12f)
                    )
                ),
                RoundedCornerShape(10.dp)
            )
            .border(0.5.dp, Color.White.copy(alpha = if (isLight) 0.4f else 0.2f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(icon, fontSize = 18.sp)
    }
}
