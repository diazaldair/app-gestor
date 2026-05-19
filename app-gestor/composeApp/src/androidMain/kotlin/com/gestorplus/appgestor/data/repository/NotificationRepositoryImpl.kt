package com.gestorplus.appgestor.data.repository

import android.content.Context
import android.util.Log
import com.gestorplus.appgestor.core.firebase.getToken
import com.gestorplus.appgestor.core.notification.NotificationHelper
import com.gestorplus.appgestor.domain.notification.NotificationRepository
import com.gestorplus.appgestor.domain.notification.PushNotification

class NotificationRepositoryImpl(
    private val context: Context,
    private val notificationHelper: NotificationHelper
) : NotificationRepository {

    override suspend fun getFCMToken(): String? {
        return try {
            getToken()
        } catch (e: Exception) {
            Log.e("NotificationRepo", "Error getting FCM token", e)
            null
        }
    }

    override suspend fun handleReceivedNotification(notification: PushNotification) {
        Log.d("NotificationRepo", "Handling notification: ${notification.title}")
        
        // Aquí podrías guardar en Room si fuera necesario, 
        // pero siguiendo Clean Architecture, eso debería ser otro UseCase o en el Domain.
        // Por ahora, mostramos la notificación local.
        notificationHelper.showNotification(notification)
    }
}
