package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.owner.data.datasource.datasource.OwnerLocalDatasource
import com.gestorplus.appgestor.owner.data.datasource.datasource.OwnerRemoteDatasource
import com.gestorplus.appgestor.owner.data.datasource.mapper.OwnerMapper
import com.gestorplus.appgestor.owner.data.datasource.repository.OwnerBookingRepository
import com.gestorplus.appgestor.owner.data.datasource.service.OwnerService
import com.gestorplus.appgestor.owner.domain.repository.OwnerRepository
import com.gestorplus.appgestor.owner.domain.usecase.*
import com.gestorplus.appgestor.owner.presentation.viewmodel.OwnerDashboardViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ownerModule = module {
    single { OwnerMapper() }
    single { OwnerService(get()) }
    single { OwnerLocalDatasource(get()) }
    single { OwnerRemoteDatasource(get()) }
    single<OwnerRepository> { OwnerBookingRepository(get(), get(), get(), get()) }
    
    factoryOf(::GetOwnerBookingsUseCase)
    factoryOf(::AcceptBookingUseCase)
    factoryOf(::RejectBookingUseCase)
    factoryOf(::SyncBookingsUseCase)
    factoryOf(::InitializeAndSyncConfigUseCase)
    factoryOf(::GetFirebaseLogsUseCase)
    
    viewModelOf(::OwnerDashboardViewModel)
}
