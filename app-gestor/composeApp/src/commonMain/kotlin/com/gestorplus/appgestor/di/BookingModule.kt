package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.booking.data.datasource.datasource.BookingRemoteDatasource
import com.gestorplus.appgestor.booking.data.datasource.mapper.BookingMapper
import com.gestorplus.appgestor.booking.data.datasource.repository.BookingRepositoryImpl
import com.gestorplus.appgestor.booking.data.datasource.service.BookingService
import com.gestorplus.appgestor.booking.domain.repository.BookingRepository
import com.gestorplus.appgestor.booking.domain.usecase.ConfirmBookingUseCase
import com.gestorplus.appgestor.booking.domain.usecase.GetAvailableSlotsUseCase
import com.gestorplus.appgestor.booking.presentation.viewmodel.BookingConfirmationViewModel
import com.gestorplus.appgestor.booking.presentation.viewmodel.BookingViewModel
import org.koin.dsl.module

val bookingModule = module {
    single { BookingMapper() }
    single { BookingService(get()) }
    single { BookingRemoteDatasource(get(), get()) }
    single<BookingRepository> { BookingRepositoryImpl(get(), get()) }
    
    single { GetAvailableSlotsUseCase(get()) }
    single { ConfirmBookingUseCase(get()) }
    
    factory { (clinicId: String, serviceId: String) -> 
        BookingViewModel(clinicId, serviceId, get(), get(), get(), get(), get()) 
    }
    
    factory { BookingConfirmationViewModel(get(), get(), get(), get(), get()) }
}
