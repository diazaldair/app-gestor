package com.gestorplus.appgestor.owner.setup_schedule.domain.usecase

import com.gestorplus.appgestor.owner.setup_schedule.domain.model.Shift
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.WorkspaceSchedule
import com.gestorplus.appgestor.owner.setup_schedule.domain.repository.SetupScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeSetupScheduleRepository : SetupScheduleRepository {
    var saveResult: Result<Unit> = Result.success(Unit)
    var lastSavedSchedule: WorkspaceSchedule? = null

    override fun getShifts(): Flow<List<Shift>> = error("Not implemented")

    override suspend fun saveWorkspaceSchedule(schedule: WorkspaceSchedule): Result<Unit> {
        lastSavedSchedule = schedule
        return saveResult
    }

    override suspend fun saveDetailedShifts(shifts: List<Shift>): Result<Unit> = Result.success(Unit)

    override suspend fun deleteShift(shiftId: String) {}
}

class SaveWorkspaceScheduleUseCaseTest {

    private val fakeRepo = FakeSetupScheduleRepository()
    private val useCase = SaveWorkspaceScheduleUseCase(fakeRepo)

    private fun validSchedule() = WorkspaceSchedule(
        workingDays = listOf("L", "M", "X", "J", "V"),
        morningStart = "08:00",
        morningEnd = "13:00",
        afternoonStart = "15:00",
        afternoonEnd = "20:00"
    )

    @Test
    fun `save valid schedule returns success`() = runTest {
        val schedule = validSchedule()
        val result = useCase(schedule)

        assertTrue(result.isSuccess)
        assertEquals(schedule, fakeRepo.lastSavedSchedule)
    }

    @Test
    fun `save schedule with only morning hours succeeds`() = runTest {
        val schedule = validSchedule().copy(afternoonStart = "", afternoonEnd = "")
        val result = useCase(schedule)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `save schedule with no working days returns failure`() = runTest {
        val schedule = validSchedule().copy(workingDays = emptyList())
        val result = useCase(schedule)

        assertTrue(result.isFailure)
        assertEquals("Debes seleccionar al menos un día laboral.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `save schedule with blank morningStart returns failure`() = runTest {
        val schedule = validSchedule().copy(morningStart = "")
        val result = useCase(schedule)

        assertTrue(result.isFailure)
        assertEquals("Debes definir al menos el horario de mañana.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `save schedule with blank morningEnd returns failure`() = runTest {
        val schedule = validSchedule().copy(morningEnd = "   ")
        val result = useCase(schedule)

        assertTrue(result.isFailure)
        assertEquals("Debes definir al menos el horario de mañana.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `save propagates repository error`() = runTest {
        fakeRepo.saveResult = Result.failure(Exception("Database error"))
        val result = useCase(validSchedule())

        assertTrue(result.isFailure)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `validation failure does not call repository`() = runTest {
        useCase(validSchedule().copy(workingDays = emptyList()))
        assertEquals(null, fakeRepo.lastSavedSchedule)
    }
}
