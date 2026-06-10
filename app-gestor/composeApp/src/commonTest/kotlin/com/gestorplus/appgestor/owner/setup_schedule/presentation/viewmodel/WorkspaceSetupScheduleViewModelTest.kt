package com.gestorplus.appgestor.owner.setup_schedule.presentation.viewmodel

import com.gestorplus.appgestor.owner.setup_schedule.presentation.state.WorkspaceSetupScheduleEvent
import com.gestorplus.appgestor.owner.setup_schedule.presentation.state.WorkspaceSetupScheduleEfffect
import com.gestorplus.appgestor.owner.setup_schedule.domain.usecase.SaveWorkspaceScheduleUseCase
import com.gestorplus.appgestor.owner.setup_schedule.domain.usecase.FakeSetupScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class WorkspaceSetupScheduleViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WorkspaceSetupScheduleViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val fakeRepo = FakeSetupScheduleRepository()
        val useCase = SaveWorkspaceScheduleUseCase(fakeRepo)
        viewModel = WorkspaceSetupScheduleViewModel(useCase)
    }

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `toggling a day updates the selectedDays list`() = runTest {
        // Act: Seleccionar Lunes
        viewModel.onEvent(WorkspaceSetupScheduleEvent.DayToggled("L"))
        assertTrue(viewModel.state.value.selectedDays.contains("L"))

        // Act: Deseleccionar Lunes
        viewModel.onEvent(WorkspaceSetupScheduleEvent.DayToggled("L"))
        assertFalse(viewModel.state.value.selectedDays.contains("L"))
    }

    @Test
    fun `when no days are selected, continue shows error message`() = runTest {
        viewModel.onEvent(WorkspaceSetupScheduleEvent.OnContinueClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Debes seleccionar al menos un día laboral.", viewModel.state.value.errorMessage)
    }

    @Test
    fun `successful schedule save emits NavigateToServices effect`() = runTest {
        // Arrange: Seleccionar un día y dejar horas por defecto
        viewModel.onEvent(WorkspaceSetupScheduleEvent.DayToggled("L"))
        viewModel.onEvent(WorkspaceSetupScheduleEvent.DayToggled("M"))

        // Act
        viewModel.onEvent(WorkspaceSetupScheduleEvent.OnContinueClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val effect = viewModel.effect.first()
        assertEquals(WorkspaceSetupScheduleEfffect.NavigateToServices, effect)
        assertFalse(viewModel.state.value.isLoading)
    }
}