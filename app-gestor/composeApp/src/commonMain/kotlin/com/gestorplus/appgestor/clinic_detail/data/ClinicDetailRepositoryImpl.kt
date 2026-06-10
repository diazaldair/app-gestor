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
            // Sincronizamos desde 'workspaces/$clinicId'
            val workspaceData = firebaseManager.getData("workspaces/$clinicId") ?: return
            
            val allServiceEntities = mutableListOf<ClinicServiceEntity>()

            // 1. Intentamos obtener el servicio inicial que se guarda durante el setup
            val serviceJson = workspaceData["initial_service"] as? String
            if (serviceJson != null) {
                try {
                    val workspaceService = json.decodeFromString<com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService>(serviceJson)
                    allServiceEntities.add(ClinicServiceEntity(
                        id = "initial_service_$clinicId",
                        clinicId = clinicId,
                        name = workspaceService.name,
                        durationMinutes = workspaceService.durationMinutes,
                        price = workspaceService.price,
                        description = workspaceService.description
                    ))
                } catch (e: Exception) {
                    println("Error decoding workspace initial service: ${e.message}")
                }
            }

            // 2. Intentamos obtener la lista de servicios adicionales si existen
            val servicesNode = workspaceData["services"] as? Map<String, Any>
            servicesNode?.forEach { (id, data) ->
                try {
                    // Si los datos vienen como JSON String (estilo setup)
                    if (data is String) {
                        val ws = json.decodeFromString<com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService>(data)
                        allServiceEntities.add(ClinicServiceEntity(
                            id = id,
                            clinicId = clinicId,
                            name = ws.name,
                            durationMinutes = ws.durationMinutes,
                            price = ws.price,
                            description = ws.description
                        ))
                    } else if (data is Map<*, *>) {
                        // Si vienen como objeto estructurado
                        allServiceEntities.add(ClinicServiceEntity(
                            id = id,
                            clinicId = clinicId,
                            name = data["name"] as? String ?: "",
                            durationMinutes = (data["durationMinutes"] as? Number)?.toInt() ?: 30,
                            price = (data["price"] as? Number)?.toDouble() ?: 0.0,
                            description = data["description"] as? String ?: ""
                        ))
                    }
                } catch (e: Exception) {
                    println("Error decoding additional service $id: ${e.message}")
                }
            }
            
            if (allServiceEntities.isNotEmpty()) {
                serviceDao.deleteServicesByClinicId(clinicId)
                serviceDao.insertServices(allServiceEntities)
            }
        } catch (e: Exception) {
            println("Error syncServices: ${e.message}")
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
