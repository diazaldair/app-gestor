package com.gestorplus.appgestor.datetimeselector.data.datasource.datasource

import com.gestorplus.appgestor.datetimeselector.data.datasource.dto.TimeSlotDto
import kotlinx.coroutines.flow.Flow

class DateTimeSelectorRemoteDataSource {
    fun getAvailableSlots(date: Int): Flow<List<TimeSlotDto>> {
        TODO("Not implemented yet")
    }
}
