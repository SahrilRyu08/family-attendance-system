package com.example.family_attendence_app.network.repository

import com.example.family_attendence_app.network.client.RetrofitClient
import com.example.family_attendence_app.network.models.CheckInRequest
import com.example.family_attendence_app.network.models.EventDto
import com.example.family_attendence_app.network.models.ParticipantDto
import com.example.family_attendence_app.network.models.StatsDto

class AttendanceRepository {

    private val api = RetrofitClient.api

    suspend fun fetchEvents(): List<EventDto> {
        val response = api.getEvents()
        return if (response.success) response.data ?: emptyList()
        else throw Exception(response.message)
    }

    suspend fun fetchReport(eventId: Long, status: String? = null): List<ParticipantDto> {
        val response = api.getReport(eventId, status)
        return if (response.success) response.data ?: emptyList()
        else throw Exception(response.message)
    }

    suspend fun submitCheckIn(eventId: Long, name: String): ParticipantDto {
        val response = api.checkIn(eventId, CheckInRequest(name))
        return if (response.success) response.data!!
        else throw Exception(response.message)
    }

    suspend fun fetchStats(eventId: Long): StatsDto {
        val response = api.getStats(eventId)
        return if (response.success) response.data!!
        else throw Exception(response.message)
    }

    suspend fun searchParticipants(eventId: Long, query: String): List<ParticipantDto> {
        if (query.length < 2) return emptyList()
        val response = api.searchParticipants(eventId, query)
        return if (response.success) response.data ?: emptyList()
        else throw Exception(response.message)
    }
}