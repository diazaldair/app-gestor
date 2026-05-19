package com.gestorplus.appgestor.onboarding.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class LandingUiState(
    val version: String = "v1.0.2",
    val statusText: String = "SERVIDORES ACTIVOS EN TIEMPO REAL",
    val isLoading: Boolean = false
)
