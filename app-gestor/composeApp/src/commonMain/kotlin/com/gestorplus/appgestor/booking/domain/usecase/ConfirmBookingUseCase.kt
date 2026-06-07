package com.gestorplus.appgestor.booking.domain.usecase

import com.gestorplus.appgestor.booking.domain.repository.BookingRepository

class ConfirmBookingUseCase(private val repository: BookingRepository) {
    suspend operator fun invoke(
        date: Int,
        slot: String,
        notes: String,
        service: String,
        doctor: String,
        totalPrice: Double
    ): Result<Unit> {
        return repository.confirmBooking(date, slot, notes, service, doctor, totalPrice)
    }
}
