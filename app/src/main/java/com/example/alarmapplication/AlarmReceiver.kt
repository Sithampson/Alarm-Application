package com.example.alarmapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class AlarmReceiver: BroadcastReceiver() {
    private val CHANNEL_ID = "alarm_channel"

    override fun onReceive(context: Context, intent: Intent) {

        val label = intent.getStringExtra("Alarm_label")
        val id = intent.getLongExtra("Alarm_id", 0).toInt()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_alarm_white)
        builder.color = ContextCompat.getColor(context, R.color.colorAccent)
        builder.setContentTitle(context.getString(R.string.app_name))
        builder.setContentText(label)
        builder.setTicker(label)
        builder.setVibrate(longArrayOf(1000, 500, 1000, 500, 1000, 500))
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        builder.setAutoCancel(true)
        builder.priority = Notification.PRIORITY_HIGH

        manager.notify(id, builder.build())

    }

    private fun createNotificationChannel(ctx: Context) {
        if (VERSION.SDK_INT < VERSION_CODES.O) return
        val mgr = ctx.getSystemService(NotificationManager::class.java)
            ?: return
        val name = ctx.getString(R.string.channel_name)
        if (mgr.getNotificationChannel(name) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 500, 1000, 500, 1000, 500)
            channel.setBypassDnd(true)
            mgr.createNotificationChannel(channel)
        }
    }

}