package com.gestorplus.appgestor.clinic_detail.domain.model

data class ClinicService(
    val id: String,
    val clinicId: String,
    val name: String,
    val durationMinutes: Int,
    val price: Double,
    val description: String = ""
)
