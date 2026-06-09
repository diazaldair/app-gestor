package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository

class DeleteExceptionUseCase(private val repository: ScheduleRepository) {
    suspend operator fun invoke(exceptionId: String): Result<Unit> = repository.deleteException(exceptionId)
}