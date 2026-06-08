package com.gestorplus.appgestor.owner.setup_profile.domain.usecase

import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Fake repository that records calls and returns configurable results.
 */
class FakeSetupProfileRepository : SetupProfileRepository {
    var saveResult: Result<Unit> = Result.success(Unit)
    var lastSavedProfile: WorkspaceProfile? = null

    override suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit> {
        lastSavedProfile = profile
        return saveResult
    }
}

class SaveWorkspaceProfileUseCaseTest {

    private val fakeRepo = FakeSetupProfileRepository()
    private val useCase = SaveWorkspaceProfileUseCase(fakeRepo)

    private fun validProfile() = WorkspaceProfile(
        clinicName = "Clínica Salud Plus",
        fullName = "Dr. Juan Pérez",
        specialities = listOf("Cardiología", "Medicina Interna"),
        biography = "15 años de experiencia en cardiología clínica.",
        exactAddress = "Av. Arce #123, La Paz",
        references = "Edf. Multicentro, Piso 4",
        galleryImages = listOf("content://image1.jpg", "content://image2.jpg")
    )

    // --- Happy path ---

    @Test
    fun `save valid profile delegates to repository and returns success`() = runTest {
        val profile = validProfile()
        val result = useCase(profile)

        assertTrue(result.isSuccess)
        assertEquals(profile, fakeRepo.lastSavedProfile)
    }

    @Test
    fun `save profile with empty specialities still succeeds`() = runTest {
        val profile = validProfile().copy(specialities = emptyList())
        val result = useCase(profile)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `save profile with no gallery images still succeeds`() = runTest {
        val profile = validProfile().copy(galleryImages = emptyList())
        val result = useCase(profile)

        assertTrue(result.isSuccess)
    }

    // --- Validation: required fields ---

    @Test
    fun `save profile with blank clinicName returns failure`() = runTest {
        val profile = validProfile().copy(clinicName = "")
        val result = useCase(profile)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals(
            "Nombre de la clínica y del profesional son obligatorios.",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun `save profile with blank fullName returns failure`() = runTest {
        val profile = validProfile().copy(fullName = "")
        val result = useCase(profile)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `save profile with both required fields blank returns failure`() = runTest {
        val profile = validProfile().copy(clinicName = "", fullName = "")
        val result = useCase(profile)

        assertTrue(result.isFailure)
    }

    @Test
    fun `save profile with whitespace-only clinicName returns failure`() = runTest {
        val profile = validProfile().copy(clinicName = "   ")
        val result = useCase(profile)

        assertTrue(result.isFailure)
    }

    // --- Repository error propagation ---

    @Test
    fun `save profile propagates repository error`() = runTest {
        fakeRepo.saveResult = Result.failure(Exception("Error de red al guardar perfil"))
        val profile = validProfile()
        val result = useCase(profile)

        assertTrue(result.isFailure)
        assertEquals("Error de red al guardar perfil", result.exceptionOrNull()?.message)
    }

    // --- Verify no repo call on validation failure ---

    @Test
    fun `validation failure does not call repository`() = runTest {
        val profile = validProfile().copy(clinicName = "")
        useCase(profile)

        // Repository should NOT have been called
        assertEquals(null, fakeRepo.lastSavedProfile)
    }
}
