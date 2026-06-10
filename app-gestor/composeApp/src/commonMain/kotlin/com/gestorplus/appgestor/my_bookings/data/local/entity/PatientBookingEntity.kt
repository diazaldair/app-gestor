package com.gestorplus.appgestor.my_bookings.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_bookings")
data class PatientBookingEntity(
    @PrimaryKey
    val id: String,
    val clinicId: String,
    val clinicName: String,
    val serviceName: String,
    val doctorName: String,
    val doctorImageUrl: String?,
    val timestamp: Long,
    val status: String,
    val price: Double,
    val currency: String,
    val date: Int? = null,
    val month: String? = null,
    val timeSlot: String? = null,
    val notes: String? = null
)
