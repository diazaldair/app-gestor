package com.gestorplus.appgestor.notifications.data.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id: String? = null,
    val title: String? = null,
    val body: String? = null,
    val timestamp: Long? = null,
    val type: String? = null
)
