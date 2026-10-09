package com.example.hydraheart

import android.Manifest
import android.graphics.BlurMaskFilter
import android.os.Bundle
import android.os.Build
import android.content.pm.PackageManager
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.hydraheart.ui.theme.AppFont
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// ── Blue / navy palette ───────────────────────────────────────────────
private val BlueAccent = Color(0xFF6E9BFF)        // HP, selected nav, bright values
private val Lavender = Color(0xFFB8C6EE)           // body / label text
private val LavenderDim = Color(0xFF8C9BC8)        // muted captions
private val LocalBottomSelection = compositionLocalOf { "Home" }

private val cardBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2C3B66).copy(alpha = 0.62f),
        Color(0xFF16213E).copy(alpha = 0.80f),
        Color(0xFF0C1428).copy(alpha = 0.82f)
    )
)

private val cardBorder = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.55f),
        Color.White.copy(alpha = 0.14f),
        BlueAccent.copy(alpha = 0.10f)
    )
)

private fun formatHydraAmount(amountMl: Float): String = "${amountMl.toInt()}ml"

class MainActivity : ComponentActivity() {
    private val hydraViewModel: HydraViewModel by lazy {
        ViewModelProvider(this)[HydraViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.BLACK)
        )
        setContent {
            HydraHeartApp(
                viewModel = hydraViewModel,
                onRequestNotificationPermission = ::requestNotificationPermission
            )
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
        }
    }
}

@Composable
private fun HydraHeartApp(
    viewModel: HydraViewModel,
    onRequestNotificationPermission: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var current by remember { mutableStateOf(HydraDestination.Home) }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = current,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                // "Soft Presence": the new screen just fades in over the old one, which is
                // held opaque then fades late — no movement, no blur, so nothing jumps or
                // flashes and there's no mid-fade dim. Only the gear-tap path
                // (Home → Settings) gets the longer, softer fade; every other nav uses
                // the default snappy fade so taps inside Settings feel responsive.
                val gearTap = initialState == HydraDestination.Home &&
                    targetState == HydraDestination.Settings
                if (gearTap) {
                    fadeIn(tween(420, easing = FastOutSlowInEasing)) togetherWith
                        fadeOut(tween(340, delayMillis = 100, easing = FastOutSlowInEasing))
                } else {
                    fadeIn(tween(180, easing = FastOutSlowInEasing)) togetherWith
                        fadeOut(tween(135, delayMillis = 80, easing = FastOutSlowInEasing))
                }
            },
            label = "screenSwap"
        ) { screen ->
            when (screen) {
                HydraDestination.History -> HistoryScreen(state)
                HydraDestination.Learn -> LearnScreen()
                HydraDestination.Trophies -> TrophiesScreen(state)
                HydraDestination.LogDrink -> LogDrinkScreen(
                    drinkTypes = state.drinkTypes,
                    onDrinkLogged = { type ->
                        viewModel.logDrink(type)
                        current = HydraDestination.Home
                    },
                    onBack = { current = HydraDestination.Home }
                )
                HydraDestination.Settings -> SettingsScreen(
                    onBack = { current = HydraDestination.Home },
                    onProfileClick = { current = HydraDestination.YourProfile },
                    onHowWeFeelClick = { current = HydraDestination.HowWeFeel },
                    onPersonalityClick = { current = HydraDestination.Personality },
                    onHydraLookClick = { current = HydraDestination.HydraLook },
                    onDailyGoalsClick = { current = HydraDestination.DailyGoals },
                    onCalendarClick = { current = HydraDestination.Calendar },
                    onRemindersClick = { current = HydraDestination.Reminders },
                    onThemesClick = { current = HydraDestination.Themes },
                    onUnitsClick = { current = HydraDestination.Units },
                    onGeneralClick = { current = HydraDestination.General },
                    onAboutClick = { current = HydraDestination.About }
                )
                HydraDestination.YourProfile -> YourProfileScreen(
                    onBack = { current = HydraDestination.Settings },
                    onReset = viewModel::clearAllProgress
                )
                HydraDestination.HowWeFeel -> HowWeFeelScreen(
                    onBack = { current = HydraDestination.Settings }
                )
                HydraDestination.Personality -> PersonalityScreen(
                    onBack = { current = HydraDestination.Settings }
                )
                HydraDestination.HydraLook -> HydraLookScreen(
                    onBack = { current = HydraDestination.Settings }
                )
                HydraDestination.DailyGoals -> DailyGoalsScreen(
                    onBack = { current = HydraDestination.Settings },
                    currentGoalMl = state.dailyWaterGoalMl,
                    onSaveGoal = viewModel::setDailyWaterGoalMl
                )
                HydraDestination.Calendar -> CalendarScreen(
                    onBack = { current = HydraDestination.Settings }
                )
                HydraDestination.Reminders -> RemindersScreen(
                    onBack = { current = HydraDestination.Settings },
                    savedReminders = state.reminders,
                    remindersEnabled = state.remindersEnabled,
                    selectedSound = state.notificationSound,
                    onToggle = { enabled ->
                        if (enabled) onRequestNotificationPermission()
                        viewModel.setRemindersEnabled(enabled)
                    },
                    onAddReminder = { viewModel.addReminder() },
                    onRemoveReminder = viewModel::deleteReminder,
                    onSoundSelected = viewModel::setNotificationSound
                )
                HydraDestination.Themes -> ThemesScreen(
                    onBack = { current = HydraDestination.Settings }
                )
                HydraDestination.Units -> UnitsScreen(
                    onBack = { current = HydraDestination.Settings }
                )
                HydraDestination.General -> GeneralScreen(
                    onBack = { current = HydraDestination.Settings }
                )
                HydraDestination.About -> AboutScreen(
                    onBack = { current = HydraDestination.Settings },
                    onDeveloperClick = { current = HydraDestination.DeveloperProfile },
                    onPrivacyPolicyClick = { current = HydraDestination.PrivacyPolicy }
                )
                HydraDestination.DeveloperProfile -> DeveloperProfileScreen(
                    onBack = { current = HydraDestination.About }
                )
                HydraDestination.PrivacyPolicy -> PrivacyPolicyScreen(
                    onBack = { current = HydraDestination.About }
                )
                HydraDestination.Home -> HydraHeartScreen(
                    state = state,
                    onSettingsClick = { current = HydraDestination.Settings },
                    onLogDrink = { current = HydraDestination.LogDrink }
                )
            }
        }

        AnimatedVisibility(
            visible = current != HydraDestination.Settings &&
                current != HydraDestination.YourProfile &&
                current != HydraDestination.HowWeFeel &&
                current != HydraDestination.Personality &&
                current != HydraDestination.HydraLook &&
                current != HydraDestination.DailyGoals &&
                current != HydraDestination.Calendar &&
                current != HydraDestination.Reminders &&
                current != HydraDestination.Themes &&
                current != HydraDestination.Units &&
                current != HydraDestination.General &&
                current != HydraDestination.About &&
                current != HydraDestination.DeveloperProfile &&
                current != HydraDestination.PrivacyPolicy,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn(tween(220, easing = FastOutSlowInEasing)),
            exit = fadeOut(tween(340, easing = FastOutSlowInEasing))
        ) {
            HydraBottomBar(
                selected = current,
                onDestinationSelected = { current = it },
                onAddClick = { current = HydraDestination.LogDrink }
            )
        }
    }
}

