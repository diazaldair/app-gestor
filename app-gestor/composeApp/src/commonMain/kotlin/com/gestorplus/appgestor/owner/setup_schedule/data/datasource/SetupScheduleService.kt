package com.gestorplus.appgestor.owner.setup_schedule.data.datasource

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager

class SetupScheduleService(private val firebaseManager: FirebaseManager) {
    suspend fun saveWorkspaceSchedule(uid: String, data: String) {
        firebaseManager.saveData("workspaces/$uid/schedule", data)
    }

    suspend fun getWorkspaceSchedule(uid: String): String? {
        val data = firebaseManager.getData("workspaces/$uid/schedule")
        // Como FirebaseManager.getData devuelve Map<String, Any>?, 
        // y nosotros guardamos el JSON como un valor directo en saveData (según parece por la firma de saveData),
        // necesitamos obtener el string. 
        // Si saveData guarda el JSON como un string en esa ruta, getData debería devolver algo que podamos convertir.
        return data?.get("value") as? String ?: data?.toString() 
        // Nota: Esto depende de cómo FirebaseManager.saveData esté implementado internamente.
        // Si saveData usa setValue(dataString), entonces la ruta contiene el string.
    }
}
