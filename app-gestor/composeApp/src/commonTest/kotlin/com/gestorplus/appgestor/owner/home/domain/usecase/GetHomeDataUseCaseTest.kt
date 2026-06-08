package com.gestorplus.appgestor.owner.home.domain.usecase

import com.gestorplus.appgestor.owner.home.domain.model.HomeData
import com.gestorplus.appgestor.owner.home.domain.model.Appointment
import com.gestorplus.appgestor.owner.home.domain.repository.HomeRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Fake repository for testing GetHomeDataUseCase.
 */
class FakeHomeRepository : HomeRepository {
    var result: Result<HomeData> = Result.failure(Exception("Not initialized"))

    override suspend fun getHomeData(): Result<HomeData> = result
}

class GetHomeDataUseCaseTest {

    private val fakeRepo = FakeHomeRepository()
    private val useCase = GetHomeDataUseCase(fakeRepo)

    @Test
    fun `when repository returns success, use case returns success with data`() = runTest {
        val expectedData = HomeData(
            doctorName = "Dr. House",
            totalAppointments = 10,
            pendingAppointments = 5,
            nextAppointment = Appointment("1", "Client", "Service", "10:00", "15 min"),
            restOfDayAppointments = emptyList()
        )
        fakeRepo.result = Result.success(expectedData)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(expectedData, result.getOrNull())
    }

    @Test
    fun `when repository returns failure, use case returns failure`() = runTest {
        val error = Exception("Network error")
        fakeRepo.result = Result.failure(error)

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }
}
