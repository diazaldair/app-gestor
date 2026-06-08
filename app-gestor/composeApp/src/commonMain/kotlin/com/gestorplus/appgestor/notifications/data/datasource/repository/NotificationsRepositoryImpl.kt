package com.gestorplus.appgestor.notifications.data.datasource.repository

import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.notifications.data.datasource.datasource.NotificationsLocalDatasource
import com.gestorplus.appgestor.notifications.data.datasource.datasource.NotificationsRemoteDatasource
import com.gestorplus.appgestor.notifications.data.datasource.mapper.NotificationMapper
import com.gestorplus.appgestor.notifications.domain.model.AppNotification
import com.gestorplus.appgestor.notifications.domain.repository.NotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotificationsRepositoryImpl(
    private val remoteDatasource: NotificationsRemoteDatasource,
    private val localDatasource: NotificationsLocalDatasource,
    private val mapper: NotificationMapper,
    private val firebaseManager: FirebaseManager
) : NotificationsRepository {

    override fun getNotifications(): Flow<List<AppNotification>> = flow {
        val uid = firebaseManager.getCurrentUserUid() ?: return@flow
        
        // Offline-First strategy
        val local = localDatasource.getCachedNotifications()
        if (local.isNotEmpty()) {
            emit(local.map { mapper.toDomain(it) })
        }

        try {
            val remote = remoteDatasource.getNotifications(uid)
            localDatasource.cacheNotifications(remote)
            emit(remote.map { mapper.toDomain(it) })
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun acceptAppointment(notificationId: String) {
        val uid = firebaseManager.getCurrentUserUid() ?: return
        remoteDatasource.acceptAppointment(uid, notificationId)
    }

    override suspend fun declineAppointment(notificationId: String) {
        val uid = firebaseManager.getCurrentUserUid() ?: return
        remoteDatasource.declineAppointment(uid, notificationId)
    }

    override suspend fun markAsRead(notificationId: String) {
        val uid = firebaseManager.getCurrentUserUid() ?: return
        remoteDatasource.markAsRead(uid, notificationId)
    }
}
