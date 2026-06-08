package com.gestorplus.appgestor.notifications.presentation.state

sealed interface NotificationsEvent {
    data class Accept(val id: String) : NotificationsEvent
    data class Decline(val id: String) : NotificationsEvent
    data class MarkRead(val id: String) : NotificationsEvent
    data class ChangeFilter(val filter: NotificationFilter) : NotificationsEvent
    object Refresh : NotificationsEvent
}
