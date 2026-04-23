package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.data.repository.BookingRepository
import com.gestorplus.appgestor.data.repository.EventRepository
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.presentation.owner.OwnerDashboardViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf

val appModule = module {
    // Data Sources
    single { FirebaseManager() }

    // Repositorios
    single { BookingRepository(get(), get()) }
    single { EventRepository(get()) }

    // ViewModels
    viewModelOf(::OwnerDashboardViewModel)
}
