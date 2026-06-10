package com.gestorplus.appgestor.core.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    // Usamos un nombre fresco para evitar cualquier residuo de versiones anteriores
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = "gestor_plus_fresh.db"
    ).fallbackToDestructiveMigration(true)
}
