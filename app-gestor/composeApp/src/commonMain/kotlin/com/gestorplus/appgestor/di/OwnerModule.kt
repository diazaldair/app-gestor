package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.owner.data.repository.OwnerBookingRepository
import com.gestorplus.appgestor.owner.domain.repository.OwnerRepository
import com.gestorplus.appgestor.owner.domain.usecase.*
import com.gestorplus.appgestor.owner.presentation.viewmodel.OwnerDashboardViewModel
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
