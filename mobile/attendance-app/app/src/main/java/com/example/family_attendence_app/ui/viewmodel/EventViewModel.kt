package com.example.family_attendence_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.family_attendence_app.network.models.EventDto
import com.example.family_attendence_app.network.repository.AttendanceRepository
import com.example.family_attendence_app.ui.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {

    private val repository = AttendanceRepository()

    private val _eventsState = MutableStateFlow<UiState<List<EventDto>>>(UiState.Loading)
    val eventsState: StateFlow<UiState<List<EventDto>>> = _eventsState.asStateFlow()

    fun loadEvents() {
        viewModelScope.launch {
            _eventsState.value = UiState.Loading
            runCatching { repository.fetchEvents() }
                .onSuccess { _eventsState.value = UiState.Success(it) }
                .onFailure { _eventsState.value = UiState.Error(it.localizedMessage ?: "Gagal memuat event") }
        }
    }
}