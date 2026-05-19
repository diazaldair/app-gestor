package com.gestorplus.appgestor.owner.data.datasource.service

import com.gestorplus.appgestor.data.datasource.FirebaseManager

class OwnerService(private val firebaseManager: FirebaseManager) {
    suspend fun getBookings(date: Int): Map<String, Any>? {
        return firebaseManager.getData("bookings/$date") as? Map<String, Any>
    }

    suspend fun updateBooking(date: Int, slot: String, value: String) {
        firebaseManager.saveData("bookings/$date/$slot", value)
    }

    suspend fun getLogs(): List<Any>? {
        return firebaseManager.getData("logs") as? List<Any>
    }
}
