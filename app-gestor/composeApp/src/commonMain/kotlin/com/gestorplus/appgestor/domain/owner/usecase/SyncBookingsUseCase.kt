package com.gestorplus.appgestor.domain.owner.usecase

import com.gestorplus.appgestor.domain.owner.repository.OwnerRepository

class SyncBookingsUseCase(private val repository: OwnerRepository) {
    suspend operator fun invoke() {
        repository.syncAllBookings()
    }
}
