package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.model.MasterSchedule
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository

class SaveMasterScheduleUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(schedule: MasterSchedule): Result<Unit> {
        return repository.saveMasterSchedule(schedule)
    }
}
