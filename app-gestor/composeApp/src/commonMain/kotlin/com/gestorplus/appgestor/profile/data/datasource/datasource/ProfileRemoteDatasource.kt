package com.gestorplus.appgestor.profile.data.datasource.datasource

import com.gestorplus.appgestor.profile.data.datasource.service.ProfileService

class ProfileRemoteDatasource(private val profileService: ProfileService) {
    suspend fun saveProfile(pipedData: String) {
        profileService.saveProfile(pipedData)
    }
}
