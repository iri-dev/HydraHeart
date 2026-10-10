package com.hydraheart2.app.data

data class Trophy(
    val id: String,
    val nameRes: Int,
    val descriptionRes: Int,
    val icon: String,
    val hpReward: Int,
    val rarity: TrophyRarity,
    val progressTarget: Int = 0
)
