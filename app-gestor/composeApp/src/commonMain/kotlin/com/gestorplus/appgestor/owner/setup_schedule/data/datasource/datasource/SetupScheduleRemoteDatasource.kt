package com.gestorplus.appgestor.owner.setup_schedule.data.datasource.datasource

import com.gestorplus.appgestor.owner.setup_schedule.data.datasource.service.SetupScheduleService

class SetupScheduleRemoteDatasource(private val setupScheduleService: SetupScheduleService) {
    suspend fun saveWorkspaceSchedule(uid: String, data: String) {
        setupScheduleService.saveWorkspaceSchedule(uid, data)
    }
}
