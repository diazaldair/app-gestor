package com.gestorplus.appgestor.domain.owner.model

data class Booking(
    val id: String,
    val clientName: String,
    val serviceName: String,
    val timestamp: Long,
    val durationMinutes: Int,
    val status: String,
    val price: Double
)
