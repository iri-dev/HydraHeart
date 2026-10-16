package com.hydraheart2.app.util

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hydraheart2.app.data.models.ActivityLevel
import com.hydraheart2.app.data.models.Climate
import com.hydraheart2.app.data.models.CustomReminder
import com.hydraheart2.app.data.models.DailyRecord
import com.hydraheart2.app.data.models.DiaryEntry
import com.hydraheart2.app.data.models.Drink
import com.hydraheart2.app.data.models.Gender
import com.hydraheart2.app.data.models.LoggedDrink
import com.hydraheart2.app.data.models.MoodEntry
import com.hydraheart2.app.data.models.Personality
import com.hydraheart2.app.data.models.Profile
import com.hydraheart2.app.viewmodel.HydraViewModel
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hydra_prefs")

object HydraDataStore {
    private val gson = Gson()

    private val KEY_PROFILE = stringPreferencesKey("profile")
    private val KEY_ONBOARDING = booleanPreferencesKey("onboarding_complete")
    private val KEY_THEME_ID = stringPreferencesKey("theme_id")
    private val KEY_SHOW_FACE = booleanPreferencesKey("show_face")
    private val KEY_SHOW_BUBBLE = booleanPreferencesKey("show_speech_bubble")
    private val KEY_HYDRA_SHAPE = stringPreferencesKey("hydra_shape")
    private val KEY_UNIT_SYSTEM = stringPreferencesKey("unit_system")
    private val KEY_CUSTOM_WATER_GOAL = intPreferencesKey("custom_water_goal")
    private val KEY_CUSTOM_GOAL_ENABLED = booleanPreferencesKey("custom_goal_enabled")
    private val KEY_SHOW_NECKLACE = booleanPreferencesKey("show_necklace")
    private val KEY_SHOW_RAINBOW_SHIMMER = booleanPreferencesKey("show_rainbow_shimmer")
    private val KEY_SHOW_BUTTERFLY = booleanPreferencesKey("show_butterfly")
    private val KEY_SHOW_GOLDEN_GLOW = booleanPreferencesKey("show_golden_glow")
    private val KEY_CAL_CLICK_ACTION = stringPreferencesKey("cal_click_action")
    private val KEY_CAL_DAY_FIRST = booleanPreferencesKey("cal_day_first")
    private val KEY_CAL_SHOW_DAY_NAME = booleanPreferencesKey("cal_show_day_name")
    private val KEY_CAL_SHOW_TIME = booleanPreferencesKey("cal_show_time")
    private val KEY_CAL_12_HOUR = booleanPreferencesKey("cal_12_hour")
    private val KEY_CAL_WEEK_START_MONDAY = booleanPreferencesKey("cal_week_start_monday")
    private val KEY_CAL_DAY_RESET_HOUR = intPreferencesKey("cal_day_reset_hour")
    private val KEY_REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
    private val KEY_REMINDER_SOUND = stringPreferencesKey("reminder_sound")
    private val KEY_CUSTOM_REMINDERS = stringPreferencesKey("custom_reminders")
    private val KEY_REMINDER_MESSAGES = stringPreferencesKey("reminder_messages")
    private val KEY_DISABLED_PRESET_INDICES = stringPreferencesKey("disabled_preset_indices")
    private val KEY_REMINDER_STOP_AT_GOAL = booleanPreferencesKey("reminder_stop_at_goal")
    private val KEY_REMINDER_SMART_SNOOZE = booleanPreferencesKey("reminder_smart_snooze")
    private val KEY_REMINDER_DAILY_SUMMARY = booleanPreferencesKey("reminder_daily_summary")
    private val KEY_HP = intPreferencesKey("hp")
    private val KEY_EARNED_TROPHIES = stringPreferencesKey("earned_trophies")
    private val KEY_STREAK = intPreferencesKey("streak")
    private val KEY_TOTAL_DRINKS = intPreferencesKey("total_drinks_all_time")
    private val KEY_CURRENT_MOOD = stringPreferencesKey("current_mood")
    private val KEY_MOOD_LOGGED_TODAY = booleanPreferencesKey("mood_logged_today")
    private val KEY_APP_OPENED_TODAY = booleanPreferencesKey("app_opened_today")
    private val KEY_TODAY_DRINKS = stringPreferencesKey("today_drinks")
    private val KEY_TODAY_DRINKS_DATE = stringPreferencesKey("today_drinks_date")
    private val KEY_MOOD_ENTRIES = stringPreferencesKey("mood_entries")
    private val KEY_DAILY_RECORDS = stringPreferencesKey("daily_records")
    private val KEY_LONGEST_STREAK = intPreferencesKey("longest_streak")
    private val KEY_UNLOCKED_PERSONALITIES = stringPreferencesKey("unlocked_personalities")
    private val KEY_REMINDER_IGNORE_COUNT = intPreferencesKey("reminder_ignore_count")
    private val KEY_OPEN_STREAK = intPreferencesKey("open_streak")
    private val KEY_FIRST_OPENED_TODAY_AT = longPreferencesKey("first_opened_today_at")
    private val KEY_EQUIPPED_ACCESSORIES = stringPreferencesKey("equipped_accessories")
    private val KEY_CUSTOM_SPEECH_LINES = stringPreferencesKey("custom_speech_lines")
    private val KEY_CUSTOM_SPEECH_ENABLED = booleanPreferencesKey("custom_speech_enabled")
    private val KEY_SPEECH_BUBBLE_STYLE = stringPreferencesKey("speech_bubble_style")
    private val KEY_HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
    private val KEY_SOUND_EFFECTS_ENABLED = booleanPreferencesKey("sound_effects_enabled")
    private val KEY_VISUAL_EFFECTS_ENABLED = booleanPreferencesKey("visual_effects_enabled")
    private val KEY_PERFORMANCE_MODE = stringPreferencesKey("performance_mode")
    private val KEY_KEEP_SCREEN_ON = booleanPreferencesKey("keep_screen_on")
    private val KEY_APP_LANGUAGE = stringPreferencesKey("app_language")
    private val KEY_INTERACTION_INTENSITY = intPreferencesKey("interaction_intensity")
    private val KEY_REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
    private val KEY_BATTERY_AWARE_MODE = booleanPreferencesKey("battery_aware_mode")
    private val KEY_AUTO_PERFORMANCE = booleanPreferencesKey("auto_performance")
    private val KEY_PARTICLES_ENABLED = booleanPreferencesKey("particles_enabled")
    private val KEY_ANIMATIONS_ENABLED = booleanPreferencesKey("animations_enabled")
    private val KEY_FULL_GLOW_ENABLED = booleanPreferencesKey("full_glow_enabled")
    private val KEY_DIARY_ENTRIES = stringPreferencesKey("diary_entries")
    private val KEY_RECOVERY_BACKUP = stringPreferencesKey("recovery_backup")

