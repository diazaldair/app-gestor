package com.gestorplus.appgestor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gestorplus.appgestor.core.work.LogScheduler
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Inicializar WorkManager (Programador de tareas)
        val scheduler = LogScheduler(this)
        scheduler.schedulePeriodicUpload()

        // Fase 4: Obtener Token para pruebas
        fetchFcmToken()

        // Fase 5: Procesar intent de notificación
        handleIntent(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun fetchFcmToken() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                Log.d("FIREBASE_TOKEN", "Mi Token es: $token")
            } catch (e: Exception) {
                Log.e("FIREBASE_TOKEN", "Error obteniendo token", e)
            }
        }
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            val screenToOpen = it.getStringExtra("OPEN_SCREEN")
            if (screenToOpen == "GITHUB") {
                Log.d("MAIN_ACTIVITY", "¡Éxito! Navegando a GithubScreen por Push Notification")
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
