package com.gestorplus.appgestor.owner.working_hours.presentation.state

import com.gestorplus.appgestor.owner.schedule.domain.model.*

sealed interface WorkingHoursEvent {
    data class UpdateShift(val shift: WorkingShift) : WorkingHoursEvent
    data class UpdateMasterSchedule(val schedule: MasterSchedule) : WorkingHoursEvent
    data class UpdateAutoLunch(val config: AutoLunchConfig) : WorkingHoursEvent
    data class UpdateTimingDefaults(val defaults: TimingDefaults) : WorkingHoursEvent
    data object SaveAll : WorkingHoursEvent
    data object LoadAll : WorkingHoursEvent
    data object NavigateBack : WorkingHoursEvent
    data object ResetAll : WorkingHoursEvent
    data class NavigateToExceptions(val date: String?) : WorkingHoursEvent
}