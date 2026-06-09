package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.clinic_detail.data.ClinicDetailRepositoryImpl
import com.gestorplus.appgestor.clinic_detail.presentation.ClinicDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val clinicDetailModule = module {
    single { ClinicDetailRepositoryImpl(get(), get(), get()) }
    viewModel { (clinicId: String) -> ClinicDetailViewModel(get(), clinicId) }
}
