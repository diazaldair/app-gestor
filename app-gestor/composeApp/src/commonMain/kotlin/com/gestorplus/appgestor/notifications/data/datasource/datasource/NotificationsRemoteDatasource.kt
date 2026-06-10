package com.gestorplus.appgestor.notifications.data.datasource.datasource

import com.gestorplus.appgestor.notifications.data.datasource.dto.NotificationDto
import com.gestorplus.appgestor.notifications.data.datasource.service.NotificationsService

class NotificationsRemoteDatasource(private val service: NotificationsService) {
    suspend fun getNotifications(userUid: String): List<NotificationDto> = service.getNotifications(userUid)
    suspend fun acceptAppointment(userUid: String, notificationId: String) = service.updateAppointmentStatus(userUid, notificationId, "ACCEPTED")
    suspend fun declineAppointment(userUid: String, notificationId: String, reason: String? = null) = 
        service.updateAppointmentStatus(userUid, notificationId, "DECLINED", reason)
    suspend fun markAsRead(userUid: String, notificationId: String) = service.markAsRead(userUid, notificationId)
}
