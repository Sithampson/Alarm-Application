package com.example.alarmapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import com.example.alarmapplication.data.DatabaseHelper
import java.util.*

class RebootReceiver : BroadcastReceiver() {
    private var alarmManager: AlarmManager? = null
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {

            val cursor: Cursor = DatabaseHelper(context).getEnabledAlarms()

            while (cursor.moveToNext()) {
                val allDays = BooleanArray(size = 7)

                val id = cursor.getLong(0)
                val time = cursor.getLong(1)
                val label = cursor.getString(2)
                allDays[0] = cursor.getInt(3) == 1
                allDays[1] = cursor.getInt(4) == 1
                allDays[2] = cursor.getInt(5) == 1
                allDays[3] = cursor.getInt(6) == 1
                allDays[4] = cursor.getInt(7) == 1
                allDays[5] = cursor.getInt(8) == 1
                allDays[6] = cursor.getInt(9) == 1

                val c = Calendar.getInstance()
                val currentTime = System.currentTimeMillis()
                val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
                c.timeInMillis = time

                if(c.timeInMillis > currentTime){
                    val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                        intent.putExtra("Alarm_label", label)
                        intent.putExtra("Alarm_id", id)
                        PendingIntent.getBroadcast(
                            context, id.toInt(), intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }

                    if(!allDays[0] && !allDays[1] && !allDays[2] && !allDays[3] && !allDays[4] && !allDays[5] && !allDays[6]){
                        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, alarmIntent)
                    }

                    else if(allDays[dayOfWeek-1]){
                        c.set(Calendar.DAY_OF_WEEK, dayOfWeek)
                        alarmManager?.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            c.timeInMillis,
                            AlarmManager.INTERVAL_DAY * 7,
                            alarmIntent
                        )
                    }
                }
            }
        }
    }
}