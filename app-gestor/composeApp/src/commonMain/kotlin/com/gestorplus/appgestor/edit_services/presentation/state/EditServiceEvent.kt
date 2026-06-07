package com.gestorplus.appgestor.edit_services.presentation.state

sealed interface EditServiceEvent {
    data class NameChanged(val name: String) : EditServiceEvent
    data class CategoryChanged(val category: String) : EditServiceEvent
    data class DescriptionChanged(val description: String) : EditServiceEvent
    data class PriceChanged(val price: String) : EditServiceEvent
    data object SaveService : EditServiceEvent
    data object BackClicked : EditServiceEvent
}
