package com.gestorplus.appgestor.owner.schedule.domain.repository

import com.gestorplus.appgestor.owner.schedule.domain.model.*
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    // Turnos por día
    fun getWorkingShifts(): Flow<List<WorkingShift>>
    suspend fun saveWorkingShifts(shifts: List<WorkingShift>): Result<Unit>

    // Master Schedule
    suspend fun getMasterSchedule(): MasterSchedule?
    suspend fun saveMasterSchedule(schedule: MasterSchedule): Result<Unit>

    // Auto Lunch
    suspend fun getAutoLunchConfig(): AutoLunchConfig?
    suspend fun saveAutoLunchConfig(config: AutoLunchConfig): Result<Unit>

    // Timing Defaults
    suspend fun getTimingDefaults(): TimingDefaults?
    suspend fun saveTimingDefaults(defaults: TimingDefaults): Result<Unit>

    // Excepciones
    fun getExceptions(): Flow<List<ScheduleException>>
    suspend fun saveException(exception: ScheduleException): Result<Unit>
    suspend fun deleteException(exceptionId: String): Result<Unit>
}