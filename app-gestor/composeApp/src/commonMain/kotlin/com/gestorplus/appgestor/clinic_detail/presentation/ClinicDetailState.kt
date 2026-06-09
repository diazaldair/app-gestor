package com.gestorplus.appgestor.clinic_detail.presentation

import com.gestorplus.appgestor.clinicProfile.domain.model.Clinic
import com.gestorplus.appgestor.clinic_detail.domain.model.ClinicService

data class ClinicDetailState(
    val clinic: Clinic? = null,
    val services: List<ClinicService> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
