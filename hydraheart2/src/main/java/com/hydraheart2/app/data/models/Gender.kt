package com.hydraheart2.app.data.models

import com.hydraheart2.app.R

enum class Gender(val label: String) {
    FEMALE("Female"),
    MALE("Male"),
    OTHER("Other");

    fun royal(): String = when (this) {
        FEMALE -> "Queen"
        MALE -> "King"
        OTHER -> "Royalty"
    }

    fun royalRes(): Int = when (this) {
        FEMALE -> R.string.gender_royal_female
        MALE -> R.string.gender_royal_male
        OTHER -> R.string.gender_royal_other
    }

    fun pronoun(): String = when (this) {
        FEMALE -> "she"
        MALE -> "he"
        OTHER -> "they"
    }
}
