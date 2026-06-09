package com.gestorplus.appgestor.di

import androidx.room.RoomDatabase
import com.gestorplus.appgestor.auth.data.service.AndroidGoogleSignInService
import com.gestorplus.appgestor.auth.domain.service.GoogleSignInService
import com.gestorplus.appgestor.core.notification.NotificationHelper
import com.gestorplus.appgestor.core.data.local.AppDatabase
import com.gestorplus.appgestor.core.data.local.getDatabaseBuilder
import com.gestorplus.appgestor.core.data.repository.NotificationRepositoryImpl
import com.gestorplus.appgestor.core.util.ActivityProvider
import com.gestorplus.appgestor.core.util.ActivityProviderImpl
import com.gestorplus.appgestor.notification.domain.NotificationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder(androidContext())
    }

    // Preferencias Locales
    single { com.gestorplus.appgestor.core.persistence.LocalPreferences(androidContext()) }

    // Notificaciones
    single { NotificationHelper(androidContext()) }
    single<NotificationRepository> { NotificationRepositoryImpl(androidContext(), get()) }

    // Google Sign-In Infrastructure
    single<ActivityProvider> { ActivityProviderImpl() }
    single<GoogleSignInService> { AndroidGoogleSignInService(androidContext(), get()) }
}
