package com.gestorplus.appgestor.profile.data.datasource.service

import com.gestorplus.appgestor.data.datasource.FirebaseManager

class ProfileService(private val firebaseManager: FirebaseManager) {
    suspend fun saveProfile(pipedData: String) {
        firebaseManager.saveData("profile/owner_profile", pipedData)
    }
}
