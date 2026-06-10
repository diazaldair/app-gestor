package com.gestorplus.appgestor.explore_clinics.data

import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicDao
import com.gestorplus.appgestor.clinicProfile.data.local.entity.ClinicEntity
import com.gestorplus.appgestor.clinicProfile.domain.model.Clinic
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

open class ExploreClinicsRepositoryImpl(
    private val firebaseManager: FirebaseManager,
    private val clinicDao: ClinicDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    open fun getClinics(): Flow<List<Clinic>> {
        return clinicDao.getAllClinics().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    open suspend fun syncClinics() {
        try {
            // Sincronizamos desde 'workspaces' que es la ruta donde se guardan los perfiles de clínicas creados
            val workspacesData = firebaseManager.getData("workspaces") ?: return
            
            val clinicEntities = workspacesData.mapNotNull { (uid, data) ->
                try {
                    val dataMap = data as? Map<String, Any> ?: return@mapNotNull null
                    val profileJson = dataMap["profile"] as? String ?: return@mapNotNull null
                    val profile = json.decodeFromString<WorkspaceProfile>(profileJson)

                    ClinicEntity(
                        id = uid,
                        name = profile.clinicName.ifBlank { "Sin nombre" },
                        address = profile.exactAddress.ifBlank { "Sin dirección" },
                        description = profile.biography,
                        imageUrl = profile.galleryImages.firstOrNull(),
                        specialtiesJson = json.encodeToString(profile.specialities),
                        isOpen = true 
                    )
                } catch (e: Exception) {
                    println("Error syncClinics for UID $uid: ${e.message}")
                    null
                }
            }
            
            if (clinicEntities.isNotEmpty()) {
                clinicDao.clearAllClinics()
                clinicDao.insertClinics(clinicEntities)
            }
        } catch (e: Exception) {
            // Manejar error
        }
    }

    private fun ClinicEntity.toDomain(): Clinic {
        return Clinic(
            id = id,
            name = name,
            address = address,
            specialties = try { json.decodeFromString(specialtiesJson) } catch (e: Exception) { emptyList() },
            imageUrl = imageUrl,
            isOpen = isOpen,
            description = description,
            rating = 0.0 // No lo usaremos según requerimiento inicial
        )
    }
}
