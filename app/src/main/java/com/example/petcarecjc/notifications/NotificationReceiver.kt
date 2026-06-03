package com.example.petcarecjc.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.Calendar

class NotificationReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NOTIF_RECEIVER", " onReceive disparado")

        val channelId   = intent.getStringExtra("channelId")   ?: return
        val title       = intent.getStringExtra("title")       ?: return
        val message     = intent.getStringExtra("message")     ?: return
        val notifId     = intent.getIntExtra("notifId", 0)
        val isDaily     = intent.getBooleanExtra("isDaily", false)
        val dailyHour   = intent.getIntExtra("dailyHour", 8)
        val dailyMinute = intent.getIntExtra("dailyMinute", 0)

        NotificationHelper.send(context, channelId, notifId, title, message)
        Log.d("NOTIF_RECEIVER", "Notificación enviada")

        if (isDaily) {
            val nextTrigger = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, dailyHour)
                set(Calendar.MINUTE, dailyMinute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.DAY_OF_YEAR, 1)
            }.timeInMillis

            NotificationScheduler.scheduleExact(
                context         = context,
                notifId         = notifId,
                channelId       = channelId,
                title           = title,
                message         = message,
                triggerAtMillis = nextTrigger,
                isDaily         = true,
                dailyHour       = dailyHour,
                dailyMinute     = dailyMinute
            )
            Log.d("NOTIF_RECEIVER", " Reprogramada para mañana a $dailyHour:$dailyMinute")
        }
    }
}