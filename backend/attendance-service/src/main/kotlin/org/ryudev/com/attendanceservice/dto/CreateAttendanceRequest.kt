package org.ryudev.com.attendanceservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateAttendanceRequest(
    @field:NotBlank(message = "Nama peserta tidak boleh kosong")
    @field:Size(min = 2, max = 100, message = "Nama harus antara 2-100 karakter")
    val name: String
)