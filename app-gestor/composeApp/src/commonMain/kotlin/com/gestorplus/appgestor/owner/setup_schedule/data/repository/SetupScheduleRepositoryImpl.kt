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
import com.gestorplus.appgestor.owner.setup_schedule.data.datasource.SetupScheduleService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SetupScheduleRepositoryImpl(
    private val remoteDatasource: SetupScheduleRemoteDatasource,
    private val firebaseManager: FirebaseManager,
    private val shiftDao: ShiftDao,
    private val mapper: ShiftMapper,
    private val setupScheduleService: SetupScheduleService
) : SetupScheduleRepository {

    override fun getShifts(): Flow<List<Shift>> {
        return shiftDao.getAllShifts().map { entities ->
            entities.map { mapper.toDomain(it) }
        }
    }

    override suspend fun refreshShifts(): Result<Unit> {
        return try {
            val currentUid = firebaseManager.getCurrentUserUid() ?: throw Exception("Usuario no autenticado")
            val remoteData = setupScheduleService.getWorkspaceSchedule(currentUid)
            
            if (remoteData != null) {
                // Intentamos parsear como lista de Shifts primero (formato detallado)
                val shifts = try {
                    Json.decodeFromString<List<Shift>>(remoteData)
                } catch (e: Exception) {
                    // Si falla, intentamos parsear como WorkspaceSchedule (formato inicial)
                    val schedule = Json.decodeFromString<WorkspaceSchedule>(remoteData)
                    convertToShifts(schedule)
                }
                
                if (shifts.isNotEmpty()) {
                    val entities = shifts.map { mapper.toEntity(it) }
                    shiftDao.updateAllShifts(entities)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveWorkspaceSchedule(schedule: WorkspaceSchedule): Result<Unit> {
        return try {
            val currentUid = firebaseManager.getCurrentUserUid() ?: throw Exception("Usuario no autenticado")
            
            // 1. Convertir a Shifts y guardar localmente
            val initialShifts = convertToShifts(schedule)
            if (initialShifts.isNotEmpty()) {
                val entities = initialShifts.map { mapper.toEntity(it) }
                shiftDao.updateAllShifts(entities)
            }

            // 2. Guardar en remoto
            val dataString = Json.encodeToString(schedule)
            remoteDatasource.saveWorkspaceSchedule(currentUid, dataString)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun convertToShifts(schedule: WorkspaceSchedule): List<Shift> {
        val shifts = mutableListOf<Shift>()
        if (schedule.morningStart.isNotEmpty() && schedule.morningEnd.isNotEmpty()) {
            shifts.add(Shift(
                id = "morning_init",
                name = "Turno Mañana",
                startTime = schedule.morningStart,
                endTime = schedule.morningEnd,
                days = schedule.workingDays
            ))
        }
        if (schedule.afternoonStart.isNotEmpty() && schedule.afternoonEnd.isNotEmpty()) {
            shifts.add(Shift(
                id = "afternoon_init",
                name = "Turno Tarde",
                startTime = schedule.afternoonStart,
                endTime = schedule.afternoonEnd,
                days = schedule.workingDays
            ))
        }
        return shifts
    }

    override suspend fun saveDetailedShifts(shifts: List<Shift>): Result<Unit> {
        return try {
            val entities = shifts.map { mapper.toEntity(it) }
            shiftDao.updateAllShifts(entities)

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
    }
}
