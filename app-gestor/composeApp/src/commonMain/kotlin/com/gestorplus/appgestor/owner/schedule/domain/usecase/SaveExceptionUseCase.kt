package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.model.ScheduleException
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository
import java.util.UUID

class SaveExceptionUseCase(private val repository: ScheduleRepository) {
    suspend operator fun invoke(exception: ScheduleException): Result<Unit> {
        val finalException = if (exception.id.isBlank()) exception.copy(id = UUID.randomUUID().toString()) else exception
        return repository.saveException(finalException)
    }
}