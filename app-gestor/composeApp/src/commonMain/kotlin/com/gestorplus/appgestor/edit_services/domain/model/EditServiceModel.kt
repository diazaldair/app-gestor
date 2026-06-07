package com.gestorplus.appgestor.edit_services.domain.model

data class EditServiceModel(
    val id: String? = null,
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val currency: String = "USD",
    val imageUrl: String? = null
)
