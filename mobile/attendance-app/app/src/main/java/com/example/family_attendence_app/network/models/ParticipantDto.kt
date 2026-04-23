package com.example.family_attendence_app.network.models

data class ParticipantDto(
    val id: Long,
    val name: String,
    val status: String,
    val eventId: Long
)