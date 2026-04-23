package com.gestorplus.appgestor.core.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class LogScheduler(
    private val context: Context
) {
    private val LOG_WORKNAME = "logUploadWork"
    private val INTERVAL_MINUTES = 15L

    fun triggerImmediateSync() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncEventsWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueue(syncRequest)
    }

    fun schedulePeriodicUpload() {
        // Configuramos las condiciones: la tarea solo se ejecuta si hay red conectada.
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Creamos la petición periódica: se ejecuta cada 15 minutos (mínimo permitido por Android)
        val logRequest = PeriodicWorkRequest.Builder(
            LogUploadWorker::class.java,
            INTERVAL_MINUTES,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        // Encolamos la tarea. Usamos enqueueUniquePeriodicWork para que si ya existe,
        // no cree otra tarea duplicada.
        WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
            LOG_WORKNAME,
            ExistingPeriodicWorkPolicy.KEEP,
            logRequest
        )
        
        println("WorkManager: Tarea periódica programada correctamente.")
    }
}
