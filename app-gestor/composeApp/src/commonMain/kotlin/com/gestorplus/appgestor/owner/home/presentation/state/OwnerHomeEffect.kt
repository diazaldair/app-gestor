package com.gestorplus.appgestor.owner.home.presentation.state

sealed class OwnerHomeEffect {
    data object NavigateToServiceCatalog : OwnerHomeEffect()
    data object NavigateToShiftConfig : OwnerHomeEffect()
    data object NavigateToConsultation : OwnerHomeEffect()
    data object NavigateToAllAppointments : OwnerHomeEffect()
    data object NavigateToAddAppointment : OwnerHomeEffect()
    data class NavigateToChat(val appointmentId: String) : OwnerHomeEffect()
    data class ShowError(val message: String) : OwnerHomeEffect()
}
