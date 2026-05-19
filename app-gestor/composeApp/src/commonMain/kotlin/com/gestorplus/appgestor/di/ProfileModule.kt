package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.profile.data.mapper.ProfileMapper
import com.gestorplus.appgestor.profile.data.repository.ProfileRepositoryImpl
import com.gestorplus.appgestor.profile.domain.repository.ProfileRepository
import com.gestorplus.appgestor.profile.domain.usecase.GetUserProfileUseCase
import com.gestorplus.appgestor.profile.domain.usecase.UpdateUserProfileUseCase
import com.gestorplus.appgestor.profile.presentation.viewmodel.ProfileViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    single { ProfileMapper() }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get()) }

    factoryOf(::GetUserProfileUseCase)
    factoryOf(::UpdateUserProfileUseCase)

    viewModelOf(::ProfileViewModel)
}
