package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.profile.data.datasource.datasource.ProfileLocalDatasource
import com.gestorplus.appgestor.profile.data.datasource.datasource.ProfileRemoteDatasource
import com.gestorplus.appgestor.profile.data.datasource.mapper.ProfileMapper
import com.gestorplus.appgestor.profile.data.datasource.repository.ProfileRepositoryImpl
import com.gestorplus.appgestor.profile.data.datasource.service.ProfileService
import com.gestorplus.appgestor.profile.domain.repository.ProfileRepository
import com.gestorplus.appgestor.profile.domain.usecase.GetUserProfileUseCase
import com.gestorplus.appgestor.profile.domain.usecase.UpdateUserProfileUseCase
import com.gestorplus.appgestor.profile.presentation.viewmodel.ProfileViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    single { ProfileMapper() }
    single { ProfileService(get()) }
    single { ProfileLocalDatasource(get()) }
    single { ProfileRemoteDatasource(get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get()) }

    factoryOf(::GetUserProfileUseCase)
    factoryOf(::UpdateUserProfileUseCase)

    viewModelOf(::ProfileViewModel)
}
