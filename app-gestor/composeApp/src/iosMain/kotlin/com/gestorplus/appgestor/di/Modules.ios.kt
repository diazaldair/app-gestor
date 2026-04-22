package com.gestorplus.appgestor.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.gestorplus.appgestor.data.local.AppDatabase
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
}
