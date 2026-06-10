package com.gestorplus.appgestor.owner.setup_schedule.domain.repository

import com.gestorplus.appgestor.owner.setup_schedule.domain.model.Shift
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.WorkspaceSchedule
import kotlinx.coroutines.flow.Flow

interface SetupScheduleRepository {
    fun getShifts(): Flow<List<Shift>>
    suspend fun refreshShifts(): Result<Unit>
    suspend fun saveWorkspaceSchedule(schedule: WorkspaceSchedule): Result<Unit>
    suspend fun saveDetailedShifts(shifts: List<Shift>): Result<Unit>
    suspend fun deleteShift(shiftId: String)
}
