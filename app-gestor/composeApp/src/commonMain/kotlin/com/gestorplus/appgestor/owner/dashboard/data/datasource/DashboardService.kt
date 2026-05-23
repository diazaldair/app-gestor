package com.gestorplus.appgestor.owner.dashboard.data.datasource

import com.gestorplus.appgestor.data.datasource.FirebaseManager

class DashboardService(private val firebaseManager: FirebaseManager) {
    suspend fun getBookings(date: Int): Map<String, Any>? {
        return firebaseManager.getData("bookings/$date") as? Map<String, Any>
    }

    suspend fun updateBooking(date: Int, slot: String, value: String) {
        firebaseManager.saveData("bookings/$date/$slot", value)
    }

    suspend fun getLogs(): List<Any>? {
        return firebaseManager.getData("logs") as? List<Any>
    }

    suspend fun getData(path: String): Any? {
        return firebaseManager.getData(path)
    }

    suspend fun saveData(path: String, value: String) {
        firebaseManager.saveData(path, value)
    }

    suspend fun getFirebaseLogs(path: String): List<String> {
        return firebaseManager.getFirebaseLogs(path)
    }
}
