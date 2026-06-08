package com.gestorplus.appgestor.owner.setup_service.presentation.viewmodel

import com.gestorplus.appgestor.owner.setup_service.domain.usecase.SaveWorkspaceServiceUseCase
import com.gestorplus.appgestor.owner.setup_service.domain.usecase.FakeSetupServiceRepository
import com.gestorplus.appgestor.owner.setup_service.presentation.state.WorkspaceSetupServiceEvent
import com.gestorplus.appgestor.owner.setup_service.presentation.state.WorkspaceSetupServiceEfffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class WorkspaceSetupServiceViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WorkspaceSetupServiceViewModel
    private lateinit var fakeRepo: FakeSetupServiceRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeSetupServiceRepository()
        val useCase = SaveWorkspaceServiceUseCase(fakeRepo)
        viewModel = WorkspaceSetupServiceViewModel(useCase)
    }

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `when name changes, state is updated`() = runTest {
        viewModel.onEvent(WorkspaceSetupServiceEvent.NameChanged("Limpieza Dental"))
        assertEquals("Limpieza Dental", viewModel.state.value.name)
    }

    @Test
    fun `when duration is selected, state reflects minutes correctly`() = runTest {
        // Supongamos que el evento DurationSelected(30) es para 30 min
        viewModel.onEvent(WorkspaceSetupServiceEvent.DurationSelected(45))
        assertEquals(45, viewModel.state.value.durationMinutes)
    }

    @Test
    fun `successful service creation emits NavigateToSuccess effect`() = runTest {
        viewModel.onEvent(WorkspaceSetupServiceEvent.NameChanged("Consulta"))
        viewModel.onEvent(WorkspaceSetupServiceEvent.PriceChanged("50.0"))
        viewModel.onEvent(WorkspaceSetupServiceEvent.DurationSelected(30))

        viewModel.onEvent(WorkspaceSetupServiceEvent.OnContinueClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(WorkspaceSetupServiceEfffect.NavigateToSuccess, viewModel.effect.first())
    }

    @Test
    fun `save fails with zero duration and shows error`() = runTest {
        viewModel.onEvent(WorkspaceSetupServiceEvent.NameChanged("Test"))
        viewModel.onEvent(WorkspaceSetupServiceEvent.DurationSelected(0))
        viewModel.onEvent(WorkspaceSetupServiceEvent.OnContinueClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.state.value.errorMessage)
    }
}