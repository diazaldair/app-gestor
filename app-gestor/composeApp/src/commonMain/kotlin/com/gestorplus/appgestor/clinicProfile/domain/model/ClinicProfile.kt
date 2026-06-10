package com.gestorplus.appgestor.clinicProfile.domain.model

data class ClinicProfile(
    val id: String = "",
    val name: String = "",
    val biography: String = "",
    val specialties: List<String> = emptyList(),
    val address: String = "",
    val locationUrl: String = "",
    val imageUrl: String? = null,
    val isOpen: Boolean = true
)
