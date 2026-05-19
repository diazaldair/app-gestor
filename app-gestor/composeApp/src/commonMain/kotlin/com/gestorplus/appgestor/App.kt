package com.gestorplus.appgestor

import androidx.compose.runtime.*
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.ThemeMode
import com.gestorplus.appgestor.owner.domain.usecase.InitializeAndSyncConfigUseCase
import com.gestorplus.appgestor.owner.presentation.screen.OwnerDashboardScreen
import com.gestorplus.appgestor.owner.presentation.screen.WorkingHoursScreen
import com.gestorplus.appgestor.owner.presentation.screen.ScheduleGroupDetailScreen
import com.gestorplus.appgestor.booking.presentation.screen.BookingScreen
import com.gestorplus.appgestor.booking.presentation.screen.BookingConfirmationScreen
import com.gestorplus.appgestor.auth.presentation.landing.screen.LandingScreen
import com.gestorplus.appgestor.auth.presentation.login.screen.LoginScreen
import com.gestorplus.appgestor.auth.presentation.register.screen.RegisterScreen
import com.gestorplus.appgestor.profile.presentation.screen.ProfileScreen
import org.koin.compose.koinInject

enum class Screen {
    Landing,
    Login,
    Register,
    ClientView,
    BookingConfirmation,
    BusinessView,
    DoctorView,
    WorkingHours,
    ScheduleGroupDetail,
    Profile
}

@Composable
fun App() {
    val initializeAndSyncConfigUseCase: InitializeAndSyncConfigUseCase = koinInject()
    
    // Simple state-based navigation
    var currentScreen by remember { mutableStateOf(Screen.Landing) }
    var selectedRole by remember { mutableStateOf("PATIENT") } // "PATIENT" o "PROFESSIONAL"

    LaunchedEffect(Unit) {
        val defaults = mapOf(
            "primary_color" to "#6200EE",
            "sync_client_name" to "Promoción de Verano",
            "sync_client_service" to "Consultoría Gratuita",
            "sync_price" to "0.0"
        )
        initializeAndSyncConfigUseCase(defaults)
    }

    DsTheme(
        mode = ThemeMode.DARK
    ) {
        when (currentScreen) {
            Screen.Landing -> {
                LandingScreen(
                    onNavigateToPatient = { 
                        selectedRole = "PATIENT"
                        currentScreen = Screen.Login 
                    },
                    onNavigateToProfessional = { 
                        selectedRole = "PROFESSIONAL"
                        currentScreen = Screen.Login 
                    }
                )
            }
            Screen.Login -> {
                LoginScreen(
                    onNavigateToHome = {
                        if (selectedRole == "PATIENT") {
                            currentScreen = Screen.ClientView
                        } else {
                            currentScreen = Screen.DoctorView
                        }
                    },
                    onNavigateToRegister = {
                        currentScreen = Screen.Register
                    }
                )
            }
            Screen.Register -> {
                RegisterScreen(
                    onNavigateToHome = {
                        if (selectedRole == "PATIENT") {
                            currentScreen = Screen.ClientView
                        } else {
                            currentScreen = Screen.DoctorView
                        }
                    },
                    onNavigateToLogin = {
                        currentScreen = Screen.Login
                    }
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
                    onNavigateToWorkingHours = { currentScreen = Screen.WorkingHours },
                    onNavigateToProfile = { currentScreen = Screen.Profile }
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
            Screen.Profile -> {
                ProfileScreen(
                    onBack = { currentScreen = Screen.BusinessView }
                )
            }
        }
    }
}
