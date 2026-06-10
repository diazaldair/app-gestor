package com.gestorplus.appgestor.owner.setup_schedule.domain.usecase

import com.gestorplus.appgestor.owner.setup_schedule.domain.model.WorkspaceSchedule
import com.gestorplus.appgestor.owner.setup_schedule.domain.repository.SetupScheduleRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeSetupScheduleRepository : SetupScheduleRepository {
    var lastSavedSchedule: WorkspaceSchedule? = null
    var shouldReturnError = false

    override suspend fun saveWorkspaceSchedule(schedule: WorkspaceSchedule): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(Exception("Database error"))
        } else {
            lastSavedSchedule = schedule
            Result.success(Unit)
        }
    }
}

class SaveWorkspaceScheduleUseCaseTest {

    private val fakeRepository = FakeSetupScheduleRepository()
    private val saveWorkspaceScheduleUseCase = SaveWorkspaceScheduleUseCase(fakeRepository)

    @Test
    fun `invoke with no working days returns failure`() = runTest {
        val schedule = WorkspaceSchedule(
            workingDays = emptyList(),
            morningStart = "08:00",
            morningEnd = "12:00",
            afternoonStart = "",
            afternoonEnd = ""
        )
        
        val result = saveWorkspaceScheduleUseCase(schedule)
        
        assertTrue(result.isFailure)
        assertEquals("Debes seleccionar al menos un día laboral.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with blank morning hours returns failure`() = runTest {
        val schedule = WorkspaceSchedule(
            workingDays = listOf("L"),
            morningStart = "",
            morningEnd = "",
            afternoonStart = "",
            afternoonEnd = ""
        )
        
        val result = saveWorkspaceScheduleUseCase(schedule)
        
        assertTrue(result.isFailure)
        assertEquals("Debes definir al menos el horario de mañana.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with valid data saves to repository`() = runTest {
        val schedule = WorkspaceSchedule(
            workingDays = listOf("L", "M"),
            morningStart = "09:00",
            morningEnd = "13:00",
            afternoonStart = "14:00",
            afternoonEnd = "18:00"
        )
        
        val result = saveWorkspaceScheduleUseCase(schedule)
        
        assertTrue(result.isSuccess)
        assertEquals(2, fakeRepository.lastSavedSchedule?.workingDays?.size)
        assertEquals("09:00", fakeRepository.lastSavedSchedule?.morningStart)
    }
}
