package org.ryudev.com.attendanceservice.dto

import org.ryudev.com.attendanceservice.entity.Checkin
import org.ryudev.com.attendanceservice.entity.Event
import org.ryudev.com.attendanceservice.entity.Peserta
import java.time.LocalDateTime

data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
) {
    companion object {
        fun <T> ok(data: T, message: String? = null) =
            ApiResponse(success = true, data = data, message = message)
        fun error(message: String) =
            ApiResponse<Nothing>(success = false, message = message)
    }
}

data class EventResponse(
    val id: Long,
    val nama: String,
    val tanggal: String,
    val lokasi: String,
    val isActive: Boolean
)

fun Event.toResponse() = EventResponse(id, nama, tanggal, lokasi, isActive)

data class PesertaResponse(
    val id: Long,
    val nama: String,
    val kodeKeluarga: String,
    val noHp: String
)

fun Peserta.toResponse() = PesertaResponse(id, nama, kodeKeluarga, noHp)

data class CheckinResponse(
    val id: Long,
    val peserta: PesertaResponse,
    val eventId: Long,
    val waktu: LocalDateTime,
    val method: String,
    val catatan: String?
)

fun Checkin.toResponse() = CheckinResponse(
    id       = id,
    peserta  = peserta.toResponse(),
    eventId  = event.id,
    waktu    = waktu,
    method   = method.name,
    catatan  = catatan
)

data class ReportResponse(
    val totalPeserta: Int,
    val totalHadir: Int,
    val totalBelumHadir: Int,
    val persenHadir: Int,
    val list: List<ReportItem>
)

data class ReportItem(
    val peserta: PesertaResponse,
    val status: String,       // "HADIR" | "BELUM_HADIR"
    val waktuCheckin: LocalDateTime?,
    val method: String?
)