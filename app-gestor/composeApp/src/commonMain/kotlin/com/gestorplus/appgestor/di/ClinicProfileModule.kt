package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.clinicprofile.presentation.viewmodel.ClinicProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val clinicProfileModule = module {
    viewModelOf(::ClinicProfileViewModel)
}
