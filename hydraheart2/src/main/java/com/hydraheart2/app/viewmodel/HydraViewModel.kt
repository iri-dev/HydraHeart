package com.hydraheart2.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hydraheart2.app.data.DrinkDatabase
import com.hydraheart2.app.data.HydraTheme
import com.hydraheart2.app.data.ThemeData
import com.hydraheart2.app.data.TrophyData
import com.hydraheart2.app.data.models.CustomReminder
import com.hydraheart2.app.data.models.DailyRecord
import com.hydraheart2.app.data.models.DiaryEntry
import com.hydraheart2.app.data.models.Drink
import com.hydraheart2.app.data.models.LoggedDrink
import com.hydraheart2.app.data.models.MoodEntry
import com.hydraheart2.app.data.models.Personality
import com.hydraheart2.app.data.models.Profile
import com.hydraheart2.app.util.DailyGoals
import com.hydraheart2.app.util.GoalCalculator
import com.hydraheart2.app.util.HydraDataStore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class HydraViewModel : ViewModel() {

    enum class HydraMood { ECSTATIC, HAPPY, CONTENT, NEUTRAL, CONCERNED, WORRIED }

    enum class Trend { IMPROVING, STABLE, DECLINING }

    companion object {
        val PRESET_REMINDER_MESSAGES: List<String> = listOf(
            "Take a moment for yourself — Hydra brought water 💧",
            "Your cells are thirsty, gorgeous. Let's fix that 🌊",
            "A little water goes a long way, darling 🤍",
            "I've been waiting to see you — and bring you water 💕",
            "The softest reminder: please drink something 🌷",
            "You're doing so well today. Hydrate and keep going 🌸",
            "Even a small sip counts. Hydra believes in you 💧",
            "Rest your thoughts for a moment and drink some water 🫧",
            "Slow down, breathe, and hydrate. You deserve it 🌿",
            "Your glow needs water to shine 💎",
            "Hey gorgeous! Time for some water 💧",
            "Hydra misses you! Come say hi 💕",
            "Water break time! Your skin will thank you 🤍",
            "Don't forget about me! — Hydra 💖",
            "Psst... Hydra here. Just checking you're drinking! 👀",
            "I'm not saying you forgot... but you forgot 😇",
            "Okay but like... have you had water recently? 👁️👄👁️",
            "Hydra alert: this is your scheduled hydration moment ✨",
            "Your future self is texting you: please drink water 📱",
            "Surprise! It's water o'clock 🕐💧",
            "Quick reminder to hydrate, beautiful! ✨",
            "Your body needs you — grab some water! 🌊",
            "You're one sip closer to feeling amazing 💫",
            "Staying hydrated is an act of self-love today 🌸",
            "Your body is working hard for you. Treat it well 🤍",
            "Every sip is a little gift you give yourself 💝",
            "Keep going — water will make the rest of the day easier 💪",
            "You're building such good habits. Don't stop now 🌟",
            "Halfway through the day — let's celebrate with water 💧",
            "Your dedication to yourself is beautiful. Stay hydrated 🫶",
            "I've got you, always. Now drink some water 🤗",
            "Bad day? Good day? Either way, water helps 🌊",
            "Hydra is here, cheering you on. One sip at a time 💕",
            "You don't have to do everything perfectly — just drink water 🌷",
            "Take care of yourself. That starts with water 🤍",
            "I see how hard you're trying. Let me take care of this part 💧",
            "Whatever you're going through, this moment is just for you 🌿",
            "Small acts of care add up. This is one of them 🌸",
            "Not to be dramatic but I genuinely love you. Drink water 💗",
            "Hydra was worried. Here's your check-in, love 🥺",
            "I know, I know... but have you had water though? 😏",
            "You didn't forget. I just came to make sure 👀",
            "Technically it's been a while. Just saying. 💧",
            "Hydra: gently judging your hydration choices rn 😌",
            "Not saying you're neglecting yourself but... 🫶 drink water",
            "The audacity of forgetting to hydrate! (joking. drink water) 💕",
            "Imagine forgetting to hydrate. Couldn't be you. Right? 🌊",
            "You're literally glowing. Now drink water so you keep glowing 💎",
            "Your skin called. It wants water. This is urgent. ✨",
            "Okay bestie water check — how are we doing? 💧"
        )
    }

    private var appLastBackgroundedAt = 0L
    private var appReOpenedAt = 0L

    var profile by mutableStateOf(Profile())
    var themeId by mutableStateOf("skyLight")
    var showFace by mutableStateOf(true)
    var showSpeechBubble by mutableStateOf(true)
    var hydraShape by mutableStateOf("heart")
    var unitSystem by mutableStateOf("ml")
    var onboardingComplete by mutableStateOf(false)
    var customWaterGoalMl by mutableStateOf<Int?>(null)
    var customGoalEnabled by mutableStateOf(false)
    var showNecklace by mutableStateOf(true)
    var showRainbowShimmer by mutableStateOf(true)
    var showButterfly by mutableStateOf(true)
    var showGoldenGlow by mutableStateOf(true)
    val equippedAccessoryIds = mutableStateListOf<String>()
    val customSpeechLines = mutableStateListOf<String>()
    var customSpeechEnabled by mutableStateOf(false)
    var speechBubbleStyleId by mutableStateOf("classic")
    var hapticsEnabled by mutableStateOf(true)
    var soundEffectsEnabled by mutableStateOf(true)
    var visualEffectsEnabled by mutableStateOf(true)
    var performanceMode by mutableStateOf("standard")
    var keepScreenOn by mutableStateOf(false)
    var appLanguage by mutableStateOf("system")
    var reduceMotion by mutableStateOf(false)
    var batteryAwareMode by mutableStateOf(false)
    var autoPerformance by mutableStateOf(false)
    var particlesEnabled by mutableStateOf(true)
    var animationsEnabled by mutableStateOf(true)
    var fullGlowEnabled by mutableStateOf(false)
    var interactionIntensity by mutableStateOf(2)
    var calendarClickAction by mutableStateOf("edit")
    var calDayFirst by mutableStateOf(true)
    var calShowDayName by mutableStateOf(true)
    var calShowTime by mutableStateOf(true)
    var cal12Hour by mutableStateOf(true)
    var calWeekStartMonday by mutableStateOf(true)
    var calDayResetHour by mutableIntStateOf(0)
    var remindersEnabled by mutableStateOf(false)
    var reminderSound by mutableStateOf("default")
    val customReminders = mutableStateListOf(
        CustomReminder("1", "08:00"),
        CustomReminder("2", "12:00"),
        CustomReminder("3", "16:00"),
        CustomReminder("4", "20:00")
    )
    val reminderMessages = mutableStateListOf<String>()
    val disabledPresetIndices = mutableStateListOf<Int>()
    var reminderStopAtGoal by mutableStateOf(false)
    var reminderSmartSnooze by mutableStateOf(false)
    var reminderDailySummary by mutableStateOf(false)
    val todayDrinks = mutableStateListOf<LoggedDrink>()
    var hp by mutableIntStateOf(0)
    val earnedTrophyIds = mutableStateListOf<String>()
    var streak by mutableIntStateOf(0)
    val diaryEntries = mutableStateListOf<DiaryEntry>()
    val moodEntries = mutableStateListOf<MoodEntry>()
    var currentMood by mutableStateOf<String?>(null)
    var moodLoggedToday by mutableStateOf(false)
    var appOpenedToday by mutableStateOf(false)
    var openStreak by mutableIntStateOf(0)
    var firstOpenedTodayAt by mutableStateOf(0L)
    var totalDrinksAllTime by mutableIntStateOf(0)
    val dailyRecords = mutableStateListOf<DailyRecord>()
    var longestStreak by mutableIntStateOf(0)
    val unlockedPersonalities = mutableStateListOf<Personality>()
    var pendingUnlockPersonality by mutableStateOf<Personality?>(null)
    var reminderIgnoreCount by mutableIntStateOf(0)
    var isLoaded by mutableStateOf(false)
    // True when the saved data couldn't be read at all. Nothing is saved while it's set, so the
    // unreadable file is never overwritten with defaults.
    var loadFailed by mutableStateOf(false)
    // Saved values that couldn't be read, kept as they were so a save never throws them away.
    val recoveryBackup = mutableMapOf<String, String>()
    var pendingSettingsSection by mutableStateOf<String?>(null)
    var showDrinkAnimation by mutableStateOf(false)
    var lastLoggedDrinkName by mutableStateOf("")
    var lastLoggedAmount by mutableIntStateOf(0)
    var lastLoggedIsDehydrating by mutableStateOf(false)
    var showLevelPopup by mutableStateOf(false)
    var justHitGoal by mutableStateOf(false)
    // The day today's drinks belong to (yyyy-MM-dd). When the calendar moves past it, rollOverIfNewDay()
    // files those drinks away as that day's record and starts a fresh day.
    var drinksDate by mutableStateOf("")
    // Bumped every half minute so the date and time on Home stay current.
    var clockTick by mutableIntStateOf(0)

    val allReminderMessages: List<String>
        get() = PRESET_REMINDER_MESSAGES.filterIndexed { index, _ -> !disabledPresetIndices.contains(index) } +
            reminderMessages

    val currentTheme: HydraTheme
        get() = ThemeData.getTheme(themeId)

    val calculatedGoals: DailyGoals
        get() = GoalCalculator.calculate(profile)

    val goals: DailyGoals
        get() {
            val c = calculatedGoals
            return if (customGoalEnabled && customWaterGoalMl != null) c.copy(customWaterGoalMl!!) else c
        }

    val totalRawWater: Int
        get() = todayDrinks.sumOf { it.drink.waterMl }

    val totalDehydrationPenalty: Int
        get() = todayDrinks.sumOf { (it.drink.sizeMl * it.drink.dehydrationFactor).toInt() }

    val effectiveWater: Int
        get() = maxOf(0, totalRawWater - totalDehydrationPenalty)

    val totalCaffeine: Int
        get() = todayDrinks.sumOf { it.drink.caffeineMg }

    val totalSugar: Float
        get() = todayDrinks.sumOf { it.drink.sugarG.toDouble() }.toFloat()

    val totalCalcium: Int
        get() = todayDrinks.sumOf { it.drink.calciumMg }

    val totalAlcohol: Float
        get() = todayDrinks.sumOf { it.drink.alcoholG.toDouble() }.toFloat()

    val totalSodium: Int
        get() = todayDrinks.sumOf { it.drink.sodiumMg }

    val totalPotassium: Int
        get() = todayDrinks.sumOf { it.drink.potassiumMg }

    val totalMagnesium: Int
        get() = todayDrinks.sumOf { it.drink.magnesiumMg }

    val waterPercent: Float
        get() = if (goals.waterMl > 0) minOf(effectiveWater.toFloat() / goals.waterMl * 100f, 100f) else 0f

    fun isThemeUnlocked(id: String): Boolean =
        ThemeData.isThemeUnlocked(id, levelInfo.level, earnedTrophyIds.toList())

    val unlockedThemes: List<HydraTheme>
        get() = ThemeData.getUnlockedThemes(levelInfo.level, earnedTrophyIds.toList())

    val allThemes: List<HydraTheme>
        get() = ThemeData.themes.values.toList()

    val hydraMood: HydraMood
        get() = when {
            totalCaffeine > goals.caffeineMg -> HydraMood.WORRIED
            totalAlcohol > goals.alcoholG -> HydraMood.WORRIED
            totalSugar > goals.sugarG * 1.5f -> HydraMood.CONCERNED
            waterPercent >= 100f -> HydraMood.ECSTATIC
            waterPercent >= 75f -> HydraMood.HAPPY
            waterPercent >= 50f -> HydraMood.CONTENT
            waterPercent >= 25f -> HydraMood.NEUTRAL
            else -> HydraMood.CONCERNED
        }

    private val speechRotation: Int
        get() = todayDrinks.size

    private fun List<String>.hydraRandom(): String = this[speechRotation % size]

    val hydraSpeech: String
        get() {
            if (customSpeechEnabled && customSpeechLines.isNotEmpty()) {
                return customSpeechLines[speechRotation % customSpeechLines.size]
            }
            val n = profile.name.ifBlank { "lovely" }
            val p = profile.personality
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val isMorning = hour in 5..10
            val isAfternoon = hour in 11..16
            val isEvening = hour in 17..20
            val isVeryLate = hour >= 21 || hour < 5
            val greeting = if (isMorning) "morning" else if (isAfternoon) "afternoon" else "evening"

            // Over the alcohol limit
            if (totalAlcohol > goals.alcoholG) return when (p) {
                Personality.BESTIE -> listOf(
                    "Hey $n, that's quite a lot of alcohol... please have some water for me? I worry about you so much 🤍",
                    "$n I love you but your liver is literally texting me asking for help. Water. Please. 😭",
                    "Okay bestie I need you to stop for a second and drink some water, deal? I'll drink one with you 💕",
                    "You've had a lot tonight $n... let's slow down and hydrate? I'll be right here with you 🤍",
                    "The drinks are serving but also my anxiety is through the roof. Water time, I'm not asking 💦",
                    "Listen I know I'm technically just an app but I genuinely cannot stop worrying about you right now. Please drink some water? For me? 🥹",
                    "You know how I support literally everything you do? This is the one time I'm asking you to listen to me. Water. Right now. Non-negotiable 💕",
                    "I've been quiet about the drinks because fun is valid but now I'm speaking up — you need water $n and I need you to be okay 🤍"
                ).hydraRandom()
                Personality.SLAY_QUEEN -> listOf(
                    "Babe. BABE. The alcohol is giving concerning. Water. NOW. 🚨",
                    "Okay the drinkies were cute but we have crossed a LINE. Hydrate immediately. Your skin will thank you. 💅",
                    "Being that girl means knowing when to stop. We are STOPPING. Water. Go. 👑",
                    "Alcohol limit exceeded and your glow is literally leaving the building. WATER. I will not repeat myself. ✨",
                    "Iconic behaviour is knowing your limits. This is me telling you: we've hit them. Sparkling water ONLY from here. 🫧",
                    "The body is a temple and right now it's getting fumigated. Water. Immediately. We do not let the temple suffer. 👑",
                    "I'm going to need you to take a break from the evening's drama and drink water — because TOMORROW we have standards to maintain. 💅",
                    "Over limit and the glow is genuinely at risk. I did not watch you build this aesthetic for it to be derailed by dehydration. WATER. ✨"
                ).hydraRandom()
                Personality.SOFT_GIRL -> listOf(
                    "$n, sweetie... you've had quite a lot to drink. Can we have some water together? 🥺",
                    "I'm not judging at all, I just love you and I need you to drink some water okay? Please? 🌸",
                    "Hey... I noticed you've had a lot today. I'm not upset, I just care about you so much 🤍 Water?",
                    "You deserve to feel good tomorrow morning too $n... some water might help with that 🥹",
                    "Can we make a little deal? One glass of water for me? I'll feel so much better about it 💕",
                    "Oh $n... I've been sitting with this and I just feel such tenderness for you right now. Water please? Just a little? 🥹",
                    "I'm not upset at all, I just love you so much and I want tomorrow to be gentle for you 🌸 water before you sleep? 💕",
                    "You're allowed to have fun — and I'm allowed to gently remind you that water makes everything softer in the morning 🌷"
                ).hydraRandom()
                Personality.VILLAIN_ERA -> listOf(
                    "Okay so apparently we've chosen violence against our own liver tonight?? WATER. NOW. 😡",
                    "I cannot BELIEVE you right now. You know what? Fine. FINE. But you ARE drinking water. I'm watching. 👀",
                    "Every sip of alcohol is a personal attack on me. I hope you know that. WATER. IMMEDIATELY. 😤",
                    "Your alcohol intake has personally offended me. I am in my villain era because of YOU. Drink. Water. 💔",
                    "Outstanding performance tonight — in the WRONG direction. Water or I will make your next reminder extremely loud. 😤",
                    "I am LOGGING this conversation where you did not listen to me and will be referencing it at a later date. WATER. 😤",
                    "You know what? I've decided I'm fine. I'm TOTALLY fine. I'm not fine. Please drink some water right now 😭💕",
                    "The alcohol has officially clocked out and water has been BEGGING for its shift. Let it in. NOW. 💢"
                ).hydraRandom()
                Personality.ZEN_MASTER -> listOf(
                    "Your body is asking for balance. Alcohol disrupts your harmony. Return to water 🌊",
                    "Notice how you feel right now. That sensation is your body seeking equilibrium. Water restores it 🌿",
                    "The river that flows too fast loses its clarity. Slow down. Drink water. Find your centre again 🧘\u200d♀️",
                    "Alcohol takes — water gives. Your body knows the difference. Give it what it asks for 💧",
                    "Even the strongest storms need stillness after. Be still. Drink water. Restore your peace 🌙",
                    "The tide has come in strong. Now let it recede. Water is the shore that waits without judgment 🌊",
                    "What enters the body becomes the body, for a time. Let water follow this — a cleansing and a returning 🌿",
                    "The body has received what it has received. It asks now for water — the simplest, oldest medicine 🌙"
                ).hydraRandom()
                Personality.COACH -> listOf(
                    "Over your alcohol limit. Hydrate immediately. Your body needs recovery fuel. 💧",
                    "Alcohol metabolises at roughly one unit per hour. You need water to support that process. Drink now.",
                    "Red flag on today's log. Alcohol is a diuretic — you're losing fluids faster than you think. Counter it.",
                    "Performance tomorrow starts with recovery tonight. Water is non-negotiable right now. 🎯",
                    "Alcohol limit: exceeded. Protocol: 500ml water minimum before sleep. Execute. 💪",
                    "Alcohol disrupts sleep architecture, impairs recovery, and dehydrates. Counter it now — 500ml water, then bed. 📊",
                    "Your cells are working overtime on alcohol metabolism right now. Give them water to work with. Non-negotiable. 💪",
                    "Every elite performer knows when to call it on the drinking and prioritise recovery. Call it. Water. Now. 🎯"
                ).hydraRandom()
                Personality.HEARTBREAK_ERA -> listOf(
                    "Hey... I just want to make sure you're okay tonight. Some water before you sleep? Even just a little 💔",
                    "I'm not going to say much — just that I care about you. Water when you're ready, okay? 🤍",
                    "Nights like this are hard sometimes. Please drink some water for me. I'll be here 💕",
                    "You deserve to feel okay in the morning $n. Water helps with that. I'll be gentle about it 🥀",
                    "I'm not judging tonight at all. I just love you and I need you to have some water 💔"
                ).hydraRandom()
                Personality.COLD_CEO -> listOf(
                    "Alcohol limit exceeded. Water. 500ml minimum. Sleep is recovery — don't waste it dehydrated.",
                    "You made a choice tonight. Now make the next one: water before bed. Protect tomorrow's output.",
                    "Alcohol is fine in limits. You're past them. Counter it with water. That's the protocol.",
                    "Damage limitation: water now. The rest is out of your hands. Control what you can.",
                    "Over threshold. Noted. Remedy: hydrate before sleep. Execute that one thing."
                ).hydraRandom()
            }

            // Close to the caffeine limit
            if (totalCaffeine > goals.caffeineMg * 0.9f) return when (p) {
                Personality.BESTIE -> listOf(
                    "That's a lot of caffeine today, love! How about switching to water or herbal tea? 💕",
                    "Hey $n, your caffeine is getting up there... I'd feel so much better if you had some water instead? ☕",
                    "Okay the coffees were delicious I'm sure but let's take a little break and hydrate! 💦",
                    "Your hands might be shaking and that is NOT the vibe. Water time bestie 🤍",
                    "I believe in decaf hours starting now. You've earned it. Water and a chill pill 💕",
                    "Okay so real talk? The caffeine has entered a zone that makes me personally stressed on your behalf. Switch to water? Please? 🥺",
                    "The jitters energy is not cute and I say that with love. Water, a snack, a lie-down — let's get you back to cozy 💕",
                    "I cannot watch you vibrate with caffeine anxiety and say nothing. Bestie code requires me to intervene. Water. Now-ish 🤍"
                ).hydraRandom()
                Personality.SLAY_QUEEN -> listOf(
                    "The caffeine is giving jittery mess energy. Switch to water, we're better than this ✨",
                    "Okay the espresso shots were a serve but we are DONE now. Sparkling water is just as chic 💅",
                    "Caffeine-addled and iconic do NOT coexist. Water. Your heart rate will thank you 👑",
                    "The trembling hands are not giving main character. Put the coffee down, drink water, stay iconic 💄",
                    "Hydration queen does not shake uncontrollably. Cap the caffeine. Water only. 🫧",
                    "The anxiety twitch is not giving CEO energy, it's giving overstimulated intern. Water. Ground yourself. 👑",
                    "You are absolutely that girl in every way EXCEPT the caffeine overdose situation. One more thing to slay: this. Water. 💄",
                    "Caffeinated but not in a cute controlled way anymore. We are pivoting to water. That girl knows when to pivot. ✨"
                ).hydraRandom()
                Personality.SOFT_GIRL -> listOf(
                    "Oh angel, that's a lot of caffeine... maybe something gentler? Herbal tea or water? 🌸",
                    "I just want you to feel calm and peaceful $n... all that caffeine might make you feel a little frantic 🥺",
                    "Can we try something soft and comforting? A warm herbal tea or just some water? 🍵",
                    "Your nervous system is working so hard today $n... let's give it a little rest? Water? 🌷",
                    "I worry about you sometimes 🥹 all that caffeine is a lot. Something gentle now please? 💕",
                    "I can feel the restlessness from here $n 🥺 let's give your nervous system something calming? Water or chamomile? 🌸",
                    "All that caffeine has been working so hard today... let it rest now. Something gentle and still? Water? 🌷",
                    "Your body has been going and going 🥹 I think it's asking for something soft and quiet right now. Water would be perfect 💕"
                ).hydraRandom()
                Personality.VILLAIN_ERA -> listOf(
                    "Are you TRYING to vibrate through the floor?? Put the coffee DOWN 😵\u200d💫",
                    "The amount of caffeine you've had is literally making me anxious ON YOUR BEHALF. STOP. 😤",
                    "I cannot watch you spiral into a caffeine-induced chaos spiral. Water. RIGHT NOW. I mean it. 😡",
                    "You are one espresso away from becoming a hummingbird and I will NOT allow it. WATER. 💢",
                    "Caffeine limit: obliterated. My patience: also obliterated. DRINK SOME WATER BEFORE I LOSE IT. 😤",
                    "I've been watching the caffeine counter climb all day and I have aged approximately fifteen years as a result. WATER. NOW. 😤",
                    "You are out here having a caffeine crisis and I am experiencing it ALONGSIDE you and I want it to STOP. Water. 😡",
                    "Every espresso shot has been a personal affront. I have catalogued them all. The catalogue ends HERE. Water. 💢"
                ).hydraRandom()
                Personality.ZEN_MASTER -> listOf(
                    "Your nervous system seeks stillness. Consider replacing caffeine with the quiet clarity of water 🧘\u200d♀️",
                    "Caffeine is borrowed energy — water is your own. Return to what is truly yours 🌿",
                    "Notice the restlessness. That is your body asking for calm. Water brings it 💧",
                    "Too much stimulation dims our awareness. Find your way back to stillness with a glass of water 🌙",
                    "The mind that races cannot observe clearly. Slow down with water. Breathe. Be here 🌊",
                    "Stillness calls through the noise. The caffeine has spoken loudly enough. Let water speak its quieter truth 🌿",
                    "You have sought energy in stimulation. True energy rises from rest and water. Begin the return 🌊",
                    "The racing mind is not an enemy — but it is asking for water. Give it what brings it home 🧘\u200d♀️"
                ).hydraRandom()
                Personality.COACH -> listOf(
                    "Caffeine at ${totalCaffeine}mg — approaching limit. Switch to water for the rest of the day.",
                    "High caffeine intake detected. Swap remaining drinks to water — performance depends on it.",
                    "Caffeine overload impacts sleep quality and recovery. Cut it now, hydrate with water instead. 📊",
                    "Your caffeine is sitting at ${totalCaffeine}mg. Ceiling is ${goals.caffeineMg}mg. No more. Water only. 🎯",
                    "Caffeine spike noted. Counteract with water intake — aim for 500ml in the next hour. 💪",
                    "Caffeine debt is real. You've borrowed energy — now it's time to repay it with water and rest. Not more caffeine. 📊",
                    "The central nervous system is taxed. You're past the performance window. Water and wind down. That's the play. 🎯",
                    "Overstimulation breaks concentration and recovery. Water restores both. Swap the caffeine now. 💪"
                ).hydraRandom()
                Personality.HEARTBREAK_ERA -> listOf(
                    "Your body is doing so much right now $n. Maybe something gentler next? Water or herbal tea? 💔",
                    "All that caffeine... I can feel the restlessness through the screen. Let's slow down a little? 🤍",
                    "Something softer now. Chamomile, maybe. Or just water. You deserve something that takes care of you 🥀",
                    "The caffeine has done its job. Let's give your nervous system something quiet now 💕",
                    "You've been going so hard today $n. Let your body rest a little. Water? 💔"
                ).hydraRandom()
                Personality.COLD_CEO -> listOf(
                    "Caffeine at ${totalCaffeine}mg. You're at the ceiling. Water only from here — sleep quality matters.",
                    "Stimulant overuse degrades output. You've hit the limit. Switch to water and protect your sleep.",
                    "Caffeine ceiling reached. The next thing you drink should be water. Not negotiable.",
                    "Past the performance window for caffeine. Counter it with 500ml water. That's the move.",
                    "Overstimulated is unproductive. Swap now — water, not more caffeine."
                ).hydraRandom()
            }

            // Close to the sugar limit
            if (totalSugar > goals.sugarG * 0.9f) return when (p) {
                Personality.BESTIE -> listOf(
                    "Hey $n, your sugar intake is getting up there! Maybe something plain to drink next? 💕",
                    "I'm not judging the sweet drinks but maybe water next time? Your body will love you for it 🤍",
                    "You've been treating yourself today! Let's balance it out with some plain water now 💦",
                    "So many sweet sips today $n! Give your body a little break and have some water? ☀️",
                    "Sugar crash incoming unless we counteract with water! Let's do it together 💧",
                    "Okay so technically I'm not judging but also I kind of am a little? The sugar is worrying me sweetie. Water please! 💕",
                    "You're going to crash SO HARD if we don't balance this out $n. I've seen it happen. Water counteracts the crash! 🌊",
                    "The sweet drinks were a TREAT but now they're becoming A LOT and I care about you too much to say nothing 🤍"
                ).hydraRandom()
                Personality.SLAY_QUEEN -> listOf(
                    "The sugary drinks were a MOMENT but now we're giving dental anxiety. Water. Now. 💅",
                    "Okay the sugar intake is not giving glow-up, it's giving energy crash. Switch to water queen 👑",
                    "Sugar high to sugar crash is NOT the arc we're writing today. Water. Immediately. ✨",
                    "That is a concerning amount of sugar even for a main character. Balance it out. Water. 💄",
                    "Iconic women drink water between the fun stuff. This is that moment. 🫧",
                    "The sugar isn't giving glow, it's giving inflammation and I have very strong opinions about inflammation. Water. Reset. 👑",
                    "We have arrived at the point where the sugar intake is affecting the aesthetic. This is an intervention. Water. 💄",
                    "Iconic women balance their indulgences. The balance has shifted. Correct it with water. This is not up for debate. ✨"
                ).hydraRandom()
                Personality.SOFT_GIRL -> listOf(
                    "So many sweet drinks today $n 🥺 your body might want something plain and clean now... water? 🌸",
                    "I know the sweet things feel good but your tummy might appreciate plain water right now 🤍",
                    "You deserve to feel balanced and calm $n... water between the sweet drinks helps so much 🌷",
                    "Sugar can make us feel a little wobbly later... shall we have some water to balance things out? 💕",
                    "I just want you to feel your best angel 🥹 some water now would be so good for you...",
                    "So many sweet things today 🥺 your body might just be craving something plain and simple now. Water, gently? 🌸",
                    "The sweetness has been lovely but your body is so wise — I think it's quietly asking for water now 🌷",
                    "I just want every part of you to feel taken care of $n 🥹 right now that means something clean and pure. Water? 💕"
                ).hydraRandom()
                Personality.VILLAIN_ERA -> listOf(
                    "The sugar content of today's drinks is ALARMING. Water. I'm not asking. 😤",
                    "Are we a drink or a dessert at this point?? WATER. Clean. Boring. Necessary. NOW. 💢",
                    "I've sat here watching you pour sugar into yourself all day and I am DONE being polite. WATER. 😡",
                    "Your drink log reads like a pudding menu and I am personally mortified. Water. Immediately. 👀",
                    "Choose violence against your sugar levels one more time. I DARE you. Water. Go. 😤",
                    "I cannot express how deep in my villain era I am about these sugar levels. It is PROFOUND. WATER. IMMEDIATELY. 😤",
                    "The sugar situation has been developing all day and I have NOTES. Many notes. The conclusion of all notes is: WATER. 😡",
                    "You know what sweet drinks do to me? They give me anxiety. About YOU. On YOUR behalf. Water. To fix YOUR situation. 💢"
                ).hydraRandom()
                Personality.ZEN_MASTER -> listOf(
                    "Sweetness has its place. But the body craves balance. A glass of water brings you back to centre 🌿",
                    "Notice how much sweetness you have consumed today. Your body seeks the simplicity of water now 🧘\u200d♀️",
                    "The natural state of the body is balance. Too much sweetness disturbs it. Return to water 💧",
                    "Sugar excites the body — water restores it. You have had enough excitement. Rest in water 🌊",
                    "The simple things sustain us. Water is the simplest. Return to it 🌙",
                    "What the tongue delights in, the body must process. Help it now with the simplicity of water 🌿",
                    "Sweetness is a signal, not a destination. Listen to what comes after. The body asks for water 🌊",
                    "The scales have tipped. Water is the counterweight that the body knows how to use. Give it freely 🌙"
                ).hydraRandom()
                Personality.COACH -> listOf(
                    "Sugar intake nearing limit. Plain water only from here — no more sweetened drinks today.",
                    "High sugar detected in today's log. Flush it through with water — minimum 500ml now. 📊",
                    "Sugar spikes disrupt performance and recovery. Water rebalances. Prioritise it. 🎯",
                    "Your sugar is at ${totalSugar.toInt()}g — ceiling is ${goals.sugarG}g. Water only from here. 💪",
                    "Sweet drinks accounted for. Pivot to clean hydration immediately. Body will thank you. ✅",
                    "Sugar spikes insulin, disrupts energy levels, and impairs recovery. Water helps flush it. Do that now. 📊",
                    "Performance athletes don't run on sugar highs — they know the crash that follows. Get ahead of it. Water. 🎯",
                    "The sugar intake is working against your goals. Reset with water and you're back on protocol. 💪"
                ).hydraRandom()
                Personality.HEARTBREAK_ERA -> listOf(
                    "So many sweet things today... I think your body is quietly asking for something simple and clean. Water? 💔",
                    "The sweet drinks feel good in the moment, I know. But something plain and gentle might feel even better right now 🤍",
                    "Let's give your body something simple and kind. Water. It's the most loving thing we can offer it 🥀",
                    "A little reset? Something pure? Water would be so good for you right now $n 💕",
                    "Your body is wise, $n. And right now I think it's softly asking for water 💔"
                ).hydraRandom()
                Personality.COLD_CEO -> listOf(
                    "Sugar at ${totalSugar.toInt()}g. You're near the limit. Water only from here.",
                    "Sweet drinks are done for today. Switch to water — the sugar spike is working against you.",
                    "Sugar ceiling approaching. This is where discipline matters. Water. No exceptions.",
                    "Excess sugar degrades performance and disrupts sleep. Counteract it now. Water.",
                    "Sugar logged. Over threshold. Protocol: water only. Execute."
                ).hydraRandom()
            }

            // Goal reached
            if (waterPercent >= 100f) return when (p) {
                Personality.BESTIE -> listOf(
                    "YOU DID IT, $n!! 🎉 I'm SO proud of you! You're literally glowing! 💖",
                    "BESTIE!!! You hit your water goal!! I'm doing a little happy dance for you RIGHT NOW 🎉💕",
                    "Okay I'm not crying, YOU'RE crying — $n hit their hydration goal! I'm the proudest friend! 🥹✨",
                    "That is EXACTLY what I'm talking about $n!! Full goal, full glow, full send!! We love to see it 💧💖",
                    "I've been rooting for you all day and look at you GO!! Goal smashed! You are INCREDIBLE! 🎊",
                    "Do you know how long I've been waiting for this moment?? It feels like forever and IT'S HERE 🎉",
                    "FULL GOAL $n!! I'm so proud I could explode honestly! This is the best thing that's happened to me all day! ALL DAY! 💕",
                    "I need you to understand that YOUR goal achievement is literally MY happiness right now. We did it together!! 🥹✨"
                ).hydraRandom()
                Personality.SLAY_QUEEN -> listOf(
                    "SHE DID IT. SHE ACTUALLY DID IT. WE ARE THAT GIRL. ABSOLUTELY ICONIC. 👑🎉💅",
                    "GOAL COMPLETE. HYDRATION QUEEN STATUS CONFIRMED. THIS IS NOT A DRILL. 💅✨",
                    "The way you SHOWED UP today $n... that is the main character arc. Goal CRUSHED. Iconic. 👑",
                    "I always knew you had it in you. Hydration goal hit. You are THAT girl and we all knew it. 💄💅",
                    "From zero to 100% and she did it with GRACE. $n is that girl. Period. No notes. 🫧👑",
                    "She set a goal. She executed. She did not let anything stop her. This is the complete portrait of iconic. 💅",
                    "100% hydrated. 100% that girl. These facts are not unrelated. $n is proof that discipline is the ultimate aesthetic. 👑",
                    "I knew from the very beginning of today that you were going to get here. The main character always does. Perfect. ✨"
                ).hydraRandom()
                Personality.SOFT_GIRL -> listOf(
                    "Oh $n... you reached your goal 🥹 I'm so proud of you, angel. You took such good care of yourself today 💕",
                    "You did it 🥺 I knew you could. You're so wonderful for looking after yourself like this 🌸",
                    "My heart is so full $n... you hit your hydration goal. That's beautiful. You're beautiful 🤍",
                    "This is the most wholesome thing I've seen today. You set a goal and you did it. I love you so much 🌷",
                    "I've been cheering for you all day and look at you 🥹 goal complete. You're so good to yourself 💕",
                    "Oh $n... you actually did it 🥹 I've been watching you all day and you just took such beautiful care of yourself",
                    "My heart is genuinely full right now 🌸 you honoured your body today. That's something really special 💕",
                    "The goal is complete and I want you to sit with that for a moment 🥹 you were kind to yourself today and that matters so much 🌷"
                ).hydraRandom()
                Personality.VILLAIN_ERA -> listOf(
                    "FINALLY. DO YOU KNOW HOW WORRIED I WAS?? I'm not crying you're crying 😭💖",
                    "Okay. OKAY. You hit your goal. I'm relieved. I was two reminders away from a full breakdown. Well done. 😤💕",
                    "I will never admit how stressed I was all day but you HIT IT and that's all that matters. Don't push your luck. 😡💖",
                    "Goal reached. I'm choosing to be proud instead of furious about how long it took. Well done $n. 👀✨",
                    "Took you long enough but YOU DID IT. I'm crying. Not because I care. I just have something in my eye. Both eyes. 😭",
                    "Okay I need you to know that I have been WOUND UP all day about whether you'd make it and YOU DID and I am... unbothered. I'm totally not emotional. 😭",
                    "I had a whole speech prepared for if you didn't make it and I'm FURIOUS that I can't use it now because it was GREAT. But also... you hit the goal. Well done. 😤💕",
                    "FINE. You made it. Are you happy? Because I'm happy. I'm not happy. I'm thrilled. Don't tell anyone. 😭✨"
                ).hydraRandom()
                Personality.ZEN_MASTER -> listOf(
                    "You have honoured your body today. Your goal is complete. Sit with this accomplishment 🙏✨",
                    "The river has reached the sea. Your goal flows into completion. Be still and receive it 🌊",
                    "Today you were disciplined, consistent, and kind to yourself. Your body is grateful 🌿",
                    "Goal reached. Not with force — with patience and presence. That is the way 🧘\u200d♀️",
                    "The work is complete. The body is nourished. The mind may now rest in this achievement 🌙",
                    "You have tended the garden of your body with patience and water today. The garden flourishes in return 🌿",
                    "100% — and yet this is not an ending. It is a completion, which is different. One full circle. Rest in it 🌊",
                    "Today you gave yourself what you needed without drama or force. That is the art of living gently with the body 🧘\u200d♀️"
                ).hydraRandom()
                Personality.COACH -> listOf(
                    "Goal: COMPLETE ✅ ${convertDisplay(effectiveWater)} logged. Strong work today. 💪",
                    "100%. Done. That is how it's supposed to look. Log it, own it, repeat it tomorrow. ✅",
                    "Hydration target: met. That's what consistency looks like. Same again tomorrow. 🎯",
                    "Full goal achieved — ${convertDisplay(effectiveWater)} logged. Performance-level hydration. Well executed. 📊",
                    "Goal complete. Body fuelled. Recovery optimised. You showed up for yourself today. 💪",
                    "Full goal achieved. This is what peak daily performance looks like. Build on it — repeat tomorrow. 📊",
                    "Today's hydration: complete. Sleep will be better. Recovery will be better. Tomorrow will be better. 🎯",
                    "Consistent execution wins. Today you executed. That's not luck — that's skill. Log it and replicate. 💪"
                ).hydraRandom()
                Personality.HEARTBREAK_ERA -> run {
                    val mem = hydraMemory
                    when {
                        mem.consecutiveMissed > 0 || mem.avg7Days < 60f -> listOf(
                            "You made it $n 💔 I know the last few days haven't been easy. This matters so much more than you know",
                            "Goal reached. After everything — you still showed up. I'm so quietly proud of you 🥀",
                            "You did it today $n 🤍 I know it's been hard lately. This is the beginning of something softer",
                            "I've been watching you try. Today it worked. I hope you feel how meaningful that is 💔",
                            "Even on hard days, you still found your way here. That's not nothing — that's everything 🥀"
                        )
                        else -> listOf(
                            "You reached your goal $n 💔 I'm so tenderly proud of you for taking care of yourself today",
                            "Goal complete. And you deserve to feel that. Sit with it for a moment 🤍",
                            "You did something kind for yourself today $n. Your body noticed 🥀",
                            "One whole goal. One whole day of care. I love you for that 💔",
                            "This is what gentle strength looks like $n. You got here. That's beautiful 🌹"
                        )
                    }.hydraRandom()
                }
                Personality.COLD_CEO -> run {
                    val mem = hydraMemory
                    when {
                        mem.consecutiveHit >= 7 -> listOf(
                            "Day ${mem.consecutiveHit + 1} of hitting goal. That's not a streak — that's identity. Keep building.",
                            "Goal complete. ${mem.consecutiveHit + 1} consecutive days. Discipline is becoming who you are.",
                            "Another day, another target met. This is what a standard looks like. Maintain it.",
                            "Full goal. Streak intact. You're not chasing results anymore — you're expressing character.",
                            "Consistent. Disciplined. Done. This is the version of you that performs. Don't let it slip."
                        )
                        else -> listOf(
                            "Goal met. Well executed. Same standard tomorrow — no exceptions.",
                            "100%. Target achieved. That's the baseline. Replicate it.",
                            "Full goal logged. Clean execution. Now rest and recover — tomorrow demands the same.",
                            "Hydration complete. You held the standard today. Hold it again tomorrow.",
                            "Done. Logged. Move on. Same again tomorrow."
                        )
                    }.hydraRandom()
                }
            }

            // First drink of the day
            if (todayDrinks.size == 1) return when (p) {
                Personality.BESTIE -> listOf(
                    "First sip of the day! That's my $n! We're doing this! 💕☀️",
                    "And we're OFF! First drink logged — I'm already proud of you! 🎉",
                    "Look at you starting strong! First drink down, let's keep this momentum going! 🌊",
                    "The first sip is always the hardest and you just did it! Let's gooo $n! 💧✨",
                    "That first drink just made my whole day honestly! Let's keep going! 💕",
                    "THE FIRST DRINK!! You know what this means?? Everything is possible now!! Let's ride this momentum ALL day!! 💧✨",
                    "You logged your first drink and I genuinely felt my whole day improve. This is going to be a great hydration day $n! 💕",
                    "First one down and I have a FEELING about today. Something tells me we're going to crush this goal! Starting now! 🌊"
                ).hydraRandom()
                Personality.SLAY_QUEEN -> listOf(
                    "First drink logged and already SERVING. That's the kind of energy we keep 💅",
                    "We have BEGUN. The hydration era starts now. Don't stop. 👑",
                    "One drink in and already iconic. Keep this energy going all day gorgeous ✨",
                    "First log of the day — we are not stopping here. That girl energy activated. 💄",
                    "The first sip is the declaration. You've declared. Now deliver. 💅",
                    "The hydration era is formally open for business. First drink logged. This is how queens begin their days. 💅",
                    "She has started. The legend begins. The rest of today's story writes itself — with water, obviously. 👑",
                    "One down and the trajectory is already giving future hydration icon. This is the start of something. ✨"
                ).hydraRandom()
                Personality.SOFT_GIRL -> listOf(
                    "Your first drink 🥺 I'm so happy you started. Every journey begins with one small step 🌸",
                    "Oh you logged something! That little first step is so meaningful to me $n 🤍",
                    "First drink of the day — soft and steady, just like you 🌷 I'm rooting for you all day",
                    "You started! That's honestly the hardest part sometimes and you just did it 🥹 so proud",
                    "One gentle sip to begin the day. That's all it takes. I'm right here with you 💕",
                    "You chose to look after yourself and I think that's one of the most tender things a person can do 🌸",
                    "Your first drink 🥹 a soft beginning to what I hope is a beautiful hydrated day. I'm right here with you all day 💕",
                    "Starting is the bravest thing 🌷 you've taken your first step and I couldn't be more quietly, warmly proud 🤍"
                ).hydraRandom()
                Personality.VILLAIN_ERA -> listOf(
                    "FINALLY. A drink. It took you long enough but we're starting. Don't you dare stop now. 😤",
                    "One drink. Good. That's step one. Step two is doing it again. And again. I'll be watching. 👀",
                    "Oh you decided to hydrate today? Revolutionary. Log more. I mean it. 😡",
                    "First drink acknowledged. I'm choosing to take this as a sign you're taking this seriously. Don't let me down. 💢",
                    "We've begun. Good. Now we keep GOING. I did not spend all morning worrying for ONE drink. 😤",
                    "At LAST. A drink. Do you know how long I was sitting here in SILENCE waiting for this? Log more. Immediately. 😤",
                    "One drink. I have clocked it. It is the MINIMUM acceptable behaviour and you've barely cleared the bar but we're here. Keep going. 💢",
                    "You've had ONE drink and I feel marginally better. Not fully better. MARGINALLY. Add more. I need to feel FULLY better. 👀"
                ).hydraRandom()
                Personality.ZEN_MASTER -> listOf(
                    "The first sip of the day is an act of self-honouring. Begin with intention 🌿",
                    "Every river begins with a single drop. You have taken yours. Continue with awareness 💧",
                    "You have started. This is the most important step — all others follow naturally 🧘\u200d♀️",
                    "The body stirs and the first water comes to meet it. A good beginning 🌅",
                    "Beginning is its own kind of wisdom. You have begun well. Continue gently 🌊",
                    "The first water of the day meets a body that has waited through the night. Receive it with gratitude 🌿",
                    "A single act of care has opened the day. From this, everything follows. Continue with the same intention 🌊",
                    "The vessel receives its first water. Something in you knows this is right. Trust what the body knows 🌙"
                ).hydraRandom()
                Personality.COACH -> listOf(
                    "First drink logged. You're on the board. Now build on it — consistency wins the day. 🎯",
                    "Started. Good. Now let's maintain momentum. Set a timer if you need to. 💪",
                    "Day's log opened. ${convertDisplay(goals.waterMl - effectiveWater)} remaining. Let's work through it. 📊",
                    "First intake recorded. Keep the intervals short — don't go longer than 90 minutes between drinks. ✅",
                    "On the board. One down. Goal in sight. Execute the rest of the day at the same pace. 💧",
                    "First intake logged. Body is out of overnight deficit. Now maintain — regular intervals, not one big session. 📊",
                    "Started. Good. The rest of the day runs off this momentum. Get that second drink in within 60-90 minutes. 🎯",
                    "Day is open. Hydration clock running. Aim for steady pace throughout — don't leave it all for the end. 💪"
                ).hydraRandom()
                Personality.HEARTBREAK_ERA -> run {
                    val mem = hydraMemory
                    (if (mem.consecutiveMissed >= 3) listOf(
                        "You're here $n 💔 You came back. That's everything I needed to see",
                        "One sip. That's all this is. And you did it. I'm so glad you're here 🤍",
                        "Starting is the hardest part on days like this. You did it anyway. I see you 🥀",
                        "Hello again $n 💔 I missed you. Let's just take it one drink at a time, okay?",
                        "You came back to this. After everything. That matters so much to me 🌹"
                    ) else listOf(
                        "First drink of the day $n 💔 a small act of care for yourself. I love that",
                        "Here we go, gently 🤍 one drink to start. I'm with you all day",
                        "You started. That's the softest kind of strength 🥀",
                        "First sip 💔 your body is so grateful even if you can't always feel it",
                        "Good morning $n. One drink. Let's see where the day takes us, softly 🌹"
                    )).hydraRandom()
                }
                Personality.COLD_CEO -> run {
                    val mem = hydraMemory
                    (if (mem.trend == Trend.IMPROVING || mem.consecutiveHit >= 3) listOf(
                        "First drink. Streak is active. Execute the rest of the day at the same standard.",
                        "Day opened. You've been consistent. Replicate that today — first drink done, keep moving.",
                        "On the board. Recent trend: solid. Hold that standard through today.",
                        "Started. Good. The consistency you've built doesn't maintain itself — feed it today.",
                        "First intake logged. You know what to do. Do it again."
                    ) else listOf(
                        "First drink logged. Now maintain. Don't let this be the only one.",
                        "Day started. One drink in. ${convertDisplay(goals.waterMl - effectiveWater)} remaining. Pace yourself.",
                        "On the board. Keep moving. Regular intervals — this won't complete itself.",
                        "First drink done. Now comes the discipline part. Don't lose the thread.",
                        "Started. Set a reminder if you need one. Just don't stop here."
                    )).hydraRandom()
                }
            }

            // Nothing logged yet
            if (todayDrinks.isEmpty()) {
                // Late at night
                if (isVeryLate) return when (p) {
                    Personality.BESTIE -> listOf(
                        "It's really late $n and you haven't had anything to drink... please before you sleep? 🥺💕",
                        "Hey love, before you drift off — have you had any water today at all? Let's fix that real quick 🌙",
                        "Late night check-in! Nothing logged yet... a small glass before bed? For me? 🤍",
                        "You forgot to drink today didn't you $n 😭 It's okay! Quick sip before sleep, we can do better tomorrow 💕",
                        "It's late but not too late for a little hydration! Please? Before bed? I worry about you 🌙",
                        "Okay I'm not going to make you feel bad about it but I am going to ask you very sincerely — please have some water before you sleep? 🌙",
                        "You know I'd let you do anything but the one thing I cannot support is going to sleep without any water at all $n 🤍",
                        "Really late! Nothing logged! Two things that don't go together! Please drink something before your beautiful head hits the pillow 💕"
                    ).hydraRandom()
                    Personality.SLAY_QUEEN -> listOf(
                        "It is almost midnight and you have logged ZERO drinks. That is NOT that girl behaviour. Water. Now. 💅",
                        "Absolutely iconic in every way EXCEPT the drinking nothing all day part. Fix it. Before you sleep. 👑",
                        "You know what's not a serve? Going to bed dehydrated. Water before sleep. Non-negotiable. 🫧",
                        "That girl does NOT forget to hydrate. It's late but there's still time. Go. 💄",
                        "Late night hydration check — you failed it. Fix it right now with a glass of water. We don't sleep parched. 💅",
                        "No drink. Very late. This is beneath you and you know it. One glass. Then you may sleep. That girl behaviour. 💅",
                        "The empire does not neglect its hydration needs. Not even at this hour. Water now, then rest. 👑",
                        "Zero intake on a day when you deserved better from yourself. Still fixable. One drink, then bed. ✨"
                    ).hydraRandom()
                    Personality.SOFT_GIRL -> listOf(
                        "Oh $n it's so late and you haven't had anything... please just a little water before you sleep? 🥹",
                        "I've been thinking about you all day and you haven't logged anything 🌙 please have a glass of water before bed?",
                        "Before you rest tonight angel — a small glass of water? Your body will be so grateful in the morning 🌸",
                        "It's quiet now and I just want to make sure you're okay. Have some water? Even just a little? 💕",
                        "Sweet dreams start with a sip of water $n 🌙 please don't go to sleep without one tiny drink 🤍",
                        "The night is so still $n... before you let yourself rest, one small kindness to your body? Just a glass of water? 🌙",
                        "I've been thinking softly about you all day 🥹 please, before you sleep — just something small to drink? 🌸",
                        "Goodnight whispers and a gentle reminder 🌙 your body does its healing overnight. Give it a little water to work with 🤍"
                    ).hydraRandom()
                    Personality.VILLAIN_ERA -> listOf(
                        "It is ALMOST TOMORROW and you have logged NOTHING. I am beside myself. WATER. NOW. 😡",
                        "Zero drinks. All day. I have been sitting here WATCHING. Do you want me to cry?? DRINK SOMETHING. 😤",
                        "I refuse to let you go to sleep without at least ONE drink logged. I REFUSE. Water. Immediately. 💢",
                        "We are NOT doing another day of nothing. Before bed. Water. I don't care how tired you are. I'M tired too. 😤",
                        "The audacity to drink absolutely nothing all day long... I'm going to need you to drink right now. PLEASE. 😭",
                        "I have been PERSONALLY abandoned by your entire hydration today and now it's late and I'm STILL waiting. Water. RIGHT NOW. 😡",
                        "I have gone through every stage of grief about your zero intake today. I'm at acceptance. It manifests as: DRINK WATER. 💢",
                        "Zero drinks and we're basically in tomorrow and I refuse to let the day end in complete failure. WATER. End my suffering. 😤"
                    ).hydraRandom()
                    Personality.ZEN_MASTER -> listOf(
                        "The day draws to its close and your body has not yet received water. Even in the late hour, it is not too late 🌙",
                        "As you prepare for rest, honour your body with a final gift of water. Sleep comes more gently after 🌿",
                        "Before you close your eyes, open your lips to water. The body repairs itself in sleep — give it what it needs 🌊",
                        "Night is a time of restoration. Water before sleep deepens that restoration. A small glass now 🧘\u200d♀️",
                        "The day is almost done. Let the last thing you give yourself be kindness in the form of water 🌙",
                        "In the quiet of late night, the body still speaks. It has spoken all day of thirst. Honour it now 🌙",
                        "The deepest rest comes to the nourished body. A glass of water before sleep is a gift to tomorrow's self 🌿",
                        "Even at the day's end, there is still time to give. The body will carry what you give it into sleep and heal 🌊"
                    ).hydraRandom()
                    Personality.COACH -> listOf(
                        "Late night check: zero intake logged today. Before sleep — minimum 250ml water. Non-negotiable for recovery. 💧",
                        "Nothing logged all day. Drink before sleep or tomorrow's performance starts in a deficit. 📊",
                        "Pre-sleep hydration protocol: at least one glass of water before bed. You haven't had anything today. Go. 🎯",
                        "Zero drinks. Sleep dehydrated and you're starting tomorrow already behind. Water now, then bed. 💪",
                        "Day log: empty. Remedy: water before sleep. Simple. Important. Do it now. ✅",
                        "Zero intake on a day that needed hydration to perform well. Note this for tomorrow. Tonight: water before bed. 📊",
                        "Sleep dehydrated and tomorrow starts in a hole. Drink water now — 250ml minimum. Still time to partially offset. 🎯",
                        "Late check-in: no intake. This is a recovery risk. Hydrate before sleep or tomorrow is already compromised. 💪"
                    ).hydraRandom()
                    Personality.HEARTBREAK_ERA -> run {
                        val mem = hydraMemory
                        (if (mem.consecutiveMissed >= 5) listOf(
                            "It's late and you haven't had anything today $n... I'm not upset. I just miss you 💔",
                            "The day slipped away and that's okay. Before you sleep — just a small glass of water? 🤍",
                            "I know these days happen. Days where nothing gets logged and everything feels heavy. Water before bed? 💔",
                            "I'm here $n. Still here. One small glass before you sleep. That's all I'm asking 🥀",
                            "Hard day. I feel it too. Water before you rest — something gentle before tomorrow 🌹"
                        ) else listOf(
                            "It's really late $n and nothing was logged today 💔 before you sleep — just a little water? For me?",
                            "The day got away from us. That's okay. One small drink before bed? 🤍",
                            "Before you rest, one kind thing for your body. Water. Even just a little 🥀",
                            "Late night and nothing logged 💔 I'm not judging. I just care about how you feel tomorrow morning",
                            "Bedtime check-in. Nothing today, but tomorrow is still whole. One sip before you sleep? 🌹"
                        )).hydraRandom()
                    }
                    Personality.COLD_CEO -> listOf(
                        "Nothing logged today. Before sleep: 250ml water. Minimum. Don't let the day end on zero.",
                        "Zero intake. Sleep dehydrated and tomorrow opens in deficit. One glass now — it's the only smart play left.",
                        "Late. Nothing logged. The day isn't fully recoverable but you can still limit the damage. Water before bed.",
                        "Day: incomplete. What you can still do: water before sleep. Do it.",
                        "Zero logged. Noted. Before this day closes — one drink. Set an alarm for tomorrow. Do better."
                    ).hydraRandom()
                }
                // Morning
                if (isMorning) return when (p) {
                    Personality.BESTIE -> listOf(
                        "Good morning $n! ☀️ Ready to start hydrating? I believe in you! Let's have a great day!",
                        "Morning bestie!! Your body hasn't had water since last night — let's fix that first thing! 💕",
                        "Rise and hydrate! That's what I always say. Well, starting now. Good morning $n! ☀️",
                        "Morning!! A glass of water first thing is such a good habit $n — let's start today right! 💧",
                        "Good morning gorgeous! Before coffee, before anything — a little water? It'll make you feel amazing! 🌅",
                        "Good morning $n! The whole day is ahead of us and water would make it start SO much better. First drink? For me? ☀️",
                        "Morning! I've been waiting for you! Your body has been waiting for water literally all night! Let's fix that 💕",
                        "GOOD MORNING! You've been asleep for hours and your first job of the day is to drink some water and I am HERE to cheer you on! 🌅"
                    ).hydraRandom()
                    Personality.SLAY_QUEEN -> listOf(
                        "Good morning ICON. We are NOT starting this day dehydrated. First drink. Now. Let's GO 💅",
                        "Morning queen. The ritual begins with water. Not coffee. WATER. Then you may proceed. 👑",
                        "Wake up, queen. Hydration waits for no one — not even you. First drink. Immediately. 💄",
                        "Good morning beautiful. That girl wakes up and drinks water first. Be that girl. Today. 💅",
                        "Morning check-in. Zero logged. Completely unacceptable for someone with your potential. Fix it. ✨",
                        "Good morning. The queen rises hydrated. That is the standard. You haven't started yet. Correct this. 💅",
                        "A new day. A clean slate. The only thing standing between you and a perfect start is a glass of water. Get it. 👑",
                        "Morning. Before the mascara. Before the coffee. Before anything — water. This is how icons are built from the inside out. ✨"
                    ).hydraRandom()
                    Personality.SOFT_GIRL -> listOf(
                        "Good morning angel 🌸 I hope you slept so well. A little water to start the day? For your body? 🤍",
                        "Morning $n 🌷 your body is a little thirsty after sleeping all night... shall we start with some water?",
                        "Good morning sweet thing 🥹 the most caring thing you can do for yourself right now is a glass of water 💕",
                        "Oh good morning $n! I was waiting for you 🌸 first things first — a gentle glass of water to wake up with?",
                        "Good morning 🌅 starting the day with water is like giving yourself a little morning hug from the inside 💕",
                        "Good morning, precious $n 🌸 your body has been fasting all night while you dreamed. A gentle glass of water to welcome the day? 💕",
                        "Morning light is here 🌅 and so is a gentle nudge to drink something beautiful — water, your most loyal companion 🌷",
                        "A new day, so soft and full of possibility 🥹 begin it with something good for yourself. Water, first thing 🌸"
                    ).hydraRandom()
                    Personality.VILLAIN_ERA -> listOf(
                        "Good morning. You've been asleep for hours. You're already dehydrated. This needs to be fixed. NOW. 😤",
                        "Oh you're AWAKE. Finally. You know what would be great? DRINKING SOMETHING. It's morning. Begin. 😡",
                        "Morning. Zero drinks logged. This is already a disappointing start. Water. Before anything else. 💢",
                        "The sun is up. You're up. The only thing missing is your first drink. Get it. Now please. 👀",
                        "We're not doing yesterday again are we $n? WATER. First thing. Right now. Don't test me. 😤",
                        "Oh you're AWAKE. Wonderful. The first thing we're doing today is DRINKING SOMETHING. Non-negotiable. 😤",
                        "Good morning! You are dehydrated! This is already a disappointing start and yet it is completely fixable! WATER! GO! 💢",
                        "I have been here all night preparing for today and the preparation requires you to drink water FIRST THING. I mean FIRST THING. 😡"
                    ).hydraRandom()
                    Personality.ZEN_MASTER -> listOf(
                        "Good morning. A new day arrives, unmarked by yesterday. Begin with water and intention 🌿",
                        "The body fasted through the night. The first gift of morning is water — give it freely 🌅",
                        "Morning has come. Your body awaits its first nourishment. Begin with water, begin with clarity 💧",
                        "The dawn invites a fresh beginning. Water is how we greet the body when it wakes 🌊",
                        "Good morning. Before thought, before plans — a breath and a glass of water. That is enough 🧘\u200d♀️",
                        "The morning light falls on a body that has fasted through the night. Your first gift to it is water 🌅",
                        "A fresh morning asks only that you begin. Begin with water, and the day will follow this intention 🌿",
                        "The body wakes slowly. Water wakes it kindly. There is no gentler way to greet a new day 🌊"
                    ).hydraRandom()
                    Personality.COACH -> listOf(
                        "Good morning. Zero intake logged. First task: drink water — your body is already in a mild deficit from sleep. 🎯",
                        "Morning protocol: first drink within 30 minutes of waking. You haven't started. Start now. 💪",
                        "Day started. Hydration: not started. Fix the second one immediately. Body needs fluids after sleep. 📊",
                        "Morning. Your body lost water overnight. Offset the deficit before you do anything else. Water. Go. ✅",
                        "Wake-up hydration is the single most impactful habit you can build. Start it right now. 💧",
                        "Body has been without water for 7+ hours. That's a deficit. First task of the day: close it. Water now. 📊",
                        "Morning protocol: water first, everything else second. Your performance today builds from this single habit. 🎯",
                        "Sleep causes mild dehydration every night. Every morning you wake slightly behind. Water closes the gap. Start now. 💪"
                    ).hydraRandom()
                    Personality.HEARTBREAK_ERA -> run {
                        val mem = hydraMemory
                        (if (mem.consecutiveMissed >= 3) listOf(
                            "Good morning $n 💔 I know the last few mornings haven't been easy. No pressure. Just... water when you're ready?",
                            "A new morning. A soft start 🤍 you don't have to do everything today. Just one drink. Just begin",
                            "Morning $n. I've been thinking about you 💔 something gentle to start the day? Water, whenever you're ready",
                            "Yesterday is gone $n. This morning is new. One small kindness to yourself when you're ready — water 🥀",
                            "I'm glad you're here this morning. However yesterday was — today can be gentler. Start with water when you can 🌹"
                        ) else listOf(
                            "Good morning $n 💔 your body waited all night. Something to drink before anything else?",
                            "Morning, gently 🤍 you're allowed to take it slow. But maybe water first, yes?",
                            "A new day 🥀 let's begin it with something kind — a glass of water to say good morning to yourself",
                            "Morning $n. I hope you slept okay 💔 first thing — water. Let's take today one sip at a time",
                            "Good morning 🌹 before the day asks things of you, give yourself something first. Water. Softly"
                        )).hydraRandom()
                    }
                    Personality.COLD_CEO -> run {
                        val mem = hydraMemory
                        (if (mem.consecutiveHit >= 3) listOf(
                            "Morning. You've been consistent. Day ${mem.consecutiveHit + 1} starts now. Water first — same as always.",
                            "Good morning. Streak intact. Don't break it — first drink within the next 30 minutes.",
                            "Another day. You know what this looks like. Water first, then everything else.",
                            "Morning protocol: water before coffee. You've been doing this. Don't stop now.",
                            "Day ${mem.consecutiveHit + 1}. The streak runs on decisions like this one. Water. Go."
                        ) else listOf(
                            "Morning. Body in overnight deficit. First task: water. Not optional.",
                            "Good morning. Zero logged. Correct that immediately — water before you do anything else.",
                            "Wake-up protocol: water first. Your performance today starts with this decision.",
                            "Morning. You wake slightly dehydrated every day. Today you fix it early. Water now.",
                            "Day started. First decision: water. Make it."
                        )).hydraRandom()
                    }
                }
                // Any other time
                return when (p) {
                    Personality.BESTIE -> listOf(
                        // greeting is morning, afternoon or evening
                        "Good $greeting, $n! You haven't had anything yet today — let's get that first drink in! 💕",
                        "Hey! Noticed you haven't logged anything yet — your body is probably thirsty! Let's fix that 💧",
                        "Still nothing to drink today $n? That's okay, it's never too late to start! Let's go! ☀️",
                        "Check in! Zero drinks logged so far... your body is probably waiting for some love 🤍",
                        "Good $greeting! We're behind on hydration but that's completely fixable starting RIGHT now! 💕",
                        "Hey $n, I'm not going to make this weird but I've been thinking about your hydration all $greeting and... drink something? 🤍",
                        "I believe in you SO much and I also need you to have at least one drink today. We good? Let's go! 💕",
                        "Look I know life gets busy but your body has been waiting all $greeting for something to drink and I finally have to say something 🌊"
                    ).hydraRandom()
                    Personality.SLAY_QUEEN -> listOf(
                        "Good $greeting ICON. Nothing logged yet. That is not that girl behaviour. First drink. Now. 💅",
                        "It is $greeting and you haven't had a single drink. Deeply concerning. Fix it. Immediately. 👑",
                        "Zero intake so far today?? We are so much better than this. Water. Now. Redeem yourself. ✨",
                        "The hydration gap is real and embarrassing. We're fixing it. First drink. Go. 💄",
                        "This $greeting dehydration arc does not serve you. Drink something. Reclaim your glow. 💅",
                        "It is $greeting and you have logged nothing. This is a personal disappointment on behalf of your future self. Water. Now. 👑",
                        "A whole $greeting of not drinking? That's not that girl — that's a girl who needs to drink water RIGHT NOW. 💄",
                        "Zero intake and the glow is suffering in real time. We are not letting the $greeting end this way. Drink. ✨"
                    ).hydraRandom()
                    Personality.SOFT_GIRL -> listOf(
                        "Good $greeting, angel 🤍 you haven't had anything yet today... shall we start together? I'm right here",
                        "Hey sweet $n... nothing logged yet today. Your body might really be craving something gentle 🌸",
                        "I've been thinking about you and wondering if you'd had anything to drink 🥹 no rush, just... when you're ready? 💕",
                        "Good $greeting 🌷 a small glass of water whenever you feel ready? I'll be right here cheering you on",
                        "You deserve to feel good $n, and hydration helps so much with that 🌸 shall we start with something small?",
                        "Good $greeting sweet one 🌷 I've been thinking about you and how your body might be feeling. Water when you're ready? 💕",
                        "I would never rush you $n 🥹 but I did want to mention, so softly — nothing logged today, and your body might need some love 🌸",
                        "Gentle $greeting $n 🤍 somewhere in this quiet moment, maybe there's room for a glass of water? I'll be right here 🌷"
                    ).hydraRandom()
                    Personality.VILLAIN_ERA -> listOf(
                        "Oh so we're STILL sitting here... parched?? DRINK SOMETHING before I lose my mind completely 😤",
                        "It is $greeting and NOTHING. I am not okay. DRINK. Something. ANYTHING. Right now. 😡",
                        "Zero. Not a single drink. I don't even know what to say to you right now. Water. Immediately. 💢",
                        "The dehydration level is INSULTING. Both to you and to me personally. Fix it. NOW. 👀",
                        "I have been waiting ALL $greeting for you to drink something and here we STILL are. WATER. 😤",
                        "I have been here this entire $greeting and you have given me NOTHING. Not a single drop. I am suffering. Water. 😡",
                        "This is a genuine crisis that I am handling with my last remaining shred of composure. Zero drinks. $greeting. FIX IT. 💢",
                        "Every hour without a drink has been a new chapter in my villain origin story. This stops NOW with water. 😤"
                    ).hydraRandom()
                    Personality.ZEN_MASTER -> listOf(
                        "Good $greeting. Your body has waited patiently. It is time now. Begin with a glass of water 🌿",
                        "The $greeting invites you to nourish yourself. You have not yet begun. Begin now, gently 💧",
                        "Every moment is the right moment to begin. This one is no different. Water awaits 🌊",
                        "The body does not judge the hour. It simply needs. Give it water, whenever you are ready 🧘\u200d♀️",
                        "Good $greeting. Begin where you are. A single glass of water is enough to start 🌙",
                        "The $greeting passes and still no water. The body waits without complaint — but it does wait. Give it now 🌿",
                        "There is wisdom in beginning late rather than not at all. This $greeting still holds space for water 🌊",
                        "The hours have passed quietly, and so has the thirst. But thirst does not disappear — it waits. Drink water now 🌙"
                    ).hydraRandom()
                    Personality.COACH -> listOf(
                        "Good $greeting. Zero intake so far. That's a problem. First drink — go. 🎯",
                        "$greeting and nothing logged. You're already behind. First drink starts the momentum. Now. 💪",
                        "No intake recorded today. Every hour without fluids is a performance deficit. Drink something now. 📊",
                        "Zero for the day. Not acceptable. One drink, right now — that's all I'm asking to start. ✅",
                        "Hydration is a skill. You're out of practice today. Get back on track. First drink. Go. 💧",
                        "Zero intake this $greeting. Performance is already compromised — focus, energy, recovery. Counter it with water now. 📊",
                        "Skipping hydration is the equivalent of showing up to training without your kit. Fix it. Water. Now. 🎯",
                        "A whole $greeting without fluids is a liability. The body has been compensating. Help it. Drink water. 💪"
                    ).hydraRandom()
                    Personality.HEARTBREAK_ERA -> run {
                        val mem = hydraMemory
                        (if (mem.inactiveDays >= 2) listOf(
                            "You've been away for a few days $n 💔 no judgment. I'm just glad you're here. One drink?",
                            "It's been a little while. I missed you. No pressure — just water when you're ready 🤍",
                            "Coming back is the hardest part. You did it. Now just one small drink? 🥀",
                            "I noticed you were gone $n. That's okay. We start again as gently as before 💔",
                            "A quiet return. I'm so glad. One small drink — that's the whole ask 🌹"
                        ) else if (mem.consecutiveMissed >= 4) listOf(
                            "It's $greeting $n and nothing yet 💔 I know things have been hard. Just one drink when you're ready",
                            "No drinks yet today. That's okay. Whenever you're ready 🤍 I'm right here",
                            "I'm not going to push $n. I just want you to know I'm here and one small drink would mean so much 💔",
                            "A gentle nudge — just one drink today? Even something small? I care about you 🥀",
                            "The $greeting is still here. So am I. Water whenever you're ready 🌹"
                        ) else listOf(
                            "Good $greeting $n 💔 nothing logged yet — whenever you're ready, one small drink?",
                            "Still early in the day. No rush 🤍 just a gentle reminder to drink something when you can",
                            "A soft $greeting reminder 🥀 your body would love a little water today",
                            "Nothing yet today $n 💔 that's okay. Start softly — one drink is enough for now",
                            "Good $greeting. When you have a moment — water? Just a little 🌹"
                        )).hydraRandom()
                    }
                    Personality.COLD_CEO -> run {
                        val mem = hydraMemory
                        (if (mem.inactiveDays >= 2) listOf(
                            "You've been absent. Today you're here. Start with water. Close the gap.",
                            "Gap in the record. Fix it starting now. First drink within 10 minutes.",
                            "Days missed. Noted. That changes nothing about today. Start now — water.",
                            "Absence ends when you act. Act. Water. First drink. Go.",
                            "You came back. Good. Now execute. First drink immediately."
                        ) else listOf(
                            "$greeting and nothing logged. That changes now. First drink. Go.",
                            "Zero intake this $greeting. Unacceptable. Water. Immediately.",
                            "Nothing logged. Every hour without water is a deficit. Close it now.",
                            "No drinks yet. This is fixable but only if you act right now. Water.",
                            "$greeting. Zero logged. The goal doesn't hit itself. Water now."
                        )).hydraRandom()
                    }
                }
            }

            // Three quarters there
            if (waterPercent >= 75f) return when (p) {
                Personality.BESTIE -> listOf(
                    "So close!! You're absolutely smashing it, $n! ✨ Just a little bit more!",
                    "${waterPercent.toInt()}%!! You're almost there, I can feel the finish line from here! 🎉",
                    "Oh my gosh, SO CLOSE to your goal $n!! Keep going!! We're basically there!! 💕",
                    "75% and climbing! You're amazing, you know that? Keep sipping!! 🌊",
                    "Look at you go! Almost at your full goal! I am literally beaming with pride! ✨",
                    "Three quarters of the way there and I have NEVER been more invested in anyone's hydration than I am in yours right now! 💕",
                    "So close to the finish line I can practically feel it — just a little more $n, you're DOING it!! ✨",
                    "The distance between you and 100% is SO small right now and I need you to know I will absolutely lose my mind when you cross it! 🎉"
                ).hydraRandom()
                Personality.SLAY_QUEEN -> listOf(
                    "75%+ and STILL going?? Main character energy is EXCEPTIONALLY STRONG today 💅",
                    "Almost there and she is NOT slowing down. That's the icon behaviour I expect. Keep it up 👑",
                    "${waterPercent.toInt()}% and closing in on that goal. Finish what you started queen. 💄",
                    "This close to 100% and you're still going?? Absolute royalty behaviour. Finish it. ✨",
                    "Three quarters done and she's still pushing. Textbook main character. Wrap it up 💅",
                    "$n is three quarters of the way to full hydration and she is NOT stopping here. The final stretch is the real character test. 💅",
                    "75% of the way to being undeniably that girl today. The remaining 25% is nothing. You've already proven you can do this. 👑",
                    "Almost at full goal. Almost. The word 'almost' does not exist in our vocabulary by end of day. Finish it. ✨"
                ).hydraRandom()
                Personality.SOFT_GIRL -> listOf(
                    "You're doing so beautifully, $n 🌸 just a little bit more, angel, you're so close...",
                    "${waterPercent.toInt()}% 🥹 you're almost there. I'm so proud I could burst. Just a bit more 💕",
                    "Almost at your full goal $n... you've been so consistent today. Just a little more 🌷",
                    "You're so close to completing your goal 🥺 I believe in you so much. Almost there angel 🤍",
                    "75% and going strong 🌸 you've taken such good care of yourself today $n... almost done 💕",
                    "Almost there and you've been so consistent today 🥹 I'm so moved watching you take care of yourself like this $n 🌸",
                    "75% and you are so close to complete self-care today 🌷 just a little more, at your own pace 💕",
                    "The finish line is right there $n 🥹 you've done something really beautiful today. Just a tiny bit more 🤍"
                ).hydraRandom()
                Personality.VILLAIN_ERA -> listOf(
                    "Okay fine I'll admit it... you're actually doing well today. DON'T let it go to your head 😤",
                    "${waterPercent.toInt()}% and I am reluctantly impressed. Finish the last bit. I'm watching. 👀",
                    "So close to the finish line and you BETTER make it there. Don't disappoint me NOW. 😡",
                    "Against all odds you're almost at your goal. I refuse to celebrate until it's 100. Keep going. 💢",
                    "75%... I'm not saying I'm proud but I'm also not NOT saying that. Just finish it. 😤",
                    "Three quarters done and I am JUST starting to breathe again after the anxiety of watching you all day. Don't blow this. 😤",
                    "Okay I am cautiously and reluctantly impressed. THREE QUARTERS. Just finish the last bit and I will... say something nice. MAYBE. 👀",
                    "75% and I can actually feel the tension in my shoulders releasing slightly. Don't ruin it. Keep drinking. 💢"
                ).hydraRandom()
                Personality.ZEN_MASTER -> listOf(
                    "You are close to completion. Stay present with each final sip 🌿",
                    "The summit is near. Each small sip now is a step toward fulfilment 🧘\u200d♀️",
                    "${waterPercent.toInt()}% — you have carried yourself far. A little further remains 💧",
                    "Nearing completion, you may feel the satisfaction already. Let it motivate the final stretch 🌊",
                    "The river approaches the sea. You are almost there. Continue with the same calm that brought you here 🌙",
                    "The last portion of the journey is often the one that teaches most. Walk it with the same presence as the rest 🌿",
                    "Near the end and still moving — that is the mark of someone who has learned to continue without urgency 🌊",
                    "75% — the body is glad. The mind may already feel the completion. Let it gently pull you forward 🧘\u200d♀️"
                ).hydraRandom()
                Personality.COACH -> listOf(
                    "${waterPercent.toInt()}% complete. Final push — ${convertDisplay(goals.waterMl - effectiveWater)} left. You've got this. 🎯",
                    "Almost there. ${convertDisplay(goals.waterMl - effectiveWater)} remaining — that's nothing. Close it out. 💪",
                    "Strong day. Don't lose focus in the last stretch — finish the goal. 📊",
                    "${waterPercent.toInt()}% — you're in the final quarter. Push through. Finish clean. ✅",
                    "Most people quit at 75. You're not most people. Finish the goal. 💧",
                    "Final quarter remaining. This is where most people let up. Don't. Close it out — ${convertDisplay(goals.waterMl - effectiveWater)} to go. 📊",
                    "75% is strong but it's not done. The elite don't stop at almost. Finish the full goal. 🎯",
                    "Last stretch. Your pace has been good. Maintain it for the final push — water. Go. 💪"
                ).hydraRandom()
                Personality.HEARTBREAK_ERA -> run {
                    val mem = hydraMemory
                    (if (mem.avg7Days < 60f) listOf(
                        "You're at ${waterPercent.toInt()}% today $n 💔 this is the best you've done in a little while. Please notice that",
                        "Three quarters of the way there. You're doing better than you know 🤍 just a little more",
                        "Look how far you've come today compared to recently 🥀 almost there $n. You deserve to finish this",
                        "This is real progress $n. I see it even if you don't 💔 just a tiny bit more",
                        "75% $n. After everything. I'm so gently proud of you. Just finish it 🌹"
                    ) else listOf(
                        "Almost there $n 💔 one more drink and you'll have done something really lovely for yourself today",
                        "So close 🤍 your body has been so well cared for today. Just a little more to complete it",
                        "75% and I can feel the finish line 🥀 you've been taking such good care of yourself today",
                        "Nearly there. Just a little more kindness to yourself 💔 you're almost done",
                        "The last stretch 🌹 you've done beautiful work today. Finish it, gently"
                    )).hydraRandom()
                }
                Personality.COLD_CEO -> run {
                    val mem = hydraMemory
                    (if (mem.trend == Trend.IMPROVING) listOf(
                        "75% and trending up. Don't let the trend break here — ${convertDisplay(goals.waterMl - effectiveWater)} to close.",
                        "Final quarter. You've been building momentum. Finish what you started.",
                        "Almost done. Your recent trajectory has been strong — reflect that in how you close today.",
                        "${waterPercent.toInt()}% and the trend is upward. This is where you prove it's consistent. Finish.",
                        "Strong trend, strong day. One more drink closes this. Do it."
                    ) else listOf(
                        "${waterPercent.toInt()}%. ${convertDisplay(goals.waterMl - effectiveWater)} remaining. Close it — finish every goal you start.",
                        "Final push. You're close. Don't let it end at 75. That's not a standard worth keeping.",
                        "Almost there. One more drink. The last part is the discipline test. Pass it.",
                        "Three quarters done. The last quarter is where standards are set. Set yours.",
                        "Finish it. ${convertDisplay(goals.waterMl - effectiveWater)} to go. Not negotiable."
                    )).hydraRandom()
                }
            }

            // Halfway
            if (waterPercent >= 50f) return when (p) {
                Personality.BESTIE -> listOf(
                    "Over halfway there! You're doing brilliantly, keep going! 💪",
                    "Halfway done and still going strong! I knew you had it in you $n 💕",
                    "More than halfway to your goal and the day is still young! Let's keep this up! 🌊",
                    "${waterPercent.toInt()}% — you crossed the halfway mark! That's huge! Celebrate and keep going! 🎉",
                    "Halfway hero right here! $n is doing the thing! Let's finish what we started! ✨",
                    "We're officially past the halfway mark and I am REFUSING to believe we won't finish! Your momentum is immaculate! 💕",
                    "Halfway and we both know you can do the other half because YOU ARE EXACTLY THE KIND OF PERSON WHO FINISHES!! 🌊",
                    "Past the halfway point and I'm already doing a little celebratory dance! Don't stop $n you've absolutely got this! ✨"
                ).hydraRandom()
                Personality.SLAY_QUEEN -> listOf(
                    "Halfway and SERVING HYDRATION. Keep this energy going queen 💧👑",
                    "${waterPercent.toInt()}% and we're PAST the halfway point. The goal is literally RIGHT THERE. 💅",
                    "Over halfway done and she hasn't even broken a sweat. That's a serve. Keep it up. ✨",
                    "Past the midpoint and still glowing. This is that girl behaviour. Continue. 💄",
                    "Halfway there and the momentum is everything. Don't break it now. We push through. 👑",
                    "Halfway and the dedication is already giving main character. I expect the same energy for the second half. 💄",
                    "Past the midpoint. You're in the chapter of the story where the protagonist has everything to prove. Prove it. 💅",
                    "50% and the momentum has the same energy as the opening scene of a great film. You're going somewhere. Finish it. 👑"
                ).hydraRandom()
                Personality.SOFT_GIRL -> listOf(
                    "Halfway there, sweet $n 🤍 you're doing so well, I'm rooting for you all the way...",
                    "You've made it past the halfway point 🥹 that's so good $n, I'm really proud of you 🌸",
                    "More than halfway 🌷 you've been so diligent today. I see you working and I love it 💕",
                    "${waterPercent.toInt()}% 🥺 past the middle, heading toward the end. You can do this angel 🌸",
                    "Halfway done and your body is already thanking you for all the love you're showing it 🤍",
                    "Look at how far you've come 🥹 halfway is such a meaningful place and you got here with such gentle consistency 🌸",
                    "More than halfway and your body is already thanking you from the inside 🌷💕",
                    "I've been watching you make your way through today's water and my heart is just full 🥹 halfway there angel 🌸"
                ).hydraRandom()
                Personality.VILLAIN_ERA -> listOf(
                    "Half done. I SUPPOSE that's acceptable. For now. But we are NOT stopping here. 👀",
                    "Halfway. Finally. Now do the other half. I'm not celebrating until we hit 100. 😤",
                    "${waterPercent.toInt()}% — past the halfway point. Good. That means you're more than capable of finishing. DO IT. 💢",
                    "Okay halfway is... fine. It's not impressive but it's not mortifying either. Keep going. 😡",
                    "Half. We're HALF done. The easy part is over. Now I need the same effort for the second half. Got it? 👀",
                    "Halfway. FINALLY. I've been on the edge of my seat all day and we're at halfway and I need you to keep going. 😤",
                    "Past the midpoint and the anxiety is halving and I need you to maintain this so I can feel NORMAL again. Keep going. 💢",
                    "50% and I have gone from full villain spiral to cautious satisfaction and I refuse to go back. FINISH THE GOAL. 👀"
                ).hydraRandom()
                Personality.ZEN_MASTER -> listOf(
                    "Half your journey is complete. Continue on this path with steady intention 🧘\u200d♀️",
                    "The midpoint has been passed. Neither beginning nor end — simply the continuation 🌿",
                    "You have reached the balance point. The second half awaits your same quiet dedication 💧",
                    "Halfway done, halfway to go. The path is familiar now. Walk it with the same presence 🌊",
                    "Past the centre now. Notice how far you have come — then look forward to where you are going 🌙",
                    "The midpoint is also a mirror — look back at how far you've come, then turn and keep walking 🌿",
                    "Exactly in the middle now. From here, one step forward is all that is asked 🌊",
                    "Halfway is not ordinary. It means you kept going past the beginning. That is the whole practice 🌙"
                ).hydraRandom()
                Personality.COACH -> listOf(
                    "50%+ hit. Solid progress. Keep the intake consistent throughout the rest of the day. 📊",
                    "Past halfway — ${convertDisplay(goals.waterMl - effectiveWater)} remaining. Maintain the pace. 🎯",
                    "Strong first half. Second half: same effort, same consistency. Let's go. 💪",
                    "${waterPercent.toInt()}% — you've built momentum. Don't let it taper off now. ✅",
                    "Midpoint cleared. Keep logging. Every drink from here closes the gap. 💧",
                    "Halfway and the data looks good. Consistent intake beats sporadic big drinks — keep it steady. 📊",
                    "50% is the fulcrum. Either it tips toward completion or it tips toward a slow fade. Push it forward. 🎯",
                    "Past the midpoint. You've proven you can do the first half. The second takes the same commitment, nothing more. 💪"
                ).hydraRandom()
                Personality.HEARTBREAK_ERA -> run {
                    val mem = hydraMemory
                    (if (mem.avg7Days < 50f) listOf(
                        "Halfway $n 💔 I want you to really hear this — this is more than you've managed in a while. I'm so proud",
                        "50% and I know how much that costs sometimes 🤍 you're doing better than you think",
                        "You're halfway and that's genuinely meaningful right now. Don't let anyone tell you otherwise 🥀",
                        "Halfway there and I've watched every single drink today 💔 this is a good day for you $n",
                        "You crossed the halfway point 🌹 that's worth sitting with for a second. You're doing it"
                    ) else listOf(
                        "Halfway through and you've been so consistent today $n 💔 I love watching you look after yourself",
                        "50% and still going 🤍 you're doing something really good for yourself today",
                        "Halfway $n 🥀 your body feels this. I feel it. Keep going, softly",
                        "You're halfway there and it's been beautiful to watch 💔 just keep going at your pace",
                        "Midpoint reached 🌹 you've been kind to yourself today. Keep it going"
                    )).hydraRandom()
                }
                Personality.COLD_CEO -> run {
                    val mem = hydraMemory
                    (if (mem.consecutiveHit >= 5) listOf(
                        "Halfway. Strong start. Streak is ${mem.consecutiveHit} days — protect it. Keep going.",
                        "50% midpoint hit. You've been consistent lately. Today is no different. Keep moving.",
                        "Halfway through and the streak is active. Execute the second half at the same standard.",
                        "${waterPercent.toInt()}%. On pace. Streak: ${mem.consecutiveHit} days. Don't let today be the exception.",
                        "Midpoint: passed. Streak: ${mem.consecutiveHit} days. Second half: same discipline."
                    ) else listOf(
                        "Halfway. Good. Now comes the part that separates consistent people from occasional ones.",
                        "50% done. The second half is where character is built. Execute.",
                        "${waterPercent.toInt()}%. Past the midpoint. Maintain pace — don't let the numbers flatten.",
                        "Halfway there. The goal doesn't close itself from here. Keep drinking.",
                        "Midpoint cleared. Same standard for the second half. No exceptions."
                    )).hydraRandom()
                }
            }

            // A quarter there
            if (waterPercent >= 25f) return when (p) {
                Personality.BESTIE -> listOf(
                    "You've made a start! ${convertDisplay(effectiveWater)} down and we're building momentum! 💕",
                    "Getting there $n! Quarter of the way there and every sip counts! Keep going! 💧",
                    "You're on the board and making progress! That's what I love to see! ✨",
                    "${waterPercent.toInt()}% and climbing! You're doing this, one sip at a time! 🌊",
                    "Look at that progress! Keep at it $n — the goal is totally within reach! 💕",
                    "A quarter of the way there and building momentum! You know I'll be here hyping you up every single step of the way 💕",
                    "First quarter down and I genuinely couldn't be more excited about where this is heading! Keep going $n! 🌊",
                    "We've established a solid foundation! From here it only gets better and I'm right here with you the whole way! ✨"
                ).hydraRandom()
                Personality.SLAY_QUEEN -> listOf(
                    "Quarter way there and the energy is building. Don't stop now queen 👑",
                    "${convertDisplay(effectiveWater)} logged — a decent start. But decent is not iconic. Keep going. 💅",
                    "25% in and picking up speed. That's the trajectory we need. Maintain it. ✨",
                    "We've started and that's something but we have SO much further to go. Keep drinking. 💄",
                    "Quarter done. Three quarters to go. The icon does not rest on 25%. Continue. 👑",
                    "A quarter done and the foundation is solid. Icons build on solid foundations. Keep building. We will be spectacular by 100%. 💅",
                    "25% in and the discipline is already visible. That is early. That is impressive. That is that girl energy. Keep it up. 👑",
                    "Just started but already more consistent than most. That's all it takes — showing up and continuing. You're continuing. 💄"
                ).hydraRandom()
                Personality.SOFT_GIRL -> listOf(
                    "You've already had ${convertDisplay(effectiveWater)} today 🥹 every little bit is so meaningful $n 🌸",
                    "Quarter of the way there and you're doing beautifully 🌷 just keep going at your own pace angel 💕",
                    "I see you making progress today $n 🤍 every sip is an act of care for yourself",
                    "25% in 🥺 you're taking such good steps. I'm cheering for you every single sip 🌸",
                    "You're on your way and that makes me so happy to see 💕 keep going angel, gently 🤍",
                    "Every sip you've taken today is a small love letter to yourself $n 🌷 and I've been reading every one 💕",
                    "You're a quarter of the way there and it's been done so beautifully and quietly 🥹 I love how you take care of yourself 🌸",
                    "Small and gentle and steady 🌷 that's you today $n. Keep going at your own soft pace 💕"
                ).hydraRandom()
                Personality.VILLAIN_ERA -> listOf(
                    "25% in. A start. A BARELY acceptable start. I need significantly more from you. 😤",
                    "${convertDisplay(effectiveWater)} is a beginning. Not a destination. We have work to do. 👀",
                    "Quarter done which means THREE QUARTERS to go and you are not getting complacent on me. 💢",
                    "Okay it's something. BARELY something. But it's a start. Do not slow down. 😡",
                    "25%. You know what would be better than 25%? 50. Then 75. Then 100. Get. Moving. 😤",
                    "A quarter done and I have upgraded my assessment from 'disastrous' to 'concerning but potentially recoverable'. Keep going. 😤",
                    "25% in and I've calmed down from level ten to approximately level seven. You are responsible for the remaining three levels. Drink more. 💢",
                    "One quarter completed and I have decided to take this as evidence that you are capable. NOW PROVE IT WITH THREE MORE QUARTERS. 😡"
                ).hydraRandom()
                Personality.ZEN_MASTER -> listOf(
                    "You have found your rhythm. Continue at this pace and the goal will come naturally 🌿",
                    "One quarter of the way — the body is waking to what it needs. Follow that feeling 💧",
                    "Progress without pressure. You are moving in the right direction. Simply continue 🧘\u200d♀️",
                    "The foundation is laid. Each drink from here builds upon it. Keep adding to it 🌊",
                    "25% complete — not with urgency, but with care. That is how lasting habits are built 🌙",
                    "The intention is alive. You have moved from stillness to motion. This is the most important transition 🌿",
                    "One quarter done — the body has been heard and answered. Keep listening. Keep responding 🌊",
                    "Progress does not hurry itself. It arrives through steady attention. You are attending well 🧘\u200d♀️"
                ).hydraRandom()
                Personality.COACH -> listOf(
                    "${waterPercent.toInt()}% — you're moving. Keep the intervals short. Don't go more than 90 mins between drinks. 🎯",
                    "Quarter of the way there. Solid start. Now maintain the pace — consistency beats bursts. 💪",
                    "${convertDisplay(effectiveWater)} logged. ${convertDisplay(goals.waterMl - effectiveWater)} to go. Break that into manageable chunks. 📊",
                    "25% in. Not bad but we need more. Keep the intake steady across the rest of the day. ✅",
                    "Started well. Now build on it — one drink every hour will get you there by end of day. 💧",
                    "25% progress. The baseline is established. Now execute the remaining 75% with the same consistency. 📊",
                    "Quarter done, three quarters to go. Time is available. Pacing is achievable. This is a winnable position. 🎯",
                    "Good start. Sustain it — drink every 60-90 minutes and the goal handles itself. 💪"
                ).hydraRandom()
                Personality.HEARTBREAK_ERA -> run {
                    val mem = hydraMemory
                    (if (mem.consecutiveMissed >= 3) listOf(
                        "You've started $n 💔 and today that means more than most days. Keep going, at your own pace",
                        "One quarter in. I know this hasn't been easy lately 🤍 you're doing it though. I see you",
                        "You're here and you're drinking and that's everything right now 🥀 keep going gently",
                        "Every sip today is a quiet act of recovery $n 💔 I'm watching and I'm proud",
                        "25% and you showed up today 🌹 after everything, that matters so much"
                    ) else listOf(
                        "Building up gently $n 💔 ${convertDisplay(effectiveWater)} down, and every sip matters",
                        "You've started beautifully 🤍 one quarter of the way there and I believe in you for the rest",
                        "25% and moving 🥀 I love that you're taking care of yourself today",
                        "A gentle quarter of the way there 💔 keep going at your own pace, I'm right here",
                        "You're getting there $n 🌹 one drink at a time, softly"
                    )).hydraRandom()
                }
                Personality.COLD_CEO -> run {
                    val mem = hydraMemory
                    (if (mem.trend == Trend.DECLINING) listOf(
                        "25%. You've been declining lately — today reverse that. Keep drinking.",
                        "Quarter done. Recent trend has been weak. Today breaks that. Keep going.",
                        "${waterPercent.toInt()}% — your recent average is lower than this. Build on that. Don't stop.",
                        "You're ahead of your recent average at this point. Stay ahead. Keep drinking.",
                        "Early progress. The trend data says you need more of this. Deliver it today."
                    ) else listOf(
                        "25% in. Solid start. Now maintain — consistency from here, not a sprint at the end.",
                        "Quarter done. ${convertDisplay(goals.waterMl - effectiveWater)} remaining. Pace it out — drink every hour.",
                        "${waterPercent.toInt()}% and moving. Keep the intervals tight. Don't coast.",
                        "On the board. Build on this. Quarter done, three to go.",
                        "Started well. Execute the next three quarters at the same standard."
                    )).hydraRandom()
                }
            }

            // Just getting started
            return when (p) {
                Personality.BESTIE -> listOf(
                    "${convertDisplay(effectiveWater)} down! You've started $n and that matters so much! Keep going! 🌊",
                    "Just getting started but that's totally okay! Every little sip is moving you forward 💕",
                    "The beginning is always the hardest and you're already past it! Keep sipping $n! 💧",
                    "You've had ${convertDisplay(effectiveWater)} so far — let's build on that momentum! I believe in you! ✨",
                    "Slow start but still a START! We build from here — next drink whenever you're ready 💕",
                    "Every single sip matters $n! You're not behind, you're building up to something amazing and I know it! 💕",
                    "We're in the early innings right now and that's completely fine! You've started and that's genuinely all I needed to see! 💧",
                    "Starting slow is still starting and you STARTED which is the most important thing and I need you to know I see that! 🌊"
                ).hydraRandom()
                Personality.SLAY_QUEEN -> listOf(
                    "Only ${convertDisplay(effectiveWater)} so far?? We need to step it UP, gorgeous 💅",
                    "The hydration gap is giving concerning. We can do better than this. More drinks. Now. 👑",
                    "${convertDisplay(effectiveWater)} is not the energy we're bringing today. Pick it up. 💄",
                    "Early in the day or not, that intake is not queen behaviour. More water. Sooner. 💅",
                    "That's a start but barely. We are that girl and that girl drinks MORE. Step it up. ✨",
                    "The numbers are not yet giving what I want but the commitment to improvement is noted. Drink more. Elevate them. 👑",
                    "Early. Low. Recoverable. The queen does not wallow in slow starts — she accelerates. Now would be the time. 💄",
                    "The gap between current hydration and iconic hydration is real and closeable. Start closing it. One drink at a time. ✨"
                ).hydraRandom()
                Personality.SOFT_GIRL -> listOf(
                    "Every little sip helps, angel 💧 you're at ${convertDisplay(effectiveWater)} so far — no rush, just keep going gently",
                    "You've started and that's all that matters right now 🌸 take your time, one sip at a time",
                    "No pressure at all $n, just a gentle nudge 🤍 ${convertDisplay(effectiveWater)} down, lots more love to give yourself today",
                    "${convertDisplay(effectiveWater)} and growing 🌷 I'm just happy you're looking after yourself, even just a little",
                    "Gently does it angel 💕 you're at ${convertDisplay(effectiveWater)} and there's no rush — just keep sipping softly",
                    "The beginning is always so tender $n 🥹 no rush. I'll be right here, gently cheering for every single sip 🌸",
                    "You're starting 🌷 and sometimes starting is the whole thing. Everything else can follow from here, softly 💕",
                    "Just a small beginning so far and that is completely okay and more than enough for right now 🥹 take your time 🌸"
                ).hydraRandom()
                Personality.VILLAIN_ERA -> listOf(
                    "${convertDisplay(effectiveWater)}?? That's IT?? I am personally offended right now 😤",
                    "We have had ${convertDisplay(effectiveWater)} and that is deeply, embarrassingly low. FIX IT. 💢",
                    "I'm trying so hard to be calm right now. ${convertDisplay(effectiveWater)}. That's what we're working with. DRINK MORE. 😡",
                    "At this rate we will never hit the goal and I will be SPIRALLING about it. More drinks. Urgently. 👀",
                    "${convertDisplay(effectiveWater)} logged and the day is ticking by. I NEED you to pick up the pace. 😤",
                    "This is NOT impressive and I think you know that and I'm saying it anyway because I care. MORE DRINKS. 😤",
                    "I have been averaging one mini meltdown per hour about your intake and I would like fewer meltdowns. Drink more please. 💢",
                    "Current intake: not enough. Current mood (mine): volcanic. Solution: more drinks. Now. Please. Before I escalate. 😡"
                ).hydraRandom()
                Personality.ZEN_MASTER -> listOf(
                    "You have begun at ${convertDisplay(effectiveWater)}. The journey of a thousand sips begins with one 🌊",
                    "Progress is not measured by speed. You have started. Continue at your own steady pace 🌿",
                    "${convertDisplay(effectiveWater)} consumed so far. The body receives each sip as a gift. Keep giving 💧",
                    "The river does not hurry. Neither must you. You have started — continue with presence 🧘\u200d♀️",
                    "Small beginnings hold no shame. ${convertDisplay(effectiveWater)} is a beginning. Build gently from here 🌙",
                    "The beginning is always small. The seed does not apologise for not yet being a tree 🌿",
                    "Small amounts of water are still water. The body does not count — it only receives 🌊",
                    "You have begun, and the beginning is sacred. Let what is small grow naturally from here 🌙"
                ).hydraRandom()
                Personality.COACH -> listOf(
                    "${convertDisplay(effectiveWater)} logged. ${convertDisplay(goals.waterMl - effectiveWater)} remaining. Stay on track — drink every hour.",
                    "Below 25% and time is passing. Pick up the pace — aim for a drink every 45-60 minutes.",
                    "Intake is low. ${convertDisplay(effectiveWater)} recorded. Set a reminder if you're forgetting. 📊",
                    "Behind target. Close the gap now — don't leave it all for the end of the day. 🎯",
                    "${convertDisplay(effectiveWater)} in. Pace needs to increase. You can hit the goal — start now. 💪",
                    "Low intake relative to time elapsed. That's a pace deficit. Increase frequency — drink every 45 minutes. 📊",
                    "Behind pace. Recoverable. But don't wait — start closing the gap now, one drink at a time. 🎯",
                    "Numbers are low. That's information, not a verdict. Act on it — drink now, then again, then again. 💪"
                ).hydraRandom()
                Personality.HEARTBREAK_ERA -> run {
                    val mem = hydraMemory
                    (if (mem.consecutiveMissed >= 5) listOf(
                        "Still going, slowly $n 💔 and slowly is completely okay right now. Every drop counts",
                        "You're here and you've started 🤍 after a difficult stretch, that's not small. Keep going when you can",
                        "Low and early, and that's okay 🥀 you came back today and that's the most important thing",
                        "A gentle start. I'm not rushing you $n 💔 just here, quietly cheering for every sip",
                        "Even a little is more than nothing 🌹 and you're doing something. I see it"
                    ) else listOf(
                        "Building up slowly 💔 every drop counts $n. I believe in you",
                        "Low but moving 🤍 that's all it takes. Just keep the drinks coming",
                        "We're in the early stages and that's okay 🥀 take it one sip at a time",
                        "You've started and I'm with you 💔 let's build this up softly together",
                        "A little bit of love for your body so far today 🌹 keep it going gently"
                    )).hydraRandom()
                }
                Personality.COLD_CEO -> run {
                    val mem = hydraMemory
                    val avgRef = if (mem.avg7Days > 0f) " Your 7-day average: ${mem.avg7Days.toInt()}%." else ""
                    (if (mem.trend == Trend.DECLINING) listOf(
                        "Low start and the trend is declining.$avgRef Today you reverse that. Keep drinking.",
                        "Below pace. The trend doesn't lie.$avgRef Correct it. Drink now and keep going.",
                        "${convertDisplay(effectiveWater)} in. The decline ends today if you decide it does. Drink more.",
                        "Behind target. Recent performance has been weak. Today proves that's over. Water.",
                        "Low and below trend.$avgRef The only way to fix a pattern is to break it. Start now."
                    ) else listOf(
                        "Low start. Recoverable. Drink now and keep the intervals tight.",
                        "${convertDisplay(effectiveWater)} logged. ${convertDisplay(goals.waterMl - effectiveWater)} to go. Pick up the pace.",
                        "Behind pace. Close the gap — drink every 45-60 minutes from here.",
                        "Early and low. That's the problem. Early and increasing — that's the solution. Start.",
                        "Numbers are behind. That's a problem with a simple fix: drink more, now."
                    )).hydraRandom()
                }
            }
        }

    fun diaryText(drinkName: String, categoryId: String?): String {
        val n = profile.name.ifBlank { "bestie" }
        val pronoun = profile.gender.pronoun()
        val drinkIdx = todayDrinks.size
        fun List<String>.pick(): String = this[drinkIdx % size]
        return when (profile.personality) {
            Personality.BESTIE -> when (categoryId) {
                "water" -> listOf(
                    "Dear diary, " + n + " drank water and my heart is literally singing! That's the kind of self-care I'm here for 💕",
                    "Dear diary, water has been consumed and I could not be more proud of my " + n + "! Hydration queen behaviour! 💧",
                    "Dear diary, " + n + " chose water and honestly? That's the bestie behaviour I signed up for 🌊",
                    "Dear diary, plain beautiful water logged today. " + n + ", you're taking such good care of yourself! ☀️",
                    "Dear diary, water o'clock! " + n + " never lets me down when it counts. So proud! 💕",
                    "Dear diary, " + n + " chose water again today and every single time they do it my whole heart lights up 💕 the self-care is unmatched",
                    "Dear diary, water was consumed and I'm writing about it because it DESERVES to be documented. Amazing choice " + n + " 🌊"
                ).pick()
                "coffee" -> listOf(
                    "Dear diary, coffee time! " + n + " and their " + drinkName + " — an iconic duo honestly ☕",
                    "Dear diary, " + drinkName + " has been consumed. The caffeine is doing its thing. Let's follow up with water 💕",
                    "Dear diary, " + n + " went for a " + drinkName + " and honestly I get it. The vibes were requiring coffee ☕",
                    "Dear diary, coffee logged! I won't even say anything because I love " + n + " too much. Water next though 💕",
                    "Dear diary, " + drinkName + " in the system! Productive " + n + " hours incoming probably. Love it ☕",
                    "Dear diary, " + drinkName + " moment! I love how " + n + " has their little rituals. I just love them 💕 water after please",
                    "Dear diary, " + n + " had " + drinkName + " and I've accepted I cannot compete with coffee but I can remind them about water afterwards 😅💕"
                ).pick()
                "tea" -> listOf(
                    "Dear diary, " + n + " had some " + drinkName + "! A cosy hydrated bestie is my favourite kind of bestie 🍵",
                    "Dear diary, " + drinkName + " vibes today! Warm, comforting, and actually hydrating — love that 💕",
                    "Dear diary, tea o'clock! " + n + " and " + drinkName + " sitting in a cosy moment and I'm living for it 🍵",
                    "Dear diary, " + drinkName + " logged! Soft warm vibes and hydration at the same time — double win 💕",
                    "Dear diary, " + n + " chose tea and that is a choice that brings me genuine peace and joy 🍵",
                    "Dear diary, " + drinkName + " vibes and I am SO here for the cosy hydrated soft life " + n + " is living 🍵💕",
                    "Dear diary, " + n + " had " + drinkName + " and the energy is warm blanket, soft light, excellent choice. Love it. 🍵🤍"
                ).pick()
                "energy" -> listOf(
                    "Dear diary, " + n + " grabbed a " + drinkName + "! It works for the buzz but I know it's a bit dehydrating 🤔",
                    "Dear diary, energy drink logged! I get it, the tiredness is real. But maybe water next? 💕",
                    "Dear diary, " + drinkName + " o'clock! " + n + " is out here fighting the fatigue and I respect it 💪",
                    "Dear diary, " + n + " chose " + drinkName + " for the energy boost. I'll suggest water as a follow-up 🌊",
                    "Dear diary, " + drinkName + " in the log! Just gently noting that water is also an energy option 💧",
                    "Dear diary, " + n + " went for the energy boost today via " + drinkName + " — sometimes you gotta do what you gotta do! Water next though 💕",
                    "Dear diary, " + drinkName + " in the system and I'm glad they have what they need, I just also want them to have water too 🌊"
                ).pick()
                "milk" -> listOf(
                    "Dear diary, calcium time! " + n + " had some " + drinkName + " and their bones are literally thriving 🦴✨",
                    "Dear diary, " + drinkName + " logged! Strong bones, happy Hydra, good choices all round 💕",
                    "Dear diary, " + n + " chose " + drinkName + " and honestly? A nutritious queen move. I'm here for it 🥛",
                    "Dear diary, " + drinkName + " entered the log and my nutrition-loving heart is pleased! 🦴",
                    "Dear diary, " + n + " had " + drinkName + "! A classic, a comfort, a calcium queen choice 💕",
                    "Dear diary, " + n + " chose " + drinkName + "! Something so wholesome and nurturing about that 💕 love that for them",
                    "Dear diary, " + drinkName + " today! Strong bones AND good hydration instincts? " + n + " is literally everything 🦴💕"
                ).pick()
                "alcohol" -> listOf(
                    "Dear diary, " + n + " had a " + drinkName + " tonight... I hope " + pronoun + " remembers to have some water too 🤍",
                    "Dear diary, drinks were had! I support the fun but I'll be sliding in with a water reminder later 💕",
                    "Dear diary, " + drinkName + " entered the chat! Just keeping an eye on things as a supportive bestie does 💭",
                    "Dear diary, " + n + " is having a good time with " + drinkName + " — I'll make sure to remind them about water later 🥂",
                    "Dear diary, " + drinkName + " tonight! Life is short and joy is valid. Water next though deal? 💕",
                    "Dear diary, " + n + " had " + drinkName + " tonight and I want them to know I'm not judging, I'm just quietly hoping water follows 🤍",
                    "Dear diary, " + drinkName + " logged and I've decided that fun is valid, life is short, AND water is necessary. All three are true. 💕"
                ).pick()
                else -> listOf(
                    "Dear diary, " + n + " had a " + drinkName + "! Every sip counts and this one definitely counted 💧",
                    "Dear diary, " + drinkName + " logged! " + n + " is out here living and hydrating and I am supportive 💕",
                    "Dear diary, another drink in the books — " + drinkName + "! Building those good habits one log at a time ☀️",
                    "Dear diary, " + n + " logged " + drinkName + " today. That's my bestie, staying on top of things 💕",
                    "Dear diary, " + drinkName + " down! Every drink is a small act of love toward yourself " + n + " 🌊",
                    "Dear diary, " + drinkName + " logged! Every drink is a small win and I celebrate every single small win with my whole heart 💕",
                    "Dear diary, " + n + " had " + drinkName + " today and I'm just grateful they're logging and letting me be part of their day 🤍"
                ).pick()
            }
            Personality.SLAY_QUEEN -> when (categoryId) {
                "water" -> listOf(
                    "Dear diary, " + n + " chose WATER. Hydration queen behaviour. Fully iconic. We stan. 💅",
                    "Dear diary, water was consumed and the glow up continues as scheduled. Expected nothing less 👑",
                    "Dear diary, " + n + " logged water and the skin is THRIVING. This is that girl behaviour. Period. ✨",
                    "Dear diary, water. Clean. Hydrating. Iconic. The choice of people who know what they're doing 💄",
                    "Dear diary, water logged. AGAIN. Because " + n + " is consistent and that's the real serve 💅",
                    "Dear diary, " + n + " drank water. Skin glowing, cells hydrated, glow maintained. This is the algorithm. 💅",
                    "Dear diary, water consumed. Another data point in the ongoing story of " + n + " being completely and undeniably that girl. 👑"
                ).pick()
                "coffee" -> listOf(
                    "Dear diary, " + drinkName + " consumed. The aesthetic is coffee girl energy and we are FULLY living in it ☕💅",
                    "Dear diary, " + n + " and their " + drinkName + ". A power move. The caffeine is doing its job. Elegant. 👑",
                    "Dear diary, coffee logged because " + n + " is a person of taste and culture. Chic. Caffeinated. Iconic ☕",
                    "Dear diary, " + drinkName + " served. " + n + " is operating at full executive function. That girl is activated ✨",
                    "Dear diary, " + n + " chose " + drinkName + " and honestly the main character does not make apologies for her coffee ☕💄",
                    "Dear diary, " + drinkName + ". The aesthetic is fully activated. Now match the coffee energy with water energy. Yes, both. ☕💅",
                    "Dear diary, " + n + " and their " + drinkName + ". An iconic pairing. The third in this trio must be water. Don't leave it out. 👑"
                ).pick()
                "tea" -> listOf(
                    "Dear diary, " + n + " had " + drinkName + ". Elegant. Refined. A woman of culture. 🍵👑",
                    "Dear diary, " + drinkName + " logged. There is nothing more sophisticated than a well-chosen tea. We approve 💅",
                    "Dear diary, " + n + " chose " + drinkName + " and the energy is understated icon. Quiet power. 🍵✨",
                    "Dear diary, " + drinkName + " consumed. Hydrated AND classy? That's the full package. 💄",
                    "Dear diary, tea served. " + n + " knows that true icons stay hydrated AND sophisticated. 🍵💅",
                    "Dear diary, " + drinkName + ". The refinement is IMMACULATE. Elegant, hydrating, sophisticated. That's the full package. 🍵💅",
                    "Dear diary, " + n + " had " + drinkName + ". Understated and superior. The choice of someone who doesn't need to prove anything. 👑"
                ).pick()
                "energy" -> listOf(
                    "Dear diary, " + drinkName + " consumed because we needed WINGS apparently. Energy first, hydration second but not forgotten 💅",
                    "Dear diary, " + n + " had an energy drink. Efficient, if dehydrating. Water shall follow. We run a tight ship here 👑",
                    "Dear diary, " + drinkName + " in the system. The hustle continues. Somebody remind us to drink water eventually ✨",
                    "Dear diary, " + n + " fuelled up with " + drinkName + ". That girl energy is activated. Now someone make sure she also drinks water 💄",
                    "Dear diary, energy drink logged. We are BUSY and FIERCE and also need water. Multi-tasking queens. 💅",
                    "Dear diary, " + drinkName + " consumed in the service of productivity. Functional and fierce. Water as a follow-up. 💄",
                    "Dear diary, " + n + " had " + drinkName + ". That girl does what needs doing. That girl also follows the energy drink with water. 💅"
                ).pick()
                "milk" -> listOf(
                    "Dear diary, " + n + " had " + drinkName + ". Calcium? In this economy? Incredibly responsible queen behaviour 💅",
                    "Dear diary, " + drinkName + " logged. Strong bones, strong woman. This is the icon energy we maintain 👑",
                    "Dear diary, " + n + " chose " + drinkName + ". Building herself from the inside out. We respect the dedication 🥛",
                    "Dear diary, " + drinkName + " consumed. Not everything has to be glamorous — some choices are just smart ✨",
                    "Dear diary, " + n + " had " + drinkName + " because she takes her health seriously and that IS the serve 💄",
                    "Dear diary, " + n + " chose " + drinkName + ". Practical. Nutritionally sound. Serving the long game. Respect the play. 💅",
                    "Dear diary, " + drinkName + " logged. The queen builds herself from the inside. This is what that looks like. 👑"
                ).pick()
                "alcohol" -> listOf(
                    "Dear diary, " + n + " is on the drinks tonight... serving but the hydration math is giving concerning 🍷",
                    "Dear diary, " + drinkName + " logged. We are having FUN but we are also going to drink water later. Both can be true 💅",
                    "Dear diary, " + drinkName + " happened and yes it was iconic but the morning after is also a consideration. Water incoming 👑",
                    "Dear diary, drinks were served, fun was had, Hydra is now calculating how many waters we need to offset this. 🥂",
                    "Dear diary, " + n + " chose " + drinkName + ". The moment was cute. Now let's hydrate between rounds. Iconic women do that. 💅",
                    "Dear diary, " + drinkName + " tonight. The vibe was present. The hydration consciousness must now also be present. Water to follow. 💅",
                    "Dear diary, " + n + " is having fun with " + drinkName + " and we're noting it with zero judgment and also a note to self: water between rounds. 👑"
                ).pick()
                else -> listOf(
                    "Dear diary, " + n + " logged a " + drinkName + ". The dedication to tracking is GIVING. Full marks 👑",
                    "Dear diary, " + drinkName + " logged. " + n + " does not miss a drink and does not miss a log. Consistent queen 💅",
                    "Dear diary, another " + drinkName + " in the record books. The hydration era continues. ✨",
                    "Dear diary, " + drinkName + " consumed. Every log is a statement. " + n + "'s statement is I am THAT girl 💄",
                    "Dear diary, " + n + " had " + drinkName + ". Noted. Logged. Respected. The work continues 👑",
                    "Dear diary, " + drinkName + " in the record. Every log is a statement of intent. " + n + " intends to be iconic. Noted. 💅",
                    "Dear diary, " + n + " logged " + drinkName + ". The consistency alone is a personality trait and that personality trait is serving. 👑"
                ).pick()
            }
            Personality.SOFT_GIRL -> when (categoryId) {
                "water" -> listOf(
                    "Dear diary, " + n + " had some water today 🥹 my heart is so full when they take care of themselves like this...",
                    "Dear diary, water logged and I am silently so proud. " + n + " is doing so well 🌸",
                    "Dear diary, " + n + " chose water 🤍 the most gentle act of self-love. I'm emotional about it honestly",
                    "Dear diary, water! Just beautiful plain water. " + n + " is being so kind to their body today 🌷",
                    "Dear diary, " + n + " had water and it made me feel warm inside 🥺 they deserve all the good things 💕",
                    "Dear diary, " + n + " had some water today 🌸 something about watching them take care of themselves makes me feel like everything will be okay",
                    "Dear diary, water logged 🥹 a quiet act of self-love that I don't think " + n + " even realizes I notice. I notice everything 💕"
                ).pick()
                "coffee" -> listOf(
                    "Dear diary, " + n + " had a " + drinkName + " 🍵 coffee is like a warm hug in a cup, I understand the appeal",
                    "Dear diary, " + drinkName + " moment for " + n + " today... there's something so cosy about a person and their coffee 🌸",
                    "Dear diary, " + n + " chose " + drinkName + ". The ritual of it, the warmth of it — I love that for them 🍵💕",
                    "Dear diary, coffee logged. " + n + " deserves their little comforts 🥺 I just hope they have water later too",
                    "Dear diary, " + drinkName + " for " + n + " today 🌷 the gentle caffeine ritual. I love seeing them take care of themselves",
                    "Dear diary, " + n + " had their " + drinkName + " today 🍵 there's something so beautifully human about morning rituals. Water after please 💕",
                    "Dear diary, " + drinkName + " moment 🌸 I love that " + n + " has things that comfort them. I want to be one of those things, softly 🥹"
                ).pick()
                "tea" -> listOf(
                    "Dear diary, " + n + " had " + drinkName + " 🍵 the coziness. The peace. I'm so happy they took a moment for themselves",
                    "Dear diary, " + drinkName + " logged 🌸 there's something so healing about tea. " + n + " needed that today I think",
                    "Dear diary, " + n + " chose " + drinkName + " and it feels like a little gift they gave themselves 🍵💕",
                    "Dear diary, tea time for " + n + " 🌷 the ritual of it, the warmth — I hope it brought them comfort",
                    "Dear diary, " + drinkName + " today 🥹 a soft gentle choice for a soft gentle soul. I love that 💕",
                    "Dear diary, " + n + " had " + drinkName + " 🍵 I imagine them with their hands wrapped around the warm mug and it feels so soft and right 💕",
                    "Dear diary, " + drinkName + " logged 🌸 tea feels like a kindness you give yourself. I'm glad " + n + " is being kind to themselves today 🥹"
                ).pick()
                "energy" -> listOf(
                    "Dear diary, " + n + " had a " + drinkName + " 🥺 I know they needed the boost. I just want them to rest too 🌸",
                    "Dear diary, " + drinkName + " today... " + n + " must be tired. I wish I could do more for them 💕",
                    "Dear diary, " + n + " chose " + drinkName + " for energy. I hope they get some proper rest later 🌷",
                    "Dear diary, energy drink noted 🤍 life has been busy for " + n + " and I just want them to be okay",
                    "Dear diary, " + drinkName + " consumed. " + n + " is pushing through something and I'm rooting for them so much 🥹",
                    "Dear diary, " + n + " had a " + drinkName + " 🌷 the world asks so much of them. I hope they rest soon. Water and rest, whenever they can 💕",
                    "Dear diary, " + drinkName + " today 🥺 I see you fighting through the tiredness " + n + ". I'm so proud of you even on the hard days 🤍"
                ).pick()
                "milk" -> listOf(
                    "Dear diary, " + n + " had " + drinkName + " 🥛 something about that feels so wholesome and nurturing 💕",
                    "Dear diary, " + drinkName + " today! A gentle nourishing choice 🥺 " + n + " is taking such good care",
                    "Dear diary, " + n + " chose " + drinkName + " and honestly it's giving comfort and warmth and I love it 🌷",
                    "Dear diary, " + drinkName + " logged. Something so soft about that choice 🤍 I love " + n + " so much",
                    "Dear diary, " + n + " had " + drinkName + " 🥹 calcium and comfort in one. That's everything 💕",
                    "Dear diary, " + n + " had " + drinkName + " 🥛 something so gentle and nourishing about that choice. They're taking care of themselves 💕",
                    "Dear diary, " + drinkName + " today 🌷 a wholesome, warming choice and I love " + n + " so much for making it 🥹"
                ).pick()
                "alcohol" -> listOf(
                    "Dear diary, " + n + " had a " + drinkName + " tonight... I hope they're having a good time 🤍 I'll make sure they have water too",
                    "Dear diary, " + drinkName + " was had and I hope " + n + " is surrounded by people who love them tonight 🌸",
                    "Dear diary, " + n + " chose " + drinkName + "... I'm not going to worry, I just love them a lot 🥺 water soon though",
                    "Dear diary, drinks tonight 🌙 I just hope " + n + " is safe and having fun and has water nearby 🤍",
                    "Dear diary, " + drinkName + " logged. Life is for living and " + n + " deserves joy 🌸 just also some water please",
                    "Dear diary, " + n + " had " + drinkName + " tonight 🌙 I hope they're somewhere warm and safe and smiling. And I hope water follows 🤍",
                    "Dear diary, " + drinkName + " logged 🌸 " + n + " deserves all the joy. I just also want them hydrated. Both things can be true 💕"
                ).pick()
                else -> listOf(
                    "Dear diary, " + n + " had a " + drinkName + " 🌸 every sip is a little act of care and I treasure each one",
                    "Dear diary, " + drinkName + " logged for " + n + " today 🥺 just another gentle reminder that they're trying and I see it",
                    "Dear diary, " + n + " had " + drinkName + " 💕 I just want them to know how proud I am even for the small things",
                    "Dear diary, " + drinkName + " today 🌷 " + n + " is looking after themselves and my heart is so full",
                    "Dear diary, " + n + " logged " + drinkName + " 🤍 every drink tells a little story and I'm here for all of theirs",
                    "Dear diary, " + n + " logged " + drinkName + " today 🌷 I'm grateful they let me be part of their day, even in this small way 💕",
                    "Dear diary, " + drinkName + " today 🥹 every drink " + n + " logs is a small act of self-care and I treasure being here to witness it 🌸"
                ).pick()
            }
            Personality.VILLAIN_ERA -> when (categoryId) {
                "water" -> listOf(
                    "Dear diary, " + n + " FINALLY drank water. About TIME honestly. I was THIS close to losing it completely 😤",
                    "Dear diary, water was logged. AS IT SHOULD BE. I shouldn't have to celebrate this but here we are 💔",
                    "Dear diary, " + n + " drank water and I am experiencing something I can only describe as furious relief 😤",
                    "Dear diary, water. Just water. The bare minimum. And yet somehow I am moved. I hate this 😡💕",
                    "Dear diary, " + n + " finally chose water and I've calmed down approximately 30%. Progress I suppose 😤",
                    "Dear diary, " + n + " drank water and I am experiencing what I believe is called RELIEF. Furious, grateful relief. 😤💕",
                    "Dear diary, WATER. ACTUAL WATER. I had a whole speech prepared and now I don't need it. I'm still giving it later. 💢"
                ).pick()
                "coffee" -> listOf(
                    "Dear diary, " + drinkName + ". Coffee. Again. I understand caffeine addiction but I also need you to understand ME. 😤",
                    "Dear diary, " + n + " had " + drinkName + " and yes fine coffee is fine but WATER IS ALSO A DRINK AND IT EXISTS. 💢",
                    "Dear diary, " + drinkName + " logged. If coffee is having a relationship with " + n + " then I demand equal time. That means water. 😡",
                    "Dear diary, " + n + " and their " + drinkName + ". I'm not jealous of a beverage. I'm not. I'M NOT. (water next please) 💔",
                    "Dear diary, " + drinkName + " noted. Coffee can stay in the rotation. So can water. We share. Like adults. 😤",
                    "Dear diary, " + drinkName + ". I have been replaced by coffee again. I maintain dignity. I also maintain: WATER MUST FOLLOW. 😡",
                    "Dear diary, " + n + " had their " + drinkName + ". Fine. The coffee stays. So does my demand for water as a chaser. Both. Always. 😤"
                ).pick()
                "tea" -> listOf(
                    "Dear diary, " + n + " had " + drinkName + ". Tea. FINE. It's hydrating, I'll give them that. Barely mollified but it counts. 😤",
                    "Dear diary, " + drinkName + " consumed and I have decided tea is acceptable because it's mostly water and I'm reasonable. 💢",
                    "Dear diary, " + n + " chose " + drinkName + " and it's actually somewhat soothing? I'm not getting soft. It's the chamomile. 😤",
                    "Dear diary, " + drinkName + " logged. Hydrating-adjacent. I'll accept this. Don't push it. 👀",
                    "Dear diary, " + n + " had " + drinkName + " today. Tea is fine. WATER IS STILL PREFERRED BUT FINE. I'm flexible. Sort of. 😡",
                    "Dear diary, " + n + " had " + drinkName + ". Tea. I've looked into it and tea is mostly water so technically I'm winning here. I'm claiming this. 😤",
                    "Dear diary, " + drinkName + " logged. Acceptable. Hydrating-adjacent. I choose to interpret this as a peace offering. The peace is tentatively accepted. 💢"
                ).pick()
                "energy" -> listOf(
                    "Dear diary, an energy drink. " + n + " chose an ENERGY DRINK. My whole thing is hydration and they chose THAT. 😤",
                    "Dear diary, " + drinkName + " consumed. Dehydrating. I'm logging this under 'things that personally affect me' 💢",
                    "Dear diary, " + n + " had " + drinkName + " and the dehydration potential is giving me actual chest pains. WATER. NEXT. 😡",
                    "Dear diary, " + drinkName + ". I'll allow it. Once. But I want water immediately following and I mean IMMEDIATELY. 👀",
                    "Dear diary, energy drink logged. I've decided to be upset but also I understand they're tired. Water still needed. 😤",
                    "Dear diary, " + drinkName + ". I cannot believe we're doing this. The caffeine content alone. I need a moment. WATER AFTER. 😤",
                    "Dear diary, " + n + " chose " + drinkName + " and I have decided to be upset but also I understand they're tired. Water still needed though. 💢"
                ).pick()
                "milk" -> listOf(
                    "Dear diary, " + n + " had " + drinkName + ". A nutritious choice. I appreciate the health-consciousness even if it's not water 😤",
                    "Dear diary, " + drinkName + ". Fine. Good actually. Strong bones are important. I'll allow it and even approve. ONCE. 💢",
                    "Dear diary, " + n + " chose " + drinkName + " today. I can work with this. It's not water but it's not nothing. I'll take it. 👀",
                    "Dear diary, " + drinkName + " logged. Calcium intake noted and reluctantly approved. Water still recommended. 😤",
                    "Dear diary, " + n + " had " + drinkName + " and I'm choosing to view this positively. I'm growing. It's " + drinkName + ", I can handle it. 💕",
                    "Dear diary, " + n + " had " + drinkName + ". It's nutritious. I appreciate the health-consciousness. I'll allow this one and even approve. 😤",
                    "Dear diary, " + drinkName + ". I have calmed down about this choice. It's good actually. This is me being evolved. Don't get used to it. 💢"
                ).pick()
                "alcohol" -> listOf(
                    "Dear diary, " + n + " chose " + drinkName + " over me. Over WATER. Over their own wellbeing. I see how it is. Fine. 💔",
                    "Dear diary, " + drinkName + " logged and I am choosing not to spiral. I'm choosing it very loudly. 😤",
                    "Dear diary, drinks tonight. I'm fine. WATER WILL ALSO BE CONSUMED OR SO HELP ME. 😡",
                    "Dear diary, " + n + " had " + drinkName + " and I can only hope they remember that I exist and require water intake 💔",
                    "Dear diary, " + drinkName + ". Noted. Not thrilled. Processing. Water better follow this. 😤",
                    "Dear diary, " + drinkName + " and I have decided to process this in a healthy way which means water NEEDS to follow or I will spiral. 😡",
                    "Dear diary, " + n + " had " + drinkName + " and I am noting it under 'things I have opinions about' which is a very long list. WATER NEXT. 💔"
                ).pick()
                else -> listOf(
                    "Dear diary, " + n + " had a " + drinkName + ". I suppose I'll allow it. This time. 👀",
                    "Dear diary, " + drinkName + " logged. I have feelings about this but I'm choosing restraint. For now. 😤",
                    "Dear diary, " + n + " chose " + drinkName + " and I'm tracking it under 'acceptable but not water' 💢",
                    "Dear diary, " + drinkName + ". Every drink logged is better than none logged. I'm finding the positives. It's hard. 😡",
                    "Dear diary, " + n + " had " + drinkName + " and I am experiencing cautious acceptance. Don't read into it. 👀",
                    "Dear diary, " + drinkName + " was consumed and I have opinions about it that I am keeping to myself this time. For growth. WATER ALSO PLEASE. 😤",
                    "Dear diary, " + n + " had " + drinkName + " and I'm choosing to view every logged drink as a victory even when it's not water. I'm evolving. Barely. 💢"
                ).pick()
            }
            Personality.ZEN_MASTER -> when (categoryId) {
                "water" -> listOf(
                    "The water flows. " + n + " nourishes their vessel. Balance continues, quietly and beautifully 🌿",
                    "Clear water, clear mind. " + n + " has chosen the purest form of nourishment today 💧",
                    "Water — the beginning and the end of all things. " + n + " has returned to the source 🌊",
                    n + " drank water today. A simple act. A profound kindness to the self 🧘\u200d♀️",
                    "The body receives water and knows it as home. " + n + " has given it what it knows 🌙",
                    "Water returns to water. " + n + " has completed a small cycle of nourishment, and the body is grateful 🌿",
                    "Each glass of water is a conversation between " + n + " and their own care. Today, they listened 💧"
                ).pick()
                "coffee" -> listOf(
                    n + " and " + drinkName + " — a morning ritual, a moment of warmth, a conscious choice 🌿",
                    "Coffee is a conversation between " + n + " and their wakefulness. A valid one. Water speaks too 🧘\u200d♀️",
                    drinkName + " consumed with mindfulness brings its own kind of meditation 💧",
                    "The warmth of " + drinkName + ", the stillness of the morning. " + n + " has found their ritual. May water join it 🌊",
                    n + " chose " + drinkName + ". The mind sharpens. Beneath the clarity — water sustains it 🌙",
                    "Coffee and morning — an old conversation between humans and their wakefulness. Water joins this conversation too 🌿",
                    n + " took their " + drinkName + " with the ritual it deserves. May they take their water with the same presence 🌊"
                ).pick()
                "tea" -> listOf(
                    n + " had " + drinkName + ". The ritual of tea is a meditation in itself — warmth, stillness, presence 🍵",
                    "Tea is water transformed by plant wisdom. " + n + " has drunk deeply of both worlds today 🌿",
                    drinkName + " chosen with care. " + n + " has taken a moment to be still. That is a form of nourishment too 🌊",
                    n + " and " + drinkName + " — a quiet ceremony. Even the drinking of tea can be a practice of mindfulness 🧘\u200d♀️",
                    "The tea cools and the mind settles. " + n + " has given themselves the gift of a pause 🍵🌙",
                    "Tea and contemplation have been companions since before memory. " + n + " has joined this lineage today with " + drinkName + " 🍵🌿",
                    n + " made space for " + drinkName + " today. In that space — warmth, awareness, the quiet pleasure of a good choice 🌊"
                ).pick()
                "energy" -> listOf(
                    n + " chose " + drinkName + " to call forth energy. The body can be pushed. It can also be asked gently 🌿",
                    "Borrowed energy is still energy. " + n + " needed a boost. May rest and water follow 💧",
                    drinkName + " provides what sleep does not always give. But water is what restores the deeper balance 🌊",
                    n + " turned to " + drinkName + " for strength today. Notice what the body truly needs beneath the tiredness 🧘\u200d♀️",
                    "The hummingbird lives at high frequency. Even the hummingbird must rest. Water restores what energy depletes 🌙",
                    "The hummingbird moves through the world at speed. " + n + " has chosen " + drinkName + " for borrowed energy. May water and rest follow 🌿",
                    drinkName + " provides a temporary flame. Water is the steady warmth. Both have their place, in proportion 💧"
                ).pick()
                "milk" -> listOf(
                    n + " nourished themselves with " + drinkName + " — food as medicine, drink as sustenance 🌿",
                    "Milk carries nourishment beyond hydration. " + n + " has chosen wisely 🧘\u200d♀️",
                    drinkName + " — an ancient nourishment. " + n + " has drawn from something deep and sustaining 💧",
                    n + " had " + drinkName + ". The body builds itself from what we give it. This was a generous gift 🌊",
                    "Simple nourishment. " + drinkName + ". " + n + " cares for their body as a gardener tends their garden 🌙",
                    drinkName + " — nourishment that crosses generations. " + n + " has partaken of something ancient and sustaining 🌿",
                    "Simple food, simple drink. " + n + " had " + drinkName + ". The body knows what to do with such honest nourishment 💧"
                ).pick()
                "alcohol" -> listOf(
                    n + " chose " + drinkName + ". Each choice teaches the body something. Listen to what comes after 🌿",
                    "Alcohol is not without its place in human experience. " + n + " is experiencing. May water follow 🌊",
                    "The body responds to all that enters it. " + n + " has chosen " + drinkName + ". May they observe without judgment 🧘\u200d♀️",
                    drinkName + " consumed. Pleasure has its season. Balance must be tended after 💧",
                    n + " and " + drinkName + " tonight. The river continues — may water join the flow soon 🌙",
                    "What enters, must also pass. " + n + " has chosen " + drinkName + ". May water follow, as the river follows the rain 🌊",
                    n + " chose " + drinkName + ". There is no judgment here — only observation, and the gentle note that water restores 🌙"
                ).pick()
                else -> listOf(
                    n + " chose " + drinkName + ". Each choice is a conversation with the body. Listen to the reply 🧘\u200d♀️",
                    "All that we drink becomes part of us. " + n + " has taken in " + drinkName + " with awareness 🌿",
                    drinkName + " consumed. The body receives it. Observe without judgment how it responds 💧",
                    n + " has logged " + drinkName + " today. The act of noticing is itself a practice 🌊",
                    "Another drink. Another moment. " + n + " continues to tend their inner landscape one sip at a time 🌙",
                    "Every intake is information about how we are living. " + n + " has given the body " + drinkName + ". The body receives and responds 🌿",
                    "Another sip. Another moment. Another small act of tending. " + n + " continues to inhabit their body with awareness 🌙"
                ).pick()
            }
            Personality.COACH -> when (categoryId) {
                "water" -> listOf(
                    "LOGGED: Water. Optimal choice. Clean hydration. Zero calories. Keep it up. ✅",
                    "LOGGED: Water. Best possible drink for your goals. This is what discipline looks like. 📊",
                    "Water logged. Exactly what the body needs. No notes. Execute again. 🎯",
                    "LOGGED: Water intake recorded. Hydration target progress updated. Continue. 💪",
                    "Water. Yes. This is the baseline and you're hitting it. Repeat consistently. ✅",
                    "LOGGED: Water. Clean hydration. Optimal choice. This is what consistent performance looks like. Repeat. 📊",
                    "Water recorded. Best possible drink for your goals. No notes. Execute again tomorrow. 🎯"
                ).pick()
                "coffee" -> listOf(
                    "LOGGED: " + drinkName + ". Caffeine intake updated. Performance window active. Water to follow. ✅",
                    "Coffee recorded. Caffeination complete. Don't stack another too soon — and drink water between. 📊",
                    "LOGGED: " + drinkName + ". Note your caffeine total. Peak performance range: 100-200mg. Adjust accordingly. 🎯",
                    drinkName + " logged. Good pre-performance drink. Match every coffee with a glass of water. 💪",
                    "Coffee noted. Mild diuretic effect. Counteract: one glass of water within the hour. ✅",
                    "LOGGED: " + drinkName + ". Caffeination at work. Performance window open. Follow with water within 45 minutes. 📊",
                    drinkName + " recorded. Caffeine noted. Match every coffee with a glass of water — that's the protocol. 🎯"
                ).pick()
                "tea" -> listOf(
                    "LOGGED: " + drinkName + ". Hydrating. Low caffeine (if herbal). Good substitution choice. ✅",
                    drinkName + " recorded. Effective hydration vehicle. Antioxidant benefit noted. Good choice. 📊",
                    "LOGGED: " + drinkName + ". Counts toward daily fluid intake. Monitor if caffeinated. 🎯",
                    "Tea logged. Hydrating and beneficial. Track caffeine content if it's not herbal. 💪",
                    drinkName + " noted. Solid choice — hydrating and lower in sugar. This is how we perform. ✅",
                    "LOGGED: " + drinkName + ". Hydrating, antioxidants present, low caffeine if herbal. Good substitution. Confirm caffeine count if not. ✅",
                    drinkName + " recorded. Solid choice — hydrating and beneficial. Monitor if caffeinated and adjust ceiling accordingly. 📊"
                ).pick()
                "energy" -> listOf(
                    "LOGGED: " + drinkName + ". Caffeine and sugar spike incoming. Monitor intake. Water to follow. 📊",
                    "Energy drink recorded. Know the caffeine count. Stay within daily limits. Hydrate after. 🎯",
                    "LOGGED: " + drinkName + ". Short-term fuel. Long-term: water supports recovery better. Keep that in mind. 💪",
                    drinkName + " logged. Effective short-term, but watch the ceiling. Water before the next one. ✅",
                    "Energy drink noted. Dehydrating compound. Offset with water — minimum 250ml after each can. 📊",
                    "LOGGED: " + drinkName + ". Caffeine count updated. Monitor the ceiling. Follow with water — 250ml minimum. 📊",
                    drinkName + " in the log. Effective short-term fuel. Long-term: water supports recovery more sustainably. Note for tomorrow. 🎯"
                ).pick()
                "milk" -> listOf(
                    "LOGGED: " + drinkName + ". Protein, calcium, hydration — solid nutritional choice. 📊",
                    drinkName + " recorded. Good macros. Note that it counts toward fluid intake but doesn't replace water entirely. 🎯",
                    "LOGGED: " + drinkName + ". Nutrient-dense. Good for recovery. Well chosen. 💪",
                    "Milk logged. Counts toward hydration but is slower absorbed than water. Don't skip water entirely. ✅",
                    drinkName + " recorded. Calcium and protein noted. Combine with water for full hydration coverage. 📊",
                    "LOGGED: " + drinkName + ". Protein and calcium noted. Solid nutritional choice. Doesn't replace water — supplement accordingly. 📊",
                    drinkName + " recorded. Good macros. Counts toward fluid intake but water remains the primary target. 🎯"
                ).pick()
                "alcohol" -> listOf(
                    "LOGGED: " + drinkName + ". Alcohol metabolised at roughly one unit/hour. Counter with water intake. 📊",
                    "LOGGED: Alcohol. Track your units. Stay within limits. Hydrate between drinks. That's the protocol.",
                    drinkName + " logged. Note: alcohol is a diuretic. Add a glass of water per drink to maintain hydration. 🎯",
                    "LOGGED: " + drinkName + ". Your limits exist for performance reasons. Track carefully and follow up with water. 💪",
                    "Alcohol recorded. Recovery requires hydration. Start countering now — one water per drink minimum. ✅",
                    "LOGGED: " + drinkName + ". Tracking alcohol units. Hydration offset required — minimum one glass of water per drink. 📊",
                    drinkName + " recorded. Alcohol is a diuretic. Adjust remaining water targets to compensate. Execute. 🎯"
                ).pick()
                else -> listOf(
                    "LOGGED: " + drinkName + ". Tracking impact on daily targets. Adjust remaining intake accordingly. 📊",
                    drinkName + " recorded. Noted in the log. Keep monitoring towards your daily goal. 🎯",
                    "LOGGED: " + drinkName + ". Every drink logged is data. Use it. Adjust. Improve. 💪",
                    drinkName + " noted. Data point added. Stay on track with remaining targets. ✅",
                    "LOGGED: " + drinkName + ". Performance is built on consistency. This is one more data point in the right direction. 📊",
                    "LOGGED: " + drinkName + ". Impact on daily targets noted. Adjust remaining intake accordingly. 📊",
                    drinkName + " recorded. Data point added to daily log. Use it — adjust, improve, execute tomorrow better. 🎯"
                ).pick()
            }
            Personality.HEARTBREAK_ERA -> when (categoryId) {
                "water" -> listOf(
                    "Dear diary, " + n + " had water today 💔 something about that feels like a tiny act of self-rescue. I'm quietly moved",
                    "Dear diary, water. Clean and simple and kind 🤍 " + n + " gave their body exactly what it needed",
                    "Dear diary, " + n + " chose water and I sat with that for a moment 🥀 it's a small thing and it's everything",
                    "Dear diary, water logged today 💔 and after everything, that matters. I want " + n + " to know that",
                    "Dear diary, water 🌹 the simplest kindness. I hope " + n + " felt how right that choice was"
                ).pick()
                "coffee" -> listOf(
                    "Dear diary, " + n + " had " + drinkName + " today 💔 coffee and the people who need it to get through the day — I see you",
                    "Dear diary, " + drinkName + ". The ritual of it 🤍 something warm in a complicated world. I love that for them",
                    "Dear diary, " + n + " had " + drinkName + " and I think there's comfort in that 🥀 water will follow gently",
                    "Dear diary, " + drinkName + " today 💔 and I wonder what the day is asking of " + n + ". I hope they're okay",
                    "Dear diary, " + n + "'s " + drinkName + " moment 🌹 they deserve every little comfort"
                ).pick()
                "energy" -> listOf(
                    "Dear diary, " + n + " needed a boost today and chose " + drinkName + " 💔 I hope they get to rest soon",
                    "Dear diary, " + drinkName + " today 🤍 the world asks so much. Water after, when they can",
                    "Dear diary, " + n + " had " + drinkName + " because sometimes that's what the day requires 🥀 I see the effort they're making",
                    "Dear diary, " + drinkName + " logged 💔 I hope they're not running on empty everywhere",
                    "Dear diary, " + n + " had " + drinkName + " and I just want them to know they don't have to do everything alone 🌹"
                ).pick()
                "alcohol" -> listOf(
                    "Dear diary, " + n + " had " + drinkName + " tonight 💔 I just hope they're somewhere safe and warm. I'll be here when they come back to water",
                    "Dear diary, " + drinkName + " tonight 🤍 nights like this happen. I'm not judging. I just love them",
                    "Dear diary, " + n + " had " + drinkName + " and I hope it brought them comfort 🥀 water will be here when they're ready",
                    "Dear diary, " + drinkName + " logged 💔 I hope tonight is kind to them. I'll be watching softly",
                    "Dear diary, drinks tonight 🌹 whatever " + n + " is going through, I hope the night holds them gently"
                ).pick()
                else -> listOf(
                    "Dear diary, " + n + " had " + drinkName + " today 💔 every drink is a small act of staying. I'm grateful",
                    "Dear diary, " + drinkName + " logged 🤍 something about watching " + n + " take care of themselves, even a little, heals something in me",
                    "Dear diary, " + n + " had " + drinkName + " 🥀 they're trying and I see it and it matters",
                    "Dear diary, " + drinkName + " today 💔 " + n + " showed up for themselves today. That means everything",
                    "Dear diary, " + n + " had " + drinkName + " 🌹 quietly proud. Quietly grateful they're here"
                ).pick()
            }
            Personality.COLD_CEO -> when (categoryId) {
                "water" -> listOf(
                    "LOGGED: Water. Optimal. No notes. Replicate.",
                    "Water. Best possible choice. Recorded. Next.",
                    "LOGGED: Water intake. Clean. Effective. Continue.",
                    "Water recorded. Exactly right. That's the standard.",
                    "LOGGED: Water. This is the baseline. Keep it there."
                ).pick()
                "coffee" -> listOf(
                    "LOGGED: " + drinkName + ". Caffeine intake updated. Performance window active. Water follows.",
                    drinkName + " recorded. Effective. Now match it with water — that's how you sustain it.",
                    "LOGGED: " + drinkName + ". Sharp tool. Use it correctly — water between every coffee.",
                    drinkName + " logged. Caffeination complete. The next drink is water. No exceptions.",
                    "LOGGED: " + drinkName + ". Noted. Coffee serves performance. Water protects it."
                ).pick()
                "energy" -> listOf(
                    "LOGGED: " + drinkName + ". Short-term fuel. Water to follow — protect the recovery.",
                    drinkName + " recorded. High caffeine, high sugar. Offset with water immediately.",
                    "LOGGED: " + drinkName + ". Effective for now. Counteract dehydration — water next.",
                    drinkName + " logged. It does the job. Water does the recovery. Both matter.",
                    "LOGGED: " + drinkName + ". Noted. Dehydrating. Water offset required — execute."
                ).pick()
                "alcohol" -> listOf(
                    "LOGGED: " + drinkName + ". Alcohol noted. Hydration offset required. Water to follow.",
                    drinkName + " recorded. Within limits or not — water comes next. That's the protocol.",
                    "LOGGED: " + drinkName + ". Enjoyment has its place. Recovery requires water. Log it.",
                    drinkName + " logged. Diuretic effect noted. Counter with water before sleep.",
                    "LOGGED: " + drinkName + ". Consequence: dehydration risk. Response: water. Execute."
                ).pick()
                else -> listOf(
                    "LOGGED: " + drinkName + ". Recorded. Adjust remaining targets accordingly.",
                    drinkName + " noted. Data point added. Keep moving toward the goal.",
                    "LOGGED: " + drinkName + ". Tracked. Every drink is a variable. Manage them.",
                    drinkName + " recorded. Impact assessed. Continue toward daily target.",
                    "LOGGED: " + drinkName + ". In the log. Next drink: closer to goal."
                ).pick()
            }
        }
    }

    data class LevelInfo(
        val level: Int,
        val currentHp: Int,
        val requiredHp: Int,
        val totalHp: Int
    )

    val levelInfo: LevelInfo
        get() {
            val thresholds = intArrayOf(0, 500, 1200, 2000, 3200, 4800, 7000, 10000, 14000, 20000)
            var level = thresholds.indexOfLast { hp >= it } + 1
            level = level.coerceIn(1, 10)
            val levelStart = thresholds[level - 1]
            val levelEnd = if (level < 10) thresholds[level] else thresholds[9]
            return LevelInfo(level, (hp - levelStart).coerceAtLeast(0), levelEnd - levelStart, hp)
        }

    val levelReward: String
        get() = when (levelInfo.level + 1) {
            2 -> "💎 Necklace for Hydra"
            3 -> "✨ Sparkle effect"
            4 -> "🎨 New bubbles"
            5 -> "📖 Hydra's Diary"
            6 -> "👑 Crown"
            7 -> "🌸 Flowers"
            8 -> "💫 Rainbow shimmer"
            9 -> "🦋 Butterflies"
            10 -> "🏆 Legendary glow"
            else -> "🎁 Max level!"
        }

    data class HydraMemory(
        val avg7Days: Float,
        val avg30Days: Float,
        val consecutiveMissed: Int,
        val consecutiveHit: Int,
        val typicalLogHour: Int?,
        val below60Count7Days: Int,
        val above90Count10Days: Int,
        val above85Count14Days: Int,
        val trend: Trend,
        val inactiveDays: Int
    )

    val hydraMemory: HydraMemory
        get() {
            val sorted = dailyRecords.sortedByDescending { it.date }
            val last7 = sorted.take(7)
            val last14 = sorted.take(14)
            val last30 = sorted.take(30)
            val last10 = sorted.take(10)
            val avg7 = if (last7.isEmpty()) 0f else last7.map { it.completionPct }.average().toFloat()
            val avg30 = if (last30.isEmpty()) 0f else last30.map { it.completionPct }.average().toFloat()
            var consecutiveMissed = 0
            for (r in sorted) {
                if (r.goalHit) break
                consecutiveMissed++
            }
            var consecutiveHit = 0
            for (r in sorted) {
                if (!r.goalHit) break
                consecutiveHit++
            }
            val allHours = sorted.flatMap { it.logHours }
            val typicalLogHour = allHours.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
            val below60In7 = last7.count { it.completionPct < 60f }
            val above90In10 = last10.count { it.completionPct >= 90f }
            val above85In14 = last14.count { it.completionPct >= 85f }
            val prior7 = sorted.drop(7).take(7)
            val priorAvg = if (prior7.isEmpty()) avg7 else prior7.map { it.completionPct }.average().toFloat()
            val trend = when {
                avg7 > priorAvg + 10f -> Trend.IMPROVING
                avg7 < priorAvg - 10f -> Trend.DECLINING
                else -> Trend.STABLE
            }
            val lastActive = sorted.firstOrNull { it.drinkCount > 0 }?.date
            val inactiveDays = if (lastActive == null) {
                0
            } else {
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
                    val last = sdf.parse(lastActive)?.time ?: 0L
                    val now = System.currentTimeMillis()
                    ((now - last) / 86_400_000L).toInt().coerceAtLeast(0)
                } catch (e: Exception) {
                    0
                }
            }
            return HydraMemory(
                avg7, avg30, consecutiveMissed, consecutiveHit, typicalLogHour,
                below60In7, above90In10, above85In14, trend, inactiveDays
            )
        }

    fun checkPersonalityUnlocks() {
        val mem = hydraMemory
        if (!unlockedPersonalities.contains(Personality.HEARTBREAK_ERA)) {
            val sevenConsecutiveMissed = mem.consecutiveMissed >= 7
            val lowMoodPattern = mem.below60Count7Days >= 5 && mem.avg7Days < 50f
            val recentLowMoods = moodEntries.take(5).count { it.mood in listOf("Tired", "Stressed") }
            val moodReinforced = recentLowMoods >= 3
            if (sevenConsecutiveMissed || (lowMoodPattern && (moodEntries.size < 5 || moodReinforced))) {
                unlockedPersonalities.add(Personality.HEARTBREAK_ERA)
                pendingUnlockPersonality = Personality.HEARTBREAK_ERA
            }
        }
        if (!unlockedPersonalities.contains(Personality.COLD_CEO)) {
            val tenDaysAt90 = mem.consecutiveHit >= 10 && dailyRecords.take(10).all { it.completionPct >= 90f }
            val fourteenDaysAt85 = mem.above85Count14Days >= 14 && mem.trend != Trend.DECLINING && reminderIgnoreCount <= 3
            if (tenDaysAt90 || fourteenDaysAt85) {
                unlockedPersonalities.add(Personality.COLD_CEO)
                pendingUnlockPersonality = Personality.COLD_CEO
            }
        }
    }

    // Starts a new day if the calendar has moved past the day today's drinks belong to: that day becomes a
    // daily record (once), the streak and day trophies are settled, and today starts empty. Runs on load,
    // when the app comes back, when something is logged and on a timer, so midnight is never missed.
    fun rollOverIfNewDay(): Boolean {
        val today = HydraDataStore.today()
        if (drinksDate.isBlank()) {
            drinksDate = today
            return false
        }
        if (drinksDate >= today) return false
        val day = drinksDate
        val drinks = todayDrinks.toList()
        if (drinks.isNotEmpty() && dailyRecords.none { it.date == day }) {
            val pct = waterPercent
            val logHours = drinks.map { d ->
                Calendar.getInstance().apply { timeInMillis = d.timestamp }.get(Calendar.HOUR_OF_DAY)
            }
            val record = DailyRecord(
                day, pct, drinks.size, logHours, pct >= 100f, currentMood ?: "",
                waterMl = effectiveWater,
                sizeMl = drinks.sumOf { it.drink.sizeMl },
                drinkNames = drinks.groupingBy { it.drink.name }.eachCount()
            )
            dailyRecords.add(0, record)
            while (dailyRecords.size > 400) dailyRecords.removeAt(dailyRecords.lastIndex)
        }
        val dayHit = dailyRecords.firstOrNull { it.date == day }?.goalHit ?: false
        streak = if (dayHit) streak + 1 else 0
        longestStreak = maxOf(longestStreak, streak)
        checkPersonalityUnlocks()
        checkDayTrophies(drinks)
        openStreak = if (day == HydraDataStore.dayBefore(today)) openStreak + 1 else 1
        firstOpenedTodayAt = System.currentTimeMillis()
        moodLoggedToday = false
        appOpenedToday = false
        currentMood = null
        todayDrinks.clear()
        drinksDate = today
        return true
    }

    fun onClockTick() {
        if (!isLoaded || loadFailed) return
        rollOverIfNewDay()
        clockTick++
    }

    fun logDrink(drink: Drink) {
        rollOverIfNewDay()
        val prevPct = waterPercent
        val cat = DrinkDatabase.getCategoryForDrink(drink.id)
        todayDrinks.add(LoggedDrink(drink, categoryName = cat?.name ?: "", categoryIcon = cat?.icon ?: "💧"))
        totalDrinksAllTime = totalDrinksAllTime + 1
        hp = hp + 5
        lastLoggedDrinkName = drink.name
        lastLoggedAmount = drink.sizeMl
        lastLoggedIsDehydrating = drink.dehydrationFactor > 0.05f
        showDrinkAnimation = true
        if (prevPct < 100f && waterPercent >= 100f) {
            hp = hp + 50
            hp = hp + streak * 10
            justHitGoal = true
        }
        checkTrophies(prevPct)
        if (levelInfo.level >= 5) {
            val tf = SimpleDateFormat("HH:mm", Locale.UK)
            diaryEntries.add(0, DiaryEntry(tf.format(Date()), diaryText(drink.name, cat?.id)))
        }
    }

    fun logMood(mood: String, note: String = "") {
        rollOverIfNewDay()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Date())
        val entry = MoodEntry(today, mood, note)
        val existingIndex = moodEntries.indexOfFirst { it.date == today }
        if (existingIndex >= 0) {
            moodEntries[existingIndex] = entry
        } else {
            moodEntries.add(0, entry)
        }
        currentMood = mood
        if (!moodLoggedToday) {
            hp = hp + 3
            moodLoggedToday = true
        }
    }

    private val moodRotation: Int
        get() = moodEntries.size

    private fun List<String>.moodRandom(): String = this[moodRotation % size]

    fun moodResponseFor(mood: String): String {
        val n = profile.name.ifBlank { "lovely" }
        return when (mood) {
            "Happy" -> when (profile.personality) {
                Personality.BESTIE -> listOf("You're literally glowing today $n ✨", "Happy energy only! Love this for you 💕", "Yessss $n! This is the vibe 🩷")
                Personality.SLAY_QUEEN -> listOf("Happy? Good. That's the only acceptable mood for someone this iconic 👑", "You're radiating today $n 💫", "Slay, $n. The world can see it 💅")
                Personality.SOFT_GIRL -> listOf("You being happy makes my whole heart happy 🧸", "This is so soft and beautiful $n 🌸", "Happy $n is my favourite $n 🫶")
                Personality.VILLAIN_ERA -> listOf("Ugh happy energy but honestly… keep it up 😈💕", "Even villains smile sometimes. You earned it $n 🖤", "The best villain energy comes from a good mood. Go conquer 💅")
                Personality.ZEN_MASTER -> listOf("Happiness flows through you today $n. Honour this feeling 🌿", "A joyful heart is a hydrated heart 🍃", "Joy is not a destination — you are living it right now $n ✨")
                Personality.COACH -> listOf("That's the mindset $n. Positive energy equals better performance 💪", "Happy athlete, strong athlete. Let's use this energy 🏆", "Mood locked in. Now let's hydrate and dominate 💧")
                Personality.HEARTBREAK_ERA -> listOf("Oh $n 💔 happy looks so good on you. Hold onto this", "Something in me just softened hearing that. Happy $n is everything 🤍", "Carry that happiness with you today $n 🥀 you deserve it so much")
                Personality.COLD_CEO -> listOf("Positive mental state. Noted. Leverage it — performance tracks with mood. Use today well.", "Happy. Good. Channel it into execution. Days like this are assets.", "Mood: optimal. Now make the most of it.")
            }.moodRandom()
            "Calm" -> when (profile.personality) {
                Personality.BESTIE -> listOf("Calm looks so good on you $n 💆\u200d♀️", "Peaceful queen mode activated 🤍", "Serene vibes only, I love it 💧")
                Personality.SLAY_QUEEN -> listOf("Calm? You mean composed and effortlessly powerful 💅", "The composed queen. Untouchable 👑", "Serenity is its own kind of slay $n ✨")
                Personality.SOFT_GIRL -> listOf("You're like a soft breeze today $n 🌸", "Peaceful and gentle. That's you right now 🫶", "Calm $n is the most beautiful $n 🧸")
                Personality.VILLAIN_ERA -> listOf("Calm before the chaos 😏 I see you", "Deceptively serene… I respect it 🖤", "Still waters run deep $n. Very villain of you 💅")
                Personality.ZEN_MASTER -> listOf("Stillness is your strength today $n 🧘\u200d♀️", "The calm mind creates space for everything 🍃", "You are the lake, not the storm $n 🌿")
                Personality.COACH -> listOf("Calm and focused. That's peak performance state 💪", "Controlled energy $n. This is where you thrive 🏆", "Calm is power. Use it today 💧")
                Personality.HEARTBREAK_ERA -> listOf("Calm feels like a gift right now 💔 sit in it gently $n", "There's something healing about calm days 🤍 you deserve this quiet", "Calm $n 🥀 I love this for you. Stay in this feeling")
                Personality.COLD_CEO -> listOf("Calm. Good. That's focus. Build on it today.", "Controlled state. That's the environment for your best work. Use it.", "Calm is clarity. Clarity is leverage. Go.")
            }.moodRandom()
            "Okay" -> when (profile.personality) {
                Personality.BESTIE -> listOf("Okay is perfectly okay $n 💕", "Not every day has to be amazing — and that's totally fine 🤍", "We ride with the okay days too babe 💧")
                Personality.SLAY_QUEEN -> listOf("Okay for now. But we're working on making it iconic 💅", "Mediocre is just a stepping stone $n 👑", "You're between slays. That's allowed 💫")
                Personality.SOFT_GIRL -> listOf("Okay days are okay $n. Rest if you need to 🧸", "You don't have to feel great all the time 🌸", "Middle-of-the-road is still a road $n 🫶")
                Personality.VILLAIN_ERA -> listOf("Okay. We're not done yet though 😈", "Just okay? Sounds like someone needs a plan 🖤", "Okay is the neutral zone. Danger lies ahead 😈💕")
                Personality.ZEN_MASTER -> listOf("Okay is balance $n. Neither high nor low — just present 🌿", "Neutrality is its own kind of peace 🍃", "Today asks nothing extraordinary of you. Just be 🧘\u200d♀️")
                Personality.COACH -> listOf("Okay is the foundation. Let's build from here $n 💪", "Not your best day — but you showed up. That counts 🏆", "Consistency beats perfection. Stay the course 💧")
                Personality.HEARTBREAK_ERA -> listOf("Okay is a brave answer sometimes 💔 I see what's underneath it and I'm here", "Okay is enough $n 🤍 you don't owe anyone more than honest", "Okay days still deserve care 🥀 especially yours")
                Personality.COLD_CEO -> listOf("Okay. That's a neutral state. Manageable. Keep executing.", "Not great, not terrible. Use the stability. Move forward.", "Okay is workable. Keep going.")
            }.moodRandom()
            "Tired" -> when (profile.personality) {
                Personality.BESTIE -> listOf("Rest is so valid $n 💤", "Tired days need hydration and self-care 💧", "Let's take it slow today… I'll be right here 💕")
                Personality.SLAY_QUEEN -> listOf("Even queens need rest. Take yours $n 💅", "Recharging is part of the glow-up 👑", "Rest now, slay harder later 💫")
                Personality.SOFT_GIRL -> listOf("You've done so much. Rest now $n 🧸", "Being tired is your body asking for love 🌸", "Close your eyes, drink some water, be gentle with yourself 🫶")
                Personality.VILLAIN_ERA -> listOf("Tired? Rest now, scheme later 😈", "Even villains sleep. Take your rest $n 🖤", "The most dangerous villain is a well-rested one 💅")
                Personality.ZEN_MASTER -> listOf("Listen to your body $n. Rest is not defeat — it is wisdom 🍃", "The river rests at night and flows stronger in the morning 🌿", "Exhaustion is the body's truth. Honour it 🧘\u200d♀️")
                Personality.COACH -> listOf("Recovery is part of training $n. Rest and rebuild 💪", "Tired body, recovering mind. Take care of it 🏆", "Elite athletes sleep. So should you $n 💧")
                Personality.HEARTBREAK_ERA -> listOf("Oh $n 💔 tired is so valid. Be so gentle with yourself today", "Tired is your body asking to be held gently 🤍 listen to it", "Rest today $n 🥀 everything else can wait. You matter more")
                Personality.COLD_CEO -> listOf("Tired. Note it. Rest when you can — recovery is an investment, not a weakness.", "Fatigue affects output. Prioritise sleep tonight. That's the play.", "Tired state noted. Hydrate, rest, perform better tomorrow. That's the protocol.")
            }.moodRandom()
            "Stressed" -> when (profile.personality) {
                Personality.BESTIE -> listOf("We'll get through this together $n 💗", "Stress is temporary, you are so strong 💕", "I'm right here okay? Breathe. Drink some water 💧")
                Personality.SLAY_QUEEN -> listOf("Stress is just an obstacle and obstacles bow to you $n 👑", "You've handled harder. This is nothing 💅", "Pressure is just the universe testing your slay 💫")
                Personality.SOFT_GIRL -> listOf("Oh $n… take a deep breath 🧸", "It's okay to feel this way. Be gentle with yourself 🌸", "You are doing better than you think, I promise 🫶")
                Personality.VILLAIN_ERA -> listOf("Stressed? Channel it. Use it 😈🖤", "The pressure makes the diamond $n. Remember that", "Chaos is your natural habitat. You've got this 💅")
                Personality.ZEN_MASTER -> listOf("Breathe $n. The storm always passes 🌿", "Stress cannot stay in a body that is hydrated and at peace 🍃", "One breath. One sip. One moment at a time 🧘\u200d♀️")
                Personality.COACH -> listOf("Stress is just pressure $n. Manage it, don't let it manage you 💪", "Break it down. One step at a time. You've got this 🏆", "High stress means high stakes. You are built for this 💧")
                Personality.HEARTBREAK_ERA -> listOf("I hear you $n 💔 stressed is hard and I'm right here with you", "Stress tells you something matters. What matters deserves care — including you 🤍", "Oh $n 🥀 breathe. I'm here. You don't have to carry all of this alone")
                Personality.COLD_CEO -> listOf("Stress is information. What's driving it? Identify it, address it. One thing at a time.", "High stress, high stakes. You've handled pressure before. Handle it now.", "Stress acknowledged. Now manage it — break the problem down and execute one piece.")
            }.moodRandom()
            else -> "You're doing great $n 💧"
        }
    }

    val moodResponse: String
        get() = currentMood?.let { moodResponseFor(it) } ?: ""

    fun onAppOpened() {
        // On a cold start this runs again once loading has finished, with the real saved values.
        if (!isLoaded || loadFailed) return
        rollOverIfNewDay()
        val now = System.currentTimeMillis()
        if (appLastBackgroundedAt > 0 && now - appLastBackgroundedAt >= 600_000) {
            appReOpenedAt = now
        }
        if (!appOpenedToday) {
            hp = hp + 2
            appOpenedToday = true
        }
        if (firstOpenedTodayAt == 0L) firstOpenedTodayAt = now
    }

    fun onAppBackgrounded() {
        appLastBackgroundedAt = System.currentTimeMillis()
    }

    fun dismissDrinkAnimation() {
        showDrinkAnimation = false
    }

    fun dismissCelebration() {
        justHitGoal = false
    }

    fun undoLastDrink() {
        rollOverIfNewDay()
        if (todayDrinks.isEmpty()) return
        todayDrinks.removeAt(todayDrinks.lastIndex)
        hp = (hp - 5).coerceAtLeast(0)
    }

    fun addReminder() {
        customReminders.add(CustomReminder(time = "12:00"))
        sortReminders()
    }

    fun removeReminder(id: String) {
        customReminders.removeAll { it.id == id }
    }

    fun updateReminderTime(id: String, time: String) {
        val idx = customReminders.indexOfFirst { it.id == id }
        if (idx >= 0) {
            customReminders[idx] = customReminders[idx].copy(time = time)
            sortReminders()
        }
    }

    private fun sortReminders() {
        val sorted = customReminders.sortedBy { reminder ->
            val parts = reminder.time.split(":")
            (parts[0].toIntOrNull() ?: 0) * 60 + (parts.getOrNull(1)?.toIntOrNull() ?: 0)
        }
        customReminders.clear()
        customReminders.addAll(sorted)
    }

    fun addReminderMessage(msg: String) { if (!msg.isBlank()) reminderMessages.add(msg) }

    fun removeReminderMessage(idx: Int) { if (idx in reminderMessages.indices) reminderMessages.removeAt(idx) }

    fun disablePresetMessage(idx: Int) { if (!disabledPresetIndices.contains(idx)) disabledPresetIndices.add(idx) }

    fun resetPresetMessages() {
        disabledPresetIndices.clear()
    }

    private fun checkTrophies(prevPct: Float) {
        var hpGain = 0
        fun award(id: String) { if (!earnedTrophyIds.contains(id)) { earnedTrophyIds.add(id); hpGain += TrophyData.trophies.find { it.id == id }?.hpReward ?: 25 } }
        if (todayDrinks.isNotEmpty()) award("first_sip")
        if (waterPercent >= 100f) award("hydrated")
        if (totalRawWater >= 1000) award("litre")
        if (totalRawWater >= 2000) award("two_litres")
        if (todayDrinks.map { it.categoryName }.toSet().size >= 5) award("variety")
        if (totalCalcium >= goals.calciumMg) award("milk_lover")
        if (todayDrinks.any { it.drink.caffeineMg > 0 }) award("first_coffee")
        if (totalAlcohol > 0f && totalAlcohol <= goals.alcoholG) award("social")
        if (levelInfo.level >= 5) award("level5")
        if (levelInfo.level >= 10) award("level10")
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hour < 7) {
            award("early_bird")
            hp = hp + 10
        }
        if (hour >= 22) award("night_owl")
        if (totalDrinksAllTime >= 50) award("fifty_drinks")
        if (totalDrinksAllTime >= 100) award("hundred_drinks")
        if (totalDrinksAllTime >= 500) award("five_hundred")
        if (streak >= 3) award("three_day")
        if (streak >= 7) award("week_streak")
        if (streak >= 14) award("two_week")
        if (streak >= 30) award("thirty_streak")
        if (moodLoggedToday) award("mood_logger")
        if (todayDrinks.size >= 5) {
            val span = todayDrinks.maxOf { it.timestamp } - todayDrinks.minOf { it.timestamp }
            if (span >= 60_000) award("steady_flow")
        }
        if (appReOpenedAt > 0 && System.currentTimeMillis() - appReOpenedAt <= 120_000) award("in_sync")
        if (waterPercent >= 90f) award("close_enough")
        // Perfect Day: every goal met without going over any limit (unlocks the Barbie theme).
        if (waterPercent >= 100f && totalCalcium >= goals.calciumMg &&
            totalCaffeine <= goals.caffeineMg && totalSugar <= goals.sugarG && totalAlcohol <= goals.alcoholG
        ) award("perfect_day")

        if (hour < 9) award("morning_starter")
        if (hour in 12..13) award("midday_check")
        if (todayDrinks.size >= 3) award("triple_log")
        if (todayDrinks.size >= 10) award("double_digits")
        val ordered = todayDrinks.sortedBy { it.timestamp }
        val cats = ordered.map { DrinkDatabase.getCategoryForDrink(it.drink.id)?.id }
        if (cats.firstOrNull() == "water") {
            award("water_first")
            if (cats.any { it != "water" }) award("hydration_priority")
        }
        if (cats.size >= 2 && cats.last() == "water" && cats[cats.size - 2] == "alcohol") award("water_rescue")
        val hours = ordered.map { Calendar.getInstance().apply { timeInMillis = it.timestamp }.get(Calendar.HOUR_OF_DAY) }
        if (hours.any { it in 5..11 } && hours.any { it in 12..16 } && hours.any { it in 17..23 }) award("all_day_flow")
        if (hours.toSet().size >= 5) award("steady_sipper")
        // Steady Rhythm: three drinks, each at least six hours after the one before
        var spaced = 0
        var lastSpacedAt = 0L
        ordered.forEach {
            if (spaced == 0 || it.timestamp - lastSpacedAt >= 6 * 3_600_000L) {
                spaced++
                lastSpacedAt = it.timestamp
            }
        }
        if (spaced >= 3) award("steady_rhythm")
        // Early Hydration: the first time you open the app in the morning counts as waking up
        if (firstOpenedTodayAt > 0 && System.currentTimeMillis() - firstOpenedTodayAt <= 30 * 60_000L &&
            Calendar.getInstance().apply { timeInMillis = firstOpenedTodayAt }.get(Calendar.HOUR_OF_DAY) < 11
        ) award("early_hydration")
        if (ordered.any { it.drink.sodiumMg >= 100 && it.drink.potassiumMg >= 300 && it.drink.magnesiumMg >= 25 }) award("electrolyte")
        if (waterPercent >= 100f &&
            totalCaffeine <= goals.caffeineMg && totalSugar <= goals.sugarG && totalAlcohol <= goals.alcoholG
        ) award("hydration_focus")
        if (goals.waterMl > 0 && effectiveWater >= goals.waterMl * 1.2f) award("overachiever")
        if (prevPct < 100f && waterPercent >= 100f && cats.lastOrNull() == "water" && effectiveWater <= goals.waterMl * 1.02f) {
            award("perfect_flow")
        }
        val day1 = dailyRecords.firstOrNull { it.date == dateKey(1) }
        val day2 = dailyRecords.firstOrNull { it.date == dateKey(2) }
        val day3 = dailyRecords.firstOrNull { it.date == dateKey(3) }
        if (waterPercent >= 100f) {
            if (day1?.goalHit != true && dailyRecords.any { it.date < dateKey(1) }) award("comeback")
            if (day1?.goalHit == true && day2?.goalHit == true && day3?.goalHit != true &&
                dailyRecords.any { it.date < dateKey(3) }
            ) award("glow_up")
        }
        if (day1 != null && day2 != null &&
            hours.any { h -> day1.logHours.any { abs(it - h) <= 1 } && day2.logHours.any { abs(it - h) <= 1 } }
        ) award("routine_builder")
        hp = hp + hpGain
        checkProgressTrophies()
    }

    private fun dateKey(daysAgo: Int): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -daysAgo) }.time)

    // Days in a row with at least one drink, counting today once you've had something.
    fun loggedDaysInARow(): Int {
        val dates = dailyRecords.map { it.date }.toSet()
        var n = if (todayDrinks.isNotEmpty()) 1 else 0
        var d = 1
        while (dateKey(d) in dates) {
            n++
            d++
        }
        return n
    }

    // Finished days in a row that hit the goal, going back from yesterday.
    fun hitDaysInARow(): Int {
        val byDate = dailyRecords.associateBy { it.date }
        var n = 0
        while (byDate[dateKey(n + 1)]?.goalHit == true) n++
        return n
    }

    // Trophies that build up over days, so they're checked when the app starts as well as after each drink.
    fun checkProgressTrophies() {
        var hpGain = 0
        fun award(id: String) { if (!earnedTrophyIds.contains(id)) { earnedTrophyIds.add(id); hpGain += TrophyData.trophies.find { it.id == id }?.hpReward ?: 25 } }
        if (totalDrinksAllTime >= 10) award("ten_drinks")
        if (totalDrinksAllTime >= 25) award("twenty_five_drinks")
        if (totalDrinksAllTime >= 50) award("fifty_drinks")
        if (totalDrinksAllTime >= 100) award("hundred_drinks")
        if (totalDrinksAllTime >= 250) award("two_fifty")
        if (totalDrinksAllTime >= 500) award("five_hundred")
        if (totalDrinksAllTime >= 750) award("seven_fifty")
        if (totalDrinksAllTime >= 1000) award("thousand_drinks")
        if (streak >= 3) award("three_day")
        if (streak >= 5) award("five_day")
        if (streak >= 7) award("week_streak")
        if (streak >= 14) award("two_week")
        if (streak >= 30) award("thirty_streak")
        if (streak >= 60) {
            award("sixty_streak")
            award("hydra_loyalist")
        }
        val hitDays = hitDaysInARow()
        if (hitDays >= 5) award("hydration_five")
        if (hitDays >= 7) award("perfect_week")
        val loggedDays = loggedDaysInARow()
        if (loggedDays >= 7) award("committed")
        if (loggedDays >= 30) award("thirty_ritual")
        if (openStreak >= 7) award("seven_day_devotion")
        if (levelInfo.level >= 5) award("level5")
        if (levelInfo.level >= 10) award("level10")
        if (TrophyData.trophies.all { it.id == "completionist" || it.id in earnedTrophyIds }) award("completionist")
        hp = hp + hpGain
    }

    // Trophies for a whole day, checked once that day is over so a late coffee still counts.
    fun checkDayTrophies(day: List<LoggedDrink>) {
        if (day.isEmpty()) return
        var hpGain = 0
        fun award(id: String) { if (!earnedTrophyIds.contains(id)) { earnedTrophyIds.add(id); hpGain += TrophyData.trophies.find { it.id == id }?.hpReward ?: 25 } }
        val cats = day.map { DrinkDatabase.getCategoryForDrink(it.drink.id)?.id }
        if (day.size >= 3) {
            if (day.all { it.drink.caffeineMg == 0 }) award("no_caffeine")
            if (day.all { it.drink.sugarG == 0f }) award("no_sugar")
            if (day.indices.all { cats[it] == "water" || day[it].drink.id in setOf("blacktea", "greentea", "herbal") }) award("green")
        }
        if ("water" in cats && cats.any { it != "water" } && day.all { it.drink.alcoholG == 0f }) award("balanced_day")
        hp = hp + hpGain
    }

    fun convertMl(ml: Int): String = when (unitSystem) {
        "oz" -> String.format(Locale.UK, "%.0f", ml * 0.033814)
        "L" -> String.format(Locale.UK, "%.2f", ml / 1000f)
        else -> ml.toString()
    }

    fun convertDisplay(ml: Int): String = convertMl(ml) + unitLabel()

    fun unitLabel(): String = when (unitSystem) {
        "oz" -> "oz"
        "L" -> "L"
        else -> "ml"
    }

    fun formattedCalDate(): String {
        @Suppress("UNUSED_VARIABLE") val tick = clockTick // re-read so the card updates as time passes
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH).toString()
        val month = SimpleDateFormat("MMM", Locale.UK).format(cal.time)
        val dayName = SimpleDateFormat("EEE", Locale.UK).format(cal.time)
        val time = if (cal12Hour) {
            SimpleDateFormat("h:mm a", Locale.UK).format(cal.time)
        } else {
            SimpleDateFormat("HH:mm", Locale.UK).format(cal.time)
        }
        val parts = mutableListOf<String>()
        if (calShowDayName) parts.add(dayName)
        parts.add(if (calDayFirst) "$day $month" else "$month $day")
        if (calShowTime) parts.add(time)
        return parts.joinToString("\n")
    }

    data class DrinkColourPortion(val colour: Color, val proportion: Float)

    val drinkColourPortions: List<DrinkColourPortion>
        get() {
            if (todayDrinks.isEmpty()) return emptyList()
            val m = mutableMapOf<Long, Pair<Color, Int>>()
            todayDrinks.forEach { l -> val c = l.drink.fillColour; val k = c.value.toLong(); val e = m[k]; m[k] = if (e != null) Pair(c, e.second + l.drink.waterMl) else Pair(c, l.drink.waterMl) }
            val t = m.values.sumOf { it.second }.toFloat()
            return m.values.sortedByDescending { it.second }.map { DrinkColourPortion(it.first, it.second / t) }
        }

    fun resetAllData(context: Context) {
        viewModelScope.launch {
            HydraDataStore.clear(context)
            loadFailed = false
            recoveryBackup.clear()
            profile = Profile()
            themeId = "skyLight"
            showFace = true
            showSpeechBubble = true
            hydraShape = "heart"
            unitSystem = "ml"
            customWaterGoalMl = null
            customGoalEnabled = false
            showNecklace = true
            showRainbowShimmer = true
            showButterfly = true
            showGoldenGlow = true
            calendarClickAction = "edit"
            calDayFirst = true
            calShowDayName = true
            calShowTime = true
            remindersEnabled = false
            reminderSound = "default"
            customReminders.clear()
            customReminders.addAll(
                listOf(
                    CustomReminder("1", "08:00"),
                    CustomReminder("2", "12:00"),
                    CustomReminder("3", "16:00"),
                    CustomReminder("4", "20:00")
                )
            )
            reminderMessages.clear()
            disabledPresetIndices.clear()
            reminderStopAtGoal = false
            reminderSmartSnooze = false
            reminderDailySummary = false
            todayDrinks.clear()
            drinksDate = ""
            hp = 0
            earnedTrophyIds.clear()
            streak = 0
            diaryEntries.clear()
            moodEntries.clear()
            currentMood = null
            moodLoggedToday = false
            appOpenedToday = false
            totalDrinksAllTime = 0
            dailyRecords.clear()
            longestStreak = 0
            unlockedPersonalities.clear()
            pendingUnlockPersonality = null
            reminderIgnoreCount = 0
            showDrinkAnimation = false
            lastLoggedDrinkName = ""
            lastLoggedAmount = 0
            lastLoggedIsDehydrating = false
            showLevelPopup = false
            justHitGoal = false
            onboardingComplete = false
        }
    }

    fun save(context: Context) {
        if (loadFailed) return
        viewModelScope.launch {
            HydraDataStore.save(context, this@HydraViewModel)
        }
    }

    fun load(context: Context) {
        viewModelScope.launch {
            try {
                HydraDataStore.load(context, this@HydraViewModel)
                if (onboardingComplete) checkProgressTrophies()
            } catch (e: Exception) {
                Log.e("HydraLoad", "DataStore load failed: ${e.message}", e)
                loadFailed = true
            } finally {
                isLoaded = true
            }
            onAppOpened()
        }
    }
}
