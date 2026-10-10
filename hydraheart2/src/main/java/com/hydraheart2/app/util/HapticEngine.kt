package com.hydraheart2.app.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object HapticEngine {
    var isEnabled: Boolean = true

    private fun vibrator(context: Context): Vibrator =
        if (Build.VERSION.SDK_INT >= 31) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

    fun tick(context: Context) {
        if (!isEnabled) return
        if (Build.VERSION.SDK_INT >= 29) {
            vibrator(context).vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator(context).vibrate(8L)
        }
    }

    fun click(context: Context) {
        if (!isEnabled) return
        if (Build.VERSION.SDK_INT >= 29) {
            vibrator(context).vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator(context).vibrate(15L)
        }
    }

    fun heavyClick(context: Context) {
        if (!isEnabled) return
        if (Build.VERSION.SDK_INT >= 29) {
            vibrator(context).vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator(context).vibrate(35L)
        }
    }

    fun doubleTap(context: Context) {
        if (!isEnabled) return
        if (Build.VERSION.SDK_INT >= 29) {
            vibrator(context).vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator(context).vibrate(longArrayOf(0, 15, 60, 15), -1)
        }
    }

    fun celebration(context: Context) {
        if (!isEnabled) return
        vibrator(context).vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(0, 12, 50, 12, 50, 25, 40, 35),
                intArrayOf(0, 100, 0, 80, 0, 120, 0, 200),
                -1
            )
        )
    }

    fun warning(context: Context) {
        if (!isEnabled) return
        vibrator(context).vibrate(
            VibrationEffect.createWaveform(longArrayOf(0, 25, 80, 20), intArrayOf(0, 80, 0, 50), -1)
        )
    }

    fun liquidPour(context: Context) {
        if (!isEnabled) return
        vibrator(context).vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(0, 8, 20, 8, 20, 8, 20, 8, 20, 8, 20, 12),
                intArrayOf(0, 40, 0, 50, 0, 60, 0, 70, 0, 80, 0, 100),
                -1
            )
        )
    }
}
