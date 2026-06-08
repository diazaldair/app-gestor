package com.gestorplus.appgestor.owner.setup_profile.presentation.viewmodel

import com.gestorplus.appgestor.owner.setup_profile.domain.usecase.SaveWorkspaceProfileUseCase
import com.gestorplus.appgestor.owner.setup_profile.presentation.state.WorkspaceSetupProfileEfffect
import com.gestorplus.appgestor.owner.setup_profile.presentation.state.WorkspaceSetupProfileEvent
import com.gestorplus.appgestor.owner.setup_profile.domain.usecase.FakeSetupProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WorkspaceSetupProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WorkspaceSetupProfileViewModel
    private lateinit var fakeRepo: FakeSetupProfileRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeSetupProfileRepository()
        val useCase = SaveWorkspaceProfileUseCase(fakeRepo)
        viewModel = WorkspaceSetupProfileViewModel(useCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when photo is selected, it is added to the state gallery`() = runTest {
        val dummyUri = "content://media/photo.jpg"
        viewModel.onEvent(WorkspaceSetupProfileEvent.PhotoSelected(dummyUri))
        
        assertTrue(viewModel.state.value.galleryImages.contains(dummyUri))
    }

    @Test
    fun `when speciality is added, input is cleared and list updated`() = runTest {
        viewModel.onEvent(WorkspaceSetupProfileEvent.InputSpecialityChanged("Odontología"))
        viewModel.onEvent(WorkspaceSetupProfileEvent.AddSpecialityClicked)
        
        assertEquals("", viewModel.state.value.inputSpeciality)
        assertTrue(viewModel.state.value.specialities.contains("Odontología"))
    }

    @Test
    fun `fix location updates state and emits snackbar effect`() = runTest {
        viewModel.onEvent(WorkspaceSetupProfileEvent.FixLocationClicked)
        
        assertTrue(viewModel.state.value.isLocationFixed)
        assertEquals("Av. Arce #123, La Paz", viewModel.state.value.exactAddress)
        
        val effect = viewModel.effect.first()
        assertTrue(effect is WorkspaceSetupProfileEfffect.ShowSnackbar)
    }

    @Test
    fun `successful profile save emits NavigateToServices effect`() = runTest {
        viewModel.onEvent(WorkspaceSetupProfileEvent.ClinicNameChanged("Clinica Central"))
        viewModel.onEvent(WorkspaceSetupProfileEvent.FullNameChanged("Dr. House"))

        viewModel.onEvent(WorkspaceSetupProfileEvent.OnContinueClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        val effect = viewModel.effect.first()
        assertEquals(WorkspaceSetupProfileEfffect.NavigateToServices, effect)
    }

    @Test
    fun `failed save sets error message in state`() = runTest {
        viewModel.onEvent(WorkspaceSetupProfileEvent.OnContinueClicked) // Sin datos
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Nombre de la clínica y del profesional son obligatorios.", viewModel.state.value.errorMessage)
    }
}