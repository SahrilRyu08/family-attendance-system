package com.example.family_attendence_app.network.service

import com.example.family_attendence_app.network.models.ApiResponse
import com.example.family_attendence_app.network.models.CheckInRequest
import com.example.family_attendence_app.network.models.EventDto
import com.example.family_attendence_app.network.models.ParticipantDto
import com.example.family_attendence_app.network.models.StatsDto
import retrofit2.http.*

interface ApiService {

    // ✅ Get all events
    @GET("api/v1/events")
    suspend fun getEvents(): ApiResponse<List<EventDto>>

    // ✅ Get attendance report
    @GET("api/v1/events/{eventId}/report")
    suspend fun getReport(
        @Path("eventId") eventId: Long,
        @Query("status") status: String? = null
    ): ApiResponse<List<ParticipantDto>>

    // ✅ Check-in participant
    @POST("api/v1/events/{eventId}/checkin")
    suspend fun checkIn(
        @Path("eventId") eventId: Long,
        @Body request: CheckInRequest
    ): ApiResponse<ParticipantDto>

    // ✅ Get statistics
    @GET("api/v1/events/{eventId}/stats")
    suspend fun getStats(@Path("eventId") eventId: Long): ApiResponse<StatsDto>

    // ✅ Search participants for autocomplete
    @GET("api/v1/events/{eventId}/participants/search")
    suspend fun searchParticipants(
        @Path("eventId") eventId: Long,
        @Query("query") query: String
    ): ApiResponse<List<ParticipantDto>>
}