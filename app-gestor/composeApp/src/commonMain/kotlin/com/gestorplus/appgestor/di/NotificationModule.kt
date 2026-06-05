package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.notification.domain.HandleNotificationUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val notificationModule = module {
    factoryOf(::HandleNotificationUseCase)
}
