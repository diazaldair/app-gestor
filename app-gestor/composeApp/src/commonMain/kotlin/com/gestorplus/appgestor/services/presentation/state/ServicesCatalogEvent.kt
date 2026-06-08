package com.gestorplus.appgestor.services.presentation.state

sealed interface ServicesCatalogEvent {
    data object LoadServices : ServicesCatalogEvent
    data class OnSearchQueryChanged(val query: String) : ServicesCatalogEvent
    data class OnToggleService(val serviceId: String, val isActive: Boolean) : ServicesCatalogEvent
    data class OnEditService(val serviceId: String) : ServicesCatalogEvent
}
