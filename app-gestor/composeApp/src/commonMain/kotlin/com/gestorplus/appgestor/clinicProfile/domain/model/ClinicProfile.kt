package com.gestorplus.appgestor.clinicProfile.domain.model

data class ClinicProfile(
    val name: String,
    val biography: String,
    val specialties: List<String>,
    val address: String,
    val locationMapUrl: String? = null
)
