package com.gestorplus.appgestor.notifications.presentation

import com.gestorplus.appgestor.notifications.domain.model.AppNotification
import com.gestorplus.appgestor.notifications.domain.model.NotificationType
import com.gestorplus.appgestor.notifications.domain.repository.NotificationsRepository
import com.gestorplus.appgestor.notifications.domain.usecase.AcceptAppointmentUseCase
import com.gestorplus.appgestor.notifications.domain.usecase.DeclineAppointmentUseCase
import com.gestorplus.appgestor.notifications.domain.usecase.GetNotificationsUseCase
import com.gestorplus.appgestor.notifications.presentation.state.NotificationsEfffect
import com.gestorplus.appgestor.notifications.presentation.state.NotificationsEvent
import com.gestorplus.appgestor.notifications.presentation.viewmodel.NotificationsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    
    private lateinit var viewModel: NotificationsViewModel
    private lateinit var fakeRepository: FakeNotificationsRepository
    
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeNotificationsRepository()
        viewModel = NotificationsViewModel(
            GetNotificationsUseCase(fakeRepository),
            AcceptAppointmentUseCase(fakeRepository),
            DeclineAppointmentUseCase(fakeRepository)
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        assertTrue(viewModel.state.value.isLoading)
    }

    @Test
    fun `observing notifications updates state`() = runTest {
        val notifications = listOf(
            AppNotification("1", "Title", "Desc", 0L, NotificationType.APPOINTMENT_REQUEST, "TODAY")
        )
        fakeRepository.emit(notifications)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(notifications, viewModel.state.value.notifications)
    }

    @Test
    fun `handling accept event emits effect`() = runTest {
        viewModel.onEvent(NotificationsEvent.Accept("1"))
        testDispatcher.scheduler.advanceUntilIdle()
        
        val effect = viewModel.effect.first()
        assertTrue(effect is NotificationsEfffect.ShowMessage)
        assertEquals("Solicitud aceptada", (effect as NotificationsEfffect.ShowMessage).message)
        assertTrue(fakeRepository.acceptedIds.contains("1"))
    }

    @Test
    fun `handling decline event emits effect`() = runTest {
        viewModel.onEvent(NotificationsEvent.Decline("1"))
        testDispatcher.scheduler.advanceUntilIdle()
        
        val effect = viewModel.effect.first()
        assertTrue(effect is NotificationsEfffect.ShowMessage)
        assertEquals("Solicitud rechazada", (effect as NotificationsEfffect.ShowMessage).message)
        assertTrue(fakeRepository.declinedIds.contains("1"))
    }

    class FakeNotificationsRepository : NotificationsRepository {
        private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
        val acceptedIds = mutableListOf<String>()
        val declinedIds = mutableListOf<String>()

        fun emit(list: List<AppNotification>) {
            _notifications.value = list
        }

        override fun getNotifications(): Flow<List<AppNotification>> = _notifications

        override suspend fun acceptAppointment(notificationId: String) {
            acceptedIds.add(notificationId)
        }

        override suspend fun declineAppointment(notificationId: String) {
            declinedIds.add(notificationId)
        }

        override suspend fun markAsRead(notificationId: String) {}
    }
}
