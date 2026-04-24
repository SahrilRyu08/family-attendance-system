package com.example.family_attendence_app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?
)

data class EventDto(
    val id: Long,
    val nama: String,
    val tanggal: String,
    val lokasi: String,
    @SerializedName("is_active") val isActive: Boolean
)

data class PesertaDto(
    val id: Long,
    val nama: String,
    @SerializedName("kode_keluarga") val kodeKeluarga: String,
    @SerializedName("no_hp") val noHp: String
)

data class CheckinDto(
    val id: Long,
    val peserta: PesertaDto,
    @SerializedName("event_id") val eventId: Long,
    val waktu: String,
    val method: String,
    val catatan: String?
)

data class ReportItemDto(
    val peserta: PesertaDto,
    val status: String,
    @SerializedName("waktu_checkin") val waktuCheckin: String?,
    val method: String?
)

data class ReportDto(
    @SerializedName("total_peserta")    val totalPeserta: Int,
    @SerializedName("total_hadir")      val totalHadir: Int,
    @SerializedName("total_belum_hadir")val totalBelumHadir: Int,
    @SerializedName("persen_hadir")     val persenHadir: Int,
    val list: List<ReportItemDto>
)

data class TotalDto(
    @SerializedName("event_id")       val eventId: Long,
    @SerializedName("event_nama")     val eventNama: String,
    @SerializedName("total_peserta")  val totalPeserta: Long,
    @SerializedName("total_hadir")    val totalHadir: Long,
    @SerializedName("total_belum")    val totalBelum: Long,
    @SerializedName("persen_hadir")   val persenHadir: Long
)

data class CheckinRequest(
    @SerializedName("pesertaId") val pesertaId: Long,
    @SerializedName("eventId")   val eventId: Long,
    val catatan: String? = null
)