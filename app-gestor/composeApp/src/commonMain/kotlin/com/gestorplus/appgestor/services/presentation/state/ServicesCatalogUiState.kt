package com.gestorplus.appgestor.services.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.services.domain.model.ServiceModel

@Immutable
data class ServicesCatalogUiState(
    val services: List<ServiceModel> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)
