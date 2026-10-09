package com.example.hydraheart

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class HydraViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val _state = MutableStateFlow(loadState())

    val state: StateFlow<HydraState> = _state.asStateFlow()

    fun logDrink(type: HydraDrinkType, amountMl: Float = type.defaultAmountMl) {
        val now = System.currentTimeMillis()
        val safeAmount = amountMl.coerceAtLeast(1f)
        val entry = HydraDrinkLog(
            id = now,
            drinkTypeId = type.id,
            drinkName = type.name,
            amountMl = safeAmount,
            waterMl = safeAmount * type.waterPercent,
            caffeineMg = type.caffeineMg * safeAmount / type.defaultAmountMl.coerceAtLeast(1f),
            calciumMg = type.calciumMg * safeAmount / type.defaultAmountMl.coerceAtLeast(1f),
            emoji = type.emoji,
            category = type.category,
            loggedAt = now,
            dateKey = dateKeyFor(now)
        )
        val nextLogs = (_state.value.logs + entry).sortedByDescending { it.loggedAt }
        persistLogs(nextLogs)
        _state.value = loadState(nextLogs)
    }

    fun deleteDrink(log: HydraDrinkLog) {
        val nextLogs = _state.value.logs.filterNot { it.id == log.id }
        persistLogs(nextLogs)
        _state.value = loadState(nextLogs)
    }

    fun setDailyWaterGoalMl(goalMl: Float) {
        val value = goalMl.coerceIn(250f, 10_000f)
        preferences.edit().putFloat(KEY_WATER_GOAL, value).apply()
        _state.value = _state.value.copy(dailyWaterGoalMl = value)
    }

    fun setDailyCaffeineLimitMg(limitMg: Float) {
        val value = limitMg.coerceIn(0f, 5_000f)
        preferences.edit().putFloat(KEY_CAFFEINE_LIMIT, value).apply()
        _state.value = _state.value.copy(dailyCaffeineLimitMg = value)
    }

    fun setDailyCalciumGoalMg(goalMg: Float) {
        val value = goalMg.coerceIn(0f, 5_000f)
        preferences.edit().putFloat(KEY_CALCIUM_GOAL, value).apply()
        _state.value = _state.value.copy(dailyCalciumGoalMg = value)
    }

    fun addReminder(hour: Int = 13, minute: Int = 0, label: String = "Hydration") {
        val nextId = (_state.value.reminders.maxOfOrNull { it.id } ?: 0) + 1
        val next = _state.value.reminders + HydraReminder(
            id = nextId,
            hour = hour.coerceIn(0, 23),
            minute = minute.coerceIn(0, 59),
            label = label.ifBlank { "Hydration" }
        )
        persistReminders(next)
        if (_state.value.remindersEnabled) {
            HydraReminderScheduler.schedule(getApplication(), next.last())
        }
        _state.value = _state.value.copy(reminders = next)
    }

    fun deleteReminder(reminder: HydraReminder) {
        HydraReminderScheduler.cancel(getApplication(), reminder)
        val next = _state.value.reminders.filterNot { it.id == reminder.id }
        persistReminders(next)
        _state.value = _state.value.copy(reminders = next)
    }

    fun setRemindersEnabled(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_REMINDERS_ENABLED, enabled).apply()
        HydraReminderScheduler.sync(getApplication(), _state.value.reminders, enabled)
        _state.value = _state.value.copy(remindersEnabled = enabled)
    }

    fun setNotificationSound(sound: String) {
        preferences.edit().putString(KEY_NOTIFICATION_SOUND, sound).apply()
        _state.value = _state.value.copy(notificationSound = sound)
    }

    fun clearAllProgress() {
        preferences.edit()
            .remove(KEY_LOGS)
            .remove(KEY_ACHIEVEMENTS)
            .remove(KEY_WATER_GOAL)
            .remove(KEY_CAFFEINE_LIMIT)
            .remove(KEY_CALCIUM_GOAL)
            .remove(KEY_REMINDERS)
            .remove(KEY_REMINDERS_ENABLED)
            .remove(KEY_NOTIFICATION_SOUND)
            .apply()
        _state.value = loadState(emptyList())
    }

    private fun loadState(logsOverride: List<HydraDrinkLog>? = null): HydraState {
        val logs = logsOverride ?: loadLogs()
        val goal = preferences.getFloat(KEY_WATER_GOAL, 3_000f)
        val caffeineLimit = preferences.getFloat(KEY_CAFFEINE_LIMIT, 400f)
        val calciumGoal = preferences.getFloat(KEY_CALCIUM_GOAL, 1_000f)
        val reminders = loadReminders()
        val remindersEnabled = preferences.getBoolean(KEY_REMINDERS_ENABLED, false)
        val notificationSound = preferences.getString(KEY_NOTIFICATION_SOUND, "Shimmer") ?: "Shimmer"
        val unlocked = calculateAchievements(logs, goal, caffeineLimit, calciumGoal)
        persistAchievements(unlocked)
        return HydraState(
            drinkTypes = defaultHydraDrinkTypes(),
            logs = logs,
            dailyWaterGoalMl = goal,
            dailyCaffeineLimitMg = caffeineLimit,
            dailyCalciumGoalMg = calciumGoal,
            currentStreak = calculateStreak(logs, goal),
            earnedAchievements = unlocked,
            reminders = reminders,
            remindersEnabled = remindersEnabled,
            notificationSound = notificationSound
        )
    }

    private fun loadLogs(): List<HydraDrinkLog> {
        val raw = preferences.getString(KEY_LOGS, null) ?: return emptyList()
        return runCatching {
            val json = JSONArray(raw)
            buildList(json.length()) {
                for (index in 0 until json.length()) {
                    val item = json.getJSONObject(index)
                    add(
                        HydraDrinkLog(
                            id = item.getLong("id"),
                            drinkTypeId = item.getString("drinkTypeId"),
                            drinkName = item.getString("drinkName"),
                            amountMl = item.getDouble("amountMl").toFloat(),
                            waterMl = item.getDouble("waterMl").toFloat(),
                            caffeineMg = item.getDouble("caffeineMg").toFloat(),
                            calciumMg = item.getDouble("calciumMg").toFloat(),
                            emoji = item.getString("emoji"),
                            category = HydraDrinkCategory.valueOf(item.getString("category")),
                            loggedAt = item.getLong("loggedAt"),
                            dateKey = item.getString("dateKey")
                        )
                    )
                }
            }.sortedByDescending { it.loggedAt }
        }.getOrDefault(emptyList())
    }

    private fun persistLogs(logs: List<HydraDrinkLog>) {
        val json = JSONArray()
        logs.forEach { log ->
            json.put(
                JSONObject()
                    .put("id", log.id)
                    .put("drinkTypeId", log.drinkTypeId)
                    .put("drinkName", log.drinkName)
                    .put("amountMl", log.amountMl)
                    .put("waterMl", log.waterMl)
                    .put("caffeineMg", log.caffeineMg)
                    .put("calciumMg", log.calciumMg)
                    .put("emoji", log.emoji)
                    .put("category", log.category.name)
                    .put("loggedAt", log.loggedAt)
                    .put("dateKey", log.dateKey)
            )
        }
        preferences.edit().putString(KEY_LOGS, json.toString()).apply()
    }

    private fun loadReminders(): List<HydraReminder> {
        val raw = preferences.getString(KEY_REMINDERS, null) ?: return emptyList()
        return runCatching {
            val json = JSONArray(raw)
            buildList(json.length()) {
                for (index in 0 until json.length()) {
                    val item = json.getJSONObject(index)
                    add(
                        HydraReminder(
                            id = item.getInt("id"),
                            hour = item.getInt("hour"),
                            minute = item.getInt("minute"),
                            label = item.getString("label"),
                            enabled = item.optBoolean("enabled", true)
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }

    private fun persistReminders(reminders: List<HydraReminder>) {
        val json = JSONArray()
        reminders.forEach { reminder ->
            json.put(
                JSONObject()
                    .put("id", reminder.id)
                    .put("hour", reminder.hour)
                    .put("minute", reminder.minute)
                    .put("label", reminder.label)
                    .put("enabled", reminder.enabled)
            )
        }
        preferences.edit().putString(KEY_REMINDERS, json.toString()).apply()
    }

    private fun calculateAchievements(
        logs: List<HydraDrinkLog>,
        waterGoal: Float,
        caffeineLimit: Float,
        calciumGoal: Float
    ): Set<String> {
        if (logs.isEmpty()) return emptySet()
        val result = mutableSetOf("FIRST_SIP")
        logs.firstOrNull { Instant.ofEpochMilli(it.loggedAt).atZone(ZoneId.systemDefault()).hour < 7 }
            ?.let { result += "EARLY_BIRD" }
        logs.firstOrNull { Instant.ofEpochMilli(it.loggedAt).atZone(ZoneId.systemDefault()).hour >= 22 }
            ?.let { result += "NIGHT_OWL" }
        logs.firstOrNull { it.amountMl >= 500f }?.let { result += "BIG_GULP" }
        if (logs.filter { it.dateKey == todayDateKey() }.map { it.drinkTypeId }.distinct().size >= 5) {
            result += "VARIETY_PACK"
        }
        val todayWater = logs.filter { it.dateKey == todayDateKey() }.sumOf { it.waterMl.toDouble() }.toFloat()
        val todayCalcium = logs.filter { it.dateKey == todayDateKey() }.sumOf { it.calciumMg.toDouble() }.toFloat()
        val todayCaffeine = logs.filter { it.dateKey == todayDateKey() }.sumOf { it.caffeineMg.toDouble() }.toFloat()
        if (todayWater >= waterGoal) result += "HYDRATED_DAY"
        if (todayCalcium >= calciumGoal) result += "CALCIUM_HERO"
        if (todayCaffeine <= caffeineLimit && todayWater >= waterGoal) result += "TRIPLE_GOAL"
        if (logs.size >= 100) result += "MILESTONE_100"
        if (logs.size >= 365) result += "MILESTONE_365"
        return result
    }

    private fun calculateStreak(logs: List<HydraDrinkLog>, waterGoal: Float): Int {
        var date = LocalDate.now()
        var streak = 0
        val byDate = logs.groupBy { it.dateKey }
        while ((byDate[date.toString()] ?: emptyList()).sumOf { it.waterMl.toDouble() } >= waterGoal) {
            streak++
            date = date.minusDays(1)
        }
        return streak
    }

    private fun persistAchievements(achievements: Set<String>) {
        preferences.edit().putStringSet(KEY_ACHIEVEMENTS, achievements).apply()
    }

    companion object {
        private const val PREFERENCES_NAME = "hydra_heart_data"
        private const val KEY_LOGS = "drink_logs"
        private const val KEY_ACHIEVEMENTS = "earned_achievements"
        private const val KEY_WATER_GOAL = "daily_water_ml"
        private const val KEY_CAFFEINE_LIMIT = "daily_caffeine_mg"
        private const val KEY_CALCIUM_GOAL = "daily_calcium_mg"
        private const val KEY_REMINDERS = "reminders"
        private const val KEY_REMINDERS_ENABLED = "reminders_enabled"
        private const val KEY_NOTIFICATION_SOUND = "notification_sound"
    }
}
