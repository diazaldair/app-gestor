package com.gestorplus.appgestor.owner.home.presentation.state

sealed class OwnerHomeEvent {
    data object OnRefresh : OwnerHomeEvent()
    data object OnServiceCatalogClicked : OwnerHomeEvent()
    data object OnShiftConfigClicked : OwnerHomeEvent()
    data object OnStartConsultationClicked : OwnerHomeEvent()
    data object OnViewAllClicked : OwnerHomeEvent()
    data object OnAddAppointmentClicked : OwnerHomeEvent()
    data class OnChatClicked(val appointmentId: String) : OwnerHomeEvent()
}
