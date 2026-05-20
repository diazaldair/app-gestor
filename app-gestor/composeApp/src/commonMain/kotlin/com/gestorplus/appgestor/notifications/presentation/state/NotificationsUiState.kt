package com.gestorplus.appgestor.notifications.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.notifications.domain.model.Notification

@Immutable
data class NotificationsUiState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val selectedFilter: String = "Todas"
)
