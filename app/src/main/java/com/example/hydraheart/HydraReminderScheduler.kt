package com.example.hydraheart

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.json.JSONArray
import java.util.Calendar

object HydraReminderScheduler {
    private const val CHANNEL_ID = "hydra_reminders"
    private const val ACTION_REMINDER = "com.example.hydraheart.ACTION_REMINDER"
    private const val EXTRA_ID = "reminder_id"
    private const val EXTRA_LABEL = "reminder_label"
    private const val PREFS = "hydra_heart_data"
    private const val KEY_REMINDERS = "reminders"
    private const val KEY_ENABLED = "reminders_enabled"
    private const val DAY_MS = 24 * 60 * 60 * 1_000L

    fun sync(context: Context, reminders: List<HydraReminder>, enabled: Boolean) {
        reminders.forEach { reminder ->
            if (enabled) schedule(context, reminder) else cancel(context, reminder)
        }
    }

    fun schedule(context: Context, reminder: HydraReminder) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        if (Build.VERSION.SDK_INT >= 31 && !alarmManager.canScheduleExactAlarms()) return
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, reminder.hour)
            set(Calendar.MINUTE, reminder.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            DAY_MS,
            pendingIntent(context, reminder)
        )
    }

    fun cancel(context: Context, reminder: HydraReminder) {
        context.getSystemService(AlarmManager::class.java)?.cancel(pendingIntent(context, reminder))
    }

    fun rescheduleFromPreferences(context: Context) {
        val preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (!preferences.getBoolean(KEY_ENABLED, false)) return
        val raw = preferences.getString(KEY_REMINDERS, null) ?: return
        runCatching {
            val json = JSONArray(raw)
            for (index in 0 until json.length()) {
                val item = json.getJSONObject(index)
                schedule(
                    context,
                    HydraReminder(
                        id = item.getInt("id"),
                        hour = item.getInt("hour"),
                        minute = item.getInt("minute"),
                        label = item.getString("label"),
                        enabled = item.optBoolean("enabled", true)
                    )
                )
            }
        }
    }

    private fun pendingIntent(context: Context, reminder: HydraReminder): PendingIntent {
        val intent = Intent(context, HydraReminderReceiver::class.java).apply {
            action = ACTION_REMINDER
            putExtra(EXTRA_ID, reminder.id)
            putExtra(EXTRA_LABEL, reminder.label)
        }
        return PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    internal fun showNotification(context: Context, id: Int, label: String) {
        val manager = context.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            manager?.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "Hydration reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("HydraHeart")
            .setContentText(if (label.isBlank()) "Time for a sip 💧" else label)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        runCatching { NotificationManagerCompat.from(context).notify(id, notification) }
    }

    internal const val ACTION = ACTION_REMINDER
    internal const val ID = EXTRA_ID
    internal const val LABEL = EXTRA_LABEL
}

class HydraReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == HydraReminderScheduler.ACTION) {
            HydraReminderScheduler.showNotification(
                context,
                intent.getIntExtra(HydraReminderScheduler.ID, 0),
                intent.getStringExtra(HydraReminderScheduler.LABEL).orEmpty()
            )
        }
    }
}

class HydraBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            HydraReminderScheduler.rescheduleFromPreferences(context)
        }
    }
}

