package com.gestorplus.appgestor.owner.setup_service.domain.model

data class WorkspaceService(
    val name: String,
    val description: String,
    val price: Double,
    val currency: String = "USD",
    val durationMinutes: Int
)
