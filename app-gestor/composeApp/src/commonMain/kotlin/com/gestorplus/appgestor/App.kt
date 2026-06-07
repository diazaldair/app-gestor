package com.gestorplus.appgestor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.gestorplus.appgestor.auth.presentation.landing.screen.LandingScreen
import com.gestorplus.appgestor.auth.presentation.login.screen.LoginScreen
import com.gestorplus.appgestor.auth.presentation.register.screen.RegisterScreen
import com.gestorplus.appgestor.booking.presentation.screen.BookingConfirmationScreen
import com.gestorplus.appgestor.booking.presentation.screen.BookingScreen
import com.gestorplus.appgestor.booking.presentation.screen.BookingSuccessScreen
import com.gestorplus.appgestor.booking.presentation.screen.MyBookingsScreen
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.ThemeMode
import com.gestorplus.appgestor.home.presentation.screen.HomeScreen
import com.gestorplus.appgestor.onboarding.domain.usecase.IsOnboardingCompletedUseCase
import com.gestorplus.appgestor.onboarding.presentation.screen.OnboardingScreen
import com.gestorplus.appgestor.owner.dashboard.presentation.screen.OwnerDashboardScreen
import com.gestorplus.appgestor.owner.presentation.screen.ScheduleGroupDetailScreen
import com.gestorplus.appgestor.owner.presentation.screen.WorkingHoursScreen
import com.gestorplus.appgestor.owner.setup_intro.domain.usecase.InitializeAndSyncConfigUseCase
import com.gestorplus.appgestor.owner.setup_intro.presentation.screen.WorkspaceSetupIntroScreen
import com.gestorplus.appgestor.owner.setup_profile.presentation.screen.WorkspaceSetupProfileScreen
import com.gestorplus.appgestor.owner.setup_schedule.presentation.screen.WorkspaceSetupScheduleScreen
import com.gestorplus.appgestor.owner.setup_service.presentation.screen.WorkspaceSetupServiceScreen
import com.gestorplus.appgestor.owner.setup_success.presentation.screen.WorkspaceSetupSuccessScreen
import com.gestorplus.appgestor.profile.presentation.screen.ProfileScreen
import org.koin.compose.koinInject

enum class Screen {
    Onboarding,
    Home,
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
    BookingSuccess,
    MyBookings,
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

    var currentScreen by remember {
        mutableStateOf(
            if (isOnboardingCompletedUseCase()) Screen.Home else Screen.Onboarding
        )
    }
    var selectedRole by remember { mutableStateOf("PATIENT") }

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
            "sync_client_name" to "Promoci\u00f3n de Verano",
            "sync_client_service" to "Consultor\u00eda Gratuita",
            "sync_price" to "0.0",
            "onboarding_config" to DEFAULT_ONBOARDING_CONFIG
        )
        initializeAndSyncConfigUseCase(defaults)
    }

    DsTheme(mode = ThemeMode.DARK) {
        when (currentScreen) {
            Screen.Onboarding -> {
                OnboardingScreen(
                    onNavigateToHome = { currentScreen = Screen.Home }
                )
            }

            Screen.Home -> {
                HomeScreen(
                    onNavigateToProfiles = {
                        currentScreen = Screen.Landing
                    },
                    onNavigateToOnboarding = {
                        currentScreen = Screen.Onboarding
                    }
                )
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
                    },
                    onNavigateToOnboarding = {
                        currentScreen = Screen.Onboarding
                    }
                )
            }

            Screen.Login -> {
                LoginScreen(
                    onNavigateToHome = {
                        currentScreen = if (selectedRole == "PATIENT") {
                            Screen.ClientView
                        } else {
                            Screen.WorkspaceSetupIntro
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
                        currentScreen = if (selectedRole == "PATIENT") {
                            Screen.ClientView
                        } else {
                            Screen.WorkspaceSetupIntro
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
                    onBack = { currentScreen = Screen.Home },
                    onConfirm = { currentScreen = Screen.BookingConfirmation }
                )
            }

            Screen.BookingConfirmation -> {
                BookingConfirmationScreen(
                    onBack = { currentScreen = Screen.ClientView },
                    onConfirm = { currentScreen = Screen.BookingSuccess }
                )
            }

            Screen.BookingSuccess -> {
                BookingSuccessScreen(
                    onNavigateToMyBookings = { currentScreen = Screen.MyBookings },
                    onNavigateToHome = { currentScreen = Screen.Home }
                )
            }

            Screen.MyBookings -> {
                MyBookingsScreen(
                    onBack = { currentScreen = Screen.Home }
                )
            }

            Screen.BusinessView, Screen.DoctorView -> {
                OwnerDashboardScreen(
                    onBack = { currentScreen = Screen.Home },
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

private const val DEFAULT_ONBOARDING_CONFIG = """
{
  "onboarding_config": [
    {
      "id": 1,
      "title": {
        "es": "\u00a1Organiza tu negocio!",
        "en": "Organize your business!",
        "fr": "Organisez votre empresa !"
      },
      "description": {
        "es": "Gestiona tus proyectos y prioridades de forma sencilla con GestorPlus.",
        "en": "Easily manage projects and priorities with GestorPlus.",
        "fr": "G\u00e9rez facilement vos t\u00e2ches, proyectos et priorit\u00e9s avec GestorPlus."
      },
      "image_url": {
        "es": "https://cdn-icons-png.flaticon.com/512/2620/2620667.png",
        "en": "https://cdn-icons-png.flaticon.com/512/2620/2620667.png",
        "fr": "https://cdn-icons-png.flaticon.com/512/2620/2620667.png"
      }
    },
    {
      "id": 2,
      "title": {
        "es": "Trabaja en equipo",
        "en": "Teamwork",
        "fr": "Travail d'\u00e9quipe"
      },
      "description": {
        "es": "Colabora en tiempo real y mant\u00e9n a todo tu equipo sincronizado.",
        "en": "Collaborate in real-time and keep your entire team in sync.",
        "fr": "Collaborez en temps r\u00e9el et gardez toute votre \u00e9quipe synchronis\u00e9e."
      },
      "image_url": {
        "es": "https://cdn-icons-png.flaticon.com/512/1256/1256650.png",
        "en": "https://cdn-icons-png.flaticon.com/512/1256/1256650.png",
        "fr": "https://cdn-icons-png.flaticon.com/512/1256/1256650.png"
      }
    },
    {
      "id": 3,
      "title": {
        "es": "Todo listo para empezar",
        "en": "All ready to start",
        "fr": "Tout est pr\u00eat"
      },
      "description": {
        "es": "Transforma tu manera de trabajar desde hoy mismo.",
        "en": "Transform the way you work starting today.",
        "fr": "Transformez votre fa\u00e7on de travailler d\u00e8s aujourd'hui."
      },
      "image_url": {
        "es": "https://cdn-icons-png.flaticon.com/512/1533/1533913.png",
        "en": "https://cdn-icons-png.flaticon.com/512/1533/1533913.png",
        "fr": "https://cdn-icons-png.flaticon.com/512/1533/1533913.png"
      }
    }
  ]
}
"""
