package com.gestorplus.appgestor.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gestorplus.appgestor.core.data.local.dao.BookingDao
import com.gestorplus.appgestor.core.data.local.dao.BookingDraftDao
import com.gestorplus.appgestor.core.data.local.dao.EventLogDao
import com.gestorplus.appgestor.core.data.local.dao.UserProfileDao
import com.gestorplus.appgestor.core.data.local.dao.WorkingShiftDao
import com.gestorplus.appgestor.core.data.local.dao.MasterScheduleDao
import com.gestorplus.appgestor.core.data.local.dao.AutoLunchDao
import com.gestorplus.appgestor.core.data.local.dao.TimingDefaultsDao
import com.gestorplus.appgestor.core.data.local.dao.ScheduleExceptionDao
import com.gestorplus.appgestor.core.data.local.entity.BookingEntity
import com.gestorplus.appgestor.core.data.local.entity.BookingDraftEntity
import com.gestorplus.appgestor.core.data.local.entity.EventLogEntity
import com.gestorplus.appgestor.core.data.local.entity.UserProfileEntity
import com.gestorplus.appgestor.core.data.local.entity.WorkingShiftEntity
import com.gestorplus.appgestor.core.data.local.entity.MasterScheduleEntity
import com.gestorplus.appgestor.core.data.local.entity.AutoLunchEntity
import com.gestorplus.appgestor.core.data.local.entity.TimingDefaultsEntity
import com.gestorplus.appgestor.core.data.local.entity.ScheduleExceptionEntity

@Database(
    entities = [
        BookingEntity::class,
        BookingDraftEntity::class,
        EventLogEntity::class,
        UserProfileEntity::class,
        WorkingShiftEntity::class,
        MasterScheduleEntity::class,
        AutoLunchEntity::class,
        TimingDefaultsEntity::class,
        ScheduleExceptionEntity::class,
    ],
    version = 4,
)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun bookingDao(): BookingDao
    abstract fun bookingDraftDao(): BookingDraftDao
    abstract fun eventLogDao(): EventLogDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun workingShiftDao(): WorkingShiftDao
    abstract fun masterScheduleDao(): MasterScheduleDao
    abstract fun autoLunchDao(): AutoLunchDao
    abstract fun timingDefaultsDao(): TimingDefaultsDao
    abstract fun scheduleExceptionDao(): ScheduleExceptionDao
}

// Interfaz marcadora para Room en KMP
interface DB
