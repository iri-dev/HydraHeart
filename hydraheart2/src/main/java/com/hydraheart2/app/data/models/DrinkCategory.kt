package com.hydraheart2.app.data.models

import androidx.compose.ui.graphics.Color

data class DrinkCategory(
    val id: String,
    val name: String,
    val nameRes: Int,
    val icon: String,
    val colour: Color,
    val drinks: List<Drink>
)
