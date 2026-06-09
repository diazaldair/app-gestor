package com.gestorplus.appgestor.edit_services.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class EditServiceUiState(
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val price: String = "",
    val currency: String = "Bs",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false
)
