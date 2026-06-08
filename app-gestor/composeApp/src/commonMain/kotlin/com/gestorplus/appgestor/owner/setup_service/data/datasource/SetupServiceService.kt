package com.gestorplus.appgestor.owner.setup_service.data.datasource

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager

class SetupServiceService(private val firebaseManager: FirebaseManager) {
    suspend fun saveWorkspaceService(uid: String, data: String) {
        // En un caso real se guardaría en una colección de servicios
        // Aquí simulamos guardar el "primer servicio" de la configuración
        firebaseManager.saveData("workspaces/$uid/initial_service", data)
    }
}
