package com.gestorplus.appgestor.clinicProfile.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.clinicProfile.data.local.entity.ClinicProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClinicProfileDao {
    @Query("SELECT * FROM clinic_profile WHERE id = 'single_clinic_profile'")
    fun getProfile(): Flow<ClinicProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ClinicProfileEntity)

    @Query("DELETE FROM clinic_profile")
    suspend fun clearProfile()
}
