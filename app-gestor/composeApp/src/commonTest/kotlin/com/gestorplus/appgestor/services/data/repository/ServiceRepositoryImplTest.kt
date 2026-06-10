package com.gestorplus.appgestor.services.data.repository

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.services.data.local.dao.ServiceDao
import com.gestorplus.appgestor.services.data.local.entity.ServiceEntity
import com.gestorplus.appgestor.services.data.datasource.mapper.ServiceMapper
import com.gestorplus.appgestor.services.domain.model.ServiceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServiceRepositoryImplTest {

    private lateinit var repository: ServiceRepositoryImpl
    private lateinit var fakeDao: FakeServiceDao
    private lateinit var fakeFirebase: FakeFirebaseManager
    private val mapper = ServiceMapper()

    @BeforeTest
    fun setup() {
        fakeDao = FakeServiceDao()
        fakeFirebase = FakeFirebaseManager()
        repository = ServiceRepositoryImpl(fakeDao, mapper, fakeFirebase)
    }

    @Test
    fun `getServices returns mapped domain objects`() = runTest {
        val entity = ServiceEntity("1", "Service", "Cat", "Desc", 100.0, "Bs", 30, true, null)
        fakeDao.emit(listOf(entity))

        val result = repository.getServices().first()

        assertEquals(1, result.size)
        assertEquals("Service", result.first().name)
    }

    @Test
    fun `saveService saves to dao and firebase`() = runTest {
        val service = ServiceModel("1", "New Service", "Cat", "Desc", 150.0)
        
        repository.saveService(service)

        assertEquals(1, fakeDao.services.size)
        assertEquals("New Service", fakeDao.services.first().name)
        assertTrue(fakeFirebase.savedData.containsKey("workspaces/fake-uid/services/1"))
    }

    @Test
    fun `toggleService updates dao and firebase`() = runTest {
        repository.toggleService("1", false)

        assertEquals("1", fakeDao.lastStatusId)
        assertEquals(false, fakeDao.lastStatusValue)
        assertTrue(fakeFirebase.savedData.containsKey("workspaces/fake-uid/services/1/isActive"))
        assertEquals(false, fakeFirebase.savedData["workspaces/fake-uid/services/1/isActive"])
    }

    @Test
    fun `deleteService deletes from dao`() = runTest {
        repository.deleteService("1")
        assertEquals("1", fakeDao.deletedId)
    }

    class FakeServiceDao : ServiceDao {
        private val _flow = MutableStateFlow<List<ServiceEntity>>(emptyList())
        val services = mutableListOf<ServiceEntity>()
        var lastStatusId: String? = null
        var lastStatusValue: Boolean? = null
        var deletedId: String? = null

        fun emit(list: List<ServiceEntity>) { _flow.value = list }
        override fun getAllServices(): Flow<List<ServiceEntity>> = _flow
        override suspend fun getServiceById(id: String): ServiceEntity? = services.find { it.id == id }
        override suspend fun insertService(service: ServiceEntity) { services.add(service) }
        override suspend fun updateService(service: ServiceEntity) {}
        override suspend fun updateServiceStatus(id: String, isActive: Boolean) {
            lastStatusId = id
            lastStatusValue = isActive
        }
        override suspend fun deleteService(id: String) { deletedId = id }
    }

    class FakeFirebaseManager : FirebaseManager() {
        val savedData = mutableMapOf<String, Any>()
        override fun getCurrentUserUid(): String = "fake-uid"
        override suspend fun saveData(path: String, value: Any) {
            savedData[path] = value
        }
    }
}
