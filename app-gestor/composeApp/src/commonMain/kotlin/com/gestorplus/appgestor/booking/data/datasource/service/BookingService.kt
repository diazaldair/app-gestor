package com.gestorplus.appgestor.booking.data.datasource.service

import com.gestorplus.appgestor.data.datasource.FirebaseManager

class BookingService(private val firebaseManager: FirebaseManager) {
    suspend fun getAvailableSlots(date: Int): Map<String, Any>? {
        return firebaseManager.getData("available_slots/$date") as? Map<String, Any>
    }

    suspend fun saveBooking(date: Int, slot: String, value: Any) {
        firebaseManager.saveData("bookings/$date/$slot", value)
    }
}
