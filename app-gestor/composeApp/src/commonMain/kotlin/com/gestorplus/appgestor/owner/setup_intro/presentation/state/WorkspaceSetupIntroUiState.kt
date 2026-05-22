package com.gestorplus.appgestor.owner.setup_intro.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class WorkspaceSetupIntroUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
