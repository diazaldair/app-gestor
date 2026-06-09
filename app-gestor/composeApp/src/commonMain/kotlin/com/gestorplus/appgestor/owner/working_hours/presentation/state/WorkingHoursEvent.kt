package com.gestorplus.appgestor.owner.working_hours.presentation.state

import com.gestorplus.appgestor.owner.setup_schedule.domain.model.Shift

sealed interface WorkingHoursEvent {
    data object LoadShifts : WorkingHoursEvent
    data class AddShift(val shift: Shift) : WorkingHoursEvent
    data class UpdateShift(val shift: Shift) : WorkingHoursEvent
    data class DeleteShift(val shiftId: String) : WorkingHoursEvent
    data class ToggleDayInShift(val shiftId: String, val day: String) : WorkingHoursEvent
    data class TimeChanged(val shiftId: String, val isStart: Boolean, val time: String) : WorkingHoursEvent
    data object SaveChanges : WorkingHoursEvent
}
