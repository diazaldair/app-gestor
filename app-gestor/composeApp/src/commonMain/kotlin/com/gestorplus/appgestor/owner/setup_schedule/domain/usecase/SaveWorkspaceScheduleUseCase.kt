package com.gestorplus.appgestor.owner.setup_schedule.domain.usecase

import com.gestorplus.appgestor.owner.setup_schedule.domain.model.WorkspaceSchedule
import com.gestorplus.appgestor.owner.setup_schedule.domain.repository.SetupScheduleRepository

class SaveWorkspaceScheduleUseCase(private val repository: SetupScheduleRepository) {
    suspend operator fun invoke(schedule: WorkspaceSchedule): Result<Unit> {
        if (schedule.workingDays.isEmpty()) {
            return Result.failure(IllegalArgumentException("Debes seleccionar al menos un día laboral."))
        }
        if (schedule.morningStart.isBlank() || schedule.morningEnd.isBlank()) {
            return Result.failure(IllegalArgumentException("Debes definir al menos el horario de mañana."))
        }
        return repository.saveWorkspaceSchedule(schedule)
    }
}
