package com.hydraheart2.app.data.models

data class Trophy(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val hpReward: Int,
    val requirement: (Int, Int) -> Boolean = { _, _ -> true }
)
