package com.hydraheart2.app.data.models

import java.util.Calendar

data class Profile(
    val name: String = "",
    val gender: Gender = Gender.FEMALE,
    val birthDate: String = "",
    val heightCm: Int = 160,
    val weightKg: Float = 55f,
    val activityLevel: ActivityLevel = ActivityLevel.LIGHTLY_ACTIVE,
    val climate: Climate = Climate.MODERATE,
    val personality: Personality = Personality.BESTIE
) {
    val age: Int
        get() = calculateAge(birthDate)

    private fun calculateAge(birthDate: String): Int {
        if (birthDate.isBlank()) return 25
        val parts = birthDate.split("/")
        if (parts.size != 3) return 25
        val day = parts[0].toIntOrNull() ?: return 25
        val month = parts[1].toIntOrNull() ?: return 25
        val year = parts[2].toIntOrNull() ?: return 25
        val today = Calendar.getInstance()
        val birth = Calendar.getInstance()
        birth.set(year, month - 1, day)
        var age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) age--
        return age.coerceAtLeast(0)
    }
}
