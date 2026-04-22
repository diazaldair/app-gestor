package com.gestorplus.appgestor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.ThemeMode
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.presentation.owner.OwnerDashboardScreen
import org.koin.compose.koinInject

@Composable
fun App() {
   val firebaseManager: FirebaseManager = koinInject()
   val snackbarHostState = remember { SnackbarHostState() }

   LaunchedEffect(Unit) {
       val defaults = mapOf("primary_color" to "#6200EE")
       firebaseManager.initializeRemoteConfig(defaults)
       firebaseManager.fetchAndActivate()
   }

   DsTheme(
       mode = ThemeMode.DARK
   ) {
       Scaffold(
           contentWindowInsets = WindowInsets.safeDrawing,
           snackbarHost = { SnackbarHost(snackbarHostState) }
       ) { paddingValues ->
           Box(modifier = Modifier.padding(paddingValues)) {
               OwnerDashboardScreen()
           }
       }
   }
}
