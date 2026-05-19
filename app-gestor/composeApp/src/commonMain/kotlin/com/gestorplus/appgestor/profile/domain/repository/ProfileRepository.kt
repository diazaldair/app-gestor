package com.gestorplus.appgestor.profile.domain.repository

import com.gestorplus.appgestor.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUserProfile(): Flow<UserProfile?>
    suspend fun saveUserProfile(profile: UserProfile): Result<Unit>
}
