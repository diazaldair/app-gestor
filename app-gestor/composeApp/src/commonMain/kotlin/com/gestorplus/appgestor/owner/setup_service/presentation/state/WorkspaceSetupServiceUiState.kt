package com.gestorplus.appgestor.owner.setup_service.presentation.state

import androidx.compose.runtime.Immutable

enum class DurationOption(val label: String, val minutes: Int?) {
    MIN_15("15m", 15),
    MIN_30("30m", 30),
    MIN_45("45m", 45),
    MIN_60("60m", 60),
    CUSTOM("Pers.", null)
}

@Immutable
data class WorkspaceSetupServiceUiState(
    val serviceName: String = "",
    val description: String = "",
    val price: String = "",
    val currency: String = "Bs",
    val selectedDurationOption: DurationOption = DurationOption.MIN_15,
    val customHours: Int = 0,
    val customMinutes: Int = 15,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
