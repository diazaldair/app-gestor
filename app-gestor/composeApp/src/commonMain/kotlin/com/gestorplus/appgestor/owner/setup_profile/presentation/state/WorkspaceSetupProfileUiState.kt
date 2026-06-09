package com.gestorplus.appgestor.owner.setup_profile.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class WorkspaceSetupProfileUiState(
    val clinicName: String = "",
    val fullName: String = "",
    val specialities: List<String> = emptyList(),
    val inputSpeciality: String = "",
    val biography: String = "",
    val isLocationFixed: Boolean = false,
    val exactAddress: String = "",
    val references: String = "",
    val galleryImages: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
