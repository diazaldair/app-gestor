package com.gestorplus.appgestor.owner.setup_service.domain.repository

import com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService

interface SetupServiceRepository {
    suspend fun saveWorkspaceService(service: WorkspaceService): Result<Unit>
}
