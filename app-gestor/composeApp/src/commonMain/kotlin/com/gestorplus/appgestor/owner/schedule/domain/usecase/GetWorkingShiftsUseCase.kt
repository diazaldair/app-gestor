package com.gestorplus.appgestor.owner.schedule.domain.usecase

import com.gestorplus.appgestor.owner.schedule.domain.model.WorkingShift
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetWorkingShiftsUseCase(private val repository: ScheduleRepository) {
    operator fun invoke(): Flow<List<WorkingShift>> = repository.getWorkingShifts()
}