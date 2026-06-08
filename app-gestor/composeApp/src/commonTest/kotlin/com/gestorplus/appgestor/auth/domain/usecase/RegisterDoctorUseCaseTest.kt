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
 * Fake AuthRepository for testing Register use case in isolation.
 */
class FakeAuthRepositoryForRegister : AuthRepository {
    var registerResult: Result<UserSession> = Result.success(
        UserSession("uid-new", "doctor@clinic.com", "Dr. Nuevo", "PROFESSIONAL", "token-new")
    )

    override fun getActiveSession(): Flow<UserSession?> = flowOf(null)
    override suspend fun loginWithEmail(email: String, password: String): Result<UserSession> =
        Result.failure(Exception("Not implemented in this fake"))
    override suspend fun registerDoctor(name: String, email: String, password: String): Result<UserSession> = registerResult
    override suspend fun logout(): Result<Unit> = Result.success(Unit)
}

class RegisterDoctorUseCaseTest {

    private val fakeRepo = FakeAuthRepositoryForRegister()
    private val useCase = RegisterDoctorUseCase(fakeRepo)

    // --- Happy path ---

    @Test
    fun `register with valid data returns success`() = runTest {
        val result = useCase("Dr. García", "garcia@clinic.com", "Secure123", "Secure123")

        assertTrue(result.isSuccess)
        assertEquals("uid-new", result.getOrNull()?.userId)
    }

    // --- Validation: empty fields ---

    @Test
    fun `register with blank name returns failure`() = runTest {
        val result = useCase("", "email@test.com", "pass123", "pass123")

        assertTrue(result.isFailure)
        assertEquals("Por favor, completa todos los campos.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `register with blank email returns failure`() = runTest {
        val result = useCase("Dr. Test", "", "pass123", "pass123")

        assertTrue(result.isFailure)
        assertEquals("Por favor, completa todos los campos.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `register with blank password returns failure`() = runTest {
        val result = useCase("Dr. Test", "email@test.com", "", "pass123")

        assertTrue(result.isFailure)
    }

    @Test
    fun `register with blank confirmPassword returns failure`() = runTest {
        val result = useCase("Dr. Test", "email@test.com", "pass123", "")

        assertTrue(result.isFailure)
    }

    // --- Validation: email format ---

    @Test
    fun `register with email missing @ returns failure`() = runTest {
        val result = useCase("Dr. Test", "emailsinArroba", "pass123", "pass123")

        assertTrue(result.isFailure)
        assertEquals("Por favor, ingresa un correo electrónico válido.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `register with email missing dot returns failure`() = runTest {
        val result = useCase("Dr. Test", "email@dominio", "pass123", "pass123")

        assertTrue(result.isFailure)
        assertEquals("Por favor, ingresa un correo electrónico válido.", result.exceptionOrNull()?.message)
    }

    // --- Validation: password rules ---

    @Test
    fun `register with short password returns failure`() = runTest {
        val result = useCase("Dr. Test", "email@test.com", "abc", "abc")

        assertTrue(result.isFailure)
        assertEquals("La contraseña debe tener al menos 6 caracteres.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `register with mismatched passwords returns failure`() = runTest {
        val result = useCase("Dr. Test", "email@test.com", "password1", "password2")

        assertTrue(result.isFailure)
        assertEquals("Las contraseñas no coinciden.", result.exceptionOrNull()?.message)
    }

    // --- Repository error propagation ---

    @Test
    fun `register propagates repository error`() = runTest {
        fakeRepo.registerResult = Result.failure(Exception("El correo ya está registrado"))

        val result = useCase("Dr. Test", "existing@test.com", "pass123", "pass123")

        assertTrue(result.isFailure)
        assertEquals("El correo ya está registrado", result.exceptionOrNull()?.message)
    }

    // --- Edge cases ---

    @Test
    fun `register with exactly 6 char password succeeds`() = runTest {
        val result = useCase("Dr. Test", "test@test.com", "123456", "123456")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `validation order prioritizes empty fields over email format`() = runTest {
        val result = useCase("", "invalidEmail", "short", "diff")

        assertTrue(result.isFailure)
        // Empty fields check should fire first
        assertEquals("Por favor, completa todos los campos.", result.exceptionOrNull()?.message)
    }
}
