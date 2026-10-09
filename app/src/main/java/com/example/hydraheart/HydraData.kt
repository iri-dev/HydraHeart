package com.example.hydraheart

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class HydraDrinkCategory {
    WATER,
    MILK,
    CAFFEINE,
    OTHER
}

data class HydraDrinkType(
    val id: String,
    val name: String,
    val emoji: String,
    val defaultAmountMl: Float,
    val waterPercent: Float,
    val caffeineMg: Float,
    val calciumMg: Float,
    val category: HydraDrinkCategory,
    val isCustom: Boolean = false
)

data class HydraDrinkLog(
    val id: Long,
    val drinkTypeId: String,
    val drinkName: String,
    val amountMl: Float,
    val waterMl: Float,
    val caffeineMg: Float,
    val calciumMg: Float,
    val emoji: String,
    val category: HydraDrinkCategory,
    val loggedAt: Long,
    val dateKey: String
)

data class HydraReminder(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val label: String,
    val enabled: Boolean = true
) {
    val timeText: String
        get() = "%02d:%02d".format(hour, minute)
}

data class HydraState(
    val drinkTypes: List<HydraDrinkType> = emptyList(),
    val logs: List<HydraDrinkLog> = emptyList(),
    val dailyWaterGoalMl: Float = 3_000f,
    val dailyCaffeineLimitMg: Float = 400f,
    val dailyCalciumGoalMg: Float = 1_000f,
    val currentStreak: Int = 0,
    val earnedAchievements: Set<String> = emptySet(),
    val reminders: List<HydraReminder> = emptyList(),
    val remindersEnabled: Boolean = false,
    val notificationSound: String = "Shimmer"
) {
    private val todayKey: String
        get() = todayDateKey()

    val todayLogs: List<HydraDrinkLog>
        get() = logs.filter { it.dateKey == todayKey }.sortedByDescending { it.loggedAt }

    val todayWaterMl: Float
        get() = todayLogs.sumOf { it.waterMl.toDouble() }.toFloat()

    val todayCaffeineMg: Float
        get() = todayLogs.sumOf { it.caffeineMg.toDouble() }.toFloat()

    val todayCalciumMg: Float
        get() = todayLogs.sumOf { it.calciumMg.toDouble() }.toFloat()

    val hydrationProgress: Float
        get() = (todayWaterMl / dailyWaterGoalMl.coerceAtLeast(1f)).coerceIn(0f, 1f)
}

fun todayDateKey(): String =
    LocalDateKeys.formatter.format(Instant.now().atZone(ZoneId.systemDefault()))

fun dateKeyFor(timestamp: Long): String =
    LocalDateKeys.formatter.format(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()))

private object LocalDateKeys {
    val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
}

fun defaultHydraDrinkTypes(): List<HydraDrinkType> = listOf(
    HydraDrinkType("water_350", "Water", "💧", 350f, 1f, 0f, 0f, HydraDrinkCategory.WATER),
    HydraDrinkType("water_500", "Water", "💧", 500f, 1f, 0f, 0f, HydraDrinkCategory.WATER),
    HydraDrinkType("water_750", "Water", "💧", 750f, 1f, 0f, 0f, HydraDrinkCategory.WATER),
    HydraDrinkType("milk_250", "Milk", "🥛", 250f, .88f, 0f, 300f, HydraDrinkCategory.MILK),
    HydraDrinkType("milk_500", "Milk", "🥛", 500f, .88f, 0f, 600f, HydraDrinkCategory.MILK),
    HydraDrinkType("monster", "Monster", "🟢", 500f, .95f, 160f, 0f, HydraDrinkCategory.CAFFEINE),
    HydraDrinkType("coffee", "Coffee", "☕", 240f, .99f, 95f, 0f, HydraDrinkCategory.CAFFEINE),
    HydraDrinkType("tea", "Tea", "🍵", 240f, .99f, 47f, 0f, HydraDrinkCategory.CAFFEINE),
    HydraDrinkType("juice", "Juice", "🧃", 250f, .85f, 0f, 15f, HydraDrinkCategory.OTHER),
    HydraDrinkType("coconut", "Coconut Water", "🥥", 330f, .95f, 0f, 58f, HydraDrinkCategory.WATER),
    HydraDrinkType("sports", "Sports Drink", "⚡", 500f, .92f, 0f, 0f, HydraDrinkCategory.OTHER)
)
