package com.gestorplus.appgestor.core.data.notification

import android.util.Log
import com.gestorplus.appgestor.notification.domain.HandleNotificationUseCase
import com.gestorplus.appgestor.notification.domain.PushNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FirebaseService : FirebaseMessagingService() {

    private val handleNotificationUseCase: HandleNotificationUseCase by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        val notification = PushNotification(
            id = remoteMessage.messageId ?: System.currentTimeMillis().toString(),
            title = remoteMessage.notification?.title ?: remoteMessage.data["title"],
            body = remoteMessage.notification?.body ?: remoteMessage.data["body"],
            data = remoteMessage.data
        )

        serviceScope.launch {
            handleNotificationUseCase(notification)
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // En una implementación real, aquí se enviaría el token al servidor
    }

    companion object {
        private const val TAG = "FirebaseService"
    }
}
