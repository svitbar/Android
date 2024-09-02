package com.example.lab6.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.lab6.ChatActivity
import com.example.lab6.R
import com.example.lab6.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val ref = FirebaseDatabase.getInstance().getReference("/users")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val userList = mutableListOf<User>()
            snapshot.children.forEach { dataSnapshot ->
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    userList.add(it)
                }
            }

            val currentUid = FirebaseAuth.getInstance().uid
            val widgetUserList = mutableListOf<User>()

            userList.forEach { user ->
                if (user.uid != currentUid) {
                    widgetUserList.add(user)
                }
            }

            val views = RemoteViews(context.packageName, determineLayout(widgetUserList))

            if (widgetUserList.size >= 2) {
                val userId1 = widgetUserList[0].uid
                val username1 = widgetUserList[0].username
                val email1 = widgetUserList[0].email

                val intentUser1 = Intent(context, ChatActivity::class.java).apply {
                    action = "OPEN_CHAT_ROOM_FRAGMENT"
                    putExtra("USER_ID", userId1)
                    putExtra("USERNAME", username1)
                    putExtra("EMAIL", email1)
                }

                val pendingIntentUser1 = PendingIntent.getActivity(context, 0, intentUser1, PendingIntent.FLAG_UPDATE_CURRENT)

                views.setTextViewText(R.id.first_chat_user, username1)
                views.setOnClickPendingIntent(R.id.first_container, pendingIntentUser1)

                val userId2 = widgetUserList[1].uid
                val username2 = widgetUserList[1].username
                val email2 = widgetUserList[1].email

                val intentUser2 = Intent(context, ChatActivity::class.java).apply {
                    action = "OPEN_CHAT_ROOM_FRAGMENT"
                    putExtra("USER_ID", userId2)
                    putExtra("USERNAME", username2)
                    putExtra("EMAIL", email2)
                }

                val pendingIntentUser2 = PendingIntent.getActivity(context, 1, intentUser2, PendingIntent.FLAG_UPDATE_CURRENT)

                views.setTextViewText(R.id.second_chat_user, username2)
                views.setOnClickPendingIntent(R.id.second_container, pendingIntentUser2)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } else {
                val userId1 = widgetUserList[0].uid
                val username1 = widgetUserList[0].username
                val email1 = widgetUserList[0].email

                val intentUser1 = Intent(context, ChatActivity::class.java).apply {
                    action = "OPEN_CHAT_ROOM_FRAGMENT"
                    putExtra("USER_ID", userId1)
                    putExtra("USERNAME", username1)
                    putExtra("EMAIL", email1)
                }

                val pendingIntentUser1 = PendingIntent.getActivity(context, 0, intentUser1, PendingIntent.FLAG_UPDATE_CURRENT)

                views.setTextViewText(R.id.first_chat_user, username1)
                views.setOnClickPendingIntent(R.id.first_container, pendingIntentUser1)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("ChatListFragment", "Failed to read value.", error.toException())
        }
    })
}

private fun determineLayout(widgetUserList: List<User>): Int {
    return if (widgetUserList.size >= 2) {
        R.layout.chat_widget_default
    } else {
        R.layout.chat_widget_one_container
    }
}