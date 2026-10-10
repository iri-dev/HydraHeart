package com.hydraheart2.app.data.models

data class DiaryEntry(
    val time: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
