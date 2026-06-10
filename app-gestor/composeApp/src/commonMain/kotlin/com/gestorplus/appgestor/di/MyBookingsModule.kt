package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.my_bookings.data.PatientBookingRepositoryImpl
import com.gestorplus.appgestor.my_bookings.presentation.MyBookingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val myBookingsModule = module {
    single { PatientBookingRepositoryImpl(get(), get(), get()) }
    viewModelOf(::MyBookingsViewModel)
}
