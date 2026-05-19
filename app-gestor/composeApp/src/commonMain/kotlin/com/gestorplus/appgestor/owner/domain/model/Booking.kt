package com.gestorplus.appgestor.owner.domain.model

data class Booking(
    val id: String,
    val clientName: String,
    val serviceName: String,
    val timestamp: Long,
    val durationMinutes: Int,
    val status: String,
    val price: Double
)
