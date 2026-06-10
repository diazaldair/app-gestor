package com.gestorplus.appgestor

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import com.gestorplus.appgestor.core.firebase.getToken
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.content.pm.PackageManager

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d("FCM_DEBUG", "Permiso notificaciones: $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // PASO 1: Cancelar CUALQUIER tarea de fondo que esté causando el crash loop
        try {
            WorkManager.getInstance(applicationContext).cancelAllWork()
            Log.w("APP_FIX", "Tareas de fondo canceladas para detener el crash loop")
        } catch (e: Exception) {
            Log.e("APP_FIX", "No se pudo limpiar WorkManager: ${e.message}")
        }

        askNotificationPermission()

        // PASO 2: Obtener token de forma segura
        lifecycleScope.launch {
            try {
                val token = getToken()
                Log.w("FCM_DEBUG", "FCM TOKEN: $token")
            } catch (e: Exception) {
                Log.e("FCM_DEBUG", "Error obteniendo token: ${e.message}")
            }
        }

        // PASO 3: Arrancar la UI sin tocar la base de datos en el hilo principal
        setContent {
            App()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
