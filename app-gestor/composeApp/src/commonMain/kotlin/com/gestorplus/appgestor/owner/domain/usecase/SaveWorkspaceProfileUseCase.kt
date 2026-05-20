package com.gestorplus.appgestor.owner.domain.usecase

import com.gestorplus.appgestor.owner.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.domain.repository.OwnerRepository

class SaveWorkspaceProfileUseCase(private val repository: OwnerRepository) {
    suspend operator fun invoke(profile: WorkspaceProfile): Result<Unit> {
        if (profile.clinicName.isBlank() || profile.fullName.isBlank()) {
            return Result.failure(IllegalArgumentException("Nombre de la clínica y del profesional son obligatorios."))
        }
        return repository.saveWorkspaceProfile(profile)
    }
}
