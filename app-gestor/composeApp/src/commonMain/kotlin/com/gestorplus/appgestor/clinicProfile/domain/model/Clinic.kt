package com.gestorplus.appgestor.clinicProfile.domain.model

data class Clinic(
    val id: String,
    val name: String,
    val address: String,
    val specialties: List<String>,
    val imageUrl: String? = null,
    val isOpen: Boolean = true,
    val description: String = ""
)
