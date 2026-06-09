package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.owner.setup_success.data.datasource.SetupSuccessLocalDatasource
import com.gestorplus.appgestor.owner.setup_success.data.repository.SetupSuccessRepositoryImpl
import com.gestorplus.appgestor.owner.setup_success.domain.repository.SetupSuccessRepository
import com.gestorplus.appgestor.owner.setup_success.domain.usecase.GetRegisteredClinicNameUseCase
import com.gestorplus.appgestor.owner.setup_success.presentation.viewmodel.WorkspaceSetupSuccessViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val setupSuccessModule = module {
    // 1. Fuentes de Datos (Datasources)
    single { SetupSuccessLocalDatasource(get()) }

    // 2. Repositorio
    single<SetupSuccessRepository> { SetupSuccessRepositoryImpl(get()) }

    // 3. Casos de Uso
    factoryOf(::GetRegisteredClinicNameUseCase)

    // 4. ViewModel
    viewModelOf(::WorkspaceSetupSuccessViewModel)
}
