package com.gestorplus.appgestor.services.domain.model

data class Service(
    val id: String,
    val name: String,
    val price: Double,
    val deposit: Double,
    val depositPercentage: Int? = null,
    val isActive: Boolean = true
)
