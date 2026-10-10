package com.hydraheart2.app.data.models

data class LoggedDrink(
    val drink: Drink,
    val timestamp: Long = System.currentTimeMillis(),
    val categoryName: String = "",
    val categoryIcon: String = ""
)
