package com.gestorplus.appgestor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import app_gestor.composeapp.generated.resources.Res
import app_gestor.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    MaterialTheme {
        val firebaseManager = remember { FirebaseManager() }
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        
        var welcomeMessage by remember { mutableStateOf("Cargando config...") }
        var isFeatureEnabled by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(true) }
        var syncStatus by remember { mutableStateOf("Iniciando...") }

        // Función para sincronizar con Firebase
        val syncConfig = suspend {
            isLoading = true
            syncStatus = "Sincronizando con Firebase..."
            val defaults = mapOf(
                "welcome_text" to "Bienvenido (Local Default)",
                "show_extra_feature" to "false"
            )
            firebaseManager.initializeRemoteConfig(defaults)
            
            val success = firebaseManager.fetchAndActivate()
            if (success) {
                syncStatus = "✅ Sincronizado correctamente"
            } else {
                syncStatus = "⚠️ Falló el fetch (usando defaults)"
            }
            
            welcomeMessage = firebaseManager.getString("welcome_text")
            isFeatureEnabled = firebaseManager.getString("show_extra_feature") == "true"
            isLoading = false
        }

        LaunchedEffect(Unit) {
            syncConfig()
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Estado: $syncStatus",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = welcomeMessage,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Image(
                        painter = painterResource(Res.drawable.compose_multiplatform),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tarjeta que solo aparece si Remote Config envía "true"
                    if (isFeatureEnabled) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text(
                                "🔥 ¡NUEVA FUNCIÓN ACTIVA! 🔥",
                                modifier = Modifier.padding(16.dp),
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                syncConfig() // Re-sincronizar al pulsar
                                snackbarHostState.showSnackbar("Configuración actualizada")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        Text("Sincronizar ahora")
                    }
                    
                    TextButton(onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Navegando a la App...")
                        }
                    }) {
                        Text("Explorar App")
                    }
                }
            }
        }
    }
}
