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
        "es": "Tu Consultorio Digital",
        "en": "Your Digital Clinic"
      },
      "description": {
        "es": "Configura tu perfil profesional, especialidades y servicios de forma centralizada.",
        "en": "Configure your professional profile, specialties and services centrally."
      },
      "image_url": {
        "es": "https://cdn-icons-png.flaticon.com/512/3304/3304567.png",
        "en": "https://cdn-icons-png.flaticon.com/512/3304/3304567.png"
      }
    },
    {
      "id": 2,
      "title": {
        "es": "Gesti\u00f3n de Citas \u00c1gil",
        "en": "Agile Appointment Management"
      },
      "description": {
        "es": "Recibe solicitudes de reserva y organiza tu agenda diaria sin complicaciones.",
        "en": "Receive booking requests and organize your daily schedule without complications."
      },
      "image_url": {
        "es": "https://cdn-icons-png.flaticon.com/512/2693/2693507.png",
        "en": "https://cdn-icons-png.flaticon.com/512/2693/2693507.png"
      }
    },
    {
      "id": 3,
      "title": {
        "es": "Todo Bajo Control",
        "en": "Everything Under Control"
      },
      "description": {
        "es": "Tus datos siempre seguros y sincronizados para que nunca pierdas informaci\u00f3n importante.",
        "en": "Your data is always secure and synced so you never lose important information."
      },
      "image_url": {
        "es": "https://cdn-icons-png.flaticon.com/512/1162/1162951.png",
        "en": "https://cdn-icons-png.flaticon.com/512/1162/1162951.png"
      }
    }
  ]
}
"""
    }
}
