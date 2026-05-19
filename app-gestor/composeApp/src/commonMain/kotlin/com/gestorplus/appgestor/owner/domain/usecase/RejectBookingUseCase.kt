package com.gestorplus.appgestor.owner.domain.usecase

import com.gestorplus.appgestor.owner.domain.repository.OwnerRepository

class RejectBookingUseCase(private val repository: OwnerRepository) {
    suspend operator fun invoke(bookingId: String) {
        repository.updateStatus(bookingId, "REJECTED")
    }
}
