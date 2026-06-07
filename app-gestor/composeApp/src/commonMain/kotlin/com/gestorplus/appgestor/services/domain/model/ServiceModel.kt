package com.gestorplus.appgestor.services.domain.model

data class ServiceModel(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val currency: String = "Bs",
    val durationMinutes: Int = 30,
    val isActive: Boolean = true,
    val imageUrl: String? = null
)
