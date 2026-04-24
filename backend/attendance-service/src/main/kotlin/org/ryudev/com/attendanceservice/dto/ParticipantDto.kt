package org.ryudev.com.attendanceservice.dto

import org.ryudev.com.attendanceservice.entity.Participant

data class ParticipantDto(
    val id: Long,
    val name: String,
    val status: String,
    val eventId: Long
) {
    companion object {
        fun fromEntity(p: Participant) = ParticipantDto(
            id = p.id,
            name = p.name,
            status = p.status,
            eventId = p.event.id
        )
    }
}