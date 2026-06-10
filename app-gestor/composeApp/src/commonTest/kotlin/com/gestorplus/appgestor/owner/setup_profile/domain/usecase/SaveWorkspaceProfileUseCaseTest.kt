package com.gestorplus.appgestor.owner.setup_profile.domain.usecase

import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeSetupProfileRepository : SetupProfileRepository {
    var lastSavedProfile: WorkspaceProfile? = null
    var shouldReturnError = false

    override suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(Exception("Save failed"))
        } else {
            lastSavedProfile = profile
            Result.success(Unit)
        }
    }
}

class SaveWorkspaceProfileUseCaseTest {

    private val fakeRepository = FakeSetupProfileRepository()
    private val saveWorkspaceProfileUseCase = SaveWorkspaceProfileUseCase(fakeRepository)

    @Test
    fun `invoke with empty clinic name returns failure`() = runTest {
        val profile = WorkspaceProfile(clinicName = "", fullName = "Dr. Test")
        val result = saveWorkspaceProfileUseCase(profile)
        
        assertTrue(result.isFailure)
        assertEquals("Nombre de la clínica y del profesional son obligatorios.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with valid data saves to repository`() = runTest {
        val profile = WorkspaceProfile(clinicName = "Clinica X", fullName = "Dr. Test")
        val result = saveWorkspaceProfileUseCase(profile)
        
        assertTrue(result.isSuccess)
        assertEquals("Clinica X", fakeRepository.lastSavedProfile?.clinicName)
    }
}
