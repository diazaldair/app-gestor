package com.gestorplus.appgestor.data.booking.model

import com.gestorplus.appgestor.domain.booking.model.SlotPeriod

/**
 * Data Transfer Object for Firebase Realtime Database.
 * Represents the raw structure stored in the database.
 */
data class FirebaseBookingDto(
    val clientName: String = "Unknown",
    val serviceName: String = "General Service",
    val status: String = "PENDING",
    val time: String = "00:00",
    val isAvailable: Boolean = true,
    val period: SlotPeriod = SlotPeriod.MORNING
)
