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
import com.gestorplus.appgestor.clinicProfile.presentation.screen.ClinicProfileScreen
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.ThemeMode
import com.gestorplus.appgestor.home.presentation.screen.HomeScreen
import com.gestorplus.appgestor.notifications.presentation.screen.NotificationsScreen
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
import com.gestorplus.appgestor.services.presentation.screen.ServicesCatalogScreen
import com.gestorplus.appgestor.services_entry.presentation.screen.ServicesEntryScreen
import com.gestorplus.appgestor.edit_services.presentation.screen.EditServiceScreen
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
    BusinessView,
    DoctorView,
    WorkingHours,
    ScheduleGroupDetail,
    Profile,
    ServicesEntry,
    ServicesCatalog,
    EditService,
    ClinicProfile,
    Notifications
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
    var editingServiceId by remember { mutableStateOf<String?>(null) }

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
                        currentScreen = Screen.ServicesEntry
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
                    onConfirm = { currentScreen = Screen.Home }
                )
            }

            Screen.BusinessView, Screen.DoctorView -> {
                OwnerDashboardScreen(
                    onBack = { currentScreen = Screen.Home },
                    onNavigateToWorkingHours = { currentScreen = Screen.WorkingHours },
                    onNavigateToProfile = { currentScreen = Screen.Profile },
                    onNavigateToServices = { currentScreen = Screen.ServicesEntry }
                )
            }

            Screen.WorkingHours -> {
                WorkingHoursScreen(
                    onBack = { currentScreen = Screen.ServicesEntry },
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

            Screen.ServicesEntry -> {
                ServicesEntryScreen(
                    viewModel = koinInject(),
                    onBack = { currentScreen = Screen.BusinessView },
                    onOpenMenu = { /* TODO */ },
                    onNavigateToCatalog = { currentScreen = Screen.ServicesCatalog },
                    onNavigateToTurns = { currentScreen = Screen.WorkingHours }
                )
            }

            Screen.ServicesCatalog -> {
                ServicesCatalogScreen(
                    onBack = { currentScreen = Screen.ServicesEntry },
                    onNavigateToEdit = { id -> 
                        editingServiceId = id
                        currentScreen = Screen.EditService 
                    },
                    onOpenMenu = { /* TODO */ }
                )
            }

            Screen.EditService -> {
                EditServiceScreen(
                    onBack = { currentScreen = Screen.ServicesCatalog },
                    serviceId = editingServiceId ?: "8"
                )
            }

            Screen.ClinicProfile -> {
                ClinicProfileScreen(
                    onNavigateBack = { currentScreen = Screen.Profile }
                )
            }

            Screen.Notifications -> {
                NotificationsScreen(
                    onBack = { currentScreen = Screen.Profile }
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
        "es": "¡Organiza tu negocio!",
        "en": "Organize your business!",
        "fr": "Organisez votre empresa !"
      },
      "description": {
        "es": "Gestiona tus proyectos y prioridades de forma sencilla con GestorPlus.",
        "en": "Easily manage projects and priorities with GestorPlus.",
        "fr": "Gérez facilement vos tâches, proyectos et priorités avec GestorPlus."
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
        "fr": "Travail d'équipe"
      },
      "description": {
        "es": "Colabora en tiempo real y mantén a todo tu equipo sincronizado.",
        "en": "Collaborate in real-time and keep your entire team in sync.",
        "fr": "Collaborez en temps réel et gardez toute votre équipe synchronisée."
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
        "fr": "Tout est prêt"
      },
      "description": {
        "es": "Transforma tu manera de trabajar desde hoy mismo.",
        "en": "Transform the way you work starting today.",
        "fr": "Transformez votre façon de travailler dès aujourd'hui."
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
