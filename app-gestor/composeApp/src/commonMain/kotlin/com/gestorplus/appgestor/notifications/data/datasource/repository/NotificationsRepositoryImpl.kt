package com.gestorplus.appgestor.notifications.data.datasource.repository

import com.gestorplus.appgestor.notifications.domain.model.Notification
import com.gestorplus.appgestor.notifications.domain.model.NotificationType
import com.gestorplus.appgestor.notifications.domain.repository.NotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NotificationsRepositoryImpl : NotificationsRepository {
    override fun getNotifications(): Flow<List<Notification>> {
        return flowOf(
            listOf(
                Notification(
                    id = "1",
                    title = "Nueva solicitud de cita",
                    description = "Carlos Mendoza ha solicitado una consulta de Cardiología para el viernes 24 de mayo a las 15:30.",
                    time = "10m",
                    type = NotificationType.APPOINTMENT_REQUEST,
                    dateCategory = "HOY"
                ),
                Notification(
                    id = "2",
                    title = "Recordatorio de cita",
                    description = "Su próxima cita con Lucía Fernández comienza en 60 minutos.",
                    time = "1h",
                    type = NotificationType.REMINDER,
                    dateCategory = "HOY"
                ),
                Notification(
                    id = "3",
                    title = "Actualización del sistema",
                    description = "La plataforma SoloBook ha sido actualizada a la versión 2.4 con mejoras en la gestión de expedientes.",
                    time = "28h",
                    type = NotificationType.SYSTEM_UPDATE,
                    dateCategory = "AYER"
                )
            )
        )
    }

    override suspend fun acceptAppointment(notificationId: String) {
        // Implementación real aquí
    }

    override suspend fun declineAppointment(notificationId: String) {
        // Implementación real aquí
    }
}
