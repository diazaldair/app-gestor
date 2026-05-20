package com.gestorplus.appgestor.notifications.domain.usecase

import com.gestorplus.appgestor.notifications.domain.repository.NotificationsRepository

class GetNotificationsUseCase(private val repository: NotificationsRepository) {
    operator fun invoke() = repository.getNotifications()
}
