package com.example.family_attendence_app.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.family_attendence_app.data.model.*
import com.example.family_attendence_app.data.remote.NetworkClient
import com.example.family_attendence_app.data.remote.dto.*
import com.example.family_attendence_app.data.repository.Result.Success
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AttendanceRepository {

    private val api = NetworkClient.api

    private fun PesertaDto.toDomain() = Peserta(id, nama, kodeKeluarga, noHp)

    private fun EventDto.toDomain() = Event(id, nama, tanggal, lokasi, isActive)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun String?.toDateTime(): LocalDateTime? = this?.let {
        runCatching {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }.getOrNull()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun ReportItemDto.toDomain() = ReportItem(
        peserta = peserta.toDomain(),
        status = status,
        waktuCheckin = waktuCheckin.toDateTime(),
        method = method
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun ReportDto.toDomain() = ReportData(
        totalPeserta = totalPeserta,
        totalHadir = totalHadir,
        totalBelumHadir = totalBelumHadir,
        persenHadir = persenHadir,
        list = list.map { it.toDomain() }
    )

    private fun TotalDto.toDomain() = TotalData(
        eventId = eventId,
        eventNama = eventNama,
        totalPeserta = totalPeserta,
        totalHadir = totalHadir,
        totalBelum = totalBelum,
        persenHadir = persenHadir
    )

    suspend fun getActiveEvent(): Result<Event> = try {
        val r = api.getActiveEvent()
        if (r.isSuccessful && r.body()?.success == true)
            Success(r.body()!!.data!!.toDomain())
        else Result.Error(r.body()?.message ?: "Gagal ambil event")
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun checkin(pesertaId: Long, eventId: Long, catatan: String?): Result<String> = try {
        val r = api.checkin(CheckinRequest(pesertaId, eventId, catatan))
        if (r.isSuccessful && r.body()?.success == true)
            Success(r.body()?.message ?: "Berhasil")
        else Result.Error(r.body()?.message ?: "Gagal checkin")
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun searchPeserta(nama: String): Result<List<Peserta>> = try {
        val r = api.searchPeserta(nama)
        if (r.isSuccessful && r.body()?.success == true)
            Success(r.body()!!.data!!.map { it.toDomain() })
        else Result.Error(r.body()?.message ?: "Tidak ditemukan")
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getReport(eventId: Long, filter: String?): Result<ReportData> = try {
        val r = api.getList(eventId, filter)
        if (r.isSuccessful && r.body()?.success == true)
            Success(r.body()!!.data!!.toDomain())
        else Result.Error(r.body()?.message ?: "Gagal ambil report")
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }

    suspend fun getTotal(eventId: Long): Result<TotalData> = try {
        val r = api.getTotal(eventId)
        if (r.isSuccessful && r.body()?.success == true)
            Success(r.body()!!.data!!.toDomain())
        else Result.Error(r.body()?.message ?: "Gagal ambil total")
    } catch (e: Exception) { Result.Error(e.message ?: "Network error") }
}