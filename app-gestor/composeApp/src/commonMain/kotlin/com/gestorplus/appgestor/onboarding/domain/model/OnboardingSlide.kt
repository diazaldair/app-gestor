package com.gestorplus.appgestor.onboarding.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingSlide(
    val id: Int,
    val title: Map<String, String>,
    val description: Map<String, String>,
    @SerialName("image_url")
    val imageUrl: Map<String, String>
)

@Serializable
data class OnboardingConfig(
    @SerialName("onboarding_config")
    val slides: List<OnboardingSlide>
)
