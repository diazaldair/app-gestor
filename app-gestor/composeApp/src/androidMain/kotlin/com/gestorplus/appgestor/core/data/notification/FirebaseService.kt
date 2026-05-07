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
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import com.gestorplus.appgestor.data.repository.OwnerBookingRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FirebaseService : FirebaseMessagingService() {

    private val repository: OwnerBookingRepository by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.w(TAG, "¡NOTIFICACIÓN RECIBIDA!")

        // 1. Extraer datos del mensaje
        val id = remoteMessage.data["id"] ?: System.currentTimeMillis().toString()
        val client = remoteMessage.data["client"] ?: "Cliente Nuevo"
        val service = remoteMessage.data["service"] ?: "Servicio General"
        val price = remoteMessage.data["price"]?.toDoubleOrNull() ?: 0.0
        
        val title = remoteMessage.notification?.title ?: "Nueva Cita"
        val body = remoteMessage.notification?.body ?: "Has recibido una nueva solicitud de $client"

        // 2. GUARDAR EN ROOM AUTOMÁTICAMENTE
        serviceScope.launch {
            val newBooking = BookingEntity(
                id = id,
                clientName = client,
                serviceName = service,
                timestamp = System.currentTimeMillis(),
                durationMinutes = 60,
                status = "PENDING",
                price = price,
                categoryColor = 0xFF6200EE // Color por defecto
            )
            repository.addBooking(newBooking)
            Log.d(TAG, "Cita guardada en Room desde Push: $client")
        }

        // 3. Mostrar la notificación visual
        sendNotification(title, body)
    }

    private fun sendNotification(title: String, messageBody: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "fcm_default_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificaciones de Gestión",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "FCM_SERVICE"
    }
}
