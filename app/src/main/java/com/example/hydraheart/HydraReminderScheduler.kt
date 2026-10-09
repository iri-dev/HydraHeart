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
    private const val PREFS = "hydra_heart_data"
    private const val KEY_REMINDERS = "reminders"
    private const val KEY_ENABLED = "reminders_enabled"
    private const val FALLBACK_WINDOW_MS = 10 * 60 * 1_000L

    fun canScheduleExactAlarms(context: Context): Boolean {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return false
        return Build.VERSION.SDK_INT < 31 || alarmManager.canScheduleExactAlarms()
    }

    fun sync(context: Context, reminders: List<HydraReminder>, enabled: Boolean) {
        reminders.forEach { reminder ->
            if (enabled && reminder.enabled) schedule(context, reminder) else cancel(context, reminder)
        }
    }

    fun schedule(context: Context, reminder: HydraReminder) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, reminder.hour)
            set(Calendar.MINUTE, reminder.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }
        val pendingIntent = pendingIntent(context, reminder)
        // Each alarm fires once and books the next day when it goes off, because
        // repeating alarms are inexact and can arrive hours late.
        if (canScheduleExactAlarms(context)) {
            val booked = runCatching {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }.isSuccess
            if (booked) return
        }
        // Without exact-alarm access, still ask for delivery within 10 minutes of the time.
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            FALLBACK_WINDOW_MS,
            pendingIntent
        )
    }

    fun cancel(context: Context, reminder: HydraReminder) {
        context.getSystemService(AlarmManager::class.java)?.cancel(pendingIntent(context, reminder))
    }

    fun rescheduleFromPreferences(context: Context) {
        sync(context, savedReminders(context), remindersEnabled(context))
    }

    /** Shows the reminder that just went off and books the same time for tomorrow. */
    internal fun onReminderFired(context: Context, id: Int) {
        if (!remindersEnabled(context)) return
        val reminder = savedReminders(context).firstOrNull { it.id == id && it.enabled } ?: return
        showNotification(context, reminder.id, reminder.label)
        schedule(context, reminder)
    }

    private fun remindersEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_ENABLED, false)

    private fun savedReminders(context: Context): List<HydraReminder> {
        val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_REMINDERS, null) ?: return emptyList()
        return runCatching {
            val json = JSONArray(raw)
            List(json.length()) { index ->
                val item = json.getJSONObject(index)
                HydraReminder(
                    id = item.getInt("id"),
                    hour = item.getInt("hour"),
                    minute = item.getInt("minute"),
                    label = item.getString("label"),
                    enabled = item.optBoolean("enabled", true)
                )
            }
        }.getOrDefault(emptyList())
    }

    private fun pendingIntent(context: Context, reminder: HydraReminder): PendingIntent {
        val intent = Intent(context, HydraReminderReceiver::class.java).apply {
            action = ACTION_REMINDER
            putExtra(EXTRA_ID, reminder.id)
        }
        return PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showNotification(context: Context, id: Int, label: String) {
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
        val openApp = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("HydraHeart")
            .setContentText(if (label.isBlank()) "Time for a sip 💧" else label)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(openApp)
            .setAutoCancel(true)
            .build()
        runCatching { NotificationManagerCompat.from(context).notify(id, notification) }
    }

    internal const val ACTION = ACTION_REMINDER
    internal const val ID = EXTRA_ID
}

class HydraReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == HydraReminderScheduler.ACTION) {
            HydraReminderScheduler.onReminderFired(
                context,
                intent.getIntExtra(HydraReminderScheduler.ID, 0)
            )
        }
    }
}

class HydraBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Alarms are cleared by a reboot or an app update, and they need booking again
        // once exact-alarm access is granted so they switch to arriving on time.
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED ->
                HydraReminderScheduler.rescheduleFromPreferences(context)
        }
    }
}
