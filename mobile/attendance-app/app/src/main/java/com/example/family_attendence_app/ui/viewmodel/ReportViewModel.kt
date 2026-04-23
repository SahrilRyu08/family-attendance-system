package com.example.family_attendence_app.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.family_attendence_app.network.models.ParticipantDto
import com.example.family_attendence_app.network.models.StatsDto
import com.example.family_attendence_app.network.repository.AttendanceRepository
import com.example.family_attendence_app.ui.util.UiState
import com.example.family_attendence_app.ui.util.UiState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val repository = AttendanceRepository()
    private val eventId: Long = savedStateHandle.get<Long>("eventId") ?: throw IllegalArgumentException("Event ID required")

    // Report list
    private val _reportState = MutableStateFlow<UiState<List<ParticipantDto>>>(Loading)
    val reportState: StateFlow<UiState<List<ParticipantDto>>> = _reportState.asStateFlow()

    // Stats
    private val _statsState = MutableStateFlow<UiState<StatsDto>>(Loading)
    val statsState: StateFlow<UiState<StatsDto>> = _statsState.asStateFlow()

    // Filter status
    private val _filterStatus = MutableStateFlow<String?>(null)
    val filterStatus: StateFlow<String?> = _filterStatus.asStateFlow()

    init {
        loadReport()
        loadStats()
    }

    fun loadReport() {
        viewModelScope.launch {
            _reportState.value = Loading
            runCatching { repository.fetchReport(eventId, _filterStatus.value) }
                .onSuccess { _reportState.value = Success(it) }
                .onFailure { _reportState.value =
                    Error(it.localizedMessage ?: "Gagal memuat report")
                }
        }
    }

    fun loadStats() {
        viewModelScope.launch {
            _statsState.value = Loading
            runCatching { repository.fetchStats(eventId) }
                .onSuccess { _statsState.value = Success(it) }
                .onFailure { _statsState.value =
                    Error(it.localizedMessage ?: "Gagal memuat statistik")
                }
        }
    }

    fun updateFilter(status: String?) {
        _filterStatus.value = status
        loadReport()
    }

    fun refresh() {
        loadReport()
        loadStats()
    }

    fun getEventId(): Long = eventId
}