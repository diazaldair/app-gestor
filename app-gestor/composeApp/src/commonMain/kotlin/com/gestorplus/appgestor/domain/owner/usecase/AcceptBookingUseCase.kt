package com.gestorplus.appgestor.domain.owner.usecase

import com.gestorplus.appgestor.domain.owner.repository.OwnerRepository

class AcceptBookingUseCase(private val repository: OwnerRepository) {
    suspend operator fun invoke(bookingId: String) {
        repository.updateStatus(bookingId, "CONFIRMED")
    }
}
