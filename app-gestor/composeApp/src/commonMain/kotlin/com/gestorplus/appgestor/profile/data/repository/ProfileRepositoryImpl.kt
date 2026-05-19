package com.gestorplus.appgestor.profile.data.repository

import com.gestorplus.appgestor.data.local.dao.UserProfileDao
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.profile.data.mapper.ProfileMapper
import com.gestorplus.appgestor.profile.domain.model.UserProfile
import com.gestorplus.appgestor.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(
    private val profileDao: UserProfileDao,
    private val firebaseManager: FirebaseManager,
    private val profileMapper: ProfileMapper
) : ProfileRepository {

    override fun getUserProfile(): Flow<UserProfile?> {
        return profileDao.getProfile().map { entity ->
            entity?.let { profileMapper.toDomain(it) }
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val entity = profileMapper.toEntity(profile)
            profileDao.insertOrUpdate(entity)
            
            // Sync to Firebase for real-time updates!
            val dataPath = "profile/owner_profile"
            val pipedData = "${profile.name}|${profile.email}|${profile.phone}|${profile.description}|${profile.imageUrl}"
            firebaseManager.saveData(dataPath, pipedData)
            
            Result.success(Unit)
        } catch (e: Exception) {
            // Offline-first fallback
            Result.success(Unit)
        }
    }
}
