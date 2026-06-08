package com.gestorplus.appgestor.clinicProfile.domain.model

data class ClinicProfile(
    val name: String = "",
    val biography: String = "",
    val specialties: List<String> = emptyList(),
    val address: String = "",
    val mapPreviewUrl: String? = null
)
