package com.gestorplus.appgestor.auth.domain.usecase

import com.gestorplus.appgestor.auth.domain.repository.AuthRepository
import com.gestorplus.appgestor.auth.domain.model.UserSession
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Fake de AuthRepository para pruebas unitarias.
 */
class FakeAuthRepository : AuthRepository {
    var shouldReturnError = false
    var lastEmailAttempt = ""

    override suspend fun loginWithEmail(email: String, password: String): Result<UserSession> {
        lastEmailAttempt = email
        return if (shouldReturnError) {
            Result.failure(Exception("Invalid credentials"))
        } else {
            Result.success(UserSession("uid_123", email, "DOCTOR"))
        }
    }

    override suspend fun registerDoctor(fullName: String, email: String, password: String): Result<Unit> {
        return Result.success(Unit)
    }
}

class LoginUseCaseTest {

    private val fakeRepository = FakeAuthRepository()
    private val loginWithEmailUseCase = LoginWithEmailUseCase(fakeRepository)

    @Test
    fun `invoke calls repository and returns success`() = runTest {
        // Arrange
        fakeRepository.shouldReturnError = false
        val email = "test@example.com"

        // Act
        val result = loginWithEmailUseCase(email, "password123")

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(email, fakeRepository.lastEmailAttempt)
        assertEquals("uid_123", result.getOrNull()?.uid)
    }

    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        // Arrange
        fakeRepository.shouldReturnError = true

        // Act
        val result = loginWithEmailUseCase("wrong@email.com", "wrong_pass")

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Invalid credentials", result.exceptionOrNull()?.message)
    }
}
