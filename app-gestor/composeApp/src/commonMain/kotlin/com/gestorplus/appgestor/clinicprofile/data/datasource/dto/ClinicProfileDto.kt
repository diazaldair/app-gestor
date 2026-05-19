package com.gestorplus.appgestor.clinicprofile.data.datasource.dto

/**
 * Data Transfer Object for Clinic Profile.
 */
data class ClinicProfileDto(
    val name: String? = null,
    val subtitle: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val specialties: List<String>? = null,
    val services: List<ClinicServiceDto>? = null
)

data class ClinicServiceDto(
    val id: String? = null,
    val name: String? = null,
    val duration: String? = null,
    val price: String? = null,
    val description: String? = null
)
