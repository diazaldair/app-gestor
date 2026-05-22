package com.gestorplus.appgestor.owner.dashboard.data.datasource.datasource

import com.gestorplus.appgestor.owner.dashboard.data.datasource.service.DashboardService

class DashboardRemoteDatasource(private val dashboardService: DashboardService) {
    suspend fun getBookings(date: Int): Map<String, Any>? {
        return dashboardService.getBookings(date)
    }

    suspend fun updateBooking(date: Int, slot: String, value: String) {
        dashboardService.updateBooking(date, slot, value)
    }

    suspend fun getLogs(): List<Any>? {
        return dashboardService.getLogs()
    }

    suspend fun getFirebaseLogs(path: String): List<String> {
        return dashboardService.getFirebaseLogs(path)
    }

    suspend fun getData(path: String): Any? {
        return dashboardService.getData(path)
    }

    suspend fun saveData(path: String, value: String) {
        dashboardService.saveData(path, value)
    }
}
