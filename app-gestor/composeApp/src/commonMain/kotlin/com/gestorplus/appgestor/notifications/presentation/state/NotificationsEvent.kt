package com.gestorplus.appgestor.notifications.presentation.state

sealed interface NotificationsEvent {
    data class OnFilterSelected(val filter: String) : NotificationsEvent
    data class OnAcceptAppointment(val id: String) : NotificationsEvent
    data class OnDeclineAppointment(val id: String) : NotificationsEvent
}
