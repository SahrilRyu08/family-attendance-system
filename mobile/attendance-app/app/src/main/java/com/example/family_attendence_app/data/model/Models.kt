package com.example.family_attendence_app.data.model

import java.time.LocalDateTime

data class Event(
    val id: Long,
    val nama: String,
    val tanggal: String,
    val lokasi: String,
    val isActive: Boolean
)

data class Peserta(
    val id: Long,
    val nama: String,
    val kodeKeluarga: String?,
    val noHp: String?
)

data class CheckinRecord(
    val id: Long,
    val peserta: Peserta,
    val eventId: Long,
    val waktu: LocalDateTime,
    val method: String,
    val catatan: String?
)

data class ReportItem(
    val peserta: Peserta,
    val status: String,           // "HADIR" | "BELUM_HADIR"
    val waktuCheckin: LocalDateTime?,
    val method: String?
)

data class ReportData(
    val totalPeserta: Int,
    val totalHadir: Int,
    val totalBelumHadir: Int,
    val persenHadir: Int,
    val list: List<ReportItem>
)

data class TotalData(
    val eventId: Long,
    val eventNama: String,
    val totalPeserta: Long,
    val totalHadir: Long,
    val totalBelum: Long,
    val persenHadir: Long
)