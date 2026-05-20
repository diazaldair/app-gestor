package com.gestorplus.appgestor.notifications.presentation.state

sealed interface NotificationsEfffect {
    data class ShowToast(val message: String) : NotificationsEfffect
}
