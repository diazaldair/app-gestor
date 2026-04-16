package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.data.booking.repository.BookingRepositoryImpl
import com.gestorplus.appgestor.domain.booking.repository.BookingRepository
import com.gestorplus.appgestor.domain.booking.usecase.ConfirmBookingUseCase
import com.gestorplus.appgestor.domain.booking.usecase.GetAvailableSlotsUseCase
import org.koin.dsl.module

val appModule = module {
    // Repositorios
    single<BookingRepository> { BookingRepositoryImpl() }

    // Use Cases
    factory { GetAvailableSlotsUseCase(get()) }
    factory { ConfirmBookingUseCase(get()) }
}
