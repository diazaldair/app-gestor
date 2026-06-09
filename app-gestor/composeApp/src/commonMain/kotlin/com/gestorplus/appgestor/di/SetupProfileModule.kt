package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileRemoteDatasource
import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileRemoteDatasourceImpl
import com.gestorplus.appgestor.owner.setup_profile.data.repository.SetupProfileRepositoryImpl
import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileService
import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository
import com.gestorplus.appgestor.owner.setup_profile.domain.usecase.SaveWorkspaceProfileUseCase
import com.gestorplus.appgestor.owner.setup_profile.presentation.viewmodel.WorkspaceSetupProfileViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val setupProfileModule = module {
    // 1. Servicios
    single { SetupProfileService(get()) }

    // 2. Fuentes de Datos (Datasources)
    single<SetupProfileRemoteDatasource> { SetupProfileRemoteDatasourceImpl(get()) }

    // 3. Repositorio
    single<SetupProfileRepository> { SetupProfileRepositoryImpl(get(), get()) }

    // 4. Casos de Uso
    factoryOf(::SaveWorkspaceProfileUseCase)

    // 5. ViewModel
    viewModelOf(::WorkspaceSetupProfileViewModel)
}
