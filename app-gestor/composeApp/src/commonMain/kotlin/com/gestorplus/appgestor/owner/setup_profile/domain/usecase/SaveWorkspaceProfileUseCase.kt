package com.gestorplus.appgestor.owner.setup_profile.domain.usecase

import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository

class SaveWorkspaceProfileUseCase(private val repository: SetupProfileRepository) {
    suspend operator fun invoke(profile: WorkspaceProfile): Result<Unit> {
        if (profile.clinicName.isBlank() || profile.fullName.isBlank()) {
            return Result.failure(IllegalArgumentException("Nombre de la clínica y del profesional son obligatorios."))
        }
        return repository.saveWorkspaceProfile(profile)
    }
}
