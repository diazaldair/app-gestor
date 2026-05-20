package com.gestorplus.appgestor.clinicProfile.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.clinicProfile.domain.model.ClinicProfile

@Immutable
data class ClinicProfileUiState(
    val isLoading: Boolean = false,
    val profile: ClinicProfile = ClinicProfile(
        name = "",
        biography = "",
        specialties = emptyList(),
        address = ""
    ),
    val isSaving: Boolean = false
)
