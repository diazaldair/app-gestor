package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.data.local.AppDatabase
import com.gestorplus.appgestor.data.local.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

// Módulo compartido para la base de datos local
val databaseModule = module {
    // Provee la instancia de AppDatabase usando el builder de plataforma
    single<AppDatabase> { getRoomDatabase(get()) }
    
    // Provee los DAOs para que sea fácil de inyectar en Repositorios
    single { get<AppDatabase>().bookingDao() }
    single { get<AppDatabase>().bookingDraftDao() }
    single { get<AppDatabase>().eventLogDao() }
}

// Módulo que debe ser implementado en cada plataforma (androidMain e iosMain)
expect val platformModule: Module
