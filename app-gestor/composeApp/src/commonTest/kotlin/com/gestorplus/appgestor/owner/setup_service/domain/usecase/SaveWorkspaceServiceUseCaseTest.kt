package com.gestorplus.appgestor.owner.setup_service.domain.usecase

import com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService
import com.gestorplus.appgestor.owner.setup_service.domain.repository.SetupServiceRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeSetupServiceRepository : SetupServiceRepository {
    var lastSavedService: WorkspaceService? = null
    var shouldReturnError = false

    override suspend fun saveWorkspaceService(service: WorkspaceService): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(Exception("Error saving service"))
        } else {
            lastSavedService = service
            Result.success(Unit)
        }
    }
}

class SaveWorkspaceServiceUseCaseTest {

    private val fakeRepository = FakeSetupServiceRepository()
    private val saveWorkspaceServiceUseCase = SaveWorkspaceServiceUseCase(fakeRepository)

    @Test
    fun `invoke with blank name returns failure`() = runTest {
        val service = WorkspaceService(name = "", durationMinutes = 30)
        val result = saveWorkspaceServiceUseCase(service)
        
        assertTrue(result.isFailure)
        assertEquals("El nombre del servicio es obligatorio.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with zero duration returns failure`() = runTest {
        val service = WorkspaceService(name = "Checkup", durationMinutes = 0)
        val result = saveWorkspaceServiceUseCase(service)
        
        assertTrue(result.isFailure)
        assertEquals("La duración debe ser mayor a 0.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with valid data saves to repository`() = runTest {
        val service = WorkspaceService(name = "Dental Clean", durationMinutes = 45)
        val result = saveWorkspaceServiceUseCase(service)
        
        assertTrue(result.isSuccess)
        assertEquals("Dental Clean", fakeRepository.lastSavedService?.name)
    }
}
