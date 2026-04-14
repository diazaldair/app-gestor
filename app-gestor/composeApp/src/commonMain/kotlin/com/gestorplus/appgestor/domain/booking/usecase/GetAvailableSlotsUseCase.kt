package com.gestorplus.appgestor.domain.booking.usecase

import com.gestorplus.appgestor.domain.booking.model.BookingSlot
import com.gestorplus.appgestor.domain.booking.repository.BookingRepository

class GetAvailableSlotsUseCase(private val repository: BookingRepository) {
    suspend operator fun invoke(date: Int): List<BookingSlot> {
        return repository.getAvailableSlots(date)
    }
}
