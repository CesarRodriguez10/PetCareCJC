package com.example.petcarecjc.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.petcarecjc.R

object NotificationHelper {

    const val CHANNEL_VACUNAS      = "channel_vacunas"
    const val CHANNEL_CITAS        = "channel_citas"
    const val CHANNEL_ALIMENTACION = "channel_alimentacion"
    const val CHANNEL_CUMPLE       = "channel_cumple"

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannels(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        val channels = listOf(
            NotificationChannel(CHANNEL_VACUNAS,      "Vacunas y Medicamentos", NotificationManager.IMPORTANCE_HIGH),
            NotificationChannel(CHANNEL_CITAS,        "Citas Veterinarias",     NotificationManager.IMPORTANCE_HIGH),
            NotificationChannel(CHANNEL_ALIMENTACION, "Alimentación",           NotificationManager.IMPORTANCE_DEFAULT),
            NotificationChannel(CHANNEL_CUMPLE,       "Cumpleaños",             NotificationManager.IMPORTANCE_DEFAULT)
        )

        channels.forEach { ch ->
            ch.description = "Notificaciones de PetCare CJC"
            manager.createNotificationChannel(ch)
        }
    }

    fun send(
        context: Context,
        channelId: String,
        notifId: Int,
        title: String,
        message: String
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        val notif = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(notifId, notif)
    }
}