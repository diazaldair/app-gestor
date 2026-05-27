package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.onboarding.data.datasource.OnboardingRemoteDatasource
import com.gestorplus.appgestor.onboarding.data.datasource.OnboardingService
import com.gestorplus.appgestor.onboarding.data.repository.OnboardingRepositoryImpl
import com.gestorplus.appgestor.onboarding.domain.repository.OnboardingRepository
import com.gestorplus.appgestor.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.gestorplus.appgestor.onboarding.domain.usecase.GetOnboardingConfigUseCase
import com.gestorplus.appgestor.onboarding.domain.usecase.IsOnboardingCompletedUseCase
import com.gestorplus.appgestor.onboarding.presentation.viewmodel.OnboardingViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onboardingModule = module {
    // 1. Service
    single { OnboardingService(get()) }
    
    // 2. Datasource
    single { OnboardingRemoteDatasource(get()) }
    
    // 3. Repository
    single<OnboardingRepository> { OnboardingRepositoryImpl(get(), get()) }
    
    // 4. UseCases
    factoryOf(::GetOnboardingConfigUseCase)
    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::CompleteOnboardingUseCase)
    
    // 5. ViewModel
    viewModelOf(::OnboardingViewModel)
}
