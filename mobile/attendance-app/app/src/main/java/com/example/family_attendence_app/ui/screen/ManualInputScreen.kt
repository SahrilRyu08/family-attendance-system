package com.example.family_attendence_app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.family_attendence_app.ui.theme.PrimaryGreen
import com.example.family_attendence_app.ui.theme.SecondaryOrange
import com.example.family_attendence_app.ui.util.UiState
import com.example.family_attendence_app.ui.viewmodel.CheckInViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualInputScreen(
    viewModel: CheckInViewModel = viewModel(),
    eventId: Long,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var namaLengkap by remember { mutableStateOf("") }
    var kodeKeluarga by remember { mutableStateOf("") }
    val checkInState by viewModel.checkInState.collectAsStateWithLifecycle()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Manual", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Reuni Keluarga Besar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Sabtu, 12 Juli 2025 · Aula Serbaguna",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Form Fields
            OutlinedTextField(
                value = namaLengkap,
                onValueChange = {
                    namaLengkap = it
                    errorMessage = null
                },
                label = { Text("Nama Lengkap") },
                placeholder = { Text("Contoh: Ibu Siti Rahayu") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    focusedLabelColor = PrimaryGreen
                )
            )

            OutlinedTextField(
                value = kodeKeluarga,
                onValueChange = { kodeKeluarga = it },
                label = { Text("No. Anggota / Kode Keluarga") },
                placeholder = { Text("Contoh: KLG-042") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    focusedLabelColor = PrimaryGreen
                )
            )

            OutlinedTextField(
                value = "Reuni Keluarga 2025",
                onValueChange = { },
                label = { Text("Acara") },
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            // Error Message
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    when {
                        namaLengkap.isBlank() -> {
                            errorMessage = "Nama lengkap harus diisi"
                        }
                        namaLengkap.length < 3 -> {
                            errorMessage = "Nama minimal 3 karakter"
                        }
                        else -> {
                            viewModel.submitCheckIn(namaLengkap)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen
                ),
                enabled = checkInState !is UiState.Loading
            ) {
                if (checkInState is UiState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Konfirmasi Hadir",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SecondaryOrange.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = SecondaryOrange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Untuk Orang Tua / Lansia",
                            fontWeight = FontWeight.SemiBold,
                            color = SecondaryOrange
                        )
                        Text(
                            text = "Input manual tanpa perlu scan QR code",
                            fontSize = 13.sp,
                            color = SecondaryOrange.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }

    // Handle Check-in Result
    LaunchedEffect(checkInState) {
        when (checkInState) {
            is UiState.Success -> {
                onSuccess()
            }
            is UiState.Error -> {
                val error = (checkInState as UiState.Error).message
                errorMessage = when {
                    error.contains("tidak terdaftar", ignoreCase = true) ->
                        "Nama tidak ditemukan di database"
                    error.contains("sudah check-in", ignoreCase = true) ->
                        "Peserta sudah check-in sebelumnya"
                    else -> error
                }
                viewModel.resetCheckInState()
            }
            else -> {}
        }
    }
}