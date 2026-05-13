package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.data.repository.OwnerBookingRepository
import com.gestorplus.appgestor.data.repository.EventRepository
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.domain.booking.repository.BookingRepository
import com.gestorplus.appgestor.data.booking.repository.BookingRepositoryImpl
import com.gestorplus.appgestor.domain.booking.usecase.GetAvailableSlotsUseCase
import com.gestorplus.appgestor.domain.booking.usecase.ConfirmBookingUseCase
import com.gestorplus.appgestor.presentation.owner.OwnerDashboardViewModel
import com.gestorplus.appgestor.presentation.booking.BookingViewModel
import com.gestorplus.appgestor.services.data.repository.ServiceRepositoryImpl
import com.gestorplus.appgestor.services.domain.repository.ServiceRepository
import com.gestorplus.appgestor.services.domain.usecase.GetServicesUseCase
import com.gestorplus.appgestor.services.presentation.viewmodel.ServiceViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf

val appModule = module {
    // Data Sources
    single { FirebaseManager() }

    // Repositorios
    single { OwnerBookingRepository(get(), get()) }
    single<BookingRepository> { BookingRepositoryImpl(get()) }
    single { EventRepository(get()) }
    single<ServiceRepository> { ServiceRepositoryImpl() }

    // Use Cases
    factory { GetAvailableSlotsUseCase(get()) }
    factory { ConfirmBookingUseCase(get()) }
    factory { GetServicesUseCase(get()) }

    // ViewModels
    viewModelOf(::OwnerDashboardViewModel)
    viewModelOf(::BookingViewModel)
    viewModelOf(::ServiceViewModel)
}
