package com.gestorplus.appgestor.booking.data.datasource.dto

import com.gestorplus.appgestor.booking.domain.model.SlotPeriod

data class FirebaseBookingDto(
    val clientName: String = "Unknown",
    val serviceName: String = "General Service",
    val status: String = "PENDING",
    val time: String = "00:00",
    val isAvailable: Boolean = true,
    val period: SlotPeriod = SlotPeriod.MORNING
)
