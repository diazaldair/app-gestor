package com.gestorplus.appgestor.notifications.presentation.state

import com.gestorplus.appgestor.notifications.domain.model.AppNotification

data class NotificationsUiState(
    val isLoading: Boolean = true,
    val notifications: List<AppNotification> = emptyList(),
    val filter: NotificationFilter = NotificationFilter.All,
    val error: String? = null
)

enum class NotificationFilter { All, Appointments, Messages, System }
