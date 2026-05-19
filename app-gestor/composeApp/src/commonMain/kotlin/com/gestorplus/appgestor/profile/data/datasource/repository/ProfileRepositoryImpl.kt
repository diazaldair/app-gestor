package com.gestorplus.appgestor.profile.data.datasource.repository

import com.gestorplus.appgestor.profile.data.datasource.datasource.ProfileLocalDatasource
import com.gestorplus.appgestor.profile.data.datasource.datasource.ProfileRemoteDatasource
import com.gestorplus.appgestor.profile.data.datasource.mapper.ProfileMapper
import com.gestorplus.appgestor.profile.domain.model.UserProfile
import com.gestorplus.appgestor.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(
    private val localDatasource: ProfileLocalDatasource,
    private val remoteDatasource: ProfileRemoteDatasource,
    private val profileMapper: ProfileMapper
) : ProfileRepository {

    override fun getUserProfile(): Flow<UserProfile?> {
        return localDatasource.getProfileFlow().map { entity ->
            entity?.let { profileMapper.toDomain(it) }
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val entity = profileMapper.toEntity(profile)
            localDatasource.saveProfile(entity)
            
            // Sync to Firebase for real-time updates!
            val pipedData = "${profile.name}|${profile.email}|${profile.phone}|${profile.description}|${profile.imageUrl}"
            remoteDatasource.saveProfile(pipedData)
            
            Result.success(Unit)
        } catch (e: Exception) {
            // Offline-first fallback
            Result.success(Unit)
        }
    }
}
