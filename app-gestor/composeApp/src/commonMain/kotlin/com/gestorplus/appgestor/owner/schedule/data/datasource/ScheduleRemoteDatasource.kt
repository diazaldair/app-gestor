package com.gestorplus.appgestor.owner.schedule.data.datasource

import com.gestorplus.appgestor.owner.schedule.data.datasource.service.ScheduleRemoteService
import com.gestorplus.appgestor.owner.schedule.domain.model.*

class ScheduleRemoteDatasource(
    private val service: ScheduleRemoteService
) {
    suspend fun saveWorkingShifts(uid: String, shifts: List<WorkingShift>) = service.saveWorkingShifts(uid, shifts)
    suspend fun getWorkingShifts(uid: String) = service.getWorkingShifts(uid)
    suspend fun saveMasterSchedule(uid: String, schedule: MasterSchedule) = service.saveMasterSchedule(uid, schedule)
    suspend fun getMasterSchedule(uid: String) = service.getMasterSchedule(uid)
    suspend fun saveAutoLunch(uid: String, config: AutoLunchConfig) = service.saveAutoLunch(uid, config)
    suspend fun getAutoLunch(uid: String) = service.getAutoLunch(uid)
    suspend fun saveTimingDefaults(uid: String, defaults: TimingDefaults) = service.saveTimingDefaults(uid, defaults)
    suspend fun getTimingDefaults(uid: String) = service.getTimingDefaults(uid)
    suspend fun saveException(uid: String, exception: ScheduleException) = service.saveException(uid, exception)
    suspend fun deleteException(uid: String, exceptionId: String) = service.deleteException(uid, exceptionId)
    suspend fun getExceptions(uid: String) = service.getExceptions(uid)
}