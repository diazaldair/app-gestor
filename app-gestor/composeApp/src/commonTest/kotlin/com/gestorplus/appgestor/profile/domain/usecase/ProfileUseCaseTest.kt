package com.gestorplus.appgestor.profile.domain.usecase

import com.gestorplus.appgestor.profile.domain.model.UserProfile
import com.gestorplus.appgestor.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeProfileRepository : ProfileRepository {
    var profile: UserProfile? = null
    var saveResult: Result<Unit> = Result.success(Unit)

    override fun getUserProfile(): Flow<UserProfile?> = flowOf(profile)

    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        this.profile = profile
        return saveResult
    }
}

class ProfileUseCaseTest {

    private val fakeRepo = FakeProfileRepository()
    private val getUserProfileUseCase = GetUserProfileUseCase(fakeRepo)
    private val updateUserProfileUseCase = UpdateUserProfileUseCase(fakeRepo)

    private val sampleProfile = UserProfile(
        name = "Dr. Diaz",
        email = "diaz@test.com",
        phone = "123456",
        description = "Test Desc",
        imageUrl = "http://image.com"
    )

    @Test
    fun `getUserProfile returns profile from repository`() = runTest {
        fakeRepo.profile = sampleProfile
        
        val result = getUserProfileUseCase().first()
        
        assertEquals(sampleProfile, result)
    }

    @Test
    fun `updateUserProfile delegates to repository and returns success`() = runTest {
        val result = updateUserProfileUseCase(sampleProfile)
        
        assertTrue(result.isSuccess)
        assertEquals(sampleProfile, fakeRepo.profile)
    }

    @Test
    fun `updateUserProfile returns failure when repository fails`() = runTest {
        fakeRepo.saveResult = Result.failure(Exception("Update failed"))
        
        val result = updateUserProfileUseCase(sampleProfile)
        
        assertTrue(result.isFailure)
        assertEquals("Update failed", result.exceptionOrNull()?.message)
    }
}
