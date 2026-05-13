package com.gestorplus.appgestor

import androidx.compose.runtime.*
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.ThemeMode
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.data.repository.OwnerBookingRepository
import com.gestorplus.appgestor.presentation.owner.OwnerDashboardScreen
import com.gestorplus.appgestor.services.presentation.screen.ServiceScreen
import com.gestorplus.appgestor.booking_success.presentation.screen.BookingSuccessScreen
import org.koin.compose.koinInject

@Composable
fun App() {
   val firebaseManager: FirebaseManager = koinInject()
   val repository: OwnerBookingRepository = koinInject()
   
   var currentScreen by remember { mutableStateOf("dashboard") }

   LaunchedEffect(Unit) {
       val defaults = mapOf(
           "primary_color" to "#6200EE",
           "sync_client_name" to "Promoción de Verano",
           "sync_client_service" to "Consultoría Gratuita",
           "sync_price" to "0.0"
       )
       firebaseManager.initializeRemoteConfig(defaults)
       firebaseManager.fetchAndActivate()
       
       // Sincronizamos con Room
       repository.syncInitialConfig()
   }

   DsTheme(
       mode = ThemeMode.DARK
   ) {
       when (currentScreen) {
           "dashboard" -> {
               OwnerDashboardScreen(
                   onNavigateToServices = { currentScreen = "services" }
               )
           }
           "services" -> {
               ServiceScreen(
                   onBack = { currentScreen = "dashboard" },
                   onNavigateToSuccess = { currentScreen = "booking_success" }
               )
           }
           "booking_success" -> {
               BookingSuccessScreen(
                   onDone = { currentScreen = "dashboard" },
                   onViewBookings = { currentScreen = "dashboard" }
               )
           }
       }
   }
}
