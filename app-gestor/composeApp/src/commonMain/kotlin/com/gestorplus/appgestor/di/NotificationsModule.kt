package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.notifications.data.datasource.datasource.NotificationsLocalDatasource
import com.gestorplus.appgestor.notifications.data.datasource.datasource.NotificationsRemoteDatasource
import com.gestorplus.appgestor.notifications.data.datasource.mapper.NotificationMapper
import com.gestorplus.appgestor.notifications.data.datasource.repository.NotificationsRepositoryImpl
import com.gestorplus.appgestor.notifications.data.datasource.service.NotificationsService
import com.gestorplus.appgestor.notifications.domain.repository.NotificationsRepository
import com.gestorplus.appgestor.notifications.domain.usecase.AcceptAppointmentUseCase
import com.gestorplus.appgestor.notifications.domain.usecase.DeclineAppointmentUseCase
import com.gestorplus.appgestor.notifications.domain.usecase.GetNotificationsUseCase
import com.gestorplus.appgestor.notifications.presentation.viewmodel.NotificationsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notificationsModule = module {
    single { NotificationMapper() }
    single { NotificationsService(get()) }
    single { NotificationsLocalDatasource() }
    single { NotificationsRemoteDatasource(get()) }
    single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get(), get(), get()) }

    factoryOf(::GetNotificationsUseCase)
    factoryOf(::AcceptAppointmentUseCase)
    factoryOf(::DeclineAppointmentUseCase)

    viewModelOf(::NotificationsViewModel)
}
