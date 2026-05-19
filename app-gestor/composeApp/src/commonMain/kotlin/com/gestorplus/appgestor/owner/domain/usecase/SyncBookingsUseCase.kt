package com.gestorplus.appgestor.owner.domain.usecase

import com.gestorplus.appgestor.owner.domain.repository.OwnerRepository

class SyncBookingsUseCase(private val repository: OwnerRepository) {
    suspend operator fun invoke() {
        repository.syncAllBookings()
    }
}
