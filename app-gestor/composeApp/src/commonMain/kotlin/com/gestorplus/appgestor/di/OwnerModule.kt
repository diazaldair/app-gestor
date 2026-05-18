package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.data.repository.OwnerBookingRepository
import com.gestorplus.appgestor.presentation.owner.OwnerDashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ownerModule = module {
    single { OwnerBookingRepository(get(), get(), get()) }
    viewModelOf(::OwnerDashboardViewModel)
}
