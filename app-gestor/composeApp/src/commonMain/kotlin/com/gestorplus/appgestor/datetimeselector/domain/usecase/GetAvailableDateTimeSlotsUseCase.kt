package com.gestorplus.appgestor.datetimeselector.domain.usecase

import com.gestorplus.appgestor.datetimeselector.domain.model.TimeSlot
import com.gestorplus.appgestor.datetimeselector.domain.repository.DateTimeSelectorRepository
import kotlinx.coroutines.flow.Flow

class GetAvailableDateTimeSlotsUseCase(private val repository: DateTimeSelectorRepository) {
    operator fun invoke(date: Int): Flow<List<TimeSlot>> {
        return repository.getAvailableSlots(date)
    }
}