@Composable
fun HydraHeartScreen(
    state: HydraState,
    onSettingsClick: () -> Unit = {},
    onLogDrink: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        scrollState.scrollTo(0)
    }

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
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(state = state, onSettingsClick = onSettingsClick)
            Spacer(modifier = Modifier.height(12.dp))
            MessageCard(state)
            Spacer(modifier = Modifier.height(30.dp))
            MainHeart()
            Spacer(modifier = Modifier.height(26.dp))
            ProgressSection(state)
            Spacer(modifier = Modifier.height(18.dp))
            StatsSection(state)
            Spacer(modifier = Modifier.height(18.dp))
            ElectrolytesSection()
            Spacer(modifier = Modifier.height(18.dp))
            StreakTrophySection(state)
            Spacer(modifier = Modifier.height(18.dp))
            NextRewardCard()
            Spacer(modifier = Modifier.height(92.dp))
        }
    }
}

@Composable
private fun TopBar(state: HydraState, onSettingsClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Date pill (3 lines)
        Box(
            modifier = Modifier
                .width(92.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2B3A66).copy(alpha = 0.52f),
                            Color(0xFF0F1730).copy(alpha = 0.68f)
                        )
                    )
                )
                .border(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.32f),
                            Color.White.copy(alpha = 0.09f),
                            BlueAccent.copy(alpha = 0.05f)
                        )
                    ),
                    width = 0.9.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 9.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val now = java.time.ZonedDateTime.now()
                AppText(now.format(java.time.format.DateTimeFormatter.ofPattern("EEE")), BlueAccent, 14, fontWeight = FontWeight.Bold, fontFamily = AppFont, fontStyle = FontStyle.Italic)
                AppText(now.format(java.time.format.DateTimeFormatter.ofPattern("d MMM")), BlueAccent, 14, fontWeight = FontWeight.Bold, fontFamily = AppFont, fontStyle = FontStyle.Italic)
                AppText(now.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a")), BlueAccent, 14, fontWeight = FontWeight.Bold, fontFamily = AppFont, fontStyle = FontStyle.Italic)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppText(
                text = "LEVEL ${state.currentStreak + 1}",
                color = Lavender.copy(alpha = 0.55f),
                fontSize = 11,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                letterSpacing = 3.6
            )
            AppText(
                text = "${state.todayLogs.size * 5}/500 HP",
                color = BlueAccent,
                fontSize = 15,
                fontWeight = FontWeight.SemiBold,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .width(88.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF2E4170),
                                Color(0xFF24355E).copy(alpha = 0.45f),
                                Color(0xFF1E2A4D).copy(alpha = 0.0f)
                            )
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(((state.todayLogs.size * 5) / 500f).coerceIn(0.02f, 1f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF3A6BE0), Color(0xFF7AA4FF))
                            )
                        )
                )
            }
            Spacer(modifier = Modifier.height(7.dp))
            AppText(
                text = "tap to view",
                color = LavenderDim.copy(alpha = 0.6f),
                fontSize = 9,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                letterSpacing = 1.0
            )
        }

        SettingsWaterGearButton(onClick = onSettingsClick)
    }
}

@Composable
private fun SettingsWaterGearButton(onClick: () -> Unit = {}) {
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    val transition = rememberInfiniteTransition()
    val breath by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val press = remember { Animatable(0f) }
    val tapRipple = remember { Animatable(1f) }

    fun pressIn() {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        scope.launch {
            press.stop()
            press.animateTo(1f, tween(170, easing = FastOutSlowInEasing))
        }
        scope.launch {
            tapRipple.stop()
            tapRipple.snapTo(0f)
            tapRipple.animateTo(1f, tween(520, easing = LinearOutSlowInEasing))
        }
    }

    fun pressOut() {
        scope.launch {
            press.stop()
            press.animateTo(0f, tween(430, easing = FastOutSlowInEasing))
        }
    }

    Canvas(
        modifier = Modifier
            .size(52.dp)
            .graphicsLayer {
                val s = 1f + 0.010f * breath + 0.050f * press.value
                scaleX = s
                scaleY = s
            }
            .pointerInteropFilter { event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        pressIn()
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        pressOut()
                        onClick()
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        pressOut()
                        true
                    }
                    else -> true
                }
            }
    ) {
        val corner = 17.dp.toPx()
        val buttonSize = Size(size.width, size.height)
        val c = center
        val p = press.value
        val ripple = tapRipple.value
        val icon = 35.5.dp.toPx() * (1f + 0.006f * breath + 0.032f * p)

        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF314676).copy(alpha = 0.32f + 0.10f * p),
                    Color(0xFF172342).copy(alpha = 0.60f + 0.06f * p),
                    Color(0xFF0A1126).copy(alpha = 0.74f)
                )
            ),
            size = buttonSize,
            cornerRadius = CornerRadius(corner, corner)
        )

        drawRoundRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.10f + 0.035f * breath + 0.08f * p),
                    Color(0xFF86A8FF).copy(alpha = 0.040f + 0.040f * breath + 0.08f * p),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.31f, size.height * 0.24f),
                radius = size.width * 0.76f
            ),
            size = buttonSize,
            cornerRadius = CornerRadius(corner, corner)
        )

        if (ripple < 1f) {
            drawCircle(
                color = Color(0xFF9BE7FF).copy(alpha = (1f - ripple) * (0.18f + 0.10f * p)),
                radius = icon * (0.48f + 0.24f * ripple),
                center = c,
                style = Stroke(width = (1.1f + 1.2f * (1f - ripple)).dp.toPx(), cap = StrokeCap.Round)
            )
        }

        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.32f),
                    Color(0xFFBFD0FF).copy(alpha = 0.11f),
                    BlueAccent.copy(alpha = 0.07f)
                )
            ),
            size = buttonSize,
            cornerRadius = CornerRadius(corner, corner),
                style = Stroke(width = 0.85.dp.toPx())
        )

        fun gearPoint(angleDeg: Float, r: Float): Offset {
            val rad = angleDeg * PI.toFloat() / 180f
            return Offset(c.x + cos(rad).toFloat() * r, c.y + sin(rad).toFloat() * r)
        }
        val teeth = 8
        val seg = 360f / teeth
        val flankW = seg * 0.11f
        val valleyW = seg * 0.42f
        val crownW = seg - valleyW - 2f * flankW
        val outerR = icon * 0.478f
        val rootR = icon * 0.362f
        val arcSub = 5
        val gearPath = Path()
        var gearStarted = false
        fun gearArc(fromDeg: Float, toDeg: Float, r: Float) {
            for (k in 0..arcSub) {
                val a = fromDeg + (toDeg - fromDeg) * k / arcSub
                val pnt = gearPoint(a, r)
                if (!gearStarted) {
                    gearPath.moveTo(pnt.x, pnt.y)
                    gearStarted = true
                } else {
                    gearPath.lineTo(pnt.x, pnt.y)
                }
            }
        }
        for (t in 0 until teeth) {
            val a0 = -90f + t * seg
            gearArc(a0, a0 + valleyW, rootR)                  // valley floor
            val rise = gearPoint(a0 + valleyW + flankW, outerR)
            gearPath.lineTo(rise.x, rise.y)                   // rising flank
            gearArc(a0 + valleyW + flankW, a0 + valleyW + flankW + crownW, outerR) // crown
        }
        gearPath.close()

        val holeR = icon * 0.255f
        val gearRing = Path().apply {
            addPath(gearPath)
            addOval(Rect(c.x - holeR, c.y - holeR, c.x + holeR, c.y + holeR))
            fillType = PathFillType.EvenOdd
        }

        drawPath(
            path = gearPath,
            color = Color(0xFF050B1D).copy(alpha = 0.36f),
            style = Stroke(width = 2.4.dp.toPx(), join = StrokeJoin.Round)
        )
        drawPath(
            path = gearRing,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFEAF3FF).copy(alpha = 0.95f),
                    Color(0xFFB1C8EF).copy(alpha = 0.93f),
                    Color(0xFF607FB8).copy(alpha = 0.91f),
                    Color(0xFF243A64).copy(alpha = 0.95f)
                ),
                center = Offset(c.x - icon * 0.17f, c.y - icon * 0.21f),
                radius = icon * 0.80f
            )
        )
        clipPath(gearRing) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.20f + 0.08f * p),
                        Color(0xFF8DDCFF).copy(alpha = 0.08f + 0.06f * p),
                        Color.Transparent
                    ),
                    center = Offset(c.x - icon * 0.18f, c.y - icon * 0.22f),
                    radius = icon * 0.45f
                ),
                radius = icon * 0.45f,
                center = Offset(c.x - icon * 0.18f, c.y - icon * 0.22f)
            )
        }
        drawPath(
            path = gearPath,
            color = Color.White.copy(alpha = 0.18f + 0.10f * p),
            style = Stroke(width = 0.75.dp.toPx(), join = StrokeJoin.Round)
        )
        val dropLift = (breath - 0.5f) * icon * 0.020f - p * icon * 0.012f
        val dropScale = 0.78f + 0.08f * p
        val dropTop = Offset(c.x, c.y - icon * 0.150f * dropScale + dropLift)
        val dropPath = Path().apply {
            moveTo(dropTop.x, dropTop.y)
            cubicTo(
                c.x + icon * 0.120f * dropScale, c.y - icon * 0.030f * dropScale + dropLift,
                c.x + icon * 0.104f * dropScale, c.y + icon * 0.116f * dropScale + dropLift,
                c.x, c.y + icon * 0.152f * dropScale + dropLift
            )
            cubicTo(
                c.x - icon * 0.104f * dropScale, c.y + icon * 0.116f * dropScale + dropLift,
                c.x - icon * 0.120f * dropScale, c.y - icon * 0.030f * dropScale + dropLift,
                dropTop.x, dropTop.y
            )
            close()
        }
        drawPath(
            path = dropPath,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.48f + 0.18f * p),
                    Color(0xFF8FE4FF).copy(alpha = 0.72f + 0.16f * p),
                    Color(0xFF3F8CFF).copy(alpha = 0.58f + 0.16f * p)
                ),
                center = Offset(c.x - icon * 0.045f, c.y - icon * 0.045f + dropLift),
                radius = icon * 0.28f
            )
        )
        drawPath(
            path = dropPath,
            color = Color(0xFFC8F0FF).copy(alpha = 0.22f + 0.18f * p),
            style = Stroke(width = 0.55.dp.toPx(), join = StrokeJoin.Round)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.30f + 0.18f * p),
            radius = icon * 0.024f,
            center = Offset(c.x - icon * 0.035f, c.y - icon * 0.020f + dropLift)
        )
    }
}

@Composable
private fun MessageCard(state: HydraState) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF223158).copy(alpha = 0.55f),
                        Color(0xFF0E1730).copy(alpha = 0.72f)
                    )
                )
            )
            .border(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.28f),
                        Color.White.copy(alpha = 0.08f),
                        BlueAccent.copy(alpha = 0.05f)
                    )
                ),
                width = 1.0.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 18.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        AppText(
            text = if (state.todayLogs.isEmpty()) {
                "Nothing logged today 💔 before you sleep — just a little water? For me?"
            } else {
                "Nice work — ${formatHydraAmount(state.todayWaterMl)} of hydration logged today 💧"
            },
            color = Lavender,
            fontSize = 14,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontFamily = AppFont,
            lineHeight = 19
        )
    }
}

@Composable
private fun MainHeart() {
    val view = LocalView.current
    val scope = rememberCoroutineScope()

    val heartScale = remember { Animatable(1f) }
    val pressGlow = remember { Animatable(0f) }
    val ripple = remember { Animatable(0f) }

    fun startHeartPress() {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        scope.launch {
            heartScale.stop()
            heartScale.snapTo(heartScale.value.coerceAtLeast(1.024f))
            heartScale.animateTo(1.092f, tween(160, easing = FastOutSlowInEasing))
        }
        scope.launch {
            pressGlow.stop()
            pressGlow.snapTo(pressGlow.value.coerceAtLeast(0.24f))
            pressGlow.animateTo(0.86f, tween(120, easing = FastOutSlowInEasing))
        }
        scope.launch {
            ripple.stop()
            ripple.snapTo(0f)
            ripple.animateTo(1f, tween(430, easing = LinearOutSlowInEasing))
        }
    }

    fun endHeartPress() {
        scope.launch {
            heartScale.stop()
            heartScale.animateTo(1f, tween(420, easing = FastOutSlowInEasing))
        }
        scope.launch {
            pressGlow.stop()
            pressGlow.animateTo(0.34f, tween(95, easing = LinearOutSlowInEasing))
            pressGlow.animateTo(0f, tween(360, easing = LinearOutSlowInEasing))
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .aspectRatio(0.96f)
                .graphicsLayer {
                    val s = heartScale.value
                    val g = pressGlow.value
                    scaleX = s + 0.010f * g
                    scaleY = s + 0.004f * g
                }
                .pointerInteropFilter { event ->
                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            startHeartPress()
                            true
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            endHeartPress()
                            true
                        }
                        else -> true
                    }
                }
        ) {
            val w = size.width
            val h = size.height
            val g = pressGlow.value
            val r = ripple.value

            val heartPath = Path().apply {
                moveTo(w * 0.5f, h * 0.940f)
                cubicTo(w * 0.392f, h * 0.872f, w * 0.248f, h * 0.750f, w * 0.155f, h * 0.600f)
                cubicTo(w * 0.068f, h * 0.460f, w * 0.030f, h * 0.305f, w * 0.096f, h * 0.178f)
                cubicTo(w * 0.166f, h * 0.044f, w * 0.326f, h * 0.040f, w * 0.396f, h * 0.100f)
                cubicTo(w * 0.455f, h * 0.150f, w * 0.486f, h * 0.222f, w * 0.5f, h * 0.292f)
                cubicTo(w * 0.516f, h * 0.222f, w * 0.552f, h * 0.150f, w * 0.622f, h * 0.100f)
                cubicTo(w * 0.702f, h * 0.040f, w * 0.862f, h * 0.044f, w * 0.934f, h * 0.178f)
                cubicTo(w * 1.000f, h * 0.305f, w * 0.942f, h * 0.460f, w * 0.848f, h * 0.600f)
                cubicTo(w * 0.752f, h * 0.750f, w * 0.608f, h * 0.872f, w * 0.5f, h * 0.940f)
                close()
            }

            if (g > 0f) {
                drawPath(
                    path = heartPath,
                    color = Color(0xFF6F99FF).copy(alpha = 0.145f * g),
                    style = Stroke(
                        width = (38.0f + 9.0f * r).dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                drawPath(
                    path = heartPath,
                    color = Color(0xFFAFC8FF).copy(alpha = 0.210f * g),
                    style = Stroke(
                        width = (24.0f + 6.0f * r).dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                drawPath(
                    path = heartPath,
                    color = Color(0xFFC8DBFF).copy(alpha = 0.120f * g),
                    style = Stroke(
                        width = (10.0f + 3.5f * r).dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                drawPath(
                    path = heartPath,
                    color = Color(0xFF9DBBFF).copy(alpha = 0.050f * g)
                )
                drawPath(
                    path = heartPath,
                    color = Color(0xFFC4D8FF).copy(alpha = 0.105f * (1f - r) * g),
                    style = Stroke(
                        width = (2.8f + 7.5f * r).dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            drawPath(
                path = heartPath,
                color = Color(0xFF8FAAFF).copy(alpha = 0.055f),
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            drawPath(
                path = heartPath,
                color = Color(0xFFCFE0FF).copy(alpha = 0.025f),
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            drawPath(
                path = heartPath,
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFF8FAAFF),
                        0.36f to Color(0xFF4F76D8),
                        0.72f to Color(0xFF25427F),
                        1.0f to Color(0xFF172A5A)
                    ),
                    center = Offset(w * 0.24f, h * 0.18f),
                    radius = w * 1.10f
                )
            )

            clipPath(heartPath) {
                // 1. soft pillow edge shading — present but smooth, gives rounded curvature
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xFF203D72).copy(alpha = 0.050f),
                            Color(0xFF10234B).copy(alpha = 0.110f)
                        ),
                        center = Offset(w * 0.48f, h * 0.42f),
                        radius = w * 0.92f
                    )
                )
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFDDE8FF).copy(alpha = 0.105f),
                            Color(0xFF9EB8FF).copy(alpha = 0.055f),
                            Color(0xFF6288F0).copy(alpha = 0.020f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.36f, h * 0.34f),
                        radius = w * 0.84f
                    )
                )
                if (g > 0f) {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFE8F0FF).copy(alpha = 0.155f * g),
                                Color(0xFFD8E4FF).copy(alpha = 0.150f * g),
                                Color(0xFF83A7FF).copy(alpha = 0.072f * g),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.44f, h * 0.40f),
                            radius = w * 0.68f
                        )
                    )
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFC9DAFF).copy(alpha = 0.13f * g),
                                Color(0xFF6494F6).copy(alpha = 0.064f * g),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.56f, h * 0.60f),
                            radius = w * 0.76f
                        )
                    )
                }
                // 2. deepen the lower body & point
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xFF1D3C79).copy(alpha = 0.036f),
                            Color(0xFF10244D).copy(alpha = 0.130f)
                        ),
                        startY = h * 0.46f,
                        endY = h
                    )
                )
                // 3. faint cool reflection down the right side
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xFF8FAAFF).copy(alpha = 0.044f),
                            Color(0xFFBBD0FF).copy(alpha = 0.028f)
                        ),
                        startX = w * 0.5f,
                        endX = w * 0.98f
                    )
                )
                // 4. diagonal light flow (upper-left bright -> lower-right shaded)
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFCFE0FF).copy(alpha = 0.072f),
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xFF172A5A).copy(alpha = 0.078f)
                        ),
                        start = Offset(w * 0.16f, h * 0.04f),
                        end = Offset(w * 0.9f, h * 0.95f)
                    )
                )
                // 5. diffused highlight on the upper-left lobe — soft & feathered, melts into the gel
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFEAF1FF).copy(alpha = 0.165f),
                            Color(0xFFCFE0FF).copy(alpha = 0.070f),
                            Color(0xFF8FAAFF).copy(alpha = 0.024f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.22f, h * 0.18f),
                        radius = w * 0.74f
                    )
                )
                // 6. soft sheen catching the top of both lobes (wet/glossy surface)
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFDDE8FF).copy(alpha = 0.020f),
                            Color(0xFFC4D6FF).copy(alpha = 0.008f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.5f, h * 0.13f),
                        radius = w * 0.62f
                    ),
                    topLeft = Offset(w * 0.02f, h * -0.02f),
                    size = Size(w * 0.94f, h * 0.28f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF83A5F4).copy(alpha = 0.082f),
                            Color(0xFF6285E2).copy(alpha = 0.040f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.68f, h * 0.66f),
                        radius = w * 0.72f
                    ),
                    center = Offset(w * 0.68f, h * 0.66f),
                    radius = w * 0.72f
                )
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF8AA7EF).copy(alpha = 0.070f),
                            Color(0xFF466FD1).copy(alpha = 0.028f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.58f, h * 0.76f),
                        radius = w * 0.78f
                    ),
                    topLeft = Offset(w * 0.04f, h * 0.48f),
                    size = Size(w * 0.92f, h * 0.46f)
                )
            }

            // ── Angry face ──
            val faceCenterY = h * 0.350f
            val faceScale = 0.88f
            val eyeSpacing = 9.4.dp.toPx() * faceScale
            val eyeY = faceCenterY

            drawCircle(Color.White.copy(alpha = 0.92f), 0.72.dp.toPx() * faceScale, Offset(w * 0.5f - eyeSpacing / 2, eyeY))
            drawCircle(Color.White.copy(alpha = 0.92f), 0.72.dp.toPx() * faceScale, Offset(w * 0.5f + eyeSpacing / 2, eyeY))

            val browW = 4.1.dp.toPx() * faceScale
            val browH = 1.75.dp.toPx() * faceScale
            val browOffset = 3.4.dp.toPx() * faceScale

            // left brow: outer-high -> inner-low (angry)
            drawLine(
                color = Color.White.copy(alpha = 0.94f),
                start = Offset(w * 0.5f - eyeSpacing / 2 - browW / 2, eyeY - browOffset - browH),
                end = Offset(w * 0.5f - eyeSpacing / 2 + browW / 2, eyeY - browOffset),
                strokeWidth = 0.85.dp.toPx() * faceScale,
                cap = StrokeCap.Round
            )
            // right brow: mirror
            drawLine(
                color = Color.White.copy(alpha = 0.94f),
                start = Offset(w * 0.5f + eyeSpacing / 2 + browW / 2, eyeY - browOffset - browH),
                end = Offset(w * 0.5f + eyeSpacing / 2 - browW / 2, eyeY - browOffset),
                strokeWidth = 0.85.dp.toPx() * faceScale,
                cap = StrokeCap.Round
            )

            val frown = Path().apply {
                moveTo(w * 0.5f - 3.7.dp.toPx() * faceScale, faceCenterY + 7.4.dp.toPx() * faceScale)
                cubicTo(
                    w * 0.5f - 1.9.dp.toPx() * faceScale,
                    faceCenterY + 4.2.dp.toPx() * faceScale,
                    w * 0.5f + 1.9.dp.toPx() * faceScale,
                    faceCenterY + 4.2.dp.toPx() * faceScale,
                    w * 0.5f + 3.7.dp.toPx() * faceScale,
                    faceCenterY + 7.4.dp.toPx() * faceScale
                )
            }
            drawPath(
                frown,
                Color.White.copy(alpha = 0.92f),
                style = Stroke(width = 0.78.dp.toPx() * faceScale, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
private fun ProgressSection(state: HydraState) {
    val amount = state.todayWaterMl
    val goal = state.dailyWaterGoalMl
    val percent = (amount / goal.coerceAtLeast(1f) * 100f).toInt()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.Bottom) {
            AppText(
                text = formatHydraAmount(amount),
                color = Color(0xFFF6F9FF),
                fontSize = 40,
                fontWeight = FontWeight.Bold,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.width(7.dp))
            AppText(
                text = "/ ${formatHydraAmount(goal)}",
                color = Lavender.copy(alpha = 0.9f),
                fontSize = 17,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontFamily = AppFont,
                modifier = Modifier.padding(bottom = 7.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF1B2A52),
                            Color(0xFF22335F),
                            Color(0xFF18254A)
                        )
                    )
                )
                .border(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.10f),
                            BlueAccent.copy(alpha = 0.10f)
                        )
                    ),
                    width = 0.8.dp,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            AppText(
                text = "$percent%",
                color = Color(0xFFF1F5FF),
                fontSize = 14,
                fontWeight = FontWeight.Bold,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
private fun StatsSection(state: HydraState) {
    GlassCard {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NutrientItem("Caffeine", "${state.todayCaffeineMg.toInt()}/${state.dailyCaffeineLimitMg.toInt()}mg", Color(0xFFC9A079), Modifier.weight(1f))
                NutrientItem("Sugar", "0/50g", Color(0xFFFF5CA8), Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NutrientItem("Calcium", "${state.todayCalciumMg.toInt()}/${state.dailyCalciumGoalMg.toInt()}mg", Color(0xFFFFDC4B), Modifier.weight(1f))
                NutrientItem("Alcohol", "0/16g", Color(0xFFA574E0), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun NutrientItem(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            AppText(
                text = label,
                color = Lavender.copy(alpha = 0.9f),
                fontSize = if (compact) 12 else 13,
                modifier = Modifier.weight(0.5f),
                fontWeight = FontWeight.Bold,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                softWrap = false
            )
            AppText(
                text = value,
                color = valueColor,
                fontSize = if (compact) 11 else 12,
                modifier = Modifier.weight(0.5f),
                fontWeight = FontWeight.Bold,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false
            )
        }
        Spacer(modifier = Modifier.height(if (compact) 4.dp else 5.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (compact) 3.dp else 4.dp)
                .clip(CircleShape)
                .background(Color(0xFF1C2A50).copy(alpha = 0.85f))
        )
    }
}

@Composable
private fun ElectrolytesSection() {
    GlassCard {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicText(text = "⚡", style = TextStyle(fontSize = 14.sp))
                Spacer(modifier = Modifier.width(6.dp))
                AppText(
                    text = "Electrolytes",
                    color = Lavender.copy(alpha = 0.9f),
                    fontSize = 14,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NutrientItem("Sodium", "0/2300mg", Color(0xFFFF7D35), Modifier.weight(1f))
                NutrientItem("Potassium", "0/2600mg", Color(0xFF45D887), Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NutrientItem("Magnesium", "0/320mg", Color(0xFF54A7FF), Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StreakTrophySection(state: HydraState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BigStatCard(icon = { PremiumStreakIcon() }, label = "${state.currentStreak} Streak", modifier = Modifier.weight(1f))
        BigStatCard(icon = { PremiumTrophyIcon() }, label = "${state.earnedAchievements.size} Trophies", modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BigStatCard(icon: @Composable () -> Unit, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(92.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(cardBrush)
            .border(brush = cardBorder, width = 0.95.dp, shape = RoundedCornerShape(18.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            icon()
            Spacer(modifier = Modifier.height(8.dp))
            AppText(
                text = label,
                color = Lavender.copy(alpha = 0.92f),
                fontSize = 13,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic,
                letterSpacing = 0.6,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PremiumStreakIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.streak_flame),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(26.dp)
    )
}

@Composable
private fun PremiumTrophyIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.trophy_gold),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(24.dp)
    )
}

@Composable
private fun FlameIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height

        // warm ambient glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFF7A1E).copy(alpha = 0.40f),
                    Color(0xFFFF4D12).copy(alpha = 0.12f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, h * 0.66f),
                radius = w * 0.66f
            ),
            radius = w * 0.66f,
            center = Offset(w * 0.5f, h * 0.66f)
        )

        // outer flame body — leans slightly for a lively silhouette
        val outer = Path().apply {
            moveTo(w * 0.51f, h * 0.02f)
            cubicTo(w * 0.66f, h * 0.18f, w * 0.92f, h * 0.42f, w * 0.85f, h * 0.66f)
            cubicTo(w * 0.81f, h * 0.88f, w * 0.67f, h * 0.99f, w * 0.50f, h * 0.99f)
            cubicTo(w * 0.32f, h * 0.99f, w * 0.16f, h * 0.87f, w * 0.15f, h * 0.65f)
            cubicTo(w * 0.11f, h * 0.44f, w * 0.34f, h * 0.22f, w * 0.51f, h * 0.02f)
            close()
        }
        // dimensional fill: hot core low-center fading to deep red at the tip/edges
        drawPath(
            path = outer,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFD64A),
                    Color(0xFFFF8A1E),
                    Color(0xFFF4470F),
                    Color(0xFFCC2D08)
                ),
                center = Offset(w * 0.48f, h * 0.74f),
                radius = w * 0.80f
            )
        )
        // cool blue base — the hottest part, a touch emojis don't have
        drawPath(
            path = outer,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Transparent,
                    Color(0xFF63C6FF).copy(alpha = 0.50f)
                ),
                startY = h * 0.58f,
                endY = h * 1.02f
            )
        )

        // inner (hot) flame
        val inner = Path().apply {
            moveTo(w * 0.50f, h * 0.44f)
            cubicTo(w * 0.62f, h * 0.55f, w * 0.67f, h * 0.69f, w * 0.63f, h * 0.80f)
            cubicTo(w * 0.60f, h * 0.89f, w * 0.55f, h * 0.94f, w * 0.50f, h * 0.94f)
            cubicTo(w * 0.45f, h * 0.94f, w * 0.40f, h * 0.89f, w * 0.37f, h * 0.80f)
            cubicTo(w * 0.33f, h * 0.69f, w * 0.38f, h * 0.55f, w * 0.50f, h * 0.44f)
            close()
        }
        drawPath(
            path = inner,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFFE067),
                    Color(0xFFFFF3C0),
                    Color(0xFFFFFFFF)
                ),
                startY = h * 0.44f,
                endY = h * 0.94f
            )
        )

        // glossy specular hotspot
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.85f), Color.Transparent),
                center = Offset(w * 0.41f, h * 0.40f),
                radius = w * 0.10f
            ),
            radius = w * 0.10f,
            center = Offset(w * 0.41f, h * 0.40f)
        )
    }
}

