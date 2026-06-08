package com.gestorplus.appgestor.auth.presentation.login.viewmodel

import com.gestorplus.appgestor.auth.domain.usecase.LoginWithEmailUseCase
import com.gestorplus.appgestor.auth.domain.usecase.FakeAuthRepositoryForLogin
import com.gestorplus.appgestor.auth.presentation.login.state.LoginEvent
import com.gestorplus.appgestor.auth.presentation.login.state.LoginEfffect
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
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
    fun `when email changes, state is updated`() {
        viewModel.onEvent(LoginEvent.EmailChanged("test@test.com"))
        assertEquals("test@test.com", viewModel.state.value.email)
    }

    @Test
    fun `when password changes, state is updated`() {
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))
        assertEquals("password123", viewModel.state.value.password)
    }

    @Test
    fun `state transition - loading to success`() = runTest {
        viewModel.onEvent(LoginEvent.EmailChanged("doctor@test.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))

        viewModel.onEvent(LoginEvent.OnSubmitClicked)

        // Check loading state
        assertTrue(viewModel.state.value.isLoading)
        
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
        val effect = viewModel.effect.first()
        assertEquals(LoginEfffect.NavigateToHome, effect)
    }

    @Test
    fun `state transition - loading to error`() = runTest {
        fakeRepo.loginResult = Result.failure(Exception("Network Error"))
        viewModel.onEvent(LoginEvent.EmailChanged("doctor@test.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("password123"))

        viewModel.onEvent(LoginEvent.OnSubmitClicked)

        assertTrue(viewModel.state.value.isLoading)
        
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
        assertEquals("Network Error", viewModel.state.value.errorMessage)
    }
}
