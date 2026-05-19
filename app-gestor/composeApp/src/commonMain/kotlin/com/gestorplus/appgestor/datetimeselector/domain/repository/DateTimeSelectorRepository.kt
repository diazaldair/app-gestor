package com.gestorplus.appgestor.datetimeselector.domain.repository

import com.gestorplus.appgestor.datetimeselector.domain.model.TimeSlot
import kotlinx.coroutines.flow.Flow

interface DateTimeSelectorRepository {
    fun getAvailableSlots(date: Int): Flow<List<TimeSlot>>
}
