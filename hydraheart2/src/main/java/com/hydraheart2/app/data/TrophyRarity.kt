package com.hydraheart2.app.data

import com.hydraheart2.app.R

enum class TrophyRarity(val labelRes: Int, val colour: Long) {
    COMMON(R.string.trophy_rarity_common, 0xFFA0B0C0),
    UNCOMMON(R.string.trophy_rarity_uncommon, 0xFF4ADE80),
    RARE(R.string.trophy_rarity_rare, 0xFF60A5FA),
    EPIC(R.string.trophy_rarity_epic, 0xFFA855F7),
    LEGENDARY(R.string.trophy_rarity_legendary, 0xFFFFD700)
}
