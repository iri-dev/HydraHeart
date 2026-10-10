package com.hydraheart2.app.data.models

data class MoodEntry(
    val date: String = "",
    val mood: String = "",
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
