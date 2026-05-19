package com.gestorplus.appgestor.di

import androidx.room.RoomDatabase
import com.gestorplus.appgestor.core.notification.NotificationHelper
import com.gestorplus.appgestor.data.local.AppDatabase
import com.gestorplus.appgestor.data.local.getDatabaseBuilder
import com.gestorplus.appgestor.data.repository.NotificationRepositoryImpl
import com.gestorplus.appgestor.domain.notification.NotificationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder(androidContext())
    }

    // Notificaciones
    single { NotificationHelper(androidContext()) }
    single<NotificationRepository> { NotificationRepositoryImpl(androidContext(), get()) }
}
