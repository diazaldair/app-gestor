package com.gestorplus.appgestor.owner.schedule_detail.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.owner.schedule.domain.model.ScheduleException

@Immutable
data class ScheduleExceptionUiState(
    val exception: ScheduleException = ScheduleException(id = "", date = "", isOpen = true),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val availableDates: List<String> = emptyList()
)