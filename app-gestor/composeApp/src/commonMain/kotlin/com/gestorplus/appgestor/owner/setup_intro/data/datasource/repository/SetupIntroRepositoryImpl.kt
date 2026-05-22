package com.gestorplus.appgestor.owner.setup_intro.data.datasource.repository

import com.gestorplus.appgestor.owner.setup_intro.data.datasource.datasource.SetupIntroRemoteDatasource
import com.gestorplus.appgestor.owner.setup_intro.domain.repository.SetupIntroRepository

class SetupIntroRepositoryImpl(
    private val remoteDatasource: SetupIntroRemoteDatasource
) : SetupIntroRepository {

    override suspend fun initializeAndSyncConfig(defaults: Map<String, String>) {
        try {
            remoteDatasource.initializeRemoteConfig(defaults)
            remoteDatasource.fetchAndActivate()
        } catch (e: Exception) {
            // Offline-first: keep local defaults if fetch fails
        }
    }
}
