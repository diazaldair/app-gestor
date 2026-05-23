package com.gestorplus.appgestor.owner.home.domain.usecase

import com.gestorplus.appgestor.owner.home.domain.model.HomeData
import com.gestorplus.appgestor.owner.home.domain.repository.HomeRepository

class GetHomeDataUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<HomeData> {
        return homeRepository.getHomeData()
    }
}
