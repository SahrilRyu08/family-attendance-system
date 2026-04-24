package org.ryudev.com.attendanceservice.dto

import org.ryudev.com.attendanceservice.entity.Attendance
import java.time.format.DateTimeFormatter

data class AttendanceDto(
    val id: Long,
    val name: String,
    val status: String,
    val eventId: Long,
    val createdAt: String
) {
    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        fun fromEntity(attendance: Attendance) = AttendanceDto(
            id = attendance.id,
            name = attendance.name,
            status = attendance.status,
            eventId = attendance.event.id,
            createdAt = attendance.createdAt.format(formatter)
        )
    }
}