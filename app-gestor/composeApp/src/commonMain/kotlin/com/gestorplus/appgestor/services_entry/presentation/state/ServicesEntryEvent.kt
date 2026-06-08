package com.gestorplus.appgestor.services_entry.presentation.state

sealed interface ServicesEntryEvent {
    data object LoadData : ServicesEntryEvent
    data object OnCatalogClick : ServicesEntryEvent
    data object OnTurnsClick : ServicesEntryEvent
}
