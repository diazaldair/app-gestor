package com.gestorplus.appgestor.owner.working_hours.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.Shift

@Immutable
data class WorkingHoursUiState(
    val shifts: List<Shift> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)
