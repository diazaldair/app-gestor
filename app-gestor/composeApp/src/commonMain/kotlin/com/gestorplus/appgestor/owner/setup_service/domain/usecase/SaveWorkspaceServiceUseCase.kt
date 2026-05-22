package com.gestorplus.appgestor.owner.setup_service.domain.usecase

import com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService
import com.gestorplus.appgestor.owner.setup_service.domain.repository.SetupServiceRepository

class SaveWorkspaceServiceUseCase(private val repository: SetupServiceRepository) {
    suspend operator fun invoke(service: WorkspaceService): Result<Unit> {
        if (service.name.isBlank()) {
            return Result.failure(Exception("El nombre del servicio es obligatorio."))
        }
        if (service.durationMinutes <= 0) {
            return Result.failure(Exception("La duración debe ser mayor a 0."))
        }
        return repository.saveWorkspaceService(service)
    }
}
