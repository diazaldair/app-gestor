package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.services_entry.presentation.viewmodel.ServicesEntryViewModel
import com.gestorplus.appgestor.monthly_calendar.presentation.viewmodel.MonthlyCalendarViewModel
import com.gestorplus.appgestor.services.presentation.viewmodel.ServicesViewModel
import com.gestorplus.appgestor.services.domain.repository.ServiceRepository
import com.gestorplus.appgestor.services.data.repository.ServiceRepositoryImpl
import com.gestorplus.appgestor.services.data.datasource.mapper.ServiceMapper
import com.gestorplus.appgestor.edit_services.presentation.viewmodel.EditServiceViewModel
import com.gestorplus.appgestor.services.domain.usecase.GetServicesUseCase
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val servicesModule = module {
    // Mapper
    single { ServiceMapper() }

    // Repository
    single<ServiceRepository> { ServiceRepositoryImpl(get(), get(), get()) }

    // Use Cases
    factoryOf(::GetServicesUseCase)

    // ViewModels
    viewModelOf(::ServicesEntryViewModel)
    viewModelOf(::MonthlyCalendarViewModel)
    viewModelOf(::ServicesViewModel)
    viewModelOf(::EditServiceViewModel)
}
