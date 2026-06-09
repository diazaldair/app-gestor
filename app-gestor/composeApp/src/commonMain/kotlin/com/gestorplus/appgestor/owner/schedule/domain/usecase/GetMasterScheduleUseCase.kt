package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.model.MasterSchedule
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository

class GetMasterScheduleUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): MasterSchedule? {
        return repository.getMasterSchedule()
    }
}
