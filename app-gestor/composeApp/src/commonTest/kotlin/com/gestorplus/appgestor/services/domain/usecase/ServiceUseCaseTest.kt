package com.gestorplus.appgestor.services.domain.usecase

import com.gestorplus.appgestor.services.domain.model.ServiceModel
import com.gestorplus.appgestor.services.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ServiceUseCaseTest {

    private lateinit var fakeRepository: FakeServiceRepository
    private lateinit var getServicesUseCase: GetServicesUseCase
    private lateinit var toggleServiceUseCase: ToggleServiceUseCase

    @BeforeTest
    fun setup() {
        fakeRepository = FakeServiceRepository()
        getServicesUseCase = GetServicesUseCase(fakeRepository)
        toggleServiceUseCase = ToggleServiceUseCase(fakeRepository)
    }

    @Test
    fun `GetServicesUseCase returns flow from repository`() = runTest {
        val services = listOf(ServiceModel(id = "1", name = "Test"))
        fakeRepository.emit(services)

        val result = getServicesUseCase().first()

        assertEquals(services, result)
    }

    @Test
    fun `ToggleServiceUseCase calls repository`() = runTest {
        toggleServiceUseCase("1", true)
        assertEquals("1", fakeRepository.lastToggledId)
        assertEquals(true, fakeRepository.lastToggledValue)
    }

    @Test
    fun `GetServicesUseCase handles empty list`() = runTest {
        fakeRepository.emit(emptyList())
        val result = getServicesUseCase().first()
        assertEquals(0, result.size)
    }

    class FakeServiceRepository : ServiceRepository {
        private val _flow = MutableStateFlow<List<ServiceModel>>(emptyList())
        var lastToggledId: String? = null
        var lastToggledValue: Boolean? = null

        fun emit(list: List<ServiceModel>) { _flow.value = list }
        override fun getServices(): Flow<List<ServiceModel>> = _flow
        override suspend fun getServiceById(id: String): ServiceModel? = null
        override suspend fun saveService(service: ServiceModel): Result<Unit> = Result.success(Unit)
        override suspend fun toggleService(serviceId: String, isActive: Boolean) {
            lastToggledId = serviceId
            lastToggledValue = isActive
        }
        override suspend fun deleteService(serviceId: String) {}
    }
}
