package com.gestorplus.appgestor.domain.notification

data class PushNotification(
    val id: String,
    val title: String?,
    val body: String?,
    val data: Map<String, String>,
    val timestamp: Long = System.currentTimeMillis()
)
