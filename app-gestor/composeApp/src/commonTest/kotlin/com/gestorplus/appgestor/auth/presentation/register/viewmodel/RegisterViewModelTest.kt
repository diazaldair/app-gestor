package com.gestorplus.appgestor.auth.presentation.register.viewmodel

import com.gestorplus.appgestor.auth.domain.usecase.RegisterDoctorUseCase
import com.gestorplus.appgestor.auth.presentation.register.state.RegisterEvent
import com.gestorplus.appgestor.auth.presentation.register.state.RegisterEfffect
import com.gestorplus.appgestor.auth.domain.repository.AuthRepository
import com.gestorplus.appgestor.auth.domain.usecase.RegisterDoctorUseCaseTest
import com.gestorplus.appgestor.auth.domain.usecase.FakeAuthRepositoryForRegister
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.flow.first
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

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
        
        val state = viewModel.state.value
        assertEquals("Dr. House", state.fullName)
        assertNull(state.errorMessage)
    }

    @Test
    fun `when toggle password visibility, isPasswordVisible changes`() = runTest {
        val initialState = viewModel.state.value.isPasswordVisible
        viewModel.onEvent(RegisterEvent.TogglePasswordVisibility)
        
        assertEquals(!initialState, viewModel.state.value.isPasswordVisible)
    }

    @Test
    fun `submit register with invalid email sets error message`() = runTest {
        viewModel.onEvent(RegisterEvent.FullNameChanged("Valid Name"))
        viewModel.onEvent(RegisterEvent.EmailChanged("invalid-email"))
        viewModel.onEvent(RegisterEvent.PasswordChanged("Pass12345"))
        viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged("Pass12345"))
        
        viewModel.onEvent(RegisterEvent.OnSubmitClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Formato de correo inválido.", viewModel.state.value.errorMessage)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `successful registration emits NavigateToHome effect`() = runTest {
        // Arrange: Datos válidos (según Regex de ViewModel)
        viewModel.onEvent(RegisterEvent.FullNameChanged("Dr John Smith"))
        viewModel.onEvent(RegisterEvent.EmailChanged("john@smith.com"))
        viewModel.onEvent(RegisterEvent.PasswordChanged("Password123"))
        viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged("Password123"))

        // Act
        viewModel.onEvent(RegisterEvent.OnSubmitClicked)
        
        // Assert: Esperar a que las corrutinas terminen
        testDispatcher.scheduler.advanceUntilIdle()
        
        val effect = viewModel.effect.first()
        assertEquals(RegisterEfffect.NavigateToHome, effect)
    }
}