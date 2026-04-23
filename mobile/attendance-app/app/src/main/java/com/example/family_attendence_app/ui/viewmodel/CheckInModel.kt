package com.example.family_attendence_app.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.family_attendence_app.network.models.ParticipantDto
import com.example.family_attendence_app.network.repository.AttendanceRepository
import com.example.family_attendence_app.ui.util.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckInViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val repository = AttendanceRepository()
    private val eventId: Long = savedStateHandle.get<Long>("eventId") ?: throw IllegalArgumentException("Event ID required")

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _suggestions = MutableStateFlow<List<ParticipantDto>>(emptyList())
    val suggestions: StateFlow<List<ParticipantDto>> = _suggestions.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Check-in result
    private val _checkInState = MutableStateFlow<UiState<ParticipantDto>?>(null)
    val checkInState: StateFlow<UiState<ParticipantDto>?> = _checkInState.asStateFlow()

    private var searchJob: Job? = null

    // ✅ Update search query dengan debounce
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()

        if (query.length < 2) {
            _suggestions.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            kotlinx.coroutines.delay(300) // Debounce 300ms
            _isSearching.value = true
            performSearch(query)
            _isSearching.value = false
        }
    }

    private suspend fun performSearch(query: String) {
        runCatching { repository.searchParticipants(eventId, query) }
            .onSuccess { _suggestions.value = it }
            .onFailure { _suggestions.value = emptyList() }
    }

    // ✅ Select suggestion & auto-fill
    fun selectSuggestion(participant: ParticipantDto) {
        _searchQuery.value = participant.name
        _suggestions.value = emptyList()
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }

    // ✅ Submit check-in
    fun submitCheckIn(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) {
            _checkInState.value = UiState.Error("Nama tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _checkInState.value = UiState.Loading
            runCatching { repository.submitCheckIn(eventId, trimmed) }
                .onSuccess { _checkInState.value = UiState.Success(it) }
                .onFailure { _checkInState.value = UiState.Error(it.localizedMessage ?: "Check-in gagal") }
        }
    }

    fun resetCheckInState() {
        _checkInState.value = null
    }

    fun getEventId(): Long = eventId
}