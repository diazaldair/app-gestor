package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.owner.dashboard.data.datasource.DashboardLocalDatasource
import com.gestorplus.appgestor.owner.dashboard.data.datasource.DashboardRemoteDatasource
import com.gestorplus.appgestor.owner.dashboard.data.mapper.DashboardMapper
import com.gestorplus.appgestor.owner.dashboard.data.repository.DashboardRepositoryImpl
import com.gestorplus.appgestor.owner.dashboard.data.datasource.DashboardService
import com.gestorplus.appgestor.owner.dashboard.domain.repository.DashboardRepository
import com.gestorplus.appgestor.owner.dashboard.domain.usecase.*
import com.gestorplus.appgestor.owner.dashboard.presentation.viewmodel.OwnerDashboardViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardModule = module {
    // 1. Mappers y Servicios
    single { DashboardMapper() }
    single { DashboardService(get()) }

    // 2. Fuentes de Datos (Datasources)
    single { DashboardLocalDatasource(get()) }
    single { DashboardRemoteDatasource(get()) }

    // 3. Repositorio
    single<DashboardRepository> { DashboardRepositoryImpl(get(), get(), get()) }

    // 4. Casos de Uso
    factoryOf(::GetOwnerBookingsUseCase)
    factoryOf(::AcceptBookingUseCase)
    factoryOf(::RejectBookingUseCase)
    factoryOf(::SyncBookingsUseCase)
    factoryOf(::GetFirebaseLogsUseCase)

    // 5. ViewModel
    viewModelOf(::OwnerDashboardViewModel)
}
