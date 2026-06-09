package com.gestorplus.appgestor.clinicProfile.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.clinicProfile.data.local.entity.ClinicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClinicDao {
    @Query("SELECT * FROM clinics")
    fun getAllClinics(): Flow<List<ClinicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClinics(clinics: List<ClinicEntity>)

    @Query("SELECT * FROM clinics WHERE id = :id")
    suspend fun getClinicById(id: String): ClinicEntity?

    @Query("DELETE FROM clinics")
    suspend fun clearAllClinics()
}
