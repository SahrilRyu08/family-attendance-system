package com.example.family_attendence_app.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.family_attendence_app.data.model.Event
import com.example.family_attendence_app.data.model.Peserta
import com.example.family_attendence_app.ui.viewmodel.AttendanceViewModel
import com.example.family_attendence_app.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualInputScreen(
    vm: AttendanceViewModel,
    onBack: () -> Unit,
    onSuccess: (nama: String) -> Unit
) {
    val eventState by vm.event.collectAsState()
    val checkinState by vm.checkinState.collectAsState()
    val searchResult by vm.searchResult.collectAsState()
    val selectedPeserta by vm.selectedPeserta.collectAsState()
    val query by vm.searchQuery.collectAsState()

    val focus = LocalFocusManager.current
    val snackbar = remember { SnackbarHostState() }

    var catatan by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val event = (eventState as? UiState.Success<Event>)?.data

    LaunchedEffect(Unit) { vm.startSearchDebounce() }

    LaunchedEffect(checkinState) {
        when (val s = checkinState) {
            is UiState.Success -> {
                val nama = selectedPeserta?.nama ?: ""
                vm.clearSearch()
                vm.resetCheckin()
                onSuccess(nama)
            }

            is UiState.Error -> {
                snackbar.showSnackbar(s.msg)
                vm.resetCheckin()
            }

            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Manual") },
                navigationIcon = {
                    IconButton(onClick = { vm.clearSearch(); onBack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Info event ─────────────────────────────
            event?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Event,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                text = it.nama,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = it.tanggal,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // ── Search ─────────────────────────────
            Text(
                text = "Cari Nama Peserta",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = query,
                onValueChange = { vm.searchQuery.value = it },
                label = { Text("Ketik nama peserta...") },
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                trailingIcon = {
                    AnimatedVisibility(visible = query.isNotEmpty()) {
                        IconButton(onClick = { vm.clearSearch() }) {
                            Icon(Icons.Outlined.Clear, null)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions {
                    focus.clearFocus()
                }
            )

            // ── Result ─────────────────────────────
            when (searchResult) {

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }

                is UiState.Success -> {
                    val list = (searchResult as UiState.Success<List<Peserta>>).data

                    if (list.isEmpty()) {
                        Text(
                            text = "Tidak ada peserta \"$query\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

                            list.forEach { peserta ->
                                val isSelected = selectedPeserta?.id == peserta.id

                                Card(
                                    onClick = {
                                        vm.selectPeserta(if (isSelected) null else peserta)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        if (isSelected)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {

                                        Surface(
                                            modifier = Modifier.size(40.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = peserta.nama.take(2).uppercase(),
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected)
                                                        MaterialTheme.colorScheme.onPrimary
                                                    else
                                                        MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = peserta.nama,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = peserta.kodeKeluarga ?: "-",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Outlined.CheckCircle,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    Text(
                        text = (searchResult as UiState.Error).msg,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                    if (query.length == 1) {
                        Text(
                            text = "Ketik minimal 2 huruf",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // ── Selected ─────────────────────────────
            AnimatedVisibility(visible = selectedPeserta != null) {

                selectedPeserta?.let { p ->

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        HorizontalDivider()

                        Text("Peserta Terpilih", fontWeight = FontWeight.SemiBold)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                Surface(
                                    modifier = Modifier.size(44.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = p.nama.take(2).uppercase(),
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }

                                Column {
                                    Text(p.nama, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = p.kodeKeluarga ?: "-",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = catatan,
                            onValueChange = { catatan = it },
                            label = { Text("Catatan (opsional)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = { showConfirmDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Konfirmasi Kehadiran")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }


    // ── Dialog konfirmasi ─────────────────────────────────────────────────
    if (showConfirmDialog && selectedPeserta != null && event != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon    = { Icon(Icons.Outlined.HowToReg, null,
                tint = MaterialTheme.colorScheme.primary) },
            title   = { Text("Konfirmasi Kehadiran") },
            text    = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Daftarkan kehadiran:")
                    Text(selectedPeserta!!.nama,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary)
                    Text("di acara: ${event.nama}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        vm.doCheckin(
                            selectedPeserta!!.id,
                            event.id,
                            catatan.ifBlank { null }
                        )
                    }
                ) { Text("Ya, Konfirmasi") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Batal") }
            }
        )
    }
}