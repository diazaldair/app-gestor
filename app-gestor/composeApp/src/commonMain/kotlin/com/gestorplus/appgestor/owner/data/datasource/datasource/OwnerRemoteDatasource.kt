package com.gestorplus.appgestor.owner.data.datasource.datasource

import com.gestorplus.appgestor.owner.data.datasource.service.OwnerService

class OwnerRemoteDatasource(private val ownerService: OwnerService) {
    suspend fun getBookings(date: Int): Map<String, Any>? {
        return ownerService.getBookings(date)
    }

    suspend fun updateBooking(date: Int, slot: String, value: String) {
        ownerService.updateBooking(date, slot, value)
    }

    suspend fun getLogs(): List<Any>? {
        return ownerService.getLogs()
    }

    suspend fun saveWorkspaceProfile(uid: String, data: String) {
        ownerService.saveWorkspaceProfile(uid, data)
    }
}
