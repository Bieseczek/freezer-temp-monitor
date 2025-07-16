package com.example.projektzespoowy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessageReceiver : FirebaseMessagingService() {

    // Override onNewToken to handle token changes if needed
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG", "New token: $token")
        // You can send the token to the server if needed for specific notifications
    }

    // Override onMessageReceived() to handle incoming FCM messages
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("TAG", "From: ${remoteMessage.from}")

        // Handle the data payload if available
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("TAG", "Message data payload: ${remoteMessage.data}")
        }

        // Handle the notification payload if available
        remoteMessage.notification?.let {
            Log.d("TAG", "Message Notification Title: ${it.title}")
            Log.d("TAG", "Message Notification Body: ${it.body}")
            // Show notification using custom method
            
        }
    }

    // Subscribe to the "global" topic when the service starts
    override fun onCreate() {
        super.onCreate()
        subscribeToGlobalTopic()
    }

    // Method to subscribe to the global topic
    private fun subscribeToGlobalTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("global")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "Subscribed to global topic")
                } else {
                    Log.d("TAG", "Subscription to global topic failed")
                }
            }
    }
}
