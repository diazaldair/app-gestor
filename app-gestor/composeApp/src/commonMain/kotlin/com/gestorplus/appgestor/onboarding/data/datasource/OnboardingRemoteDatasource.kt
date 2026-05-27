package com.gestorplus.appgestor.onboarding.data.datasource

class OnboardingRemoteDatasource(private val service: OnboardingService) {
    suspend fun fetchAndActivate() {
        service.fetchAndActivate()
    }
    
    fun getOnboardingConfig(): String {
        return service.getOnboardingConfig()
    }
}
