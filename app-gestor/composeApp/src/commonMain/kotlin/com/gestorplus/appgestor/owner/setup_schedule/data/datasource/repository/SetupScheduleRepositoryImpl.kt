package com.gestorplus.appgestor.owner.setup_schedule.data.datasource.repository

import com.gestorplus.appgestor.owner.setup_schedule.data.datasource.datasource.SetupScheduleRemoteDatasource
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.WorkspaceSchedule
import com.gestorplus.appgestor.owner.setup_schedule.domain.repository.SetupScheduleRepository

class SetupScheduleRepositoryImpl(
    private val remoteDatasource: SetupScheduleRemoteDatasource
) : SetupScheduleRepository {

    override suspend fun saveWorkspaceSchedule(schedule: WorkspaceSchedule): Result<Unit> {
        return try {
            val daysStr = schedule.workingDays.joinToString(",")
            val dataString = "$daysStr|${schedule.morningStart}|${schedule.morningEnd}|${schedule.afternoonStart}|${schedule.afternoonEnd}"
            val currentUid = "current_user_123" // TO-DO: get from Auth
            remoteDatasource.saveWorkspaceSchedule(currentUid, dataString)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
