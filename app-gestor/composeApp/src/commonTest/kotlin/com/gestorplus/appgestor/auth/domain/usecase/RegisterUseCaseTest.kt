package com.gestorplus.appgestor.auth.domain.usecase

import com.gestorplus.appgestor.auth.domain.model.UserSession
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RegisterUseCaseTest {

    private val fakeRepository = FakeAuthRepository()
    private val registerDoctorUseCase = RegisterDoctorUseCase(fakeRepository)

    @Test
    fun `invoke with blank fields returns failure`() = runTest {
        val result = registerDoctorUseCase("", "", "", "")
        assertTrue(result.isFailure)
        assertEquals("Por favor, completa todos los campos.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with invalid email returns failure`() = runTest {
        val result = registerDoctorUseCase("Dr. Test", "invalidemail", "password123", "password123")
        assertTrue(result.isFailure)
        assertEquals("Por favor, ingresa un correo electrónico válido.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with short password returns failure`() = runTest {
        val result = registerDoctorUseCase("Dr. Test", "test@test.com", "123", "123")
        assertTrue(result.isFailure)
        assertEquals("La contraseña debe tener al menos 6 caracteres.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with mismatched passwords returns failure`() = runTest {
        val result = registerDoctorUseCase("Dr. Test", "test@test.com", "password123", "password321")
        assertTrue(result.isFailure)
        assertEquals("Las contraseñas no coinciden.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with valid data calls repository and returns success`() = runTest {
        val result = registerDoctorUseCase("Dr. Test", "test@test.com", "password123", "password123")
        assertTrue(result.isSuccess)
    }
}
