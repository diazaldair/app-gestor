package com.gestorplus.appgestor.notifications.domain.usecase

import com.gestorplus.appgestor.notifications.domain.model.AppNotification
import com.gestorplus.appgestor.notifications.domain.repository.NotificationsRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationsUseCase(private val repository: NotificationsRepository) {
    operator fun invoke(): Flow<List<AppNotification>> = repository.getNotifications()
}
