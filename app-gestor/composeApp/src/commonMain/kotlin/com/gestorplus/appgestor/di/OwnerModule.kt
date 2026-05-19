package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.data.repository.OwnerBookingRepository
import com.gestorplus.appgestor.domain.owner.repository.OwnerRepository
import com.gestorplus.appgestor.domain.owner.usecase.*
import com.gestorplus.appgestor.presentation.owner.OwnerDashboardViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ownerModule = module {
    single<OwnerRepository> { OwnerBookingRepository(get(), get(), get()) }
    
    factoryOf(::GetOwnerBookingsUseCase)
    factoryOf(::AcceptBookingUseCase)
    factoryOf(::RejectBookingUseCase)
    factoryOf(::SyncBookingsUseCase)
    factoryOf(::InitializeAndSyncConfigUseCase)
    factoryOf(::GetFirebaseLogsUseCase)
    
    viewModelOf(::OwnerDashboardViewModel)
}
