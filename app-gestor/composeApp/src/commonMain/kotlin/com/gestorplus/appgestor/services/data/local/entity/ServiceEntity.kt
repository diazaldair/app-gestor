package com.gestorplus.appgestor.services.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val price: Double,
    val currency: String,
    val durationMinutes: Int,
    val isActive: Boolean,
    val imageUrl: String?
)
