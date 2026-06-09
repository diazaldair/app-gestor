package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.explore_clinics.data.ExploreClinicsRepositoryImpl
import com.gestorplus.appgestor.explore_clinics.presentation.ExploreClinicsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val exploreClinicsModule = module {
    single { ExploreClinicsRepositoryImpl(get(), get()) }
    viewModelOf(::ExploreClinicsViewModel)
}
