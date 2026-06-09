package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.core.data.local.AppDatabase
import com.gestorplus.appgestor.core.data.local.getRoomDatabase
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> { getRoomDatabase(get()) }
    
    single { get<AppDatabase>().bookingDao() }
    single { get<AppDatabase>().bookingDraftDao() }
    single { get<AppDatabase>().eventLogDao() }
    single { get<AppDatabase>().userProfileDao() }
    single { get<AppDatabase>().serviceDao() }
    single { get<AppDatabase>().clinicProfileDao() }
    single { get<AppDatabase>().notificationDao() }
    single { get<AppDatabase>().shiftDao() }
}
