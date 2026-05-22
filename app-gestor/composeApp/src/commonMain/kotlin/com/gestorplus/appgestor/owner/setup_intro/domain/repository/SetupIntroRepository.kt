package com.gestorplus.appgestor.owner.setup_intro.domain.repository

interface SetupIntroRepository {
    suspend fun initializeAndSyncConfig(defaults: Map<String, String>)
}
