package com.gestorplus.appgestor

import androidx.compose.runtime.*
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.ThemeMode
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.data.repository.OwnerBookingRepository
import com.gestorplus.appgestor.presentation.owner.OwnerDashboardScreen
import com.gestorplus.appgestor.presentation.owner.WorkingHoursScreen
import com.gestorplus.appgestor.presentation.owner.ScheduleGroupDetailScreen
import com.gestorplus.appgestor.presentation.booking.BookingScreen
import com.gestorplus.appgestor.presentation.booking.BookingConfirmationScreen
import com.gestorplus.appgestor.presentation.landing.LandingScreen
import org.koin.compose.koinInject

enum class Screen {
    Landing,
    ClientView,
    BookingConfirmation,
    BusinessView,
    DoctorView,
    WorkingHours,
    ScheduleGroupDetail
}

@Composable
fun App() {
    val firebaseManager: FirebaseManager = koinInject()
    val repository: OwnerBookingRepository = koinInject()
    
    // Simple state-based navigation
    var currentScreen by remember { mutableStateOf(Screen.Landing) }

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
            Screen.Landing -> {
                LandingScreen(
                    onNavigateToClient = { currentScreen = Screen.ClientView },
                    onNavigateToBusiness = { currentScreen = Screen.BusinessView },
                    onNavigateToDoctor = { currentScreen = Screen.DoctorView }
                )
            }
            Screen.ClientView -> {
                BookingScreen(
                    onBack = { currentScreen = Screen.Landing },
                    onConfirm = { currentScreen = Screen.BookingConfirmation }
                )
            }
            Screen.BookingConfirmation -> {
                BookingConfirmationScreen(
                    onBack = { currentScreen = Screen.ClientView },
                    onConfirm = { currentScreen = Screen.Landing }
                )
            }
            Screen.BusinessView, Screen.DoctorView -> {
                OwnerDashboardScreen(
                    onBack = { currentScreen = Screen.Landing },
                    onNavigateToWorkingHours = { currentScreen = Screen.WorkingHours }
                )
            }
            Screen.WorkingHours -> {
                WorkingHoursScreen(
                    onBack = { currentScreen = Screen.BusinessView },
                    onNavigateToGroupDetail = { currentScreen = Screen.ScheduleGroupDetail }
                )
            }
            Screen.ScheduleGroupDetail -> {
                ScheduleGroupDetailScreen(
                    onBack = { currentScreen = Screen.WorkingHours }
                )
            }
        }
    }
}
