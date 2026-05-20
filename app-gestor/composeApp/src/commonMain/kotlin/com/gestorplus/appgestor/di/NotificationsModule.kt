package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.notifications.data.datasource.datasource.NotificationsLocalDatasource
import com.gestorplus.appgestor.notifications.data.datasource.datasource.NotificationsRemoteDatasource
import com.gestorplus.appgestor.notifications.data.datasource.repository.NotificationsRepositoryImpl
import com.gestorplus.appgestor.notifications.domain.repository.NotificationsRepository
import com.gestorplus.appgestor.notifications.domain.usecase.GetNotificationsUseCase
import com.gestorplus.appgestor.notifications.presentation.viewmodel.NotificationsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notificationsFeatureModule = module {
    // 1. Mappers y Servicios
    // single { NotificationsMapper() }
    // single { NotificationsService(get()) }
     
    // 2. Fuentes de Datos (Datasources)
    single { NotificationsLocalDatasource(get()) }
    single { NotificationsRemoteDatasource(get()) }
     
    // 3. Repositorio
    single<NotificationsRepository> { NotificationsRepositoryImpl() }
     
    // 4. Casos de Uso
    factoryOf(::GetNotificationsUseCase)
     
    // 5. ViewModel
    viewModelOf(::NotificationsViewModel)
}