@Composable
private fun TrophyIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val gold = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFF4C2),
                Color(0xFFFFD24E),
                Color(0xFFE89A1E),
                Color(0xFFB06E12)
            ),
            center = Offset(w * 0.40f, h * 0.22f),
            radius = w * 0.78f
        )
        val edge = Color(0xFF7A4A0C)
        val handleGold = Color(0xFFE89A1E)

        // soft gold glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFD24E).copy(alpha = 0.22f), Color.Transparent),
                center = Offset(w * 0.5f, h * 0.34f),
                radius = w * 0.62f
            ),
            radius = w * 0.62f,
            center = Offset(w * 0.5f, h * 0.34f)
        )

        // handles — dark edge then gold, so they read as raised loops
        val handleW = w * 0.10f
        val leftHandle = Path().apply {
            moveTo(w * 0.29f, h * 0.18f)
            cubicTo(w * 0.05f, h * 0.16f, w * 0.07f, h * 0.47f, w * 0.34f, h * 0.44f)
        }
        val rightHandle = Path().apply {
            moveTo(w * 0.71f, h * 0.18f)
            cubicTo(w * 0.95f, h * 0.16f, w * 0.93f, h * 0.47f, w * 0.66f, h * 0.44f)
        }
        drawPath(leftHandle, color = edge, style = Stroke(width = handleW + 1.4.dp.toPx(), cap = StrokeCap.Round))
        drawPath(rightHandle, color = edge, style = Stroke(width = handleW + 1.4.dp.toPx(), cap = StrokeCap.Round))
        drawPath(leftHandle, color = handleGold, style = Stroke(width = handleW, cap = StrokeCap.Round))
        drawPath(rightHandle, color = handleGold, style = Stroke(width = handleW, cap = StrokeCap.Round))

        // cup bowl with dark rim + metallic fill
        val bowl = Path().apply {
            moveTo(w * 0.25f, h * 0.14f)
            lineTo(w * 0.75f, h * 0.14f)
            cubicTo(w * 0.75f, h * 0.50f, w * 0.64f, h * 0.62f, w * 0.50f, h * 0.62f)
            cubicTo(w * 0.36f, h * 0.62f, w * 0.25f, h * 0.50f, w * 0.25f, h * 0.14f)
            close()
        }
        drawPath(bowl, color = edge, style = Stroke(width = 1.6.dp.toPx(), join = StrokeJoin.Round))
        drawPath(bowl, brush = gold)

        // star emblem on the cup face
        val starPath = Path()
        val cx = w * 0.5f
        val cy = h * 0.31f
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) w * 0.115f else w * 0.048f
            val a = (-90f + i * 36f) * PI.toFloat() / 180f
            val px = cx + cos(a).toFloat() * r
            val py = cy + sin(a).toFloat() * r
            if (i == 0) starPath.moveTo(px, py) else starPath.lineTo(px, py)
        }
        starPath.close()
        drawPath(starPath, color = Color(0xFFFFF7D8))

        // glossy shine streak on the bowl
        drawLine(
            color = Color(0xFFFFF7D8).copy(alpha = 0.55f),
            start = Offset(w * 0.33f, h * 0.20f),
            end = Offset(w * 0.31f, h * 0.44f),
            strokeWidth = w * 0.03f,
            cap = StrokeCap.Round
        )

        // stem
        drawRect(
            brush = gold,
            topLeft = Offset(w * 0.45f, h * 0.60f),
            size = Size(w * 0.10f, h * 0.13f)
        )

        // base tiers — each a dark outline behind a gold top
        drawRoundRect(
            color = edge,
            topLeft = Offset(w * 0.385f, h * 0.705f),
            size = Size(w * 0.23f, h * 0.085f),
            cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
        )
        drawRoundRect(
            brush = gold,
            topLeft = Offset(w * 0.40f, h * 0.71f),
            size = Size(w * 0.20f, h * 0.075f),
            cornerRadius = CornerRadius(w * 0.02f, w * 0.02f)
        )
        drawRoundRect(
            color = edge,
            topLeft = Offset(w * 0.305f, h * 0.785f),
            size = Size(w * 0.39f, h * 0.12f),
            cornerRadius = CornerRadius(w * 0.035f, w * 0.035f)
        )
        drawRoundRect(
            brush = gold,
            topLeft = Offset(w * 0.32f, h * 0.79f),
            size = Size(w * 0.36f, h * 0.105f),
            cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
        )
    }
}

