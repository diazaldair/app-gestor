package com.gestorplus.appgestor.owner.dashboard.domain.usecase

import com.gestorplus.appgestor.owner.dashboard.domain.model.Booking
import com.gestorplus.appgestor.owner.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow

class GetOwnerBookingsUseCase(private val repository: DashboardRepository) {
    operator fun invoke(): Flow<List<Booking>> = repository.getBookings()
}
