package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.model.WorkingShift
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository

class SaveWorkingShiftsUseCase(private val repository: ScheduleRepository) {
    suspend operator fun invoke(shifts: List<WorkingShift>): Result<Unit> {
        shifts.forEach { shift ->
            shift.morningStart?.let { start ->
                shift.morningEnd?.let { end ->
                    if (start >= end) return Result.failure(IllegalArgumentException("Morning start must be before end"))
                }
            }
            shift.afternoonStart?.let { start ->
                shift.afternoonEnd?.let { end ->
                    if (start >= end) return Result.failure(IllegalArgumentException("Afternoon start must be before end"))
                }
            }
        }
        return repository.saveWorkingShifts(shifts)
    }
}