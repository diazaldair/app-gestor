package com.gestorplus.appgestor.domain.owner.usecase

import com.gestorplus.appgestor.domain.owner.model.Booking
import com.gestorplus.appgestor.domain.owner.repository.OwnerRepository
import kotlinx.coroutines.flow.Flow

class GetOwnerBookingsUseCase(private val repository: OwnerRepository) {
    operator fun invoke(): Flow<List<Booking>> = repository.getBookings()
}
