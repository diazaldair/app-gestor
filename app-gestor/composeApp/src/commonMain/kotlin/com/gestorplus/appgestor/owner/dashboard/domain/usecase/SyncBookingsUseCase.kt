package com.gestorplus.appgestor.owner.dashboard.domain.usecase

import com.gestorplus.appgestor.owner.dashboard.domain.repository.DashboardRepository

class SyncBookingsUseCase(private val repository: DashboardRepository) {
    suspend operator fun invoke() {
        repository.syncAllBookings()
    }
}
