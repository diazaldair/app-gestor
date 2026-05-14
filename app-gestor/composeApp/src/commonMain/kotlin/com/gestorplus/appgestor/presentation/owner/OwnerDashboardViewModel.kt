package com.gestorplus.appgestor.presentation.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import com.gestorplus.appgestor.data.repository.OwnerBookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.gestorplus.appgestor.core.util.DateTimeUtils
import kotlinx.datetime.*

class OwnerDashboardViewModel(
    private val repository: OwnerBookingRepository,
    private val firebaseManager: FirebaseManager
) : ViewModel() {

    private val _firebaseLogs = MutableStateFlow<List<String>>(emptyList())
    val firebaseLogs = _firebaseLogs.asStateFlow()

    private val _isLogsLoading = MutableStateFlow(false)
    val isLogsLoading = _isLogsLoading.asStateFlow()

    // Estado para el calendario: por defecto hoy
    private val _selectedDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val selectedDate = _selectedDate.asStateFlow()

    init {
        viewModelScope.launch {
            repository.syncAllBookings()
        }
    }

    // Escuchamos los cambios en Room y filtramos por fecha seleccionada
    val bookings: StateFlow<List<BookingEntity>> = repository.getBookings()
        .combine(_selectedDate) { allBookings, date ->
            allBookings.filter { booking ->
                val bInstant = Instant.fromEpochMilliseconds(booking.timestamp)
                val bDate = bInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                bDate == date
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onDateSelected(day: Int) {
        val current = _selectedDate.value
        _selectedDate.value = LocalDate(current.year, current.month, day)
    }

    fun onMonthChange(increment: Int) {
        val current = _selectedDate.value
        // Lógica simple para cambiar de mes
        _selectedDate.value = if (increment > 0) {
            current.plus(1, DateTimeUnit.MONTH)
        } else {
            current.minus(1, DateTimeUnit.MONTH)
        }
    }

    fun onAcceptBooking(bookingId: String) {
        viewModelScope.launch {
            repository.updateStatus(bookingId, "CONFIRMED")
        }
    }

    fun onRejectBooking(bookingId: String) {
        viewModelScope.launch {
            repository.updateStatus(bookingId, "REJECTED")
        }
    }

    fun loadFirebaseLogs() {
        viewModelScope.launch {
            _isLogsLoading.value = true
            val logs = firebaseManager.getFirebaseLogs("app_logs")
            _firebaseLogs.value = logs.reversed() // Los más recientes primero
            _isLogsLoading.value = false
        }
    }
}
