package com.gestorplus.appgestor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.gestorplus.appgestor.core.firebase.getToken
import com.gestorplus.appgestor.core.work.LogScheduler
import com.gestorplus.appgestor.data.repository.EventRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val eventRepository: EventRepository by inject()

    // Manejador del resultado del permiso
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("FCM_DEBUG", "Permiso de notificaciones CONCEDIDO")
        } else {
            Log.w("FCM_DEBUG", "Permiso de notificaciones DENEGADO")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // 1. Pedir permiso de notificaciones (Android 13+)
        askNotificationPermission()

        // 2. Inicializar WorkManager
        val scheduler = LogScheduler(this)
        scheduler.schedulePeriodicUpload()
        Log.w("FCM_DEBUG", "=== PUNTO 1: WorkManager OK ===")

        // 3. Obtener el Token de Firebase
        lifecycleScope.launch {
            try {
                val token = getToken()
                Log.w("FCM_DEBUG", "=== 🎯 TOKEN OBTENIDO: $token ===")
            } catch (e: Exception) {
                Log.e("FCM_DEBUG", "=== 💀 ERROR OBTENIENDO TOKEN: ${e.message}")
            }
        }

        handleIntent(intent)

        // Registro de evento de apertura
        eventRepository.logEvent("APP_START")

        setContent {
            App()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("FCM_DEBUG", "El permiso ya estaba concedido")
            } else {
                // Pedir el permiso directamente
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            val screenToOpen = it.getStringExtra("OPEN_SCREEN")
            if (screenToOpen == "GITHUB") {
                Log.d("MAIN_ACTIVITY", "¡Éxito! Navegando a GithubScreen por Push Notification")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        eventRepository.logEvent("APP_CLOSE")
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
