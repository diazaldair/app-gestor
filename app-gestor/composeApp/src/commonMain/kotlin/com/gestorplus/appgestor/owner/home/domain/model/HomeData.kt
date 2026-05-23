package com.gestorplus.appgestor.owner.home.domain.model

data class HomeData(
    val doctorName: String,
    val totalAppointments: Int,
    val pendingAppointments: Int,
    val nextAppointment: Appointment?,
    val restOfDayAppointments: List<Appointment>
)
