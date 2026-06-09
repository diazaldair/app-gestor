package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.model.AutoLunchConfig
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository

class SaveAutoLunchUseCase(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(config: AutoLunchConfig): Result<Unit> {
        return repository.saveAutoLunchConfig(config)
    }
}
