package com.example.family_attendence_app.network.models
import java.time.OffsetDateTime

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val timestamp: String = OffsetDateTime.now().toString()
) {
    companion object {
        fun <T> success(data: T?, message: String = "OK"): ApiResponse<T> =
            ApiResponse(success = true, message = message, data = data)

        fun error(message: String): ApiResponse<Unit> =
            ApiResponse(success = false, message = message, data = null)

        fun <T> error(message: String, data: T?): ApiResponse<T> =
            ApiResponse(success = false, message = message, data = data)
    }
}