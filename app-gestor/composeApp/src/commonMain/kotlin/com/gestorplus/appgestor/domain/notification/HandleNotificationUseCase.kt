package com.gestorplus.appgestor.domain.notification

class HandleNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: PushNotification) {
        // Aquí iría la lógica de negocio, por ejemplo:
        // - Guardar en base de datos local (Room)
        // - Actualizar algún estado global
        // - Validar si la notificación es relevante
        repository.handleReceivedNotification(notification)
    }
}
