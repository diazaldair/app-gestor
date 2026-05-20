package com.gestorplus.appgestor.notifications.data.datasource.mapper

import com.gestorplus.appgestor.notifications.data.datasource.dto.NotificationDto
import com.gestorplus.appgestor.notifications.domain.model.Notification
import com.gestorplus.appgestor.notifications.domain.model.NotificationType

class NotificationsMapper {
    fun toDomain(dto: NotificationDto): Notification {
        return Notification(
            id = dto.id ?: "",
            title = dto.title ?: "",
            description = dto.body ?: "",
            time = "Recién",
            type = NotificationType.SYSTEM_UPDATE,
            dateCategory = "HOY"
        )
    }
}
