package com.gestorplus.appgestor.domain.booking.usecase

import com.gestorplus.appgestor.domain.booking.repository.BookingRepository

class ConfirmBookingUseCase(private val repository: BookingRepository) {
    suspend operator fun invoke(date: Int, slot: String): Result<Unit> {
        return repository.confirmBooking(date, slot)
    }
}
