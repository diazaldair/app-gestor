package com.gestorplus.appgestor.clinicprofile.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.clinicprofile.domain.model.ClinicProfile

@Immutable
data class ClinicProfileUiState(
    val clinicProfile: ClinicProfile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
