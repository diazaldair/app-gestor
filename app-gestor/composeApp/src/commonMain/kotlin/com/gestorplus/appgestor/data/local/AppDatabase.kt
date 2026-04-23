package com.gestorplus.appgestor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gestorplus.appgestor.data.local.dao.BookingDao
import com.gestorplus.appgestor.data.local.dao.BookingDraftDao
import com.gestorplus.appgestor.data.local.dao.EventLogDao
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import com.gestorplus.appgestor.data.local.entity.BookingDraftEntity
import com.gestorplus.appgestor.data.local.entity.EventLogEntity

@Database(entities = [BookingEntity::class, BookingDraftEntity::class, EventLogEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun bookingDao(): BookingDao
    abstract fun bookingDraftDao(): BookingDraftDao
    abstract fun eventLogDao(): EventLogDao
}

// Interfaz marcadora para Room en KMP
interface DB
