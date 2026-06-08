package com.gestorplus.appgestor.clinicProfile.data.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClinicProfileDto(
    val name: String? = null,
    val bio: String? = null,
    val specialties: List<String>? = null,
    val address: String? = null,
    val mapUrl: String? = null
)
