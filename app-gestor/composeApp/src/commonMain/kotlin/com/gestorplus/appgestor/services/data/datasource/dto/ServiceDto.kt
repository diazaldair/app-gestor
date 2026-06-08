package com.gestorplus.appgestor.services.data.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServiceDto(
    val id: String? = null,
    val name: String,
    val durationMinutes: Int,
    val price: Double,
    val isActive: Boolean,
    val category: String,
    val description: String
)
