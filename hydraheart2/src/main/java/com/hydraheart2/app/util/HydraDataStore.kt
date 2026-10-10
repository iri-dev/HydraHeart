package com.hydraheart2.app.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hydraheart2.app.data.models.CustomReminder
import com.hydraheart2.app.data.models.DailyRecord
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

    private fun today(): String = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(Date())

    suspend fun load(context: Context, vm: HydraViewModel) {
        val prefs = context.dataStore.data.first()

        prefs[KEY_PROFILE]?.let { vm.profile = gson.fromJson(it, Profile::class.java) }
        prefs[KEY_ONBOARDING]?.let { vm.onboardingComplete = it }
        prefs[KEY_THEME_ID]?.let { vm.themeId = it }
        prefs[KEY_SHOW_FACE]?.let { vm.showFace = it }
        prefs[KEY_SHOW_BUBBLE]?.let { vm.showSpeechBubble = it }
        if (!vm.showFace) vm.showSpeechBubble = false
        prefs[KEY_HYDRA_SHAPE]?.let { vm.hydraShape = it }
        prefs[KEY_UNIT_SYSTEM]?.let { vm.unitSystem = it }
        prefs[KEY_CUSTOM_GOAL_ENABLED]?.let { vm.customGoalEnabled = it }
        prefs[KEY_CUSTOM_WATER_GOAL]?.let { vm.customWaterGoalMl = it }
        prefs[KEY_SHOW_NECKLACE]?.let { vm.showNecklace = it }
        prefs[KEY_EQUIPPED_ACCESSORIES]?.let {
            val type = object : TypeToken<List<String>>() {}.type
            val ids: List<String> = gson.fromJson(it, type)
            vm.equippedAccessoryIds.clear()
            vm.equippedAccessoryIds.addAll(ids)
        }
        prefs[KEY_CUSTOM_SPEECH_LINES]?.let {
            val type = object : TypeToken<List<String>>() {}.type
            val lines: List<String> = gson.fromJson(it, type)
            vm.customSpeechLines.clear()
            vm.customSpeechLines.addAll(lines)
        }
        prefs[KEY_CUSTOM_SPEECH_ENABLED]?.let { vm.customSpeechEnabled = it }
        prefs[KEY_SPEECH_BUBBLE_STYLE]?.let { vm.speechBubbleStyleId = it }
        prefs[KEY_HAPTICS_ENABLED]?.let { vm.hapticsEnabled = it }
        prefs[KEY_SOUND_EFFECTS_ENABLED]?.let { vm.soundEffectsEnabled = it }
        prefs[KEY_VISUAL_EFFECTS_ENABLED]?.let { vm.visualEffectsEnabled = it }
        prefs[KEY_PERFORMANCE_MODE]?.let { vm.performanceMode = it }
        prefs[KEY_KEEP_SCREEN_ON]?.let { vm.keepScreenOn = it }
        prefs[KEY_APP_LANGUAGE]?.let { vm.appLanguage = it }
        prefs[KEY_INTERACTION_INTENSITY]?.let { intensity ->
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
        prefs[KEY_REDUCE_MOTION]?.let { vm.reduceMotion = it }
        prefs[KEY_BATTERY_AWARE_MODE]?.let { vm.batteryAwareMode = it }
        prefs[KEY_AUTO_PERFORMANCE]?.let { vm.autoPerformance = it }
        prefs[KEY_PARTICLES_ENABLED]?.let { vm.particlesEnabled = it }
        prefs[KEY_ANIMATIONS_ENABLED]?.let { vm.animationsEnabled = it }
        prefs[KEY_FULL_GLOW_ENABLED]?.let { vm.fullGlowEnabled = it }
        if (prefs[KEY_FULL_GLOW_ENABLED] == null) {
            vm.fullGlowEnabled = vm.interactionIntensity == 3 || vm.performanceMode == "high_performance"
        }
        if (prefs[KEY_PARTICLES_ENABLED] == null) {
            vm.particlesEnabled = vm.performanceMode != "battery_saver"
        }
        if (prefs[KEY_ANIMATIONS_ENABLED] == null) {
            vm.animationsEnabled = vm.performanceMode != "battery_saver"
        }
        prefs[KEY_CAL_CLICK_ACTION]?.let { vm.calendarClickAction = it }
        prefs[KEY_CAL_DAY_FIRST]?.let { vm.calDayFirst = it }
        prefs[KEY_CAL_SHOW_DAY_NAME]?.let { vm.calShowDayName = it }
        prefs[KEY_CAL_SHOW_TIME]?.let { vm.calShowTime = it }
        prefs[KEY_CAL_12_HOUR]?.let { vm.cal12Hour = it }
        prefs[KEY_CAL_WEEK_START_MONDAY]?.let { vm.calWeekStartMonday = it }
        prefs[KEY_CAL_DAY_RESET_HOUR]?.let { vm.calDayResetHour = it }
        prefs[KEY_REMINDERS_ENABLED]?.let { vm.remindersEnabled = it }
        prefs[KEY_REMINDER_SOUND]?.let { vm.reminderSound = it }
        prefs[KEY_CUSTOM_REMINDERS]?.let {
            val type = object : TypeToken<List<CustomReminder>>() {}.type
            val reminders: List<CustomReminder> = gson.fromJson(it, type)
            vm.customReminders.clear()
            vm.customReminders.addAll(reminders)
        }
        prefs[KEY_REMINDER_MESSAGES]?.let {
            val type = object : TypeToken<List<String>>() {}.type
            val messages: List<String> = gson.fromJson(it, type)
            vm.reminderMessages.clear()
            vm.reminderMessages.addAll(messages)
        }
        prefs[KEY_DISABLED_PRESET_INDICES]?.let {
            val type = object : TypeToken<List<Int>>() {}.type
            val indices: List<Int> = gson.fromJson(it, type)
            vm.disabledPresetIndices.clear()
            vm.disabledPresetIndices.addAll(indices)
        }
        prefs[KEY_REMINDER_STOP_AT_GOAL]?.let { vm.reminderStopAtGoal = it }
        prefs[KEY_REMINDER_SMART_SNOOZE]?.let { vm.reminderSmartSnooze = it }
        prefs[KEY_REMINDER_DAILY_SUMMARY]?.let { vm.reminderDailySummary = it }
        prefs[KEY_HP]?.let { vm.hp = it }
        prefs[KEY_EARNED_TROPHIES]?.let {
            val type = object : TypeToken<List<String>>() {}.type
            val ids: List<String> = gson.fromJson(it, type)
            vm.earnedTrophyIds.clear()
            vm.earnedTrophyIds.addAll(ids)
        }
        prefs[KEY_STREAK]?.let { vm.streak = it }
        prefs[KEY_TOTAL_DRINKS]?.let { vm.totalDrinksAllTime = it }
        prefs[KEY_CURRENT_MOOD]?.let { vm.currentMood = it.ifBlank { null } }
        prefs[KEY_MOOD_LOGGED_TODAY]?.let { vm.moodLoggedToday = it }
        prefs[KEY_APP_OPENED_TODAY]?.let { vm.appOpenedToday = it }
        prefs[KEY_MOOD_ENTRIES]?.let {
            val type = object : TypeToken<List<MoodEntry>>() {}.type
            val entries: List<MoodEntry> = gson.fromJson(it, type)
            vm.moodEntries.clear()
            vm.moodEntries.addAll(entries)
        }
        prefs[KEY_DAILY_RECORDS]?.let {
            val type = object : TypeToken<List<DailyRecord>>() {}.type
            val records: List<DailyRecord> = gson.fromJson(it, type)
            vm.dailyRecords.clear()
            vm.dailyRecords.addAll(records)
        }
        prefs[KEY_LONGEST_STREAK]?.let { vm.longestStreak = it }
        prefs[KEY_UNLOCKED_PERSONALITIES]?.let { json ->
            val type = object : TypeToken<List<String>>() {}.type
            val names: List<String> = gson.fromJson(json, type)
            val personalities = names.mapNotNull { runCatching { Personality.valueOf(it) }.getOrNull() }
            vm.unlockedPersonalities.clear()
            vm.unlockedPersonalities.addAll(personalities)
        }
        prefs[KEY_REMINDER_IGNORE_COUNT]?.let { vm.reminderIgnoreCount = it }

        val savedDate = prefs[KEY_TODAY_DRINKS_DATE] ?: ""
        if (savedDate == today()) {
            prefs[KEY_TODAY_DRINKS]?.let {
                val type = object : TypeToken<List<LoggedDrink>>() {}.type
                val drinks: List<LoggedDrink> = gson.fromJson(it, type)
                vm.todayDrinks.clear()
                vm.todayDrinks.addAll(drinks)
            }
        } else if (savedDate.isNotBlank()) {
            // A new day: turn the drinks saved on the last day into its daily record.
            val yesterdayDrinks: List<LoggedDrink> = prefs[KEY_TODAY_DRINKS]?.let {
                val type = object : TypeToken<List<LoggedDrink>>() {}.type
                gson.fromJson<List<LoggedDrink>>(it, type)
            } ?: emptyList()
            if (yesterdayDrinks.isNotEmpty() && vm.dailyRecords.none { it.date == savedDate }) {
                val goals = GoalCalculator.calculate(vm.profile)
                val goalMl = if (vm.customGoalEnabled && vm.customWaterGoalMl != null) vm.customWaterGoalMl!! else goals.waterMl
                val rawWater = yesterdayDrinks.sumOf { it.drink.waterMl }
                val penalty = yesterdayDrinks.sumOf { (it.drink.sizeMl * it.drink.dehydrationFactor).toInt() }
                val effective = maxOf(0, rawWater - penalty)
                val pct = if (goalMl > 0) minOf(effective.toFloat() / goalMl * 100f, 100f) else 0f
                val logHours = yesterdayDrinks.map { drink ->
                    Calendar.getInstance().apply { timeInMillis = drink.timestamp }.get(Calendar.HOUR_OF_DAY)
                }
                vm.dailyRecords.add(
                    0,
                    DailyRecord(savedDate, pct, yesterdayDrinks.size, logHours, pct >= 100f, vm.currentMood ?: "")
                )
                while (vm.dailyRecords.size > 30) vm.dailyRecords.removeAt(vm.dailyRecords.lastIndex)
            }
            val lastDay = vm.dailyRecords.firstOrNull { it.date == savedDate }
            vm.streak = if (lastDay?.goalHit == true) vm.streak + 1 else 0
            vm.longestStreak = maxOf(vm.longestStreak, vm.streak)
            vm.checkPersonalityUnlocks()
            vm.moodLoggedToday = false
            vm.appOpenedToday = false
        }

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
            prefs[KEY_TODAY_DRINKS_DATE] = today()
            prefs[KEY_MOOD_ENTRIES] = gson.toJson(vm.moodEntries.toList())
            prefs[KEY_DAILY_RECORDS] = gson.toJson(vm.dailyRecords.toList())
            prefs[KEY_LONGEST_STREAK] = vm.longestStreak
            prefs[KEY_UNLOCKED_PERSONALITIES] = gson.toJson(vm.unlockedPersonalities.map { it.name })
            prefs[KEY_REMINDER_IGNORE_COUNT] = vm.reminderIgnoreCount
        }
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}
