//package com.example.family_attendence_app.ui.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.family_attendence_app.ui.util.UiState
//import com.example.family_attendence_app.ui.viewmodel.CheckInViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CheckInScreen(
//    viewModel: CheckInViewModel = viewModel(),
//    onNavigateToReport: () -> Unit,
//    onBack: () -> Unit
//) {
//    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
//    val suggestions by viewModel.suggestions.collectAsStateWithLifecycle()
//    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
//    val checkInState by viewModel.checkInState.collectAsStateWithLifecycle()
//
//    var expanded by remember { mutableStateOf(false) }
//    var textFieldFocus by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("📝 Check-In Peserta") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.secondary
//                )
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .padding(16.dp)
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // 🔍 Search TextField dengan ExposedDropdownMenu
//            ExposedDropdownMenuBox(
//                expanded = expanded && suggestions.isNotEmpty() && textFieldFocus,
//                onExpandedChange = { expanded = it }
//            ) {
//                OutlinedTextField(
//                    value = searchQuery,
//                    onValueChange = {
//                        viewModel.updateSearchQuery(it)
//                        expanded = true
//                        errorMessage = null
//                    },
//                    label = { Text("Cari Nama Peserta") },
//                    placeholder = { Text("Ketik minimal 2 huruf...") },
//                    leadingIcon = { Icon(Icons.Default.Check, contentDescription = null) },
//                    trailingIcon = {
//                        when {
//                            isSearching -> CircularProgressIndicator(
//                                modifier = Modifier.size(20.dp),
//                                strokeWidth = 2.dp
//                            )
//                            searchQuery.isNotEmpty() -> IconButton(onClick = {
//                                viewModel.clearSuggestions()
//                                expanded = false
//                            }) {
//                                Icon(Icons.Default.Close, contentDescription = "Clear")
//                            }
//                            else -> null
//                        }
//                    },
//                    isError = errorMessage != null,
//                    modifier = Modifier
//                        .menuAnchor()
//                        .fillMaxWidth()
//                        .onFocusChanged { textFieldFocus = it.isFocused }
//                )
//
//                // 📋 Dropdown Suggestions
//                ExposedDropdownMenu(
//                    expanded = expanded && suggestions.isNotEmpty() && textFieldFocus,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    suggestions.forEach { participant ->
//                        DropdownMenuItem(
//                            text = {
//                                Column {
//                                    Text(
//                                        text = participant.name,
//                                        fontWeight = FontWeight.Medium
//                                    )
//                                    Text(
//                                        text = "Status: ${participant.status}",
//                                        style = MaterialTheme.typography.labelSmall,
//                                        color = if (participant.status == "Hadir")
//                                            MaterialTheme.colorScheme.primary
//                                        else
//                                            MaterialTheme.colorScheme.onSurfaceVariant
//                                    )
//                                }
//                            },
//                            onClick = {
//                                viewModel.selectSuggestion(participant)
//                                expanded = false
//                            },
//                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
//                        )
//                    }
//                }
//            }
//
//            // ❌ Error Message
//            errorMessage?.let { error ->
//                Text(
//                    text = error,
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 16.dp, top = 4.dp)
//                )
//            }
//
//            Spacer(Modifier.height(24.dp))
//
//            // ✅ Check-In Button
//            Button(
//                onClick = {
//                    val name = searchQuery.trim()
//                    when {
//                        name.isBlank() -> errorMessage = "Nama tidak boleh kosong"
//                        name.length < 2 -> errorMessage = "Nama minimal 2 karakter"
//                        else -> {
//                            viewModel.submitCheckIn(name)
//                            errorMessage = null
//                        }
//                    }
//                },
//                enabled = checkInState !is UiState.Loading && searchQuery.isNotBlank(),
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                )
//            ) {
//                if (checkInState is UiState.Loading) {
//                    CircularProgressIndicator(
//                        color = MaterialTheme.colorScheme.onPrimary,
//                        modifier = Modifier.size(24.dp)
//                    )
//                } else {
//                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
//                    Spacer(Modifier.width(8.dp))
//                    Text("✓ Check-In Sekarang")
//                }
//            }
//
//            Spacer(Modifier.height(16.dp))
//
//            // 📊 Quick Stats Preview
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.surfaceVariant
//                )
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = "💡 Tips",
//                        fontWeight = FontWeight.Bold,
//                        style = MaterialTheme.typography.titleSmall
//                    )
//                    Spacer(Modifier.height(4.dp))
//                    Text(
//                        text = "• Ketik minimal 2 huruf untuk melihat saran nama\n• Pilih nama dari daftar untuk auto-fill\n• Pastikan nama sudah terdaftar di database",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            }
//
//            Spacer(Modifier.weight(1f))
//
//            // 🎯 Navigate to Report Button
//            OutlinedButton(
//                onClick = onNavigateToReport,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("📋 Lihat Report Kehadiran")
//            }
//        }
//    }
//
//    // ✅ Handle Check-In Result
//    LaunchedEffect(checkInState) {
//        when (checkInState) {
//            is UiState.Success -> {
//                // Show success & auto-navigate after delay
//                errorMessage = "✅ Check-in berhasil!"
//                viewModel.resetCheckInState()
//            }
//            is UiState.Error -> {
//                val error = (checkInState as UiState.Error).message
//                errorMessage = when {
//                    error.contains("tidak terdaftar", ignoreCase = true) ->
//                        "❌ Nama tidak ditemukan di database event ini"
//                    error.contains("sudah check-in", ignoreCase = true) ->
//                        "⚠️ Peserta ini sudah check-in sebelumnya"
//                    else -> "❌ $error"
//                }
//                viewModel.resetCheckInState()
//            }
//            else -> {}
//        }
//    }
//}
