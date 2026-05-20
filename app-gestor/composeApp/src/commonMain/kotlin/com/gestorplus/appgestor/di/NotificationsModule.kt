package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.notifications.data.datasource.repository.NotificationsRepositoryImpl
import com.gestorplus.appgestor.notifications.domain.repository.NotificationsRepository
import com.gestorplus.appgestor.notifications.domain.usecase.GetNotificationsUseCase
import com.gestorplus.appgestor.notifications.presentation.viewmodel.NotificationsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notificationsFeatureModule = module {
    single<NotificationsRepository> { NotificationsRepositoryImpl() }
    factoryOf(::GetNotificationsUseCase)
    viewModelOf(::NotificationsViewModel)
}
