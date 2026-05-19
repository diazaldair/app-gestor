package com.gestorplus.appgestor.owner.presentation.setup.state

import androidx.compose.runtime.Immutable

@Immutable
data class WorkspaceSetupIntroUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
