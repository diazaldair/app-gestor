package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.model.TimingDefaults
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository

class SaveTimingDefaultsUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(defaults: TimingDefaults): Result<Unit> {
        return repository.saveTimingDefaults(defaults)
    }
}
