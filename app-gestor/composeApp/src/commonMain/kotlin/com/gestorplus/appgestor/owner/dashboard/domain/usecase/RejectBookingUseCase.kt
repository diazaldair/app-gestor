package com.gestorplus.appgestor.owner.dashboard.domain.usecase

import com.gestorplus.appgestor.owner.dashboard.domain.repository.DashboardRepository

class RejectBookingUseCase(private val repository: DashboardRepository) {
    suspend operator fun invoke(bookingId: String) {
        repository.updateStatus(bookingId, "REJECTED")
    }
}
