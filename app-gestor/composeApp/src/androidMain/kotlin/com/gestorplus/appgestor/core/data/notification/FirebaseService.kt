package com.gestorplus.appgestor.core.data.notification

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gestorplus.appgestor.MainActivity
import com.gestorplus.appgestor.R

class FirebaseService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FirebaseService"
        private const val CHANNEL_ID = "default_channel"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let { hasData ->
            if (hasData) {
                Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            }
        }

        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Message Notification Body: ${notification.body}")
            sendCustomNotification(notification.title, notification.body)
        }
    }

    private fun sendCustomNotification(title: String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("OPEN_SCREEN", "GITHUB")
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle(title ?: "Nueva Notificación")
            .setContentText(messageBody ?: "Tienes un mensaje nuevo")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(resources.getColor(R.color.colorAccent, null))

        with(NotificationManagerCompat.from(this)) {
            notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        }
    }
}
