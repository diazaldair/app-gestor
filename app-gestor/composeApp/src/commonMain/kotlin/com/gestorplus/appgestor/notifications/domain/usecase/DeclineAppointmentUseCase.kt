package com.gestorplus.appgestor.notifications.domain.usecase

import com.gestorplus.appgestor.notifications.domain.repository.NotificationsRepository

class DeclineAppointmentUseCase(private val repository: NotificationsRepository) {
    suspend operator fun invoke(notificationId: String) = repository.declineAppointment(notificationId)
}
