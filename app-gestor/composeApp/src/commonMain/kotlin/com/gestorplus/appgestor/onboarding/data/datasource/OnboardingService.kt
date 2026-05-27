package com.gestorplus.appgestor.onboarding.data.datasource

import com.gestorplus.appgestor.data.datasource.FirebaseManager
import kotlinx.serialization.json.Json

class OnboardingService(private val firebaseManager: FirebaseManager) {
    suspend fun fetchAndActivate() {
        firebaseManager.fetchAndActivate()
    }

    fun getOnboardingConfig(): String {
        return firebaseManager.getString("onboarding_config")
    }
}
