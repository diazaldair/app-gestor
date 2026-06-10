package com.gestorplus.appgestor.auth.presentation.login.viewmodel

import com.gestorplus.appgestor.auth.domain.usecase.LoginWithEmailUseCase
import com.gestorplus.appgestor.auth.domain.usecase.FakeAuthRepositoryForLogin
import com.gestorplus.appgestor.auth.presentation.login.state.LoginEvent
import com.gestorplus.appgestor.auth.presentation.login.state.LoginEfffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeRepo: FakeAuthRepositoryForLogin

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeAuthRepositoryForLogin()
        val useCase = LoginWithEmailUseCase(fakeRepo)
        viewModel = LoginViewModel(useCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        val state = viewModel.state.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `when email changes, state is updated`() = runTest {
        viewModel.onEvent(LoginEvent.EmailChanged("test@test.com"))
        advanceUntilIdle()
        assertEquals("test@test.com", viewModel.state.value.email)
    }

    @Test
    fun `when password changes, state is updated`() = runTest {
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
        advanceUntilIdle()
        assertEquals("password123", viewModel.state.value.password)
    }

    @Test
    fun `state transition - loading to success`() = runTest {
        viewModel.onEvent(LoginEvent.EmailChanged("doctor@test.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
        advanceUntilIdle()

        viewModel.onEvent(LoginEvent.OnSubmitClicked)
        
        // El estado isLoading cambia a true en el ViewModel
        // Necesitamos ejecutar el primer fragmento de la corrutina
        runCurrent()
        assertTrue(viewModel.state.value.isLoading)
        
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
        val effect = viewModel.effect.first()
        assertEquals(LoginEfffect.NavigateToHome, effect)
    }

    @Test
    fun `state transition - loading to error`() = runTest {
        fakeRepo.loginResult = Result.failure(Exception("Network Error"))
        viewModel.onEvent(LoginEvent.EmailChanged("doctor@test.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
        advanceUntilIdle()

        viewModel.onEvent(LoginEvent.OnSubmitClicked)
        runCurrent()
        assertTrue(viewModel.state.value.isLoading)
        
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
        assertEquals("Network Error", viewModel.state.value.errorMessage)
    }
}
