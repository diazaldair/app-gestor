package com.gestorplus.appgestor.auth.domain.usecase

import com.gestorplus.appgestor.auth.domain.model.UserSession
import com.gestorplus.appgestor.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Fake AuthRepository for testing Login use case in isolation.
 * Follows the Fake pattern (not mock) for deterministic, framework-free tests.
 */
class FakeAuthRepositoryForLogin : AuthRepository {
    var loginResult: Result<UserSession> = Result.success(
        UserSession("uid-123", "test@test.com", "Dr. Test", "PROFESSIONAL", "token-abc")
    )
    var registerResult: Result<UserSession> = Result.success(
        UserSession("uid-456", "new@test.com", "Dr. New", "PROFESSIONAL", "token-def")
    )

    override fun getActiveSession(): Flow<UserSession?> = flowOf(null)
    override suspend fun loginWithEmail(email: String, password: String): Result<UserSession> = loginResult
    override suspend fun registerDoctor(name: String, email: String, password: String): Result<UserSession> = registerResult
    override suspend fun logout(): Result<Unit> = Result.success(Unit)
}

class LoginWithEmailUseCaseTest {

    private val fakeRepo = FakeAuthRepositoryForLogin()
    private val useCase = LoginWithEmailUseCase(fakeRepo)

    // --- Happy path ---

    @Test
    fun `login with valid credentials returns success`() = runTest {
        val result = useCase("doctor@clinic.com", "SecurePass123")

        assertTrue(result.isSuccess)
        assertEquals("uid-123", result.getOrNull()?.userId)
        assertEquals("PROFESSIONAL", result.getOrNull()?.role)
    }

    // --- Validation: empty fields ---

    @Test
    fun `login with blank email returns failure`() = runTest {
        val result = useCase("", "password123")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals(
            "El correo y la contraseña no pueden estar vacíos.",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun `login with blank password returns failure`() = runTest {
        val result = useCase("doctor@clinic.com", "")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `login with both fields blank returns failure`() = runTest {
        val result = useCase("", "")

        assertTrue(result.isFailure)
    }

    @Test
    fun `login with whitespace-only email returns failure`() = runTest {
        val result = useCase("   ", "password123")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    // --- Repository error propagation ---

    @Test
    fun `login propagates repository failure`() = runTest {
        fakeRepo.loginResult = Result.failure(Exception("Credenciales incorrectas"))

        val result = useCase("doctor@clinic.com", "wrongpass")

        assertTrue(result.isFailure)
        assertEquals("Credenciales incorrectas", result.exceptionOrNull()?.message)
    }

    @Test
    fun `login propagates network error from repository`() = runTest {
        fakeRepo.loginResult = Result.failure(Exception("Sin conexión a internet"))

        val result = useCase("doctor@clinic.com", "password")

        assertTrue(result.isFailure)
        assertEquals("Sin conexión a internet", result.exceptionOrNull()?.message)
    }
}
