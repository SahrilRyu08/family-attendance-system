package com.example.family_attendence_app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.family_attendence_app.network.models.ParticipantDto
import com.example.family_attendence_app.ui.theme.PrimaryGreen
import com.example.family_attendence_app.ui.theme.SecondaryOrange
import com.example.family_attendence_app.ui.util.UiState
import com.example.family_attendence_app.ui.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: ReportViewModel = viewModel(),
    onBack: () -> Unit,
    onCheckInClick: () -> Unit
) {
    val reportState by viewModel.reportState.collectAsStateWithLifecycle()
    val statsState by viewModel.statsState.collectAsStateWithLifecycle()
    val filterStatus by viewModel.filterStatus.collectAsStateWithLifecycle()

    var showFilterMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Rekap Kehadiran",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCheckInClick,
                containerColor = PrimaryGreen,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Check-In", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Stats Cards
            when (statsState) {
                is UiState.Success -> {
                    val stats = (statsState as UiState.Success).data
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCardModern(
                            title = "Hadir",
                            value = stats.hadir.toString(),
                            backgroundColor = PrimaryGreen.copy(alpha = 0.15f),
                            textColor = PrimaryGreen,
                            modifier = Modifier.weight(1f)
                        )
                        StatCardModern(
                            title = "Belum",
                            value = stats.belumHadir.toString(),
                            backgroundColor = Color(0xFFE74C3C).copy(alpha = 0.15f),
                            textColor = Color(0xFFE74C3C),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                else -> {
                    // Loading skeleton
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        repeat(2) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            )
                        }
                    }
                }
            }

            // Progress Bar
            when (statsState) {
                is UiState.Success -> {
                    val stats = (statsState as UiState.Success).data
                    val total = stats.total.toFloat()
                    val hadirPercent = if (total > 0) (stats.hadir / total) else 0f

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        LinearProgressIndicator(
                            progress = hadirPercent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = PrimaryGreen,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "${(hadirPercent * 100).toInt()}% kehadiran",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    null
                }
            }

            Spacer(Modifier.height(16.dp))

            // Filter Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter:",
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Box {
                    FilterChip(
                        selected = false,
                        onClick = { showFilterMenu = true },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    when (filterStatus) {
                                        "Hadir" -> "Hadir"
                                        "Belum Hadir" -> "Belum Hadir"
                                        else -> "Semua"
                                    }
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Semua") },
                            onClick = {
                                viewModel.updateFilter(null)
                                showFilterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Hadir") },
                            onClick = {
                                viewModel.updateFilter("Hadir")
                                showFilterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Belum Hadir") },
                            onClick = {
                                viewModel.updateFilter("Belum Hadir")
                                showFilterMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Participant List
            when (reportState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = PrimaryGreen
                        )
                    }
                }
                is UiState.Error -> {
                    ErrorMessageView((reportState as UiState.Error).message) {
                        viewModel.loadReport()
                    }
                }
                is UiState.Success -> {
                    val participants = (reportState as UiState.Success).data
                    if (participants.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.PersonOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Belum ada data peserta",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(participants, key = { it.id }) { participant ->
                                ParticipantCardModern(participant)
                            }

                            // Export Button
                            item {
                                Spacer(Modifier.height(12.dp))
                                OutlinedButton(
                                    onClick = { /* Export Excel/PDF */ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = PrimaryGreen
                                    ),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(PrimaryGreen)
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.FileDownload,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "Export Excel / PDF",
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Spacer(Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCardModern(
    title: String,
    value: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = textColor
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = textColor.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun ParticipantCardModern(
    participant: ParticipantDto
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = participant.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "#${participant.id}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (participant.status == "Hadir")
                    PrimaryGreen.copy(alpha = 0.15f)
                else
                    SecondaryOrange.copy(alpha = 0.15f)
            ) {
                Text(
                    text = if (participant.status == "Hadir") "QR" else "Manual",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (participant.status == "Hadir")
                        PrimaryGreen
                    else
                        SecondaryOrange
                )
            }
        }
    }
}