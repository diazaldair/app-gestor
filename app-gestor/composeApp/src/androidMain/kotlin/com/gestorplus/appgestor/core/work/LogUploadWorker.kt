package com.gestorplus.appgestor.core.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gestorplus.appgestor.domain.booking.usecase.GetAvailableSlotsUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogUploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val getAvailableSlotsUseCase: GetAvailableSlotsUseCase by inject()

    override suspend fun doWork(): Result {
        Log.d("WorkManager", "🚀 Iniciando tarea programada...")

        return try {
            // Usamos un número entero puro para evitar el error de String
            val dateInt = 20241231
            val result = getAvailableSlotsUseCase.invoke(dateInt) 
            
            Log.d("WorkManager", "✅ Tarea completada. Se encontraron ${result.size} slots.")
            Result.success()
        } catch (e: Exception) {
            Log.e("WorkManager", "❌ Error en el Worker: ${e.message}")
            Result.retry()
        }
    }
}
