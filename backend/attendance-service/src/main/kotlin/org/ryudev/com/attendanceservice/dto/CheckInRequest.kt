package org.ryudev.com.attendanceservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CheckInRequest(
    @field:NotBlank(message = "Nama tidak boleh kosong")
    @field:Size(min = 2, max = 100, message = "Nama harus 2-100 karakter")
    val name: String
)