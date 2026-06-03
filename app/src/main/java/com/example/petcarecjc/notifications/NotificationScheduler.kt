package com.example.petcarecjc.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.Calendar

object NotificationScheduler {

    @RequiresApi(Build.VERSION_CODES.S)
    fun schedule(
        context: Context,
        notifId: Int,
        channelId: String,
        title: String,
        message: String,
        triggerAtMillis: Long
    ) {
        scheduleExact(
            context         = context,
            notifId         = notifId,
            channelId       = channelId,
            title           = title,
            message         = message,
            triggerAtMillis = triggerAtMillis,
            isDaily         = false,
            dailyHour       = 0,
            dailyMinute     = 0
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleDaily(
        context: Context,
        notifId: Int,
        channelId: String,
        title: String,
        message: String,
        hour: Int,
        minute: Int
    ) {
        val trigger = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }.timeInMillis

        scheduleExact(
            context         = context,
            notifId         = notifId,
            channelId       = channelId,
            title           = title,
            message         = message,
            triggerAtMillis = trigger,
            isDaily         = true,
            dailyHour       = hour,
            dailyMinute     = minute
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleExact(
        context: Context,
        notifId: Int,
        channelId: String,
        title: String,
        message: String,
        triggerAtMillis: Long,
        isDaily: Boolean,
        dailyHour: Int,
        dailyMinute: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("notifId",     notifId)
            putExtra("channelId",   channelId)
            putExtra("title",       title)
            putExtra("message",     message)
            putExtra("isDaily",     isDaily)
            putExtra("dailyHour",   dailyHour)
            putExtra("dailyMinute", dailyMinute)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, notifId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val canExact = alarmManager.canScheduleExactAlarms()
        Log.d("NOTIF_SCHEDULER", "canScheduleExactAlarms=$canExact triggerAt=$triggerAtMillis ahora=${System.currentTimeMillis()} isDaily=$isDaily")

        if (canExact) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent
            )
            Log.d("NOTIF_SCHEDULER", " Alarma exacta programada")
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent
            )
            Log.w("NOTIF_SCHEDULER", "⚠Sin permiso exacto, usando set()")
        }
    }

    fun cancel(context: Context, notifId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, notifId, intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let { alarmManager.cancel(it) }
    }
}