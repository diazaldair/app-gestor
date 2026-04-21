package com.gestorplus.appgestor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.ThemeMode
import com.gestorplus.appgestor.presentation.DesignSystemTestScreen

@Composable
fun App() {
   val currentMode = ThemeMode.DARK
   val snackbarHostState = remember { SnackbarHostState() }
   
   DsTheme(
       mode = currentMode
   ) {
       Scaffold(
           contentWindowInsets = WindowInsets.safeDrawing,
           snackbarHost = { SnackbarHost(snackbarHostState) }
       ) { paddingValues ->
           Box(modifier = Modifier.padding(paddingValues)) {
               DesignSystemTestScreen()
           }
       }
   }
}
