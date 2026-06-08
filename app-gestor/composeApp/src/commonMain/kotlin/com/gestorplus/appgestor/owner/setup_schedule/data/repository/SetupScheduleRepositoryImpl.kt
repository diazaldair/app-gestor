package com.gestorplus.appgestor.owner.setup_schedule.data.repository

import com.gestorplus.appgestor.owner.setup_schedule.data.datasource.SetupScheduleRemoteDatasource
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.WorkspaceSchedule
import com.gestorplus.appgestor.owner.setup_schedule.domain.repository.SetupScheduleRepository

import kotlinx.serialization.json.Json
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager

class SetupScheduleRepositoryImpl(
    private val remoteDatasource: SetupScheduleRemoteDatasource,
    private val firebaseManager: FirebaseManager
) : SetupScheduleRepository {

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
}
