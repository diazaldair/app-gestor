package com.gestorplus.appgestor.clinic_detail.data

import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicDao
import com.gestorplus.appgestor.clinicProfile.domain.model.Clinic
import com.gestorplus.appgestor.clinic_detail.data.local.dao.ClinicServiceDao
import com.gestorplus.appgestor.clinic_detail.data.local.entity.ClinicServiceEntity
import com.gestorplus.appgestor.clinic_detail.domain.model.ClinicService
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class ClinicDetailRepositoryImpl(
    private val firebaseManager: FirebaseManager,
    private val clinicDao: ClinicDao,
    private val serviceDao: ClinicServiceDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getClinic(id: String): Flow<Clinic?> = flow {
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
    }

    fun getServices(clinicId: String): Flow<List<ClinicService>> {
        return serviceDao.getServicesByClinicId(clinicId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun syncServices(clinicId: String) {
        try {
            val workspaceData = firebaseManager.getData("workspaces/$clinicId") ?: return
            val servicesList = mutableListOf<ClinicServiceEntity>()
            
            // 1. Obtener servicio inicial
            val initialServiceJson = workspaceData["initial_service"] as? String
            if (initialServiceJson != null) {
                try {
                    val ws = json.decodeFromString<WorkspaceService>(initialServiceJson)
                    servicesList.add(ClinicServiceEntity(
                        id = "initial_$clinicId",
                        clinicId = clinicId,
                        name = ws.name,
                        durationMinutes = ws.durationMinutes,
                        price = ws.price,
                        description = ws.description ?: ""
                    ))
                } catch (e: Exception) {}
            }
            
            // 2. Obtener catálogo de servicios (si el profesional añadió más)
            val catalogData = workspaceData["services"] as? Map<String, Any>
            catalogData?.forEach { (id, data) ->
                try {
                    val dataMap = data as? Map<String, Any> ?: return@forEach
                    servicesList.add(ClinicServiceEntity(
                        id = id,
                        clinicId = clinicId,
                        name = dataMap["name"] as? String ?: "",
                        durationMinutes = (dataMap["durationMinutes"] as? Number)?.toInt() ?: 30,
                        price = (dataMap["price"] as? Number)?.toDouble() ?: 0.0,
                        description = dataMap["description"] as? String ?: ""
                    ))
                } catch (e: Exception) {}
            }
            
            if (servicesList.isNotEmpty()) {
                serviceDao.deleteServicesByClinicId(clinicId)
                serviceDao.insertServices(servicesList)
            }
        } catch (e: Exception) {
            println("DEBUG: Error syncServices: ${e.message}")
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
