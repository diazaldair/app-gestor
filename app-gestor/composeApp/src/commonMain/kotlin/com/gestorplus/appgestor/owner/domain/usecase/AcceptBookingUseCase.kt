package com.gestorplus.appgestor.owner.domain.usecase

import com.gestorplus.appgestor.owner.domain.repository.OwnerRepository

class AcceptBookingUseCase(private val repository: OwnerRepository) {
    suspend operator fun invoke(bookingId: String) {
        repository.updateStatus(bookingId, "CONFIRMED")
    }
}
