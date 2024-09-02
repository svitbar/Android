package com.example.lab5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action == "android.intent.action.MIDNIGHT_ALARM") {
            Log.d("Hello", "midnight")

//            val sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
//            sharedPreferences.edit().putInt("key1", intent.getIntExtra("stepCount", 0)).apply()
        }
    }
}