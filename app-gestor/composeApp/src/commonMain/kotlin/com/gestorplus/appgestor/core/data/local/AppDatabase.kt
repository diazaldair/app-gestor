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
import com.gestorplus.appgestor.services.data.local.dao.ServiceDao
import com.gestorplus.appgestor.services.data.local.entity.ServiceEntity
import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicProfileDao
import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicDao
import com.gestorplus.appgestor.clinicProfile.data.local.entity.ClinicProfileEntity
import com.gestorplus.appgestor.clinicProfile.data.local.entity.ClinicEntity
import com.gestorplus.appgestor.notifications.data.local.dao.NotificationDao
import com.gestorplus.appgestor.notifications.data.local.entity.NotificationEntity
import com.gestorplus.appgestor.owner.setup_schedule.data.local.dao.ShiftDao
import com.gestorplus.appgestor.owner.setup_schedule.data.local.entity.ShiftEntity
import com.gestorplus.appgestor.clinic_detail.data.local.dao.ClinicServiceDao
import com.gestorplus.appgestor.clinic_detail.data.local.entity.ClinicServiceEntity
import com.gestorplus.appgestor.my_bookings.data.local.dao.PatientBookingDao
import com.gestorplus.appgestor.my_bookings.data.local.entity.PatientBookingEntity

@Database(
    entities = [
        BookingEntity::class, 
        BookingDraftEntity::class, 
        EventLogEntity::class, 
        UserProfileEntity::class,
        ServiceEntity::class,
        ClinicProfileEntity::class,
        ClinicEntity::class,
        NotificationEntity::class,
        ShiftEntity::class,
        ClinicServiceEntity::class,
        PatientBookingEntity::class
    ], 
    version = 12
)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun bookingDao(): BookingDao
    abstract fun bookingDraftDao(): BookingDraftDao
    abstract fun eventLogDao(): EventLogDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun serviceDao(): ServiceDao
    abstract fun clinicProfileDao(): ClinicProfileDao
    abstract fun clinicDao(): ClinicDao
    abstract fun notificationDao(): NotificationDao
    abstract fun shiftDao(): ShiftDao
    abstract fun clinicServiceDao(): ClinicServiceDao
    abstract fun patientBookingDao(): PatientBookingDao
}

// Interfaz marcadora para Room en KMP
interface DB
