package com.gestorplus.appgestor.services_entry.domain.model

data class ProfessionalDashboardModel(
    val professionalName: String,
    val appointmentsCount: Int,
    val pendingCount: Int,
    val nextAppointment: NextAppointment? = null
)

data class NextAppointment(
    val patientName: String,
    val serviceName: String,
    val time: String,
    val timeRemaining: String
)
