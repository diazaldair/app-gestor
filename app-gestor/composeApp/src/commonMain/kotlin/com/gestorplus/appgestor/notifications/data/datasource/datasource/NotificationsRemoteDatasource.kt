package com.gestorplus.appgestor.notifications.data.datasource.datasource

import com.gestorplus.appgestor.notifications.data.datasource.dto.NotificationDto
import com.gestorplus.appgestor.notifications.data.datasource.service.NotificationsService

class NotificationsRemoteDatasource(private val service: NotificationsService) {
    suspend fun getNotifications(userUid: String): List<NotificationDto> = service.getNotifications(userUid)
    
    suspend fun acceptAppointment(
        userUid: String, 
        notificationId: String,
        bookingId: String?,
        patientUid: String?,
        clinicId: String?,
        date: Int?,
        timeSlot: String?
    ) = service.updateAppointmentStatus(userUid, notificationId, "ACCEPTED", bookingId, patientUid, clinicId, date, timeSlot)

    suspend fun declineAppointment(
        userUid: String, 
        notificationId: String,
        bookingId: String?,
        patientUid: String?,
        clinicId: String?,
        date: Int?,
        timeSlot: String?
    ) = service.updateAppointmentStatus(userUid, notificationId, "DECLINED", bookingId, patientUid, clinicId, date, timeSlot)

    suspend fun markAsRead(userUid: String, notificationId: String) = service.markAsRead(userUid, notificationId)
}
