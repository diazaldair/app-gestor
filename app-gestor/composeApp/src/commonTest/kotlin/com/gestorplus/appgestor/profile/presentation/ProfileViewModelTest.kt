package com.gestorplus.appgestor.profile.presentation

import com.gestorplus.appgestor.profile.domain.model.UserProfile
import com.gestorplus.appgestor.profile.domain.usecase.GetUserProfileUseCase
import com.gestorplus.appgestor.profile.domain.usecase.UpdateUserProfileUseCase
import com.gestorplus.appgestor.profile.presentation.state.ProfileEfffect
import com.gestorplus.appgestor.profile.presentation.state.ProfileEvent
import com.gestorplus.appgestor.profile.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ProfileViewModel
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase
    private lateinit var updateUserProfileUseCase: UpdateUserProfileUseCase
    private lateinit var fakeRepository: FakeProfileRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeProfileRepository()
        getUserProfileUseCase = GetUserProfileUseCase(fakeRepository)
        updateUserProfileUseCase = UpdateUserProfileUseCase(fakeRepository)
        viewModel = ProfileViewModel(getUserProfileUseCase, updateUserProfileUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load updates state with profile`() = runTest {
        val profile = UserProfile("Name", "email@test.com", "123", "Desc", "url")
        fakeRepository.emit(profile)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Name", viewModel.state.value.name)
        assertEquals("email@test.com", viewModel.state.value.email)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `onEvent NameChanged updates state`() = runTest {
        viewModel.onEvent(ProfileEvent.NameChanged("New Name"))
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("New Name", viewModel.state.value.name)
    }

    @Test
    fun `onEvent ToggleEditMode updates state`() = runTest {
        assertFalse(viewModel.state.value.isEditing)
        viewModel.onEvent(ProfileEvent.ToggleEditMode)
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.state.value.isEditing)
    }

    @Test
    fun `onEvent SaveProfile calls use case and emits effect`() = runTest {
        viewModel.onEvent(ProfileEvent.NameChanged("Saved Name"))
        viewModel.onEvent(ProfileEvent.SaveProfile)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Saved Name", fakeRepository.savedProfile?.name)
        val effect = viewModel.effect.first()
        assertTrue(effect is ProfileEfffect.ShowSnackbar)
    }

    class FakeProfileRepository : com.gestorplus.appgestor.profile.domain.repository.ProfileRepository {
        private val _flow = MutableStateFlow<UserProfile?>(null)
        var savedProfile: UserProfile? = null

        fun emit(profile: UserProfile) { _flow.value = profile }
        override fun getUserProfile(): Flow<UserProfile?> = _flow
        override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
            savedProfile = profile
            return Result.success(Unit)
        }
    }
}
