package com.hydraheart2.app.data.models

import androidx.compose.ui.graphics.Color

data class Drink(
    val id: String,
    val name: String,
    val sizeMl: Int,
    val waterMl: Int,
    val caffeineMg: Int,
    val sugarG: Float,
    val calciumMg: Int,
    val alcoholG: Float,
    val calories: Int,
    val sodiumMg: Int,
    val potassiumMg: Int,
    val magnesiumMg: Int,
    val chlorideMg: Int,
    val phosphorusMg: Int,
    val dehydrationFactor: Float,
    val fillColour: Color
)
