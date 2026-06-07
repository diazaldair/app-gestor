package com.gestorplus.appgestor.services_entry.data.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServicesEntryDto(
    val professionalName: String,
    val appointmentsCount: Int,
    val pendingCount: Int,
    val nextAppointment: NextAppointmentDto?
)

@Serializable
data class NextAppointmentDto(
    val patientName: String,
    val serviceName: String,
    val time: String,
    val timeRemaining: String
)
