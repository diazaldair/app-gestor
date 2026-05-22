package com.gestorplus.appgestor.owner.setup_success.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class WorkspaceSetupSuccessUiState(
    val clinicName: String = "",
    val isLoading: Boolean = true
)
