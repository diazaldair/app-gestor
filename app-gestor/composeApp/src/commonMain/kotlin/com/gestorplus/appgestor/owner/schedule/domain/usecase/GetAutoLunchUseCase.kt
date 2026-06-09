package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.model.AutoLunchConfig
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository

class GetAutoLunchUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(): AutoLunchConfig? {
        return repository.getAutoLunchConfig()
    }
}
