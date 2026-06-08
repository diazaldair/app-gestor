package com.gestorplus.appgestor.owner.setup_schedule.data.datasource

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager

class SetupScheduleService(private val firebaseManager: FirebaseManager) {
    suspend fun saveWorkspaceSchedule(uid: String, data: String) {
        firebaseManager.saveData("workspaces/$uid/schedule", data)
    }
}
