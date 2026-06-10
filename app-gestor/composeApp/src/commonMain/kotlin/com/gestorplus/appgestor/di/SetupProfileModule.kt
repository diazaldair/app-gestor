package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileRemoteDatasource
import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileRemoteDatasourceImpl
import com.gestorplus.appgestor.owner.setup_profile.data.repository.SetupProfileRepositoryImpl
import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileService
import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository
import com.gestorplus.appgestor.owner.setup_profile.domain.usecase.SaveWorkspaceProfileUseCase
import com.gestorplus.appgestor.owner.setup_profile.domain.usecase.IsProfileSetupUseCase
import com.gestorplus.appgestor.owner.setup_profile.presentation.viewmodel.WorkspaceSetupProfileViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val setupProfileModule = module {
    single { SetupProfileService(get()) }
    single<SetupProfileRemoteDatasource> { SetupProfileRemoteDatasourceImpl(get()) }
    // SetupProfileRepositoryImpl necesita 5 parámetros: remoteDatasource, firebaseManager, localPreferences, clinicProfileDao, notificationRepository
    single<SetupProfileRepository> { SetupProfileRepositoryImpl(get(), get(), get(), get(), get()) }
    
    factoryOf(::SaveWorkspaceProfileUseCase)
    factoryOf(::IsProfileSetupUseCase)
    viewModelOf(::WorkspaceSetupProfileViewModel)
}
