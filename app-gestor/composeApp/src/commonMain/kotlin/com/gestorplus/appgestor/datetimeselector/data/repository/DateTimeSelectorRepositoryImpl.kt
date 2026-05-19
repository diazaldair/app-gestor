package com.gestorplus.appgestor.datetimeselector.data.repository

import com.gestorplus.appgestor.datetimeselector.data.datasource.datasource.DateTimeSelectorRemoteDataSource
import com.gestorplus.appgestor.datetimeselector.data.mapper.DateTimeSelectorMapper
import com.gestorplus.appgestor.datetimeselector.domain.model.TimeSlot
import com.gestorplus.appgestor.datetimeselector.domain.repository.DateTimeSelectorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DateTimeSelectorRepositoryImpl(
    private val remoteDataSource: DateTimeSelectorRemoteDataSource,
    private val mapper: DateTimeSelectorMapper
) : DateTimeSelectorRepository {
    override fun getAvailableSlots(date: Int): Flow<List<TimeSlot>> {
        return remoteDataSource.getAvailableSlots(date).map { list ->
            list.map { mapper.toDomain(it) }
        }
    }
}
