package com.example.family_attendence_app.data.remote

import com.example.family_attendence_app.data.remote.dto.ApiResponse
import com.example.family_attendence_app.data.remote.dto.CheckinDto
import com.example.family_attendence_app.data.remote.dto.CheckinRequest
import com.example.family_attendence_app.data.remote.dto.EventDto
import com.example.family_attendence_app.data.remote.dto.PesertaDto
import com.example.family_attendence_app.data.remote.dto.ReportDto
import com.example.family_attendence_app.data.remote.dto.TotalDto
import retrofit2.Response
import retrofit2.http.*

interface AttendanceApi {

    @GET("api/event/active")
    suspend fun getActiveEvent(): Response<ApiResponse<EventDto>>

    @GET("api/event")
    suspend fun getAllEvents(): Response<ApiResponse<List<EventDto>>>

    @POST("api/checkin")
    suspend fun checkin(@Body req: CheckinRequest): Response<ApiResponse<CheckinDto>>

    @GET("api/search/{nama}")
    suspend fun searchPeserta(@Path("nama") nama: String): Response<ApiResponse<List<PesertaDto>>>

    @GET("api/list")
    suspend fun getList(
        @Query("eventId") eventId: Long,
        @Query("filter")  filter: String? = null
    ): Response<ApiResponse<ReportDto>>

    @GET("api/total")
    suspend fun getTotal(@Query("eventId") eventId: Long): Response<ApiResponse<TotalDto>>
}

object NetworkClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: AttendanceApi by lazy {
        val logging = okhttp3.logging.HttpLoggingInterceptor().apply {
            level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        }
        val client = okhttp3.OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(AttendanceApi::class.java)
    }
}