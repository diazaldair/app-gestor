package com.gestorplus.appgestor.owner.setup_intro.data.datasource.service

import com.gestorplus.appgestor.data.datasource.FirebaseManager

class SetupIntroService(private val firebaseManager: FirebaseManager) {
    suspend fun initializeRemoteConfig(defaults: Map<String, String>) {
        firebaseManager.initializeRemoteConfig(defaults)
    }

    suspend fun fetchAndActivate() {
        firebaseManager.fetchAndActivate()
    }
}
