package com.gestorplus.appgestor.booking.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class BookingUiState(
    val selectedDate: Int = 5,
    val selectedMonth: String = "October 2023",
    val selectedTimeSlot: String? = null,
    val timeSlotsMorning: List<String> = emptyList(),
    val timeSlotsAfternoon: List<String> = emptyList(),
    val isLoading: Boolean = false,
    
    // New fields for confirmation
    val doctorName: String = "Dr. Alejandro Mendoza",
    val doctorSpecialty: String = "CARDIÓLOGO ESPECIALISTA",
    val serviceName: String = "Consulta de Cardiología General",
    val serviceCategory: String = "PREMIUM SERVICE",
    val additionalNotes: String = "",
    val priceItems: List<PriceItem> = listOf(
        PriceItem("Consulta Médica", 80.0),
        PriceItem("Electrocardiograma (ECG)", 45.0)
    )
) {
    val totalPrice: Double get() = priceItems.sumOf { it.amount }
}

data class PriceItem(
    val description: String,
    val amount: Double
)
