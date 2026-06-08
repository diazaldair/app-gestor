package com.gestorplus.appgestor.profile.presentation.viewmodel

import com.gestorplus.appgestor.profile.domain.model.UserProfile
import com.gestorplus.appgestor.profile.domain.usecase.GetUserProfileUseCase
import com.gestorplus.appgestor.profile.domain.usecase.UpdateUserProfileUseCase
import com.gestorplus.appgestor.profile.domain.usecase.FakeProfileRepository
import com.gestorplus.appgestor.profile.presentation.state.ProfileEvent
import com.gestorplus.appgestor.profile.presentation.state.ProfileEfffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ProfileViewModel
    private lateinit var fakeRepo: FakeProfileRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeProfileRepository()
        val getUserProfileUseCase = GetUserProfileUseCase(fakeRepo)
        val updateUserProfileUseCase = UpdateUserProfileUseCase(fakeRepo)
        viewModel = ProfileViewModel(getUserProfileUseCase, updateUserProfileUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load should populate state with profile from repository`() = runTest {
        val profile = UserProfile("Juan", "juan@test.com", "123", "Desc", "img")
        fakeRepo.profile = profile
        
        // Re-init to trigger loadUserProfile with set profile or just wait for emission
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Juan", viewModel.state.value.name)
        assertEquals("juan@test.com", viewModel.state.value.email)
    }

    @Test
    fun `initial load with null profile should use fallback defaults`() = runTest {
        fakeRepo.profile = null
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Dr. Alejandro Díaz", viewModel.state.value.name)
    }

    @Test
    fun `toggleEditMode should update isEditing state`() {
        assertFalse(viewModel.state.value.isEditing)
        viewModel.onEvent(ProfileEvent.ToggleEditMode)
        assertTrue(viewModel.state.value.isEditing)
    }

    @Test
    fun `on name changed event should update state name`() {
        viewModel.onEvent(ProfileEvent.NameChanged("New Name"))
        assertEquals("New Name", viewModel.state.value.name)
    }

    @Test
    fun `saveProfile success should emit success effect and disable editing`() = runTest {
        viewModel.onEvent(ProfileEvent.NameChanged("Updated Name"))
        viewModel.onEvent(ProfileEvent.ToggleEditMode) // Enter edit mode
        
        viewModel.onEvent(ProfileEvent.SaveProfile)
        
        assertTrue(viewModel.state.value.isLoading)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertFalse(viewModel.state.value.isLoading)
        assertFalse(viewModel.state.value.isEditing)
        
        val effect = viewModel.effect.first()
        assertTrue(effect is ProfileEfffect.ShowSnackbar)
        assertTrue((effect as ProfileEfffect.ShowSnackbar).message.contains("éxito"))
    }

    @Test
    fun `saveProfile failure should emit error effect`() = runTest {
        fakeRepo.saveResult = Result.failure(Exception("Error saving"))
        
        viewModel.onEvent(ProfileEvent.SaveProfile)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val effect = viewModel.effect.first()
        assertTrue(effect is ProfileEfffect.ShowSnackbar)
        assertTrue((effect as ProfileEfffect.ShowSnackbar).message.contains("Error"))
    }
}
