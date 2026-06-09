package com.gestorplus.appgestor.owner.setup_schedule.data.repository

import com.gestorplus.appgestor.owner.setup_schedule.data.datasource.SetupScheduleRemoteDatasource
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.WorkspaceSchedule
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.Shift
import com.gestorplus.appgestor.owner.setup_schedule.domain.repository.SetupScheduleRepository
import com.gestorplus.appgestor.owner.setup_schedule.data.local.dao.ShiftDao
import com.gestorplus.appgestor.owner.setup_schedule.data.datasource.mapper.ShiftMapper
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SetupScheduleRepositoryImpl(
    private val remoteDatasource: SetupScheduleRemoteDatasource,
    private val firebaseManager: FirebaseManager,
    private val shiftDao: ShiftDao,
    private val mapper: ShiftMapper
) : SetupScheduleRepository {

    override fun getShifts(): Flow<List<Shift>> {
        return shiftDao.getAllShifts().map { entities ->
            entities.map { mapper.toDomain(it) }
        }
    }

    override suspend fun saveWorkspaceSchedule(schedule: WorkspaceSchedule): Result<Unit> {
        return try {
            val dataString = Json.encodeToString(schedule)
            val currentUid = firebaseManager.getCurrentUserUid() ?: throw Exception("Usuario no autenticado")
            remoteDatasource.saveWorkspaceSchedule(currentUid, dataString)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveDetailedShifts(shifts: List<Shift>): Result<Unit> {
        return try {
            // 1. Persistencia Local (Offline-First)
            val entities = shifts.map { mapper.toEntity(it) }
            shiftDao.updateAllShifts(entities)

            // 2. Persistencia Remota (Cloud Sync)
            val dataString = Json.encodeToString(shifts)
            val currentUid = firebaseManager.getCurrentUserUid() ?: throw Exception("Usuario no autenticado")
            remoteDatasource.saveWorkspaceSchedule(currentUid, dataString)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteShift(shiftId: String) {
        shiftDao.deleteShiftById(shiftId)
        // En una app real aquí dispararíamos una sincronización con Firebase para borrarlo también allá
    }
}
