package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.owner.setup_service.data.datasource.datasource.SetupServiceRemoteDatasource
import com.gestorplus.appgestor.owner.setup_service.data.datasource.repository.SetupServiceRepositoryImpl
import com.gestorplus.appgestor.owner.setup_service.data.datasource.service.SetupServiceService
import com.gestorplus.appgestor.owner.setup_service.domain.repository.SetupServiceRepository
import com.gestorplus.appgestor.owner.setup_service.domain.usecase.SaveWorkspaceServiceUseCase
import com.gestorplus.appgestor.owner.setup_service.presentation.viewmodel.WorkspaceSetupServiceViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val setupServiceModule = module {
    // 1. Servicios
    single { SetupServiceService(get()) }

    // 2. Fuentes de Datos (Datasources)
    single { SetupServiceRemoteDatasource(get()) }

    // 3. Repositorio
    single<SetupServiceRepository> { SetupServiceRepositoryImpl(get()) }

    // 4. Casos de Uso
    factoryOf(::SaveWorkspaceServiceUseCase)

    // 5. ViewModel
    viewModelOf(::WorkspaceSetupServiceViewModel)
}
