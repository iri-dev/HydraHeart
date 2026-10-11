package com.hydraheart2.app.util

import com.hydraheart2.app.data.models.ActivityLevel
import com.hydraheart2.app.data.models.Climate
import com.hydraheart2.app.data.models.Gender
import com.hydraheart2.app.data.models.Profile

object GoalCalculator {
    fun calculate(profile: Profile): DailyGoals {
        val gender = profile.gender ?: Gender.FEMALE
        val age = profile.age
        val weightKg = profile.weightKg
        val activityLevel = profile.activityLevel ?: ActivityLevel.LIGHTLY_ACTIVE
        val climate = profile.climate ?: Climate.MODERATE

        var waterMl = (35f * weightKg).toInt()
        waterMl += when (activityLevel) {
            ActivityLevel.SEDENTARY -> 0
            ActivityLevel.LIGHTLY_ACTIVE -> 300
            ActivityLevel.MODERATE -> 600
            ActivityLevel.VERY_ACTIVE -> 1000
        }
        waterMl += when (climate) {
            Climate.COLD -> -200
            Climate.MODERATE -> 0
            Climate.HOT -> 600
        }
        if (age > 65) waterMl = (waterMl * 0.9f).toInt()
        waterMl = waterMl.coerceIn(1200, 5000)

        return DailyGoals(
            waterMl,
            if (age < 18) 100 else 400,
            50,
            if (age > 50 && gender == Gender.FEMALE) 1200 else 1000,
            16,
            2300,
            when (gender) {
                Gender.FEMALE -> 2600
                Gender.MALE -> 3400
                Gender.OTHER -> 3000
            },
            when (gender) {
                Gender.FEMALE -> 320
                Gender.MALE -> 420
                Gender.OTHER -> 370
            }
        )
    }
}
