package com.example.family_attendence_app.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.family_attendence_app.data.model.Event
import com.example.family_attendence_app.data.model.ReportData
import com.example.family_attendence_app.data.model.ReportItem
import com.example.family_attendence_app.data.model.TotalData
import com.example.family_attendence_app.ui.viewmodel.AttendanceViewModel
import com.example.family_attendence_app.ui.viewmodel.UiState
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportScreen(
    vm: AttendanceViewModel,
    onBack: () -> Unit
) {
    val eventState  by vm.event.collectAsState()
    val reportState by vm.report.collectAsState()
    val filterState by vm.reportFilter.collectAsState()
    val totalState by vm.total.collectAsState()

    val event = (eventState as? UiState.Success<Event>)?.data

    // Load report
    LaunchedEffect(event) {
        event?.let { vm.loadReport(it.id, null) }
    }

    val filters = listOf(
        null to "Semua",
        "present" to "Hadir",
        "belumhadir" to "Belum Hadir"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laporan Kehadiran") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { event?.let { vm.loadReport(it.id, filterState) } }
                    ) {
                        Icon(Icons.Outlined.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        when (reportState) {

            // ================= LOADING =================
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(
                            "Memuat data...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // ================= ERROR =================
            is UiState.Error -> {
                val msg = (reportState as UiState.Error).msg

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )

                        Text(msg, color = MaterialTheme.colorScheme.error)

                        Button(
                            onClick = { event?.let { vm.loadReport(it.id, filterState) } }
                        ) {
                            Text("Coba Lagi")
                        }
                    }
                }
            }

            // ================= SUCCESS =================
            is UiState.Success -> {

                val data = (reportState as UiState.Success<ReportData>).data
                val total = (totalState as UiState.Success<TotalData>).data


                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // ===== STAT =====
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ReportStatCard(
                                value = "${total.totalHadir}",
                                label = "Hadir",
                                color = MaterialTheme.colorScheme.primary,
                                bg = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.weight(1f)
                            )
                            ReportStatCard(
                                value = "${total.totalBelum}",
                                label = "Belum",
                                color = MaterialTheme.colorScheme.error,
                                bg = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.weight(1f)
                            )
                            ReportStatCard(
                                value = "${total.totalPeserta}",
                                label = "Total",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                bg = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // ===== PROGRESS =====
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Progres Kehadiran")
                                    Text("${total.persenHadir}%")
                                }

                                LinearProgressIndicator(
                                    progress = {
                                        if (total.totalPeserta == 0L) 0f
                                        else total.totalHadir.toFloat() / total.totalPeserta
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp),
                                    strokeCap = StrokeCap.Round
                                )
                            }
                        }
                    }

                    // ===== FILTER =====
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            filters.forEach { (value, label) ->
                                FilterChip(
                                    selected = filterState == value,
                                    onClick = { event?.let { vm.loadReport(it.id, value) } },
                                    label = { Text(label) }
                                )
                            }
                        }
                    }

                    // ===== HEADER =====
                    item {
                        Text(
                            when (filterState) {
                                "present" -> "Daftar Hadir (${data.list.size})"
                                "belumhadir" -> "Belum Hadir (${data.list.size})"
                                else -> "Semua Peserta (${data.list.size})"
                            },
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // ===== LIST =====
                    if (data.list.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Tidak ada data")
                            }
                        }
                    } else {
                        items(
                            items = data.list,
                            key = { it.peserta.id ?: it.hashCode() } // SAFE
                        ) { item ->
                            ReportItemCard(item)
                        }
                    }
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun ReportStatCard(
    value: String,
    label: String,
    color: androidx.compose.ui.graphics.Color,
    bg: androidx.compose.ui.graphics.Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, color = color)
            Text(label, color = color.copy(0.7f))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ReportItemCard(item: ReportItem) {
    val isHadir = item.status == "HADIR"
    val accent = if (isHadir) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val accentBg = if (isHadir) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(10.dp),
                color = accentBg
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(item.peserta.nama.take(2).uppercase(), color = accent)
                }
            }

            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text(item.peserta.nama, fontWeight = FontWeight.SemiBold)
                Text(item.peserta.kodeKeluarga ?: "-")
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(if (isHadir) "HADIR" else "BELUM", color = accent)

                if (isHadir && item.waktuCheckin != null) {
                    Text(item.waktuCheckin.format(formatter))
                }
            }
        }
    }
}