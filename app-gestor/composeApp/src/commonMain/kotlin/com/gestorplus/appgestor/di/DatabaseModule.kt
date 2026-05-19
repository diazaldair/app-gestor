package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.data.local.AppDatabase
import com.gestorplus.appgestor.data.local.getRoomDatabase
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> { getRoomDatabase(get()) }
    
    single { get<AppDatabase>().bookingDao() }
    single { get<AppDatabase>().bookingDraftDao() }
    single { get<AppDatabase>().eventLogDao() }
    single { get<AppDatabase>().userProfileDao() }
}
