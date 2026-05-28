package com.gestorplus.appgestor.onboarding.data.repository

import com.gestorplus.appgestor.core.persistence.LocalPreferences
import com.gestorplus.appgestor.onboarding.data.datasource.OnboardingRemoteDatasource
import com.gestorplus.appgestor.onboarding.domain.model.OnboardingConfig
import com.gestorplus.appgestor.onboarding.domain.model.OnboardingSlide
import com.gestorplus.appgestor.onboarding.domain.repository.OnboardingRepository
import kotlinx.serialization.json.Json

class OnboardingRepositoryImpl(
    private val remoteDatasource: OnboardingRemoteDatasource,
    private val localPreferences: LocalPreferences
) : OnboardingRepository {

    private val jsonConfig = Json { ignoreUnknownKeys = true }

    override suspend fun getSlides(): List<OnboardingSlide> {
        remoteDatasource.fetchAndActivate()
        val jsonString = remoteDatasource.getOnboardingConfig()
        val configString = jsonString.ifBlank { FALLBACK_JSON }

        return try {
            val config = jsonConfig.decodeFromString<OnboardingConfig>(configString)
            config.slides
        } catch (e: Exception) {
            try {
                jsonConfig.decodeFromString<List<OnboardingSlide>>(configString)
            } catch (e2: Exception) {
                throw e
            }
        }
    }

    override fun isOnboardingCompleted(): Boolean {
        return localPreferences.getBoolean(ONBOARDING_COMPLETED_KEY, false)
    }

    override fun completeOnboarding() {
        localPreferences.putBoolean(ONBOARDING_COMPLETED_KEY, true)
    }

    companion object {
        private const val ONBOARDING_COMPLETED_KEY = "onboarding_completed"

        private const val FALLBACK_JSON = """
{
  "onboarding_config": [
    {
      "id": 1,
      "title": {
        "es": "\u00a1Organiza tu negocio!",
        "en": "Organize your business!",
        "fr": "Organisez votre entreprise !"
      },
      "description": {
        "es": "Gestiona tus proyectos y prioridades de forma sencilla con GestorPlus.",
        "en": "Easily manage projects and priorities with GestorPlus.",
        "fr": "G\u00e9rez facilement vos t\u00e2ches, projets et priorit\u00e9s avec GestorPlus."
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
    }
}
