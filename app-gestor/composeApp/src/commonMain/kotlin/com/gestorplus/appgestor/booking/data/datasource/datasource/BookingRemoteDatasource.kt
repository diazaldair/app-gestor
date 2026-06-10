package com.gestorplus.appgestor.booking.data.datasource.datasource

import com.gestorplus.appgestor.booking.data.datasource.service.BookingService
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import kotlinx.datetime.Clock

class BookingRemoteDatasource(
    private val bookingService: BookingService,
    private val firebaseManager: FirebaseManager
) {
    suspend fun getAvailableSlots(clinicId: String, date: Int): Map<String, Any>? {
        return firebaseManager.getData("workspaces/$clinicId/appointments/$date")
    }

    suspend fun confirmBooking(
        clinicId: String,
        serviceId: String,
        clinicName: String,
        serviceName: String,
        doctorName: String,
        patientName: String,
        date: Int,
        month: String,
        timeSlot: String,
        price: Double,
        notes: String
    ) {
        val patientUid = firebaseManager.getCurrentUserUid() ?: "anonymous"
        val timestamp = Clock.System.now().toEpochMilliseconds()
        
        val slotKey = timeSlot.replace(" ", "_")
        
        val bookingData = mapOf(
            "clinicId" to clinicId,
            "serviceId" to serviceId,
            "clinicName" to clinicName,
            "serviceName" to serviceName,
            "doctorName" to doctorName,
            "patientName" to patientName,
            "date" to date,
            "month" to month,
            "timeSlot" to timeSlot,
            "price" to price,
            "notes" to notes,
            "status" to "PENDING",
            "patientUid" to patientUid,
            "timestamp" to timestamp
        )

        // 1. Guardar en la agenda de la clínica (bloquea el slot)
        firebaseManager.saveData("workspaces/$clinicId/appointments/$date/$slotKey", bookingData)
        
        // 2. Guardar en las reservas del paciente (para "Mis Reservas")
        val bookingId = "${date}_$slotKey"
        firebaseManager.saveData("users/$patientUid/bookings/$bookingId", bookingData)

        // 3. Crear una notificación para el doctor
        val notificationId = "notif_${timestamp}"
        val notificationData = mapOf(
            "id" to notificationId,
            "title" to "Nueva solicitud de cita",
            "description" to "$patientName ha solicitado una consulta de $serviceName para el $date de $month a las $timeSlot.",
            "timestamp" to timestamp,
            "type" to "APPOINTMENT_REQUEST",
            "patientName" to patientName,
            "specialty" to serviceName,
            "appointmentDate" to "$date $month",
            "appointmentTime" to timeSlot,
            "isRead" to false,
            "bookingId" to bookingId,
            "patientUid" to patientUid,
            "clinicId" to clinicId,
            "date" to date,
            "timeSlot" to timeSlot
        )
        firebaseManager.saveData("notifications/$clinicId/$notificationId", notificationData)
    }
}
