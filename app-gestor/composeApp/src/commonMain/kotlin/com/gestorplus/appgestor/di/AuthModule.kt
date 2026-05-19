package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.auth.data.datasource.datasource.AuthLocalDatasource
import com.gestorplus.appgestor.auth.data.datasource.datasource.AuthRemoteDatasource
import com.gestorplus.appgestor.auth.data.datasource.mapper.AuthMapper
import com.gestorplus.appgestor.auth.data.datasource.repository.AuthRepositoryImpl
import com.gestorplus.appgestor.auth.data.datasource.service.AuthService
import com.gestorplus.appgestor.auth.domain.repository.AuthRepository
import com.gestorplus.appgestor.auth.domain.usecase.LoginWithEmailUseCase
import com.gestorplus.appgestor.auth.presentation.landing.viewmodel.LandingViewModel
import com.gestorplus.appgestor.auth.presentation.login.viewmodel.LoginViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    // Data & Service Layer
    single { AuthMapper() }
    single { AuthService(get()) }
    single { AuthLocalDatasource() }
    single { AuthRemoteDatasource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }

    // UseCases
    factoryOf(::LoginWithEmailUseCase)

    // ViewModels
    viewModelOf(::LandingViewModel)
    viewModelOf(::LoginViewModel)
}
