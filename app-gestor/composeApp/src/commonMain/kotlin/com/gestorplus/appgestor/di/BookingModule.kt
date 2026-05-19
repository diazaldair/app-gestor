package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.data.booking.repository.BookingRepositoryImpl
import com.gestorplus.appgestor.domain.booking.repository.BookingRepository
import com.gestorplus.appgestor.domain.booking.usecase.ConfirmBookingUseCase
import com.gestorplus.appgestor.domain.booking.usecase.GetAvailableSlotsUseCase
import com.gestorplus.appgestor.presentation.booking.BookingViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val bookingModule = module {
    single<BookingRepository> { BookingRepositoryImpl(get(), get()) }
    
    factoryOf(::GetAvailableSlotsUseCase)
    factoryOf(::ConfirmBookingUseCase)
    
    viewModelOf(::BookingViewModel)
}
