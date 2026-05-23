package com.gestorplus.appgestor.owner.home.data.datasource

import com.gestorplus.appgestor.owner.home.domain.model.Appointment
import com.gestorplus.appgestor.owner.home.domain.model.AppointmentStatus

class HomeLocalDatasource {

    // Mock data — será reemplazado por Room / Firestore
    fun getDoctorName(): String = "Dr. Castro"

    fun getTotalAppointments(): Int = 8

    fun getPendingAppointments(): Int = 3

    fun getNextAppointment(): Appointment = Appointment(
        id = "next-001",
        clientName = "Mariana Flores",
        serviceType = "Check-up General",
        timeLabel = "09:30 AM",
        countdownLabel = "EN 15 MIN",
        isNext = true,
        status = AppointmentStatus.SCHEDULED
    )

    fun getRestOfDayAppointments(): List<Appointment> = listOf(
        Appointment(
            id = "apt-002",
            clientName = "Roberto Mejía",
            serviceType = "Especialidad",
            timeLabel = "10:45 AM",
            countdownLabel = "",
            status = AppointmentStatus.SCHEDULED
        ),
        Appointment(
            id = "apt-003",
            clientName = "Lucía Torres",
            serviceType = "Seguimiento",
            timeLabel = "11:30 AM",
            countdownLabel = "",
            status = AppointmentStatus.SCHEDULED
        ),
        Appointment(
            id = "apt-004",
            clientName = "Espacio Libre",
            serviceType = "Disponible",
            timeLabel = "12:15 PM",
            countdownLabel = "",
            status = AppointmentStatus.FREE_SLOT
        )
    )
}
