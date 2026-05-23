package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.owner.setup_intro.data.datasource.SetupIntroRemoteDatasource
import com.gestorplus.appgestor.owner.setup_intro.data.repository.SetupIntroRepositoryImpl
import com.gestorplus.appgestor.owner.setup_intro.data.datasource.SetupIntroService
import com.gestorplus.appgestor.owner.setup_intro.domain.repository.SetupIntroRepository
import com.gestorplus.appgestor.owner.setup_intro.domain.usecase.InitializeAndSyncConfigUseCase
import com.gestorplus.appgestor.owner.setup_intro.presentation.viewmodel.WorkspaceSetupIntroViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val setupIntroModule = module {
    // 1. Servicios
    single { SetupIntroService(get()) }

    // 2. Fuentes de Datos (Datasources)
    single { SetupIntroRemoteDatasource(get()) }

    // 3. Repositorio
    single<SetupIntroRepository> { SetupIntroRepositoryImpl(get()) }

    // 4. Casos de Uso
    factoryOf(::InitializeAndSyncConfigUseCase)

    // 5. ViewModel
    viewModelOf(::WorkspaceSetupIntroViewModel)
}
