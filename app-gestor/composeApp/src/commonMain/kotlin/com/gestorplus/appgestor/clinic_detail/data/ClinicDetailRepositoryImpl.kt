package com.gestorplus.appgestor.clinic_detail.data

import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicDao
import com.gestorplus.appgestor.clinicProfile.domain.model.Clinic
import com.gestorplus.appgestor.clinic_detail.data.local.dao.ClinicServiceDao
import com.gestorplus.appgestor.clinic_detail.data.local.entity.ClinicServiceEntity
import com.gestorplus.appgestor.clinic_detail.domain.model.ClinicService
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ClinicDetailRepositoryImpl(
    private val firebaseManager: FirebaseManager,
    private val clinicDao: ClinicDao,
    private val serviceDao: ClinicServiceDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getClinic(id: String): Flow<Clinic?> {
        return flow {
            // Local
            val entity = clinicDao.getClinicById(id)
            if (entity != null) {
                emit(Clinic(
                    id = entity.id,
                    name = entity.name,
                    address = entity.address,
                    specialties = try { json.decodeFromString(entity.specialtiesJson) } catch (e: Exception) { emptyList() },
                    imageUrl = entity.imageUrl,
                    isOpen = entity.isOpen,
                    description = entity.description
                ))
            }
            
            // Remote sync (optional here if we already synced in explore, but good for freshness)
            try {
                val data = firebaseManager.getData("clinics/$id")
                if (data != null) {
                    val name = data["name"] as? String ?: ""
                    // ... map other fields if needed to update local DB
                }
            } catch (e: Exception) {}
        }
    }

    fun getServices(clinicId: String): Flow<List<ClinicService>> {
        return serviceDao.getServicesByClinicId(clinicId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun syncServices(clinicId: String) {
        try {
            // Assuming services for a clinic are stored at "clinics/$clinicId/services"
            val servicesData = firebaseManager.getData("clinics/$clinicId/services") ?: return
            
            val serviceEntities = servicesData.mapNotNull { (id, data) ->
                try {
                    val dataMap = data as? Map<String, Any> ?: return@mapNotNull null
                    ClinicServiceEntity(
                        id = id,
                        clinicId = clinicId,
                        name = dataMap["name"] as? String ?: "",
                        durationMinutes = (dataMap["durationMinutes"] as? Number)?.toInt() ?: 30,
                        price = (dataMap["price"] as? Number)?.toDouble() ?: 0.0,
                        description = dataMap["description"] as? String ?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            }
            
            if (serviceEntities.isNotEmpty()) {
                serviceDao.deleteServicesByClinicId(clinicId)
                serviceDao.insertServices(serviceEntities)
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    private fun ClinicServiceEntity.toDomain() = ClinicService(
        id = id,
        clinicId = clinicId,
        name = name,
        durationMinutes = durationMinutes,
        price = price,
        description = description
    )
}
