package com.gestorplus.appgestor.data.repository

import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.data.local.dao.EventLogDao
import com.gestorplus.appgestor.data.local.entity.EventLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EventRepository(
    private val eventLogDao: EventLogDao,
    private val firebaseManager: FirebaseManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun logEvent(type: String) {
        scope.launch {
            // 1. Guardar en Room
            val event = EventLogEntity(eventType = type)
            eventLogDao.insertEvent(event)

            // 2. Intentar replicar en Firebase Realtime Database
            try {
                val path = "app_logs/${event.timestamp}"
                val data = "Type: $type | Time: ${event.timestamp}"
                firebaseManager.saveData(path, data)
                
                // Si llegamos aquí, se subió con éxito (podríamos marcar como synced en Room)
            } catch (e: Exception) {
                // Si falla por falta de internet, se queda en Room con synced = false
            }
        }
    }
}
