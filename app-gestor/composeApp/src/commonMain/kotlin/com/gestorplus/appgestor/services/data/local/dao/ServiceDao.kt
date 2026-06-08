package com.gestorplus.appgestor.services.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.services.data.local.entity.ServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services")
    fun getAllServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE id = :id")
    suspend fun getServiceById(id: String): ServiceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity)

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Query("UPDATE services SET isActive = :isActive WHERE id = :id")
    suspend fun updateServiceStatus(id: String, isActive: Boolean)

    @Query("DELETE FROM services WHERE id = :id")
    suspend fun deleteService(id: String)
}
