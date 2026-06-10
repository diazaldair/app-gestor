package com.gestorplus.appgestor.profile.data.datasource.service

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager

class ProfileService(private val firebaseManager: FirebaseManager) {
    suspend fun saveProfile(pipedData: String) {
        val uid = firebaseManager.getCurrentUserUid() ?: "anonymous"
        firebaseManager.saveData("users/$uid/profile", pipedData)
    }
}
