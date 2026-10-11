package com.hydraheart2.app.data.models

data class DailyRecord(
    val date: String,
    val completionPct: Float,
    val drinkCount: Int,
    val logHours: List<Int>,
    val goalHit: Boolean,
    val moodValue: String = "",
    val waterMl: Int = 0,
    val sizeMl: Int = 0,
    val drinkNames: Map<String, Int>? = null
)
