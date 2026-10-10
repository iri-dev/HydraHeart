package com.hydraheart2.app.util

data class DailyGoals(
    val waterMl: Int,
    val caffeineMg: Int,
    val sugarG: Int,
    val calciumMg: Int,
    val alcoholG: Int,
    val sodiumMg: Int,
    val potassiumMg: Int,
    val magnesiumMg: Int
) {
    fun copy(waterMl: Int = this.waterMl): DailyGoals =
        DailyGoals(waterMl, caffeineMg, sugarG, calciumMg, alcoholG, sodiumMg, potassiumMg, magnesiumMg)
}
