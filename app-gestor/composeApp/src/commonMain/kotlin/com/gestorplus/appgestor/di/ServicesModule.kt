package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.services_entry.presentation.viewmodel.ServicesEntryViewModel
import com.gestorplus.appgestor.monthly_calendar.presentation.viewmodel.MonthlyCalendarViewModel
import com.gestorplus.appgestor.services.presentation.viewmodel.ServicesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val servicesModule = module {
    viewModelOf(::ServicesEntryViewModel)
    viewModelOf(::MonthlyCalendarViewModel)
    viewModelOf(::ServicesViewModel)
}
