package com.hydraheart2.app.data.models

data class CustomReminder(
    val id: String = System.currentTimeMillis().toString(),
    val time: String = "12:00",
    val enabled: Boolean = true
)
