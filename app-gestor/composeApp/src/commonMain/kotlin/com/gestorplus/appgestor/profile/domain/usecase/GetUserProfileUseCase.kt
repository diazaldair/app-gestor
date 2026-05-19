package com.gestorplus.appgestor.profile.domain.usecase

import com.gestorplus.appgestor.profile.domain.model.UserProfile
import com.gestorplus.appgestor.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<UserProfile?> {
        return repository.getUserProfile()
    }
}
