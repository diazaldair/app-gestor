package com.gestorplus.appgestor.core.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.data.local.dao.EventLogDao
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncEventsWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val eventLogDao: EventLogDao by inject()
    private val firebaseManager: FirebaseManager by inject()

    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "🔄 Sincronizando eventos pendientes con Firebase...")

        return try {
            // 1. Obtener eventos que no han sido sincronizados
            val unsyncedEvents = eventLogDao.getUnsyncedEvents()

            if (unsyncedEvents.isEmpty()) {
                Log.d("SyncWorker", "✅ No hay eventos nuevos para sincronizar.")
                return Result.success()
            }

            // 2. Subirlos uno por uno a Firebase
            unsyncedEvents.forEach { event ->
                val path = "app_logs/${event.timestamp}"
                val data = "Type: ${event.eventType} | Time: ${event.timestamp}"
                firebaseManager.saveData(path, data)
                
                // 3. Marcar como sincronizado en Room
                eventLogDao.markAsSynced(event.id)
            }

            Log.d("SyncWorker", "✅ Sincronización completada: ${unsyncedEvents.size} eventos subidos.")
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "❌ Error sincronizando eventos: ${e.message}")
            Result.retry()
        }
    }
}
