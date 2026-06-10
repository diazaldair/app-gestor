package com.gestorplus.appgestor.auth.presentation.register.viewmodel

import com.gestorplus.appgestor.auth.domain.usecase.RegisterDoctorUseCase
import com.gestorplus.appgestor.auth.presentation.register.state.RegisterEvent
import com.gestorplus.appgestor.auth.presentation.register.state.RegisterEfffect
import com.gestorplus.appgestor.auth.domain.usecase.FakeAuthRepositoryForRegister
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlinx.coroutines.launch
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: RegisterViewModel
    private lateinit var fakeRepo: FakeAuthRepositoryForRegister

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeAuthRepositoryForRegister()
        val useCase = RegisterDoctorUseCase(fakeRepo)
        viewModel = RegisterViewModel(useCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when name changes, state is updated and error is cleared`() = runTest {
        viewModel.onEvent(RegisterEvent.FullNameChanged("Dr. House"))
        advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals("Dr. House", state.fullName)
        assertNull(state.errorMessage)
    }

    @Test
    fun `when toggle password visibility, isPasswordVisible changes`() = runTest {
        val initialState = viewModel.state.value.isPasswordVisible
        viewModel.onEvent(RegisterEvent.TogglePasswordVisibility)
        advanceUntilIdle()
        
        assertEquals(!initialState, viewModel.state.value.isPasswordVisible)
    }

    @Test
    fun `submit register with invalid email sets error message`() = runTest {
        viewModel.onEvent(RegisterEvent.FullNameChanged("Valid Name"))
        viewModel.onEvent(RegisterEvent.EmailChanged("invalid-email"))
        viewModel.onEvent(RegisterEvent.PasswordChanged("Pass12345"))
        viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged("Pass12345"))
        advanceUntilIdle()
        
        viewModel.onEvent(RegisterEvent.OnSubmitClicked)
        advanceUntilIdle()

        assertEquals("Por favor, ingresa un correo electrónico válido.", viewModel.state.value.errorMessage)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `successful registration emits NavigateToHome effect`() = runTest {
        viewModel.onEvent(RegisterEvent.FullNameChanged("Dr John Smith"))
        viewModel.onEvent(RegisterEvent.EmailChanged("john@smith.com"))
        viewModel.onEvent(RegisterEvent.PasswordChanged("Password123"))
        viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged("Password123"))
        advanceUntilIdle()

        val effects = mutableListOf<RegisterEfffect>()
        val job = launch { 
            viewModel.effect.collect { effects.add(it) } 
        }

        viewModel.onEvent(RegisterEvent.OnSubmitClicked)
        advanceUntilIdle()
        
        assertTrue(effects.contains(RegisterEfffect.NavigateToHome))
        job.cancel()
    }
}