@Composable
private fun RewardGemIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val gem = Path().apply {
            moveTo(w * 0.50f, h * 0.05f)
            lineTo(w * 0.86f, h * 0.36f)
            lineTo(w * 0.50f, h * 0.96f)
            lineTo(w * 0.14f, h * 0.36f)
            close()
        }
        drawPath(
            path = gem,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFF7FBFF).copy(alpha = 0.96f),
                    Color(0xFF83B8FF).copy(alpha = 0.86f),
                    Color(0xFF3159B5).copy(alpha = 0.66f)
                ),
                center = Offset(w * 0.35f, h * 0.24f),
                radius = w * 0.66f
            )
        )
        drawPath(
            path = gem,
            color = Color.White.copy(alpha = 0.40f),
            style = Stroke(width = 0.8.dp.toPx(), join = StrokeJoin.Round)
        )
        drawLine(
            color = Color.White.copy(alpha = 0.28f),
            start = Offset(w * 0.14f, h * 0.36f),
            end = Offset(w * 0.86f, h * 0.36f),
            strokeWidth = 0.65.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(0xFF112452).copy(alpha = 0.20f),
            start = Offset(w * 0.50f, h * 0.05f),
            end = Offset(w * 0.50f, h * 0.96f),
            strokeWidth = 0.55.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun NextRewardCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cardBrush)
            .border(brush = cardBorder, width = 0.95.dp, shape = RoundedCornerShape(18.dp))
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Column {
            AppText(
                text = "Next Reward — Level 2",
                color = Lavender.copy(alpha = 0.78f),
                fontSize = 14,
                fontFamily = AppFont,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(7.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RewardGemIcon()
                Spacer(modifier = Modifier.width(8.dp))
                AppText(
                    text = "Necklace for Hydra",
                    color = Color(0xFFF1F5FF),
                    fontSize = 15,
                    fontFamily = AppFont,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun GlassCard(
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cardBrush)
            .border(brush = cardBorder, width = 0.95.dp, shape = RoundedCornerShape(18.dp))
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        content()
    }
}

@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    selected: String = "Home",
    onHomeClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onLearnClick: () -> Unit = {},
    onTrophiesClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalBottomSelection provides selected) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(116.dp)
    ) {
        Canvas(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(116.dp)
        ) {
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF4A78E8).copy(alpha = 0.20f),
                        Color(0xFF1B2A52).copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.22f),
                    radius = size.width * 0.52f
                ),
                topLeft = Offset(size.width * 0.1f, -18.dp.toPx()),
                size = Size(size.width * 0.8f, 86.dp.toPx())
            )
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color(0xFF060A18).copy(alpha = 0.72f)),
                    startY = size.height * 0.3f,
                    endY = size.height
                )
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(98.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 36.dp,
                        topEnd = 36.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val round = 34.dp.toPx()
                val inset = 1.dp.toPx()

                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF38496E).copy(alpha = 0.46f),
                            Color(0xFF1A2647).copy(alpha = 0.66f),
                            Color(0xFF080D1F).copy(alpha = 0.84f)
                        ),
                        startY = 0f,
                        endY = size.height
                    )
                )

                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.12f),
                            Color(0xFF7AA4FF).copy(alpha = 0.07f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.18f, size.height * 0.1f),
                        radius = size.width * 0.52f
                    ),
                    topLeft = Offset(-size.width * 0.08f, -size.height * 0.62f),
                    size = Size(size.width * 0.64f, size.height * 1.28f)
                )

                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF05080F).copy(alpha = 0.4f)
                        ),
                        startY = size.height * 0.7f,
                        endY = size.height
                    )
                )

                drawPath(
                    path = Path().apply {
                        moveTo(22.dp.toPx(), 16.dp.toPx())
                        cubicTo(
                            size.width * 0.25f, 1.dp.toPx(),
                            size.width * 0.72f, 1.dp.toPx(),
                            size.width - 22.dp.toPx(),
                            16.dp.toPx()
                        )
                    },
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.28f),
                            Color(0xFF7AA4FF).copy(alpha = 0.14f),
                            Color.Transparent
                        )
                    ),
                    style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
                )

                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.30f),
                            Color(0xFF7AA4FF).copy(alpha = 0.07f),
                            Color.Transparent
                        )
                    ),
                    cornerRadius = CornerRadius(round, round),
                    style = Stroke(width = 1.dp.toPx())
                )

                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.12f),
                    topLeft = Offset(inset * 2f, inset * 2f),
                    size = Size(size.width - inset * 4f, size.height + round),
                    cornerRadius = CornerRadius(round - inset, round - inset),
                    style = Stroke(width = 1.1.dp.toPx())
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
                .height(72.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegacyBottomItem(icon = "", label = "Home", selected = true)
            LegacyBottomItem(icon = "", label = "History")
            Spacer(modifier = Modifier.width(42.dp))
            LegacyBottomItem(icon = "", label = "Learn")
            LegacyBottomItem(icon = "", label = "Trophies")
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
                .height(72.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(62.dp)
                    .fillMaxHeight()
                    .clickable(onClick = onHomeClick)
            )
            Box(
                modifier = Modifier
                    .width(62.dp)
                    .fillMaxHeight()
                    .clickable(onClick = onHistoryClick)
            )
            Spacer(modifier = Modifier.width(42.dp))
            Box(
                modifier = Modifier
                    .width(62.dp)
                    .fillMaxHeight()
                    .clickable(onClick = onLearnClick)
            )
            Box(
                modifier = Modifier
                    .width(62.dp)
                    .fillMaxHeight()
                    .clickable(onClick = onTrophiesClick)
            )
        }

        CenterActionButton(modifier = Modifier.align(Alignment.TopCenter).offset(y = 0.dp))
    }
    }
}

