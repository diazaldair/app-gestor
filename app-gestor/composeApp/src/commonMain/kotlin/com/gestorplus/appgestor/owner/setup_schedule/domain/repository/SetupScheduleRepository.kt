package com.gestorplus.appgestor.owner.setup_schedule.domain.repository

import com.gestorplus.appgestor.owner.setup_schedule.domain.model.WorkspaceSchedule

interface SetupScheduleRepository {
    suspend fun saveWorkspaceSchedule(schedule: WorkspaceSchedule): Result<Unit>
}
