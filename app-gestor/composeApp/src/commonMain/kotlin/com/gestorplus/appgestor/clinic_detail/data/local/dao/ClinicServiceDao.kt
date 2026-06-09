package com.gestorplus.appgestor.clinic_detail.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.clinic_detail.data.local.entity.ClinicServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClinicServiceDao {
    @Query("SELECT * FROM clinic_services WHERE clinicId = :clinicId")
    fun getServicesByClinicId(clinicId: String): Flow<List<ClinicServiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ClinicServiceEntity>)

    @Query("DELETE FROM clinic_services WHERE clinicId = :clinicId")
    suspend fun deleteServicesByClinicId(clinicId: String)
}
