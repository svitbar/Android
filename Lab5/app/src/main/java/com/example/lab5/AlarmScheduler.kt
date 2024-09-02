package com.example.lab5

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.util.Calendar

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private lateinit var midnightIntent: PendingIntent
    fun schedule() {
        val midnight = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            add(Calendar.DAY_OF_YEAR, 1)
        }

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = "android.intent.action.MIDNIGHT_ALARM"
        midnightIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            midnight.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            midnightIntent
        )
    }

    fun cancel(time: LocalDateTime, mess: String) {

    }
}