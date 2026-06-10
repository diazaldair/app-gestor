package com.gestorplus.appgestor.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.gestorplus.appgestor.core.data.local.AppDatabase
import platform.Foundation.NSHomeDirectory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val dbFilePath = NSHomeDirectory() + "/gestorplus.db"
        Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
            factory = { AppDatabase::class.instantiateImpl() } // Necesario para iOS
        )
    }
    
    // Preferencias Locales
    single<com.gestorplus.appgestor.core.persistence.LocalPreferences> { 
        com.gestorplus.appgestor.core.persistence.IosLocalPreferences() 
    }
}
