package com.gestorplus.appgestor.booking.domain.usecase

import com.gestorplus.appgestor.booking.domain.model.BookingSlot
import com.gestorplus.appgestor.booking.domain.repository.BookingRepository

class GetAvailableSlotsUseCase(private val repository: BookingRepository) {
    suspend operator fun invoke(date: Int): List<BookingSlot> {
        return repository.getAvailableSlots(date)
    }
}
