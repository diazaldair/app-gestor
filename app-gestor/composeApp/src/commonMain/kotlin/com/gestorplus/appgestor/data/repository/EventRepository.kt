package com.gestorplus.appgestor.data.repository

import com.gestorplus.appgestor.data.local.dao.EventLogDao
import com.gestorplus.appgestor.data.local.entity.EventLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EventRepository(
    private val eventLogDao: EventLogDao
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun logEvent(type: String) {
        scope.launch {
            // Guardar solo en Room localmente
            val event = EventLogEntity(eventType = type)
            eventLogDao.insertEvent(event)
        }
    }
}
