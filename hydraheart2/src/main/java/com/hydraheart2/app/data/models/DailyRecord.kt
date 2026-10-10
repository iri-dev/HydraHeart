package com.hydraheart2.app.data.models

data class DailyRecord(
    val date: String,
    val completionPct: Float,
    val drinkCount: Int,
    val logHours: List<Int>,
    val goalHit: Boolean,
    val moodValue: String = ""
)
