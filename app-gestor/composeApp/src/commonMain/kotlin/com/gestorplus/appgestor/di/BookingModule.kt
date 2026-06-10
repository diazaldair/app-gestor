package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.booking.data.datasource.repository.BookingRepositoryImpl
import com.gestorplus.appgestor.booking.domain.repository.BookingRepository
import com.gestorplus.appgestor.booking.domain.usecase.ConfirmBookingUseCase
import com.gestorplus.appgestor.booking.domain.usecase.GetAvailableSlotsUseCase
import com.gestorplus.appgestor.booking.presentation.viewmodel.BookingConfirmationViewModel
import com.gestorplus.appgestor.booking.presentation.viewmodel.BookingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val bookingModule = module {
    single<BookingRepository> { BookingRepositoryImpl(get(), get(), get()) }
    single { GetAvailableSlotsUseCase(get()) }
    single { ConfirmBookingUseCase(get()) }
    viewModel { (clinicId: String, serviceId: String) -> 
        BookingViewModel(clinicId, serviceId, get(), get(), get()) 
    }
    viewModel { BookingConfirmationViewModel(get(), get(), get(), get()) }
}
