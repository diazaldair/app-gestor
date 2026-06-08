package com.gestorplus.appgestor.owner.setup_service.domain.usecase

import com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService
import com.gestorplus.appgestor.owner.setup_service.domain.repository.SetupServiceRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Fake repository for testing the SaveWorkspaceServiceUseCase in isolation.
 */
class FakeSetupServiceRepository : SetupServiceRepository {
    var saveResult: Result<Unit> = Result.success(Unit)
    var lastSavedService: WorkspaceService? = null

    override suspend fun saveWorkspaceService(service: WorkspaceService): Result<Unit> {
        lastSavedService = service
        return saveResult
    }
}

class SaveWorkspaceServiceUseCaseTest {

    private val fakeRepo = FakeSetupServiceRepository()
    private val useCase = SaveWorkspaceServiceUseCase(fakeRepo)

    private fun validService() = WorkspaceService(
        name = "Consulta General",
        description = "Revisión médica completa con diagnóstico.",
        price = 150.0,
        currency = "USD",
        durationMinutes = 30
    )

    // --- Happy path ---

    @Test
    fun `save valid service returns success`() = runTest {
        val service = validService()
        val result = useCase(service)

        assertTrue(result.isSuccess)
        assertEquals(service, fakeRepo.lastSavedService)
    }

    @Test
    fun `save service with long duration succeeds`() = runTest {
        val service = validService().copy(durationMinutes = 120)
        val result = useCase(service)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `save service with zero price succeeds`() = runTest {
        val service = validService().copy(price = 0.0)
        val result = useCase(service)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `save service with empty description succeeds`() = runTest {
        val service = validService().copy(description = "")
        val result = useCase(service)

        assertTrue(result.isSuccess)
    }

    // --- Validation: name required ---

    @Test
    fun `save service with blank name returns failure`() = runTest {
        val service = validService().copy(name = "")
        val result = useCase(service)

        assertTrue(result.isFailure)
        assertEquals("El nombre del servicio es obligatorio.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `save service with whitespace-only name returns failure`() = runTest {
        val service = validService().copy(name = "   ")
        val result = useCase(service)

        assertTrue(result.isFailure)
    }

    // --- Validation: duration > 0 ---

    @Test
    fun `save service with zero duration returns failure`() = runTest {
        val service = validService().copy(durationMinutes = 0)
        val result = useCase(service)

        assertTrue(result.isFailure)
        assertEquals("La duración debe ser mayor a 0.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `save service with negative duration returns failure`() = runTest {
        val service = validService().copy(durationMinutes = -15)
        val result = useCase(service)

        assertTrue(result.isFailure)
        assertEquals("La duración debe ser mayor a 0.", result.exceptionOrNull()?.message)
    }

    // --- Validation order ---

    @Test
    fun `blank name takes priority over invalid duration`() = runTest {
        val service = validService().copy(name = "", durationMinutes = 0)
        val result = useCase(service)

        assertTrue(result.isFailure)
        assertEquals("El nombre del servicio es obligatorio.", result.exceptionOrNull()?.message)
    }

    // --- Repository error propagation ---

    @Test
    fun `save propagates repository error`() = runTest {
        fakeRepo.saveResult = Result.failure(Exception("Firebase timeout"))
        val result = useCase(validService())

        assertTrue(result.isFailure)
        assertEquals("Firebase timeout", result.exceptionOrNull()?.message)
    }

    // --- Verify no repo call on validation failure ---

    @Test
    fun `validation failure does not call repository`() = runTest {
        useCase(validService().copy(name = ""))
        assertEquals(null, fakeRepo.lastSavedService)
    }
}
