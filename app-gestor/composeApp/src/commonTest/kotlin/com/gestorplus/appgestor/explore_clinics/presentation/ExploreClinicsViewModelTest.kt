package com.gestorplus.appgestor.explore_clinics.presentation

import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicDao
import com.gestorplus.appgestor.clinicProfile.data.local.entity.ClinicEntity
import com.gestorplus.appgestor.clinicProfile.domain.model.Clinic
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.explore_clinics.data.ExploreClinicsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreClinicsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ExploreClinicsViewModel
    private lateinit var fakeRepository: FakeExploreClinicsRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeExploreClinicsRepository()
        viewModel = ExploreClinicsViewModel(fakeRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load populates clinics and specialties`() = runTest {
        val clinics = listOf(
            Clinic("1", "Clinic A", "Addr 1", listOf("Dentist"), null, true, "Desc", 0.0),
            Clinic("2", "Clinic B", "Addr 2", listOf("Cardio"), null, true, "Desc", 0.0)
        )
        fakeRepository.emit(clinics)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, viewModel.state.value.clinics.size)
        assertEquals(listOf("Cardio", "Dentist"), viewModel.state.value.specialties)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `onSearchQueryChanged filters clinics`() = runTest {
        val clinics = listOf(
            Clinic("1", "Clinic A", "Addr 1", listOf("Dentist"), null, true, "Desc", 0.0),
            Clinic("2", "Dental Clinic", "Addr 2", listOf("Cardio"), null, true, "Desc", 0.0)
        )
        fakeRepository.emit(clinics)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSearchQueryChanged("Dental")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.filteredClinics.size)
        assertEquals("Dental Clinic", viewModel.state.value.filteredClinics.first().name)
    }

    @Test
    fun `onSpecialtySelected filters clinics`() = runTest {
        val clinics = listOf(
            Clinic("1", "Clinic A", "Addr 1", listOf("Dentist"), null, true, "Desc", 0.0),
            Clinic("2", "Clinic B", "Addr 2", listOf("Cardio"), null, true, "Desc", 0.0)
        )
        fakeRepository.emit(clinics)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSpecialtySelected("Cardio")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.filteredClinics.size)
        assertEquals("Cardio", viewModel.state.value.filteredClinics.first().specialties.first())
    }

    @Test
    fun `onSpecialtySelected toggles selection`() = runTest {
        val clinics = listOf(
            Clinic("1", "Clinic A", "Addr 1", listOf("Dentist"), null, true, "Desc", 0.0)
        )
        fakeRepository.emit(clinics)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSpecialtySelected("Dentist")
        assertEquals("Dentist", viewModel.state.value.selectedSpecialty)

        viewModel.onSpecialtySelected("Dentist")
        assertEquals(null, viewModel.state.value.selectedSpecialty)
    }

    class FakeExploreClinicsRepository : ExploreClinicsRepositoryImpl(FirebaseManager(), FakeClinicDao()) {
        private val _clinics = MutableStateFlow<List<Clinic>>(emptyList())
        fun emit(list: List<Clinic>) { _clinics.value = list }
        override fun getClinics(): Flow<List<Clinic>> = _clinics
        override suspend fun syncClinics() {}
    }

    class FakeClinicDao : ClinicDao {
        override fun getAllClinics(): Flow<List<ClinicEntity>> = MutableStateFlow(emptyList())
        override suspend fun insertClinics(clinics: List<ClinicEntity>) {}
        override suspend fun getClinicById(id: String): ClinicEntity? = null
        override suspend fun clearAllClinics() {}
    }
}
