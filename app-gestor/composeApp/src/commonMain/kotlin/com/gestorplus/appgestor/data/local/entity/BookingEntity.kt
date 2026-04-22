package com.gestorplus.appgestor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val clientName: String,
    val serviceName: String,
    val timestamp: Long, // Fecha y hora en milisegundos para fácil ordenamiento
    val durationMinutes: Int,
    val status: String, // PENDING, CONFIRMED, REJECTED, PAID
    val price: Double,
    val categoryColor: Long // Guardamos el color como Long (hex)
)
