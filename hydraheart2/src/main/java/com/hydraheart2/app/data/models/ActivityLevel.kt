package com.hydraheart2.app.data.models

import com.google.gson.annotations.SerializedName

enum class ActivityLevel(val label: String, val icon: String) {
    SEDENTARY("Sedentary", "🪑"),
    LIGHTLY_ACTIVE("Lightly Active", "🚶‍♀️"),
    @SerializedName("MODERATELY_ACTIVE") MODERATE("Moderately Active", "🏃‍♀️"),
    VERY_ACTIVE("Very Active", "💪")
}
