package com.gestorplus.appgestor.di

import org.koin.core.module.Module

/**
 * Main aggregator for all Koin modules in the project.
 * This structure allows for easy scalability as new features are added.
 */
val appModules: List<Module> = listOf(
    dataModule,
    databaseModule,
    onboardingModule,
    bookingModule,
    setupIntroModule,
    setupProfileModule,
    setupScheduleModule,
    setupServiceModule,
    setupSuccessModule,
    dashboardModule,
    homeModule,
    profileModule,
    clinicProfileModule,
    notificationModule,
    notificationsModule,
    platformModule,
    authModule
)
