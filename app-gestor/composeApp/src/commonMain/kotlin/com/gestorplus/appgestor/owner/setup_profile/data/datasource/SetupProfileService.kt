package com.gestorplus.appgestor.owner.setup_profile.data.datasource

import com.gestorplus.appgestor.data.datasource.FirebaseManager

class SetupProfileService(private val firebaseManager: FirebaseManager) {
    suspend fun saveWorkspaceProfile(uid: String, data: String) {
        firebaseManager.saveData("workspaces/$uid/profile", data)
    }
}
