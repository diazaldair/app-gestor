package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.model.ScheduleException
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetExceptionsUseCase(private val repository: ScheduleRepository) {
    operator fun invoke(): Flow<List<ScheduleException>> = repository.getExceptions()
}