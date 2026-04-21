package com.gestorplus.appgestor.core.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.gestorplus.appgestor.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // LOG CRITICO: Esto DEBE aparecer en el Logcat si Firebase está enviando algo
        Log.w(TAG, "¡¡¡ EVENTO RECIBIDO !!!")
        Log.d(TAG, "De: ${remoteMessage.from}")

        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "Notificación Forzada"
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "Mensaje recibido sin cuerpo"

        Log.d(TAG, "Procesando Notificación: $title - $body")
        sendNotification(title, body)
    }

    override fun onNewToken(token: String) {
        Log.w(TAG, "TOKEN REFRESCADO: $token")
    }

    private fun sendNotification(title: String, messageBody: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "fcm_test_channel"

        // Forzamos la creación del canal con importancia MAXIMA
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pruebas Firebase",
                NotificationManager.IMPORTANCE_HIGH // IMPORTANCIA ALTA para que suene y salga el globo
            ).apply {
                description = "Canal para pruebas de notificaciones"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 
            System.currentTimeMillis().toInt(), 
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // PRIORIDAD ALTA
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        Log.d(TAG, "Comando de notificación enviado al sistema Android")
    }

    companion object {
        private const val TAG = "FCM_SERVICE"
    }
}
