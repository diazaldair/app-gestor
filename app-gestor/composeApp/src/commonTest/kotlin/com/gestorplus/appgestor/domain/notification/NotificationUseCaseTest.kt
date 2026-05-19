package com.gestorplus.appgestor.domain.notification

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Fake implementation of NotificationRepository.
 * Validates that the Use Case correctly delegates the notification.
 */
class FakeNotificationRepository : NotificationRepository {
    var receivedNotification: PushNotification? = null
    var fcmToken: String? = "fake-token"

    override suspend fun getFCMToken(): String? = fcmToken

    override suspend fun handleReceivedNotification(notification: PushNotification) {
        receivedNotification = notification
    }
}

class NotificationUseCaseTest {

    private val fakeRepository = FakeNotificationRepository()
    private val handleNotificationUseCase = HandleNotificationUseCase(fakeRepository)

    @Test
    fun `invoke calls repository with correct notification`() = runTest {
        // Arrange
        val notification = PushNotification(
            id = "test-id",
            title = "Test Title",
            body = "Test Body",
            data = mapOf("key" to "value")
        )

        // Act
        handleNotificationUseCase(notification)

        // Assert
        assertEquals(notification, fakeRepository.receivedNotification)
        assertEquals("test-id", fakeRepository.receivedNotification?.id)
    }

    @Test
    fun `repository stores the last received notification`() = runTest {
        // Arrange
        val notification1 = PushNotification("1", "T1", "B1", emptyMap())
        val notification2 = PushNotification("2", "T2", "B2", emptyMap())

        // Act
        handleNotificationUseCase(notification1)
        handleNotificationUseCase(notification2)

        // Assert
        assertEquals(notification2, fakeRepository.receivedNotification)
        assertEquals("2", fakeRepository.receivedNotification?.id)
    }
}
