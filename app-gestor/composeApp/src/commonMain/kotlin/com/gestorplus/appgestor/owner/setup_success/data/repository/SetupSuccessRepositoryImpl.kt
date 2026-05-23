package com.gestorplus.appgestor.owner.setup_success.data.repository

import com.gestorplus.appgestor.owner.setup_success.data.datasource.SetupSuccessLocalDatasource
import com.gestorplus.appgestor.owner.setup_success.domain.repository.SetupSuccessRepository

class SetupSuccessRepositoryImpl(
    private val localDatasource: SetupSuccessLocalDatasource
) : SetupSuccessRepository {
    override suspend fun getRegisteredClinicName(): Result<String> {
        return try {
            val name = localDatasource.getClinicName()
            Result.success(name)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
