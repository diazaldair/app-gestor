package com.gestorplus.appgestor.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val serviceName: String,
    val date: String,
    val time: String,
    val status: String,
    val firebaseId: String? = null // Referencia para sincronización con Firebase
)
