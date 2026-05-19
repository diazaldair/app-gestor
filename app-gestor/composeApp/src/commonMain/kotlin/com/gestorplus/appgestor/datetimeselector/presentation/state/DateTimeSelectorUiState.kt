package com.gestorplus.appgestor.datetimeselector.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.datetimeselector.domain.model.TimeSlot

@Immutable
data class DateTimeSelectorUiState(
    val selectedDate: Int = 5,
    val selectedMonth: String = "October 2023",
    val selectedTimeSlot: String? = null,
    val morningSlots: List<TimeSlot> = emptyList(),
    val afternoonSlots: List<TimeSlot> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
