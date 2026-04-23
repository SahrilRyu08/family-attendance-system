package com.example.family_attendence_app.network.models

data class EventDto(
    val id: Long,
    val name: String,
    val description: String?,
    val createdAt: String
)