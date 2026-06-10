package com.gestorplus.appgestor.owner.setup_profile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkspaceProfile(
    val clinicName: String,
    val fullName: String,
    val specialities: List<String>,
    val biography: String,
    val exactAddress: String,
    val references: String,
    val galleryImages: List<String>,
    val mapUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
