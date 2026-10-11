package com.hydraheart2.app.data.models

import com.google.gson.annotations.SerializedName

enum class Climate(val label: String, val icon: String) {
    COLD("Cold", "❄️"),
    @SerializedName("TEMPERATE") MODERATE("Moderate", "🌤"),
    HOT("Hot", "☀️")
}
