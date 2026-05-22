package com.gestorplus.appgestor.owner.setup_schedule.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class WorkspaceSetupScheduleUiState(
    val selectedDays: List<String> = emptyList(), // e.g. ["L", "M", "X", "J", "V"]
    val morningStart: String = "08:00",
    val morningEnd: String = "13:00",
    val afternoonStart: String = "15:00",
    val afternoonEnd: String = "20:00",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
