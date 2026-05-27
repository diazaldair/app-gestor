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
        remoteDatasource.fetchAndActivate() // Aseguramos que tenemos la última info de Firebase
        val jsonString = remoteDatasource.getOnboardingConfig()
        val configString = if (jsonString.isNotEmpty()) jsonString else FALLBACK_JSON
        return try {
            val config = jsonConfig.decodeFromString<OnboardingConfig>(configString)
            config.slides
        } catch (e: Exception) {
            try {
                // If the user uploaded just the array instead of the object wrapper
                jsonConfig.decodeFromString<List<OnboardingSlide>>(configString)
            } catch (e2: Exception) {
                throw e // throw original error to see it on screen
            }
        }
    }

    override fun isOnboardingCompleted(): Boolean {
        return localPreferences.getBoolean("onboarding_completed", false)
    }

    override fun completeOnboarding() {
        localPreferences.putBoolean("onboarding_completed", true)
    }

    companion object {
        private const val FALLBACK_JSON = """
        {
          "onboarding_config": [
            {
              "id": 1,
              "title": { "es": "¡Organiza tu negocio!", "en": "Organize your business!", "fr": "Organisez votre entreprise !" },
              "description": { "es": "Gestiona tus proyectos y prioridades de forma sencilla.", "en": "Easily manage projects and priorities.", "fr": "Gérez facilement vos projets." },
              "image_url": { "es": "https://cdn-icons-png.flaticon.com/512/2620/2620667.png", "en": "https://cdn-icons-png.flaticon.com/512/2620/2620667.png", "fr": "https://cdn-icons-png.flaticon.com/512/2620/2620667.png" }
            },
            {
              "id": 2,
              "title": { "es": "Trabaja en equipo", "en": "Teamwork", "fr": "Travail d'équipe" },
              "description": { "es": "Colabora en tiempo real con todo tu equipo.", "en": "Collaborate in real-time with your team.", "fr": "Collaborez en temps réel avec votre équipe." },
              "image_url": { "es": "https://cdn-icons-png.flaticon.com/512/1256/1256650.png", "en": "https://cdn-icons-png.flaticon.com/512/1256/1256650.png", "fr": "https://cdn-icons-png.flaticon.com/512/1256/1256650.png" }
            },
            {
              "id": 3,
              "title": { "es": "Todo listo", "en": "All ready", "fr": "Tout est prêt" },
              "description": { "es": "Transforma tu manera de trabajar hoy mismo.", "en": "Transform the way you work today.", "fr": "Transformez votre façon de travailler." },
              "image_url": { "es": "https://cdn-icons-png.flaticon.com/512/1533/1533913.png", "en": "https://cdn-icons-png.flaticon.com/512/1533/1533913.png", "fr": "https://cdn-icons-png.flaticon.com/512/1533/1533913.png" }
            }
          ]
        }
        """
    }
}
