package com.gestorplus.appgestor.presentation.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.domain.owner.model.Booking
import com.gestorplus.appgestor.domain.owner.usecase.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.gestorplus.appgestor.core.util.DateTimeUtils
import kotlinx.datetime.*

class OwnerDashboardViewModel(
    private val getOwnerBookingsUseCase: GetOwnerBookingsUseCase,
    private val acceptBookingUseCase: AcceptBookingUseCase,
    private val rejectBookingUseCase: RejectBookingUseCase,
    private val syncBookingsUseCase: SyncBookingsUseCase,
    private val getFirebaseLogsUseCase: GetFirebaseLogsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OwnerDashboardState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OwnerDashboardEffect>()
    val effect = _effect.asSharedFlow()

    // Estado para el calendario: por defecto hoy
    private val _selectedDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val selectedDate = _selectedDate.asStateFlow()

    // Escuchamos los cambios en Room y filtramos por fecha seleccionada
    val bookings: StateFlow<List<Booking>> = getOwnerBookingsUseCase()
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

    init {
        observeBookings()
        refreshBookings()
    }

    private fun observeBookings() {
        getOwnerBookingsUseCase()
            .onEach { bookings ->
                _state.update { it.copy(bookings = bookings) }
            }
            .launchIn(viewModelScope)
    }

    private fun refreshBookings() {
        viewModelScope.launch {
            _state.update { it.copy(isSyncing = true) }
            try {
                syncBookingsUseCase()
            } catch (e: Exception) {
                _effect.emit(OwnerDashboardEffect.ShowSnackbar("Error syncing bookings"))
            } finally {
                _state.update { it.copy(isSyncing = false) }
            }
        }
    }

    fun onEvent(event: OwnerDashboardEvent) {
        when (event) {
            is OwnerDashboardEvent.OnAcceptBooking -> acceptBooking(event.bookingId)
            is OwnerDashboardEvent.OnRejectBooking -> rejectBooking(event.bookingId)
            is OwnerDashboardEvent.OnDateSelected -> onDateSelected(event.day)
            is OwnerDashboardEvent.OnMonthChange -> onMonthChange(event.increment)
            OwnerDashboardEvent.OnLoadLogs -> loadFirebaseLogs()
            OwnerDashboardEvent.OnRefreshBookings -> refreshBookings()
            OwnerDashboardEvent.OnClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun onDateSelected(day: Int) {
        val current = _selectedDate.value
        _selectedDate.value = LocalDate(current.year, current.month, day)
    }

    private fun onMonthChange(increment: Int) {
        val current = _selectedDate.value
        // Lógica simple para cambiar de mes
        _selectedDate.value = if (increment > 0) {
            current.plus(1, DateTimeUnit.MONTH)
        } else {
            current.minus(1, DateTimeUnit.MONTH)
        }
    }

    private fun acceptBooking(id: String) {
        viewModelScope.launch {
            acceptBookingUseCase(id)
            _effect.emit(OwnerDashboardEffect.ShowSnackbar("Booking accepted"))
        }
    }

    private fun rejectBooking(id: String) {
        viewModelScope.launch {
            rejectBookingUseCase(id)
            _effect.emit(OwnerDashboardEffect.ShowSnackbar("Booking rejected"))
        }
    }

    private fun loadFirebaseLogs() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingLogs = true) }
            try {
                val logs = getFirebaseLogsUseCase("app_logs")
                _state.update { it.copy(firebaseLogs = logs.reversed(), isLoadingLogs = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoadingLogs = false) }
                _effect.emit(OwnerDashboardEffect.ShowSnackbar("Error loading logs"))
            }
        }
    }
}
