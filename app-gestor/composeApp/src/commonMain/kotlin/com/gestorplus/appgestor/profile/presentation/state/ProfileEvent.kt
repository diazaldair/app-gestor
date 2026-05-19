package com.gestorplus.appgestor.profile.presentation.state

sealed interface ProfileEvent {
    data class NameChanged(val value: String) : ProfileEvent
    data class EmailChanged(val value: String) : ProfileEvent
    data class PhoneChanged(val value: String) : ProfileEvent
    data class DescriptionChanged(val value: String) : ProfileEvent
    data class ImageUrlChanged(val value: String) : ProfileEvent
    
    data object ToggleEditMode : ProfileEvent
    data object SaveProfile : ProfileEvent
    data object LoadProfile : ProfileEvent
}
