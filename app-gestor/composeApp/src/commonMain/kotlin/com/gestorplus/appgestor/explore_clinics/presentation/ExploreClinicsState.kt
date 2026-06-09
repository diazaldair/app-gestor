package com.gestorplus.appgestor.explore_clinics.presentation

import com.gestorplus.appgestor.clinicProfile.domain.model.Clinic

data class ExploreClinicsState(
    val clinics: List<Clinic> = emptyList(),
    val filteredClinics: List<Clinic> = emptyList(),
    val specialties: List<String> = emptyList(),
    val selectedSpecialty: String? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
