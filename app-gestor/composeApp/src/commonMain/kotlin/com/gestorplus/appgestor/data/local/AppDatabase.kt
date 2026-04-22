package com.gestorplus.appgestor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gestorplus.appgestor.data.local.dao.BookingDao
import com.gestorplus.appgestor.data.local.entity.BookingEntity

@Database(entities = [BookingEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun bookingDao(): BookingDao
}

// Interfaz marcadora para Room en KMP
interface DB
