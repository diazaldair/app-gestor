package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.datetimeselector.presentation.viewmodel.DateTimeSelectorViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dateTimeSelectorModule = module {
    viewModelOf(::DateTimeSelectorViewModel)
}
