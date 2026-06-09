package com.gestorplus.appgestor.explore_clinics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.explore_clinics.data.ExploreClinicsRepositoryImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExploreClinicsViewModel(
    private val repository: ExploreClinicsRepositoryImpl
) : ViewModel() {

    private val _state = MutableStateFlow(ExploreClinicsState())
    val state: StateFlow<ExploreClinicsState> = _state.asStateFlow()

    init {
        loadClinics()
        syncData()
    }

    private fun loadClinics() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getClinics().collect { clinics ->
                val allSpecialties = clinics.flatMap { it.specialties }.distinct().sorted()
                _state.update { 
                    it.copy(
                        clinics = clinics,
                        specialties = allSpecialties,
                        isLoading = false
                    )
                }
                applyFilters()
            }
        }
    }

    private fun syncData() {
        viewModelScope.launch {
            repository.syncClinics()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onSpecialtySelected(specialty: String?) {
        _state.update { 
            it.copy(selectedSpecialty = if (it.selectedSpecialty == specialty) null else specialty)
        }
        applyFilters()
    }

    private fun applyFilters() {
        val currentState = _state.value
        val filtered = currentState.clinics.filter { clinic ->
            val matchesSearch = clinic.name.contains(currentState.searchQuery, ignoreCase = true) ||
                    clinic.specialties.any { it.contains(currentState.searchQuery, ignoreCase = true) }
            
            val matchesSpecialty = currentState.selectedSpecialty == null || 
                    clinic.specialties.contains(currentState.selectedSpecialty)
            
            matchesSearch && matchesSpecialty
        }
        _state.update { it.copy(filteredClinics = filtered) }
    }
}
