package com.gestorplus.appgestor.services.domain.repository

import com.gestorplus.appgestor.services.domain.model.Service
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {
    fun getServices(): Flow<List<Service>>
}
