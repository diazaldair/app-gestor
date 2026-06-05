package com.gestorplus.appgestor.notification.domain

data class PushNotification(
    val id: String,
    val title: String?,
    val body: String?,
    val data: Map<String, String>,
    val timestamp: Long = System.currentTimeMillis()
)
