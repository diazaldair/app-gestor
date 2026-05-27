package com.gestorplus.appgestor

import androidx.compose.runtime.*
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.ThemeMode
import com.gestorplus.appgestor.owner.setup_intro.domain.usecase.InitializeAndSyncConfigUseCase
import com.gestorplus.appgestor.owner.dashboard.presentation.screen.OwnerDashboardScreen
import com.gestorplus.appgestor.owner.presentation.screen.WorkingHoursScreen
import com.gestorplus.appgestor.owner.presentation.screen.ScheduleGroupDetailScreen
import com.gestorplus.appgestor.booking.presentation.screen.BookingScreen
import com.gestorplus.appgestor.booking.presentation.screen.BookingConfirmationScreen
import com.gestorplus.appgestor.auth.presentation.landing.screen.LandingScreen
import com.gestorplus.appgestor.auth.presentation.login.screen.LoginScreen
import com.gestorplus.appgestor.auth.presentation.register.screen.RegisterScreen
import com.gestorplus.appgestor.owner.setup_intro.presentation.screen.WorkspaceSetupIntroScreen
import com.gestorplus.appgestor.owner.setup_profile.presentation.screen.WorkspaceSetupProfileScreen
import com.gestorplus.appgestor.owner.setup_schedule.presentation.screen.WorkspaceSetupScheduleScreen
import com.gestorplus.appgestor.owner.setup_service.presentation.screen.WorkspaceSetupServiceScreen
import com.gestorplus.appgestor.owner.setup_success.presentation.screen.WorkspaceSetupSuccessScreen
import com.gestorplus.appgestor.profile.presentation.screen.ProfileScreen
import org.koin.compose.koinInject
import com.gestorplus.appgestor.onboarding.presentation.screen.OnboardingScreen
import com.gestorplus.appgestor.onboarding.domain.usecase.IsOnboardingCompletedUseCase
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade

enum class Screen {
    Onboarding,
    OnboardingFinished,
    Landing,
    Login,
    Register,
    WorkspaceSetupIntro,
    WorkspaceSetupProfile,
    WorkspaceSetupSchedule,
    WorkspaceSetupService,
    WorkspaceSetupSuccess,
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
    val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase = koinInject()
    
    // Simple state-based navigation
    var currentScreen by remember { 
        mutableStateOf(
            if (isOnboardingCompletedUseCase()) Screen.OnboardingFinished else Screen.Onboarding
        ) 
    }
    var selectedRole by remember { mutableStateOf("PATIENT") } // "PATIENT" o "PROFESSIONAL"

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .crossfade(true)
            .build()
    }

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
            Screen.Onboarding -> {
                OnboardingScreen(
                    onNavigateToHome = { currentScreen = Screen.OnboardingFinished }
                )
            }
            Screen.OnboardingFinished -> {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("¡Onboarding Finalizado!", fontSize = 24.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Has llegado al final del flujo.", fontSize = 16.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(32.dp))
                        androidx.compose.material3.Button(onClick = { currentScreen = Screen.Landing }) {
                            Text("Ir al Proyecto Real")
                        }
                    }
                }
            }
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
                            currentScreen = Screen.WorkspaceSetupIntro
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
                            currentScreen = Screen.WorkspaceSetupIntro
                        }
                    },
                    onNavigateToLogin = {
                        currentScreen = Screen.Login
                    }
                )
            }
            Screen.WorkspaceSetupIntro -> {
                WorkspaceSetupIntroScreen(
                    onNavigateToNextStep = {
                        currentScreen = Screen.WorkspaceSetupProfile
                    }
                )
            }
            Screen.WorkspaceSetupProfile -> {
                WorkspaceSetupProfileScreen(
                    onNavigateToNextStep = {
                        currentScreen = Screen.WorkspaceSetupSchedule
                    },
                    onNavigateBack = {
                        currentScreen = Screen.WorkspaceSetupIntro
                    }
                )
            }
            Screen.WorkspaceSetupSchedule -> {
                WorkspaceSetupScheduleScreen(
                    onNavigateToNextStep = {
                        currentScreen = Screen.WorkspaceSetupService
                    },
                    onNavigateBack = {
                        currentScreen = Screen.WorkspaceSetupProfile
                    }
                )
            }
            Screen.WorkspaceSetupService -> {
                WorkspaceSetupServiceScreen(
                    onNavigateToNextStep = {
                        currentScreen = Screen.WorkspaceSetupSuccess
                    },
                    onNavigateBack = {
                        currentScreen = Screen.WorkspaceSetupSchedule
                    }
                )
            }
            Screen.WorkspaceSetupSuccess -> {
                WorkspaceSetupSuccessScreen(
                    onNavigateToDashboard = {
                        currentScreen = Screen.DoctorView
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
