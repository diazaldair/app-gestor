package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.clinicProfile.data.datasource.repository.ClinicProfileRepositoryImpl
import com.gestorplus.appgestor.clinicProfile.domain.repository.ClinicProfileRepository
import com.gestorplus.appgestor.clinicProfile.domain.usecase.GetClinicProfileUseCase
import com.gestorplus.appgestor.clinicProfile.domain.usecase.UpdateClinicProfileUseCase
import com.gestorplus.appgestor.clinicProfile.presentation.viewmodel.ClinicProfileViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val clinicProfileFeatureModule = module {
    single<ClinicProfileRepository> { ClinicProfileRepositoryImpl() }
    factoryOf(::GetClinicProfileUseCase)
    factoryOf(::UpdateClinicProfileUseCase)
    viewModelOf(::ClinicProfileViewModel)
}
