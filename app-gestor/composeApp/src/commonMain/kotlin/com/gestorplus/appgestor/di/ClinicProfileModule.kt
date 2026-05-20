package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.clinicProfile.data.datasource.datasource.ClinicProfileLocalDatasource
import com.gestorplus.appgestor.clinicProfile.data.datasource.datasource.ClinicProfileRemoteDatasource
import com.gestorplus.appgestor.clinicProfile.data.datasource.repository.ClinicProfileRepositoryImpl
import com.gestorplus.appgestor.clinicProfile.domain.repository.ClinicProfileRepository
import com.gestorplus.appgestor.clinicProfile.domain.usecase.GetClinicProfileUseCase
import com.gestorplus.appgestor.clinicProfile.domain.usecase.UpdateClinicProfileUseCase
import com.gestorplus.appgestor.clinicProfile.presentation.viewmodel.ClinicProfileViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val clinicProfileFeatureModule = module {
    // 1. Mappers y Servicios
    // single { ClinicProfileMapper() }
    // single { ClinicProfileService(get()) }
     
    // 2. Fuentes de Datos (Datasources)
    single { ClinicProfileLocalDatasource(get()) }
    single { ClinicProfileRemoteDatasource(get()) }
     
    // 3. Repositorio
    single<ClinicProfileRepository> { ClinicProfileRepositoryImpl() }
     
    // 4. Casos de Uso
    factoryOf(::GetClinicProfileUseCase)
    factoryOf(::UpdateClinicProfileUseCase)
     
    // 5. ViewModel
    viewModelOf(::ClinicProfileViewModel)
}