@Composable
private fun CenterActionButton(modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.025f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.42f,
        targetValue = 0.74f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .size(84.dp)
            .scale(pulseScale),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF6E9BFF).copy(alpha = 0.26f * glowAlpha),
                        Color(0xFF4A78E8).copy(alpha = 0.08f * glowAlpha),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.width * 0.5f
                ),
                radius = size.width * 0.5f,
                center = center
            )
        }

        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF9DBBFF),
                            Color(0xFF6E96F2),
                            Color(0xFF4A6CD8),
                            Color(0xFF2E489E)
                        )
                    )
                )
                .border(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.42f),
                            Color(0xFFBFD2FF).copy(alpha = 0.14f),
                            Color.Black.copy(alpha = 0.12f)
                        )
                    ),
                    width = 1.1.dp,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.24f),
                            Color.White.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.34f, size.height * 0.24f),
                        radius = size.width * 0.58f
                    ),
                    center = Offset(size.width * 0.34f, size.height * 0.24f),
                    radius = size.width * 0.58f
                )
            }

            Canvas(modifier = Modifier.size(28.dp)) {
                val s = 1.4.dp.toPx()
                val plusColor = Color(0xFF0E1A3A)
                drawLine(
                    color = plusColor,
                    start = Offset(size.width / 2, size.height * 0.12f),
                    end = Offset(size.width / 2, size.height * 0.88f),
                    strokeWidth = s,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = plusColor,
                    start = Offset(size.width * 0.24f, size.height / 2),
                    end = Offset(size.width * 0.76f, size.height / 2),
                    strokeWidth = s,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun LegacyBottomItem(icon: String, label: String, selected: Boolean = false) {
    val active = LocalBottomSelection.current == label
    val opacity by animateFloatAsState(if (active) 1f else 0.78f, label = "navOpacity")
    val labelColor = if (active) BlueAccent else Lavender.copy(alpha = 0.85f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(62.dp)
            .offset(y = 3.dp)
            .graphicsLayer(alpha = opacity)
    ) {
        BasicText(
            text = icon,
            style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center)
        )
        Spacer(modifier = Modifier.height(1.dp))
        AppText(
            text = label,
            color = labelColor,
            fontSize = 11,
            fontWeight = FontWeight.Bold,
            fontFamily = AppFont,
            fontStyle = FontStyle.Italic,
            letterSpacing = 0.2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AppText(
    text: String,
    color: Color,
    fontSize: Int,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = FontFamily.Default,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    lineHeight: Int? = null,
    letterSpacing: Double = 0.0,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    softWrap: Boolean = true
) {
    BasicText(
        text = text,
        modifier = modifier,
        maxLines = maxLines,
        overflow = TextOverflow.Clip,
        softWrap = softWrap,
        style = TextStyle(
            color = color,
            fontSize = fontSize.sp,
            fontFamily = fontFamily,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            lineHeight = (lineHeight ?: fontSize).sp,
            letterSpacing = letterSpacing.sp,
            textAlign = textAlign
        )
    )
}
