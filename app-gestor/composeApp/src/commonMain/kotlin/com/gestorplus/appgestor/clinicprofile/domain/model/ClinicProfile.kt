package com.gestorplus.appgestor.clinicprofile.domain.model

data class ClinicProfile(
    val name: String,
    val subtitle: String,
    val bio: String,
    val location: String,
    val specialties: List<String>,
    val services: List<ClinicService>
)
