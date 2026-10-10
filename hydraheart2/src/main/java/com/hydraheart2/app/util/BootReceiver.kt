package com.hydraheart2.app.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hydraheart2.app.data.models.CustomReminder

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val prefs = context.getSharedPreferences("hydra_prefs", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("reminders_enabled", false)) return

        val gson = Gson()
        val remindersJson = prefs.getString("custom_reminders", null) ?: return
        val messagesJson = prefs.getString("reminder_messages", null)
        val sound = prefs.getString("reminder_sound", "default") ?: "default"
        val messages: List<String> = if (messagesJson != null) {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(messagesJson, type)!!
        } else {
            emptyList()
        }
        val type = object : TypeToken<List<CustomReminder>>() {}.type
        val reminders: List<CustomReminder> = gson.fromJson(remindersJson, type)
        val pairs = reminders.map { reminder ->
            val parts = reminder.time.split(":")
            (parts[0].toIntOrNull() ?: 8) to (parts.getOrNull(1)?.toIntOrNull() ?: 0)
        }
        NotificationHelper.rescheduleAll(context, pairs, messages, sound)
    }
}
