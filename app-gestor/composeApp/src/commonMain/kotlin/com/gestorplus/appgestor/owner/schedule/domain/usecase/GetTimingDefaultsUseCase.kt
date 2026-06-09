package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.model.TimingDefaults
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository

class GetTimingDefaultsUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): TimingDefaults? {
        return repository.getTimingDefaults()
    }
}
