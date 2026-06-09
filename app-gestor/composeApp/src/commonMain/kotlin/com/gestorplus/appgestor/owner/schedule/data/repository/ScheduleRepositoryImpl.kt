package com.gestorplus.appgestor.owner.schedule.data.repository

import com.gestorplus.appgestor.core.data.local.dao.*
import com.gestorplus.appgestor.owner.schedule.data.datasource.ScheduleRemoteDatasource
import com.gestorplus.appgestor.owner.schedule.data.mapper.ScheduleMapper
import com.gestorplus.appgestor.owner.schedule.domain.model.*
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScheduleRepositoryImpl(
    private val shiftDao: WorkingShiftDao,
    private val masterDao: MasterScheduleDao,
    private val lunchDao: AutoLunchDao,
    private val timingDao: TimingDefaultsDao,
    private val exceptionDao: ScheduleExceptionDao,
    private val remoteDs: ScheduleRemoteDatasource,
    private val mapper: ScheduleMapper,
    private val uidProvider: () -> String?
) : ScheduleRepository {

    // Working Shifts
    override fun getWorkingShifts(): Flow<List<WorkingShift>> =
        shiftDao.getAll().map { entities -> entities.map { mapper.toDomain(it) } }

    override suspend fun saveWorkingShifts(shifts: List<WorkingShift>): Result<Unit> {
        val uid = uidProvider() ?: return Result.failure(IllegalStateException("No user"))
        return try {
            shiftDao.deleteAll()
            shifts.forEach { shiftDao.insert(mapper.toEntity(it)) }
            remoteDs.saveWorkingShifts(uid, shifts)
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    // Master Schedule
    override suspend fun getMasterSchedule(): MasterSchedule? {
        return masterDao.get()?.let { mapper.toDomain(it) }
    }

    override suspend fun saveMasterSchedule(schedule: MasterSchedule): Result<Unit> {
        val uid = uidProvider() ?: return Result.failure(IllegalStateException("No user"))
        return try {
            masterDao.insert(mapper.toEntity(schedule))
            remoteDs.saveMasterSchedule(uid, schedule)
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    // Auto Lunch
    override suspend fun getAutoLunchConfig(): AutoLunchConfig? {
        return lunchDao.get()?.let { mapper.toDomain(it) }
    }

    override suspend fun saveAutoLunchConfig(config: AutoLunchConfig): Result<Unit> {
        val uid = uidProvider() ?: return Result.failure(IllegalStateException("No user"))
        return try {
            lunchDao.insert(mapper.toEntity(config))
            remoteDs.saveAutoLunch(uid, config)
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    // Timing Defaults
    override suspend fun getTimingDefaults(): TimingDefaults? {
        return timingDao.get()?.let { mapper.toDomain(it) }
    }

    override suspend fun saveTimingDefaults(defaults: TimingDefaults): Result<Unit> {
        val uid = uidProvider() ?: return Result.failure(IllegalStateException("No user"))
        return try {
            timingDao.insert(mapper.toEntity(defaults))
            remoteDs.saveTimingDefaults(uid, defaults)
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    // Exceptions
    override fun getExceptions(): Flow<List<ScheduleException>> =
        exceptionDao.getAll().map { entities -> entities.map { mapper.toDomain(it) } }

    override suspend fun saveException(exception: ScheduleException): Result<Unit> {
        val uid = uidProvider() ?: return Result.failure(IllegalStateException("No user"))
        return try {
            exceptionDao.insert(mapper.toEntity(exception))
            remoteDs.saveException(uid, exception)
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun deleteException(exceptionId: String): Result<Unit> {
        val uid = uidProvider() ?: return Result.failure(IllegalStateException("No user"))
        return try {
            val entity = exceptionDao.getByDate(exceptionId) // OJO: esto busca por fecha, no por id. Mejor implementar getById.
            entity?.let { exceptionDao.delete(it) }
            remoteDs.deleteException(uid, exceptionId)
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }
}