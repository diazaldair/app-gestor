package com.gestorplus.appgestor.owner.working_hours.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.owner.schedule.domain.model.*

@Immutable
data class WorkingHoursUiState(
    val shifts: Map<Int, WorkingShift> = emptyMap(),
    val masterSchedule: MasterSchedule = MasterSchedule(startTime = "09:00 AM", endTime = "05:00 PM", enabledDays = listOf(1,2,3,4,5)),
    val autoLunch: AutoLunchConfig = AutoLunchConfig(),
    val timingDefaults: TimingDefaults = TimingDefaults(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)