package com.gestorplus.appgestor.owner.domain.usecase

import com.gestorplus.appgestor.owner.domain.model.Booking
import com.gestorplus.appgestor.owner.domain.repository.OwnerRepository
import kotlinx.coroutines.flow.Flow

class GetOwnerBookingsUseCase(private val repository: OwnerRepository) {
    operator fun invoke(): Flow<List<Booking>> = repository.getBookings()
}