    private const val DIARY_KEEP_DAYS = 60

    internal fun today(): String = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Date())

    internal fun dayBefore(date: String): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        val cal = Calendar.getInstance()
        cal.time = fmt.parse(date) ?: Date()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return fmt.format(cal.time)
    }

    // Days in a row the app was used up to [date], going by the days that have a daily record.
    private fun usedDaysEndingOn(date: String, records: List<DailyRecord>): Int {
        if (date.isBlank()) return 1
        val dates = records.map { it.date }.toSet()
        var n = 1
        var d = dayBefore(date)
        while (d in dates) {
            n++
            d = dayBefore(d)
        }
        return n
    }


    // Reads one saved value. If it can't be used, the rest still loads, and the raw value is kept in
    // [recovery] so the next save doesn't throw it away.
    private fun <T> Preferences.readKey(key: Preferences.Key<T>, recovery: MutableMap<String, String>, use: (T) -> Unit) {
        val raw = try {
            this[key]
        } catch (e: Exception) {
            Log.w("HydraLoad", "Couldn't read ${key.name}", e)
            return
        } ?: return
        try {
            use(raw)
        } catch (e: Exception) {
            Log.w("HydraLoad", "Couldn't use ${key.name}", e)
            if (raw is String) recovery[key.name] = raw
        }
    }

    private inline fun <reified T> listFrom(json: String): List<T> {
        val type = object : TypeToken<List<T>>() {}.type
        val list: List<T?>? = gson.fromJson(json, type)
        return list?.filterNotNull() ?: emptyList()
    }

    // Gson can leave Kotlin's non-null fields as null when a saved value is missing or unknown, so every
    // loaded object gets checked and filled in before the app uses it.
    @Suppress("USELESS_CAST")
    private fun Profile.sanitised(): Profile = Profile(
        name = (name as String?) ?: "",
        gender = (gender as Gender?) ?: Gender.FEMALE,
        birthDate = (birthDate as String?) ?: "",
        heightCm = heightCm,
        weightKg = weightKg,
        activityLevel = (activityLevel as ActivityLevel?) ?: ActivityLevel.LIGHTLY_ACTIVE,
        climate = (climate as Climate?) ?: Climate.MODERATE,
        personality = (personality as Personality?) ?: Personality.BESTIE
    )

    @Suppress("USELESS_CAST")
    private fun DailyRecord.sanitised(): DailyRecord? {
        val d = (date as String?) ?: return null
        return copy(date = d, logHours = (logHours as List<Int>?) ?: emptyList(), moodValue = (moodValue as String?) ?: "")
    }

    @Suppress("USELESS_CAST")
    private fun MoodEntry.sanitised(): MoodEntry? {
        val d = (date as String?) ?: return null
        val m = (mood as String?) ?: return null
        return copy(date = d, mood = m, note = (note as String?) ?: "")
    }

    @Suppress("USELESS_CAST")
    private fun LoggedDrink.sanitised(): LoggedDrink? {
        val d = (drink as Drink?) ?: return null
        if ((d.id as String?) == null || (d.name as String?) == null) return null
        return copy(categoryName = (categoryName as String?) ?: "", categoryIcon = (categoryIcon as String?) ?: "")
    }

    @Suppress("USELESS_CAST")
    private fun CustomReminder.sanitised(): CustomReminder =
        copy(id = (id as String?) ?: System.nanoTime().toString(), time = (time as String?) ?: "12:00")

    @Suppress("USELESS_CAST")
    private fun DiaryEntry.sanitised(): DiaryEntry? {
        val t = (text as String?) ?: return null
        return copy(time = (time as String?) ?: "", text = t)
    }

    suspend fun load(context: Context, vm: HydraViewModel) {
        val prefs = context.dataStore.data.first()
        val recovery = mutableMapOf<String, String>()
        prefs.readKey(KEY_RECOVERY_BACKUP, recovery) { json ->
            val type = object : TypeToken<Map<String, String>>() {}.type
            gson.fromJson<Map<String, String>?>(json, type)?.let { recovery.putAll(it) }
        }

        prefs.readKey(KEY_PROFILE, recovery) { vm.profile = gson.fromJson(it, Profile::class.java).sanitised() }
        prefs.readKey(KEY_ONBOARDING, recovery) { vm.onboardingComplete = it }
        prefs.readKey(KEY_THEME_ID, recovery) { vm.themeId = it }
        prefs.readKey(KEY_SHOW_FACE, recovery) { vm.showFace = it }
        prefs.readKey(KEY_SHOW_BUBBLE, recovery) { vm.showSpeechBubble = it }
        if (!vm.showFace) vm.showSpeechBubble = false
        prefs.readKey(KEY_HYDRA_SHAPE, recovery) { vm.hydraShape = it }
        prefs.readKey(KEY_UNIT_SYSTEM, recovery) { vm.unitSystem = it }
        prefs.readKey(KEY_CUSTOM_GOAL_ENABLED, recovery) { vm.customGoalEnabled = it }
        prefs.readKey(KEY_CUSTOM_WATER_GOAL, recovery) { vm.customWaterGoalMl = it }
        prefs.readKey(KEY_SHOW_NECKLACE, recovery) { vm.showNecklace = it }
        prefs.readKey(KEY_SHOW_RAINBOW_SHIMMER, recovery) { vm.showRainbowShimmer = it }
        prefs.readKey(KEY_SHOW_BUTTERFLY, recovery) { vm.showButterfly = it }
        prefs.readKey(KEY_SHOW_GOLDEN_GLOW, recovery) { vm.showGoldenGlow = it }
        prefs.readKey(KEY_EQUIPPED_ACCESSORIES, recovery) {
            val type = object : TypeToken<List<String>>() {}.type
            val ids: List<String> = gson.fromJson<List<String>?>(it, type)?.filterNotNull() ?: emptyList()
            vm.equippedAccessoryIds.clear()
            vm.equippedAccessoryIds.addAll(ids)
        }
        prefs.readKey(KEY_CUSTOM_SPEECH_LINES, recovery) {
            val type = object : TypeToken<List<String>>() {}.type
            val lines: List<String> = gson.fromJson<List<String>?>(it, type)?.filterNotNull() ?: emptyList()
            vm.customSpeechLines.clear()
            vm.customSpeechLines.addAll(lines)
        }
        prefs.readKey(KEY_CUSTOM_SPEECH_ENABLED, recovery) { vm.customSpeechEnabled = it }
        prefs.readKey(KEY_SPEECH_BUBBLE_STYLE, recovery) { vm.speechBubbleStyleId = it }
        prefs.readKey(KEY_HAPTICS_ENABLED, recovery) { vm.hapticsEnabled = it }
        prefs.readKey(KEY_SOUND_EFFECTS_ENABLED, recovery) { vm.soundEffectsEnabled = it }
        prefs.readKey(KEY_VISUAL_EFFECTS_ENABLED, recovery) { vm.visualEffectsEnabled = it }
        prefs.readKey(KEY_PERFORMANCE_MODE, recovery) { vm.performanceMode = it }
        prefs.readKey(KEY_KEEP_SCREEN_ON, recovery) { vm.keepScreenOn = it }
        prefs.readKey(KEY_APP_LANGUAGE, recovery) { vm.appLanguage = it }
        prefs.readKey(KEY_INTERACTION_INTENSITY, recovery) { intensity ->
            vm.interactionIntensity = intensity.coerceIn(0, 3)
            vm.hapticsEnabled = intensity > 0
            vm.soundEffectsEnabled = intensity > 1
            vm.visualEffectsEnabled = intensity > 1
        }
        if (prefs[KEY_INTERACTION_INTENSITY] == null) {
            vm.interactionIntensity = when {
                !vm.hapticsEnabled -> 0
                !vm.soundEffectsEnabled -> 1
                vm.fullGlowEnabled && vm.visualEffectsEnabled -> 3
                else -> 2
            }
        }
        prefs.readKey(KEY_REDUCE_MOTION, recovery) { vm.reduceMotion = it }
        prefs.readKey(KEY_BATTERY_AWARE_MODE, recovery) { vm.batteryAwareMode = it }
        prefs.readKey(KEY_AUTO_PERFORMANCE, recovery) { vm.autoPerformance = it }
        prefs.readKey(KEY_PARTICLES_ENABLED, recovery) { vm.particlesEnabled = it }
        prefs.readKey(KEY_ANIMATIONS_ENABLED, recovery) { vm.animationsEnabled = it }
        prefs.readKey(KEY_FULL_GLOW_ENABLED, recovery) { vm.fullGlowEnabled = it }
        if (prefs[KEY_FULL_GLOW_ENABLED] == null) {
            vm.fullGlowEnabled = vm.interactionIntensity == 3 || vm.performanceMode == "high_performance"
        }
        if (prefs[KEY_PARTICLES_ENABLED] == null) {
            vm.particlesEnabled = vm.performanceMode != "battery_saver"
        }
        if (prefs[KEY_ANIMATIONS_ENABLED] == null) {
            vm.animationsEnabled = vm.performanceMode != "battery_saver"
        }
        prefs.readKey(KEY_CAL_CLICK_ACTION, recovery) { vm.calendarClickAction = it }
        prefs.readKey(KEY_CAL_DAY_FIRST, recovery) { vm.calDayFirst = it }
        prefs.readKey(KEY_CAL_SHOW_DAY_NAME, recovery) { vm.calShowDayName = it }
        prefs.readKey(KEY_CAL_SHOW_TIME, recovery) { vm.calShowTime = it }
        prefs.readKey(KEY_CAL_12_HOUR, recovery) { vm.cal12Hour = it }
        prefs.readKey(KEY_CAL_WEEK_START_MONDAY, recovery) { vm.calWeekStartMonday = it }
        prefs.readKey(KEY_CAL_DAY_RESET_HOUR, recovery) { vm.calDayResetHour = it }
        prefs.readKey(KEY_REMINDERS_ENABLED, recovery) { vm.remindersEnabled = it }
        prefs.readKey(KEY_REMINDER_SOUND, recovery) { vm.reminderSound = it }
        prefs.readKey(KEY_CUSTOM_REMINDERS, recovery) {
            val reminders = listFrom<CustomReminder>(it).map { r -> r.sanitised() }
            vm.customReminders.clear()
            vm.customReminders.addAll(reminders)
        }
        prefs.readKey(KEY_REMINDER_MESSAGES, recovery) {
            val type = object : TypeToken<List<String>>() {}.type
            val messages: List<String> = gson.fromJson<List<String>?>(it, type)?.filterNotNull() ?: emptyList()
            vm.reminderMessages.clear()
            vm.reminderMessages.addAll(messages)
        }
        prefs.readKey(KEY_DISABLED_PRESET_INDICES, recovery) {
            val type = object : TypeToken<List<Int>>() {}.type
            val indices: List<Int> = gson.fromJson<List<Int>?>(it, type)?.filterNotNull() ?: emptyList()
            vm.disabledPresetIndices.clear()
            vm.disabledPresetIndices.addAll(indices)
        }
        prefs.readKey(KEY_REMINDER_STOP_AT_GOAL, recovery) { vm.reminderStopAtGoal = it }
        prefs.readKey(KEY_REMINDER_SMART_SNOOZE, recovery) { vm.reminderSmartSnooze = it }
        prefs.readKey(KEY_REMINDER_DAILY_SUMMARY, recovery) { vm.reminderDailySummary = it }
        prefs.readKey(KEY_HP, recovery) { vm.hp = it }
        prefs.readKey(KEY_EARNED_TROPHIES, recovery) {
            val type = object : TypeToken<List<String>>() {}.type
            val ids: List<String> = gson.fromJson<List<String>?>(it, type)?.filterNotNull() ?: emptyList()
            vm.earnedTrophyIds.clear()
            vm.earnedTrophyIds.addAll(ids)
        }
        prefs.readKey(KEY_STREAK, recovery) { vm.streak = it }
        prefs.readKey(KEY_TOTAL_DRINKS, recovery) { vm.totalDrinksAllTime = it }
        prefs.readKey(KEY_CURRENT_MOOD, recovery) { vm.currentMood = it.ifBlank { null } }
        prefs.readKey(KEY_MOOD_LOGGED_TODAY, recovery) { vm.moodLoggedToday = it }
        prefs.readKey(KEY_APP_OPENED_TODAY, recovery) { vm.appOpenedToday = it }
        prefs.readKey(KEY_MOOD_ENTRIES, recovery) {
            val entries = listFrom<MoodEntry>(it).mapNotNull { e -> e.sanitised() }
            vm.moodEntries.clear()
            vm.moodEntries.addAll(entries)
        }
        prefs.readKey(KEY_DAILY_RECORDS, recovery) {
            val records = listFrom<DailyRecord>(it).mapNotNull { r -> r.sanitised() }
            vm.dailyRecords.clear()
            vm.dailyRecords.addAll(records)
        }
        prefs.readKey(KEY_DIARY_ENTRIES, recovery) {
            val cutoff = System.currentTimeMillis() - DIARY_KEEP_DAYS * 86_400_000L
            val entries = listFrom<DiaryEntry>(it).mapNotNull { e -> e.sanitised() }.filter { e -> e.timestamp >= cutoff }
            vm.diaryEntries.clear()
            vm.diaryEntries.addAll(entries)
        }
        prefs.readKey(KEY_LONGEST_STREAK, recovery) { vm.longestStreak = it }
        prefs.readKey(KEY_UNLOCKED_PERSONALITIES, recovery) { json ->
            val type = object : TypeToken<List<String>>() {}.type
            val names: List<String> = gson.fromJson<List<String>?>(json, type)?.filterNotNull() ?: emptyList()
            val personalities = names.mapNotNull { runCatching { Personality.valueOf(it) }.getOrNull() }
            vm.unlockedPersonalities.clear()
            vm.unlockedPersonalities.addAll(personalities)
        }
        prefs.readKey(KEY_REMINDER_IGNORE_COUNT, recovery) { vm.reminderIgnoreCount = it }
        prefs.readKey(KEY_FIRST_OPENED_TODAY_AT, recovery) { vm.firstOpenedTodayAt = it }

        val savedDate = prefs[KEY_TODAY_DRINKS_DATE] ?: ""
        vm.openStreak = prefs[KEY_OPEN_STREAK] ?: usedDaysEndingOn(savedDate, vm.dailyRecords)
        prefs.readKey(KEY_TODAY_DRINKS, recovery) {
            val drinks = listFrom<LoggedDrink>(it).mapNotNull { d -> d.sanitised() }
            vm.todayDrinks.clear()
            vm.todayDrinks.addAll(drinks)
        }
        // The saved drinks belong to the day they were saved on. If that day is over, file it away now.
        vm.drinksDate = savedDate
        vm.rollOverIfNewDay()

        vm.recoveryBackup.clear()
        vm.recoveryBackup.putAll(recovery)

        if (vm.onboardingComplete && vm.profile.name.isBlank() && vm.hp == 0 && vm.earnedTrophyIds.isEmpty()) {
            vm.onboardingComplete = false
        }
    }

    suspend fun save(context: Context, vm: HydraViewModel) {
        context.dataStore.edit { prefs ->
            prefs[KEY_PROFILE] = gson.toJson(vm.profile)
            prefs[KEY_ONBOARDING] = vm.onboardingComplete
            prefs[KEY_THEME_ID] = vm.themeId
            prefs[KEY_SHOW_FACE] = vm.showFace
            prefs[KEY_SHOW_BUBBLE] = vm.showSpeechBubble
            prefs[KEY_HYDRA_SHAPE] = vm.hydraShape
            prefs[KEY_UNIT_SYSTEM] = vm.unitSystem
            prefs[KEY_CUSTOM_GOAL_ENABLED] = vm.customGoalEnabled
            vm.customWaterGoalMl?.let { prefs[KEY_CUSTOM_WATER_GOAL] = it }
            prefs[KEY_SHOW_NECKLACE] = vm.showNecklace
            prefs[KEY_SHOW_RAINBOW_SHIMMER] = vm.showRainbowShimmer
            prefs[KEY_SHOW_BUTTERFLY] = vm.showButterfly
            prefs[KEY_SHOW_GOLDEN_GLOW] = vm.showGoldenGlow
            prefs[KEY_EQUIPPED_ACCESSORIES] = gson.toJson(vm.equippedAccessoryIds.toList())
            prefs[KEY_CUSTOM_SPEECH_LINES] = gson.toJson(vm.customSpeechLines.toList())
            prefs[KEY_CUSTOM_SPEECH_ENABLED] = vm.customSpeechEnabled
            prefs[KEY_SPEECH_BUBBLE_STYLE] = vm.speechBubbleStyleId
            prefs[KEY_HAPTICS_ENABLED] = vm.hapticsEnabled
            prefs[KEY_SOUND_EFFECTS_ENABLED] = vm.soundEffectsEnabled
            prefs[KEY_VISUAL_EFFECTS_ENABLED] = vm.visualEffectsEnabled
            prefs[KEY_PERFORMANCE_MODE] = vm.performanceMode
            prefs[KEY_KEEP_SCREEN_ON] = vm.keepScreenOn
            prefs[KEY_APP_LANGUAGE] = vm.appLanguage
            prefs[KEY_INTERACTION_INTENSITY] = vm.interactionIntensity
            prefs[KEY_REDUCE_MOTION] = vm.reduceMotion
            prefs[KEY_BATTERY_AWARE_MODE] = vm.batteryAwareMode
            prefs[KEY_AUTO_PERFORMANCE] = vm.autoPerformance
            prefs[KEY_PARTICLES_ENABLED] = vm.particlesEnabled
            prefs[KEY_ANIMATIONS_ENABLED] = vm.animationsEnabled
            prefs[KEY_FULL_GLOW_ENABLED] = vm.fullGlowEnabled
            prefs[KEY_CAL_CLICK_ACTION] = vm.calendarClickAction
            prefs[KEY_CAL_DAY_FIRST] = vm.calDayFirst
            prefs[KEY_CAL_SHOW_DAY_NAME] = vm.calShowDayName
            prefs[KEY_CAL_SHOW_TIME] = vm.calShowTime
            prefs[KEY_CAL_12_HOUR] = vm.cal12Hour
            prefs[KEY_CAL_WEEK_START_MONDAY] = vm.calWeekStartMonday
            prefs[KEY_CAL_DAY_RESET_HOUR] = vm.calDayResetHour
            prefs[KEY_REMINDERS_ENABLED] = vm.remindersEnabled
            prefs[KEY_REMINDER_SOUND] = vm.reminderSound
            prefs[KEY_CUSTOM_REMINDERS] = gson.toJson(vm.customReminders.toList())
            prefs[KEY_REMINDER_MESSAGES] = gson.toJson(vm.reminderMessages.toList())
            prefs[KEY_DISABLED_PRESET_INDICES] = gson.toJson(vm.disabledPresetIndices.toList())
            prefs[KEY_REMINDER_STOP_AT_GOAL] = vm.reminderStopAtGoal
            prefs[KEY_REMINDER_SMART_SNOOZE] = vm.reminderSmartSnooze
            prefs[KEY_REMINDER_DAILY_SUMMARY] = vm.reminderDailySummary
            prefs[KEY_HP] = vm.hp
            prefs[KEY_EARNED_TROPHIES] = gson.toJson(vm.earnedTrophyIds.toList())
            prefs[KEY_STREAK] = vm.streak
            prefs[KEY_TOTAL_DRINKS] = vm.totalDrinksAllTime
            prefs[KEY_CURRENT_MOOD] = vm.currentMood ?: ""
            prefs[KEY_MOOD_LOGGED_TODAY] = vm.moodLoggedToday
            prefs[KEY_APP_OPENED_TODAY] = vm.appOpenedToday
            prefs[KEY_TODAY_DRINKS] = gson.toJson(vm.todayDrinks.toList())
            prefs[KEY_TODAY_DRINKS_DATE] = vm.drinksDate.ifBlank { today() }
            prefs[KEY_MOOD_ENTRIES] = gson.toJson(vm.moodEntries.toList())
            prefs[KEY_DAILY_RECORDS] = gson.toJson(vm.dailyRecords.toList())
            prefs[KEY_LONGEST_STREAK] = vm.longestStreak
            prefs[KEY_UNLOCKED_PERSONALITIES] = gson.toJson(vm.unlockedPersonalities.map { it.name })
            prefs[KEY_REMINDER_IGNORE_COUNT] = vm.reminderIgnoreCount
            prefs[KEY_OPEN_STREAK] = vm.openStreak
            prefs[KEY_FIRST_OPENED_TODAY_AT] = vm.firstOpenedTodayAt
            prefs[KEY_DIARY_ENTRIES] = gson.toJson(vm.diaryEntries.toList())
            if (vm.recoveryBackup.isNotEmpty()) prefs[KEY_RECOVERY_BACKUP] = gson.toJson(vm.recoveryBackup.toMap())
        }
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}
