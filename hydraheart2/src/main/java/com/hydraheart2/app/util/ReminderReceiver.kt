package com.hydraheart2.app.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.hydraheart2.app.R
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        val DEFAULT_MESSAGES = listOf(
            "Hey gorgeous! Time for some water 💧",
            "Hydra misses you! Come say hi and log a drink 💕",
            "Your body needs you — grab some water! 🌊",
            "Quick reminder to hydrate, beautiful! ✨",
            "Water break time! Your skin will thank you 🤍",
            "Don't forget about me! — Hydra 💖"
        )
    }

    override fun onReceive(context: Context, intent: Intent) {
        val hour = intent.getIntExtra("hour", 8)
        val minute = intent.getIntExtra("minute", 0)
        val requestCode = intent.getIntExtra("request_code", 1000)
        val soundUri = intent.getStringExtra("sound_uri") ?: "default"
        val messages = intent.getStringArrayExtra("custom_messages")?.toList() ?: DEFAULT_MESSAGES
        val message = messages.random()

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = NotificationHelper.ensureChannel(context, nm, soundUri)

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val tapIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("HydraHeart 💕")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setBigContentTitle("HydraHeart 💕")
                    .setSummaryText("Hydration Reminder")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(tapIntent)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()
        nm.notify(requestCode, notification)

        // Book the same reminder for tomorrow.
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val tomorrowMs = calendar.timeInMillis
        NotificationHelper.scheduleExactAt(context, tomorrowMs, requestCode, hour, minute, messages, soundUri)
    }
}
