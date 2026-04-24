package com.example.family_attendence_app.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.family_attendence_app.data.model.Event
import com.example.family_attendence_app.data.model.Peserta
import com.example.family_attendence_app.data.model.ReportData
import com.example.family_attendence_app.data.model.TotalData
import com.example.family_attendence_app.data.repository.AttendanceRepository
import com.example.family_attendence_app.data.repository.Result
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Idle                  : UiState<Nothing>
    data object Loading               : UiState<Nothing>
    data class  Success<T>(val data: T) : UiState<T>
    data class  Error(val msg: String)  : UiState<Nothing>
}

class AttendanceViewModel : ViewModel() {

    private val repo = AttendanceRepository()

    // ── Active event ──────────────────────────────────────────────────────────
    private val _event = MutableStateFlow<UiState<Event>>(UiState.Idle)
    val event: StateFlow<UiState<Event>> = _event.asStateFlow()

    // ── Checkin ───────────────────────────────────────────────────────────────
    private val _checkinState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val checkinState: StateFlow<UiState<String>> = _checkinState.asStateFlow()

    // ── Search ────────────────────────────────────────────────────────────────
    val searchQuery = MutableStateFlow("")

    private val _searchResult = MutableStateFlow<UiState<List<Peserta>>>(UiState.Idle)
    val searchResult: StateFlow<UiState<List<Peserta>>> = _searchResult.asStateFlow()

    // ── Report ────────────────────────────────────────────────────────────────
    private val _report = MutableStateFlow<UiState<ReportData>>(UiState.Idle)
    val report: StateFlow<UiState<ReportData>> = _report.asStateFlow()

    val reportFilter = MutableStateFlow<String?>(null)  // null | "present" | "belumhadir"

    // ── Total ─────────────────────────────────────────────────────────────────
    private val _total = MutableStateFlow<UiState<TotalData>>(UiState.Idle)
    val total: StateFlow<UiState<TotalData>> = _total.asStateFlow()

    // ── Selected peserta untuk checkin ────────────────────────────────────────
    private val _selectedPeserta = MutableStateFlow<Peserta?>(null)
    val selectedPeserta: StateFlow<Peserta?> = _selectedPeserta.asStateFlow()

    init { loadEvent() }

    // ── Load event aktif ──────────────────────────────────────────────────────
    fun loadEvent() {
        viewModelScope.launch {
            _event.value = UiState.Loading
            _event.value = when (val r = repo.getActiveEvent()) {
                is Result.Success -> UiState.Success(r.data)
                is Result.Error   -> UiState.Error(r.message)
            }!!
        }
    }

    // ── Checkin ───────────────────────────────────────────────────────────────
    fun doCheckin(pesertaId: Long, eventId: Long, catatan: String? = null) {
        viewModelScope.launch {
            _checkinState.value = UiState.Loading
            _checkinState.value = when (val r = repo.checkin(pesertaId, eventId, catatan)) {
                is Result.Success -> UiState.Success(r.data)
                is Result.Error   -> UiState.Error(r.message)
            }
        }
    }

    fun resetCheckin() { _checkinState.value = UiState.Idle }

    // ── Search ────────────────────────────────────────────────────────────────
    @OptIn(FlowPreview::class)
    fun startSearchDebounce() {
        viewModelScope.launch {
            searchQuery
                .debounce(400)
                .filter { it.length >= 2 }
                .distinctUntilChanged()
                .collect { query ->
                    _searchResult.value = UiState.Loading
                    _searchResult.value = when (val r = repo.searchPeserta(query)) {
                        is Result.Success -> UiState.Success(r.data)
                        is Result.Error   -> UiState.Error(r.message)
                    }
                }
        }
    }

    fun selectPeserta(p: Peserta?) { _selectedPeserta.value = p }
    fun clearSearch() {
        searchQuery.value   = ""
        _searchResult.value = UiState.Idle
        _selectedPeserta.value = null
    }

    // ── Report ────────────────────────────────────────────────────────────────
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadReport(eventId: Long, filter: String? = reportFilter.value) {
        viewModelScope.launch {
            _report.value = UiState.Loading
            reportFilter.value = filter
            _report.value = when (val r = repo.getReport(eventId, filter)) {
                is Result.Success -> UiState.Success(r.data)
                is Result.Error   -> UiState.Error(r.message)
            }
        }
    }

    fun loadTotal(eventId: Long) {
        viewModelScope.launch {
            _total.value = UiState.Loading
            _total.value = when (val r = repo.getTotal(eventId)) {
                is Result.Success -> UiState.Success(r.data)
                is Result.Error   -> UiState.Error(r.message)
            }
        }
    }
}