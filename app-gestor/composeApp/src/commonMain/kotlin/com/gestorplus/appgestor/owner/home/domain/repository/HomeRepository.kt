package com.gestorplus.appgestor.owner.home.domain.repository

import com.gestorplus.appgestor.owner.home.domain.model.HomeData

interface HomeRepository {
    suspend fun getHomeData(): Result<HomeData>
}
