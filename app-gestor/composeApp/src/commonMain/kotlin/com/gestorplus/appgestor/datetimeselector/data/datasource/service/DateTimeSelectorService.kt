package com.gestorplus.appgestor.datetimeselector.data.datasource.service

import com.gestorplus.appgestor.datetimeselector.data.datasource.dto.TimeSlotDto
import kotlinx.coroutines.flow.Flow

interface DateTimeSelectorService {
    fun fetchAvailableSlots(date: Int): Flow<List<TimeSlotDto>>
}
