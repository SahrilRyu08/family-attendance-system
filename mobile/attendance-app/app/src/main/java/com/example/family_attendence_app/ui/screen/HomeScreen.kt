package com.example.family_attendence_app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.family_attendence_app.data.model.Event
import com.example.family_attendence_app.data.model.TotalData
import com.example.family_attendence_app.ui.viewmodel.AttendanceViewModel
import com.example.family_attendence_app.ui.viewmodel.UiState

@Composable
fun HomeScreen(
    vm: AttendanceViewModel,
    onInputManual: () -> Unit,
    onReport: () -> Unit
) {
    val eventState by vm.event.collectAsState()
    val totalState by vm.total.collectAsState()

    val event = (eventState as? UiState.Success)?.data

    LaunchedEffect(event) {
        event?.let { vm.loadTotal(it.id) }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Header ────────────────────────────────────────────────────
            Column {
                Text(
                    "Wilujeng Sumping! 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Sistem Absensi Keluarga",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // ── Event card ────────────────────────────────────────────────
            when (eventState) {
                is UiState.Loading -> {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                        Box(Modifier.padding(24.dp).fillMaxWidth(), Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is UiState.Success -> {
                    val e = (eventState as UiState.Success<Event>).data
                    EventInfoCard(event = e)
                }
                is UiState.Error -> {
                    Card(
                        Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Outlined.Warning, null, tint = MaterialTheme.colorScheme.error)
                            Text((eventState as UiState.Error).msg, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
                else -> {}
            }

            // ── Stat total hadir ──────────────────────────────────────────
            if (totalState is UiState.Success) {
                val t = (totalState as UiState.Success).data
                TotalCard(total = t)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

            // ── Menu utama ────────────────────────────────────────────────
            Text(
                "Menu",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            MenuCard(
                icon      = Icons.Outlined.EditNote,
                title     = "Input Manual",
                subtitle  = "Daftarkan kehadiran secara manual",
                color     = MaterialTheme.colorScheme.secondary,
                colorBg   = MaterialTheme.colorScheme.secondaryContainer,
                onClick   = onInputManual
            )

            MenuCard(
                icon      = Icons.Outlined.TableChart,
                title     = "Laporan Kehadiran",
                subtitle  = "Lihat daftar hadir & rekap peserta",
                color     = MaterialTheme.colorScheme.primary,
                colorBg   = MaterialTheme.colorScheme.primaryContainer,
                onClick   = onReport
            )
        }
    }
}

@Composable
private fun EventInfoCard(event: Event) {
    Card(
        Modifier.fillMaxWidth(),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(event.nama, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                if (event.isActive) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "AKTIF",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Outlined.CalendarToday, null, Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(event.tanggal, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Outlined.LocationOn, null, Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(event.lokasi, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun TotalCard(total: TotalData) {
    Card(
        Modifier.fillMaxWidth(),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "Ringkasan Kehadiran",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatBox("${total.totalHadir}", "Hadir",
                    MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                StatBox("${total.totalBelum}", "Belum",
                    MaterialTheme.colorScheme.error, Modifier.weight(1f))
                StatBox("${total.totalPeserta}", "Total",
                    MaterialTheme.colorScheme.onSurfaceVariant, Modifier.weight(1f))
            }
            LinearProgressIndicator(
                progress = { total.totalHadir.toFloat() / total.totalPeserta.coerceAtLeast(1) },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                trackColor = MaterialTheme.colorScheme.surface,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            Text(
                "${total.persenHadir}% sudah hadir",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun StatBox(value: String, label: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = color.copy(0.8f))
    }
}

@Composable
private fun MenuCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    color: androidx.compose.ui.graphics.Color,
    colorBg: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = colorBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = color.copy(alpha = 0.12f),
                modifier = Modifier.size(50.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = color
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = color.copy(alpha = 0.75f)
                )
            }
            Icon(Icons.Outlined.ChevronRight, null, tint = color.copy(0.5f))
        }
    }
}