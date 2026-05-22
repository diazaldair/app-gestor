package com.gestorplus.appgestor.owner.setup_intro.data.datasource.datasource

import com.gestorplus.appgestor.owner.setup_intro.data.datasource.service.SetupIntroService

class SetupIntroRemoteDatasource(private val setupIntroService: SetupIntroService) {
    suspend fun initializeRemoteConfig(defaults: Map<String, String>) {
        setupIntroService.initializeRemoteConfig(defaults)
    }

    suspend fun fetchAndActivate() {
        setupIntroService.fetchAndActivate()
    }
}
