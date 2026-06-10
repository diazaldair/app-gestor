package com.gestorplus.appgestor.booking.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.booking.domain.repository.BookingRepository
import com.gestorplus.appgestor.booking.presentation.state.BookingConfirmationEffect
import com.gestorplus.appgestor.booking.presentation.state.BookingConfirmationState
import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicDao
import com.gestorplus.appgestor.clinic_detail.data.local.dao.ClinicServiceDao
import com.gestorplus.appgestor.core.data.local.dao.BookingDraftDao
import com.gestorplus.appgestor.core.data.local.dao.UserProfileDao
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class BookingConfirmationViewModel(
    private val bookingRepository: BookingRepository,
    private val draftDao: BookingDraftDao,
    private val clinicDao: ClinicDao,
    private val serviceDao: ClinicServiceDao,
    private val userProfileDao: UserProfileDao
) : ViewModel() {

    private val _state = MutableStateFlow(BookingConfirmationState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BookingConfirmationEffect>()
    val effect = _effect.asSharedFlow()

    private val json = Json { ignoreUnknownKeys = true }

    init {
        loadConfirmationData()
    }

    private fun loadConfirmationData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val draft = draftDao.getDraft()
            if (draft != null) {
                val clinicEntity = clinicDao.getClinicById(draft.clinicId)
                
                serviceDao.getServicesByClinicId(draft.clinicId).collect { services ->
                    val targetService = services.find { it.id == draft.serviceId }
                    
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            date = draft.selectedDate,
                            month = draft.selectedMonth,
                            timeSlot = draft.selectedTimeSlot ?: "",
                            clinic = clinicEntity?.let {
                                com.gestorplus.appgestor.clinicProfile.domain.model.Clinic(
                                    id = it.id,
                                    name = it.name,
                                    address = it.address,
                                    specialties = try { json.decodeFromString(it.specialtiesJson) } catch (e: Exception) { emptyList() },
                                    imageUrl = it.imageUrl,
                                    isOpen = it.isOpen,
                                    description = it.description
                                )
                            },
                            service = targetService?.let {
                                com.gestorplus.appgestor.clinic_detail.domain.model.ClinicService(
                                    id = it.id,
                                    clinicId = it.clinicId,
                                    name = it.name,
                                    durationMinutes = it.durationMinutes,
                                    price = it.price,
                                    description = it.description
                                )
                            }
                        )
                    }
                }
            } else {
                _state.update { it.copy(isLoading = false, error = "No se encontró el borrador de la cita") }
            }
        }
    }

    fun onNotesChanged(notes: String) {
        _state.update { it.copy(notes = notes) }
    }

    fun onConfirmClicked() {
        viewModelScope.launch {
            val currentState = _state.value
            val clinic = currentState.clinic ?: return@launch
            val service = currentState.service ?: return@launch

            _state.update { it.copy(isLoading = true) }
            
            // Intentar obtener el nombre del paciente del perfil local
            val userProfile = userProfileDao.getProfile().firstOrNull()
            val patientName = userProfile?.name ?: "Paciente"

            val result = bookingRepository.confirmBooking(
                clinicId = clinic.id,
                serviceId = service.id,
                clinicName = clinic.name,
                serviceName = service.name,
                doctorName = clinic.name, 
                patientName = patientName,
                date = currentState.date,
                month = currentState.month,
                timeSlot = currentState.timeSlot,
                price = service.price,
                notes = currentState.notes
            )
            
            _state.update { it.copy(isLoading = false) }

            if (result.isSuccess) {
                draftDao.deleteDraft()
                _effect.emit(BookingConfirmationEffect.NavigateToHome)
            } else {
                _state.update { it.copy(error = "Error al confirmar la reserva") }
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _effect.emit(BookingConfirmationEffect.NavigateBack)
        }
    }
}
