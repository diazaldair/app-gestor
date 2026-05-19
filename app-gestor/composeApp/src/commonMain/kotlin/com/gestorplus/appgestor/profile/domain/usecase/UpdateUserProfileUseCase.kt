package com.gestorplus.appgestor.profile.domain.usecase

import com.gestorplus.appgestor.profile.domain.model.UserProfile
import com.gestorplus.appgestor.profile.domain.repository.ProfileRepository

class UpdateUserProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: UserProfile): Result<Unit> {
        return repository.saveUserProfile(profile)
    }
}
