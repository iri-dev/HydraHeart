package com.hydraheart2.app.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.content.edit
import com.google.gson.Gson
import com.hydraheart2.app.data.models.CustomReminder
import java.util.Calendar

object NotificationHelper {
    val CURATED_SOUNDS: List<Pair<String, String>> = listOf(
        "hydra_chime" to "Gentle Chime",
        "hydra_drop" to "Water Drop",
        "hydra_crystal" to "Crystal",
        "hydra_bowl" to "Singing Bowl",
        "hydra_shimmer" to "Shimmer",
        "hydra_bell" to "Soft Bell"
    )

    internal fun getSystemNotificationUri(context: Context, slot: Int): Uri =
        try {
            val rm = RingtoneManager(context)
            rm.setType(RingtoneManager.TYPE_NOTIFICATION)
            val count = rm.cursor?.count ?: 0
            val ringtoneUri = if (count > 0) {
                rm.getRingtoneUri(slot % count)
            } else {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }
            ringtoneUri!!
        } catch (e: Exception) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)!!
        }

    fun getNotificationSounds(context: Context): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        result.add("silent" to "No Sound")
        result.add("default" to "System Default")
        CURATED_SOUNDS.forEach { result.add(it) }
        return result
    }

    fun getSoundUri(context: Context, soundId: String): Uri? {
        if (soundId == "silent") return null
        if (soundId == "default") return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (soundId.startsWith("hydra_")) {
            val resId = context.resources.getIdentifier(soundId, "raw", context.packageName)
            if (resId != 0) {
                return Uri.parse("android.resource://" + context.packageName + "/raw/" + soundId)
            }
            val slot = CURATED_SOUNDS.indexOfFirst { it.first == soundId }.coerceAtLeast(0)
            return getSystemNotificationUri(context, slot)
        }
        return try {
            Uri.parse(soundId)
        } catch (e: Exception) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
    }

    fun ensureChannel(context: Context, nm: NotificationManager, soundUriString: String): String {
        val channelId = when {
            soundUriString == "default" -> "hydra_rem_default_v2"
            soundUriString == "silent" -> "hydra_rem_silent_v2"
            soundUriString.startsWith("hydra_") -> "hydra_rem_" + soundUriString + "_v2"
            else -> "hydra_rem_" + soundUriString.hashCode().let { if (it < 0) "n${-it}" else "p$it" } + "_v2"
        }
        if (nm.getNotificationChannel(channelId) != null) return channelId

        val soundUri = getSoundUri(context, soundUriString)

        val channel = NotificationChannel(
            channelId,
            "Hydration Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Gentle reminders from Hydra to stay hydrated"
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 100, 50, 100)
            if (soundUri != null) {
                setSound(soundUri, AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build())
            } else {
                setSound(null, null)
            }
        }
        nm.createNotificationChannel(channel)
        return channelId
    }

    internal fun scheduleExactAt(
        context: Context,
        triggerAtMillis: Long,
        requestCode: Int,
        hour: Int,
        minute: Int,
        messages: List<String>,
        soundUri: String
    ) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra("hour", hour)
        intent.putExtra("minute", minute)
        intent.putExtra("request_code", requestCode)
        intent.putExtra("sound_uri", soundUri)
        if (messages.isNotEmpty()) {
            intent.putExtra("custom_messages", messages.toTypedArray())
        }
        val pi = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (Build.VERSION.SDK_INT >= 31 && !am.canScheduleExactAlarms()) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
        } else {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
        }
    }

    fun scheduleReminder(
        context: Context,
        hour: Int,
        minute: Int,
        requestCode: Int,
        customMessages: List<String> = emptyList(),
        soundUriString: String = "default"
    ) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (!calendar.after(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        val triggerMs = calendar.timeInMillis
        scheduleExactAt(context, triggerMs, requestCode, hour, minute, customMessages, soundUriString)
    }

    fun cancelReminder(context: Context, requestCode: Int) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(context, ReminderReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        am.cancel(pi)
    }

    fun rescheduleAll(
        context: Context,
        reminders: List<Pair<Int, Int>>,
        customMessages: List<String>,
        soundUriString: String = "default"
    ) {
        for (i in 0 until 20) cancelReminder(context, i + 1000)
        reminders.forEachIndexed { index, (hour, minute) ->
            scheduleReminder(context, hour, minute, index + 1000, customMessages, soundUriString)
        }
    }

    fun saveBootData(
        context: Context,
        remindersEnabled: Boolean,
        reminders: List<CustomReminder>,
        messages: List<String>,
        sound: String
    ) {
        val gson = Gson()
        context.getSharedPreferences("hydra_prefs", Context.MODE_PRIVATE).edit {
            putBoolean("reminders_enabled", remindersEnabled)
            putString("custom_reminders", gson.toJson(reminders))
            putString("reminder_messages", gson.toJson(messages))
            putString("reminder_sound", sound)
        }
    }
}
