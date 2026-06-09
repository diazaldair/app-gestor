package com.gestorplus.appgestor.notifications.presentation.state

sealed interface NotificationsEfffect {
    data class ShowMessage(val message: String) : NotificationsEfffect
}
