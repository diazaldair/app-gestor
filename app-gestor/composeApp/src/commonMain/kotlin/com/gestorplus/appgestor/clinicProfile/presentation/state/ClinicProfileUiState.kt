package com.gestorplus.appgestor.clinicProfile.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.clinicProfile.domain.model.ClinicProfile

@Immutable
data class ClinicProfileUiState(
    val isLoading: Boolean = false,
    val profile: ClinicProfile = ClinicProfile(),
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val newSpeciality: String = ""
)
