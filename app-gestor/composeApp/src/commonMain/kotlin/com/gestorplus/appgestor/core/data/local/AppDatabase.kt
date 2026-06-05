package com.gestorplus.appgestor.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gestorplus.appgestor.core.data.local.dao.BookingDao
import com.gestorplus.appgestor.core.data.local.dao.BookingDraftDao
import com.gestorplus.appgestor.core.data.local.dao.EventLogDao
import com.gestorplus.appgestor.core.data.local.dao.UserProfileDao
import com.gestorplus.appgestor.core.data.local.entity.BookingEntity
import com.gestorplus.appgestor.core.data.local.entity.BookingDraftEntity
import com.gestorplus.appgestor.core.data.local.entity.EventLogEntity
import com.gestorplus.appgestor.core.data.local.entity.UserProfileEntity

@Database(entities = [BookingEntity::class, BookingDraftEntity::class, EventLogEntity::class, UserProfileEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun bookingDao(): BookingDao
    abstract fun bookingDraftDao(): BookingDraftDao
    abstract fun eventLogDao(): EventLogDao
    abstract fun userProfileDao(): UserProfileDao
}

// Interfaz marcadora para Room en KMP
interface DB
