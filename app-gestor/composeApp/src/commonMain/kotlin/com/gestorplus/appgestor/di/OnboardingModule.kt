package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.onboarding.presentation.viewmodel.LandingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onboardingModule = module {
    viewModelOf(::LandingViewModel)
}
