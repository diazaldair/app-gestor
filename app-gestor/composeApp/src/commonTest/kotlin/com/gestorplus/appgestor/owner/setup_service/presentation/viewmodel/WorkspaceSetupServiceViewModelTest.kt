package com.gestorplus.appgestor.owner.setup_service.presentation.viewmodel

import com.gestorplus.appgestor.owner.setup_service.domain.usecase.SaveWorkspaceServiceUseCase
import com.gestorplus.appgestor.owner.setup_service.domain.usecase.FakeSetupServiceRepository
import com.gestorplus.appgestor.owner.setup_service.presentation.state.WorkspaceSetupServiceEvent
import com.gestorplus.appgestor.owner.setup_service.presentation.state.WorkspaceSetupServiceEfffect
import com.gestorplus.appgestor.owner.setup_service.presentation.state.DurationOption
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
        viewModel.onEvent(WorkspaceSetupServiceEvent.ServiceNameChanged("Limpieza Dental"))
        assertEquals("Limpieza Dental", viewModel.state.value.serviceName)
    }

    @Test
    fun `when duration is selected, state reflects option correctly`() = runTest {
        viewModel.onEvent(WorkspaceSetupServiceEvent.DurationOptionSelected(DurationOption.MIN_45))
        assertEquals(DurationOption.MIN_45, viewModel.state.value.selectedDurationOption)
    }

    @Test
    fun `successful service creation emits NavigateToNextStep effect`() = runTest {
        viewModel.onEvent(WorkspaceSetupServiceEvent.ServiceNameChanged("Consulta"))
        viewModel.onEvent(WorkspaceSetupServiceEvent.PriceChanged("50.0"))
        viewModel.onEvent(WorkspaceSetupServiceEvent.DurationOptionSelected(DurationOption.MIN_30))

        viewModel.onEvent(WorkspaceSetupServiceEvent.OnContinueClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(WorkspaceSetupServiceEfffect.NavigateToNextStep, viewModel.effect.first())
    }

    @Test
    fun `save fails with empty name and shows error`() = runTest {
        viewModel.onEvent(WorkspaceSetupServiceEvent.ServiceNameChanged(""))
        viewModel.onEvent(WorkspaceSetupServiceEvent.OnContinueClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.state.value.errorMessage)
    }
}
