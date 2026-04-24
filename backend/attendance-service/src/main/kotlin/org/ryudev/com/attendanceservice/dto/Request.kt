package org.ryudev.com.attendanceservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateEventRequest(
    @field:NotBlank val nama: String,
    @field:NotBlank val tanggal: String,
    @field:NotBlank val lokasi: String,
    val isActive: Boolean = false
)

data class CreatePesertaRequest(
    @field:NotBlank val nama: String,
    @field:NotBlank val kodeKeluarga: String,
    val noHp: String = ""
)

data class CheckinRequest(
    @field:NotNull val pesertaId: Long,
    @field:NotNull val eventId: Long,
    val catatan: String? = null
)