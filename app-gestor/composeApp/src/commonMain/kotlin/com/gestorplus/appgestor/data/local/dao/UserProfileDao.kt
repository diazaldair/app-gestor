package com.gestorplus.appgestor.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 'owner_profile'")
    fun getProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)
}
