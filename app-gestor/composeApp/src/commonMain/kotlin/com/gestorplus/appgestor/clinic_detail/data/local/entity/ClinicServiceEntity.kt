package com.gestorplus.appgestor.clinic_detail.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clinic_services")
data class ClinicServiceEntity(
    @PrimaryKey val id: String,
    val clinicId: String,
    val name: String,
    val durationMinutes: Int,
    val price: Double,
    val description: String
)
