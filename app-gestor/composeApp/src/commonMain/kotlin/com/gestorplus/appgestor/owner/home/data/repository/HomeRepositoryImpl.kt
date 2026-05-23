package com.gestorplus.appgestor.owner.home.data.repository

import com.gestorplus.appgestor.owner.home.data.datasource.HomeLocalDatasource
import com.gestorplus.appgestor.owner.home.domain.model.HomeData
import com.gestorplus.appgestor.owner.home.domain.repository.HomeRepository

class HomeRepositoryImpl(
    private val localDatasource: HomeLocalDatasource
) : HomeRepository {

    override suspend fun getHomeData(): Result<HomeData> {
        return try {
            val homeData = HomeData(
                doctorName = localDatasource.getDoctorName(),
                totalAppointments = localDatasource.getTotalAppointments(),
                pendingAppointments = localDatasource.getPendingAppointments(),
                nextAppointment = localDatasource.getNextAppointment(),
                restOfDayAppointments = localDatasource.getRestOfDayAppointments()
            )
            Result.success(homeData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
