package com.gestorplus.appgestor.profile.data.datasource.datasource

import com.gestorplus.appgestor.core.data.local.dao.UserProfileDao
import com.gestorplus.appgestor.core.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

class ProfileLocalDatasource(private val userProfileDao: UserProfileDao) {
    fun getProfileFlow(): Flow<UserProfileEntity?> {
        return userProfileDao.getProfile()
    }

    suspend fun saveProfile(entity: UserProfileEntity) {
        userProfileDao.insertOrUpdate(entity)
    }
}
