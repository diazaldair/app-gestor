package com.gestorplus.appgestor.booking_success.domain.model

data class BookingSuccessDetails(
    val serviceName: String,
    val schedule: String,
    val imageUrl: String? = null
)
