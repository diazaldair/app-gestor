package com.gestorplus.appgestor.owner.setup_success.domain.repository

interface SetupSuccessRepository {
    suspend fun getRegisteredClinicName(): Result<String>
}
