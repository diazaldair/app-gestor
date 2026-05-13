package com.gestorplus.appgestor.services.presentation.state

import com.gestorplus.appgestor.services.domain.model.Service

data class ServiceState(
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
