package com.gestorplus.appgestor.booking.domain.usecase

import com.gestorplus.appgestor.booking.domain.repository.BookingRepository

class ConfirmBookingUseCase(private val repository: BookingRepository) {
    suspend operator fun invoke(
        clinicId: String,
        serviceId: String,
        clinicName: String,
        serviceName: String,
        price: Double,
        date: Int,
        month: String,
        timeSlot: String
    ): Result<Unit> {
        return repository.confirmBooking(
            clinicId, serviceId, clinicName, serviceName, price, date, month, timeSlot
        )
    }
}
