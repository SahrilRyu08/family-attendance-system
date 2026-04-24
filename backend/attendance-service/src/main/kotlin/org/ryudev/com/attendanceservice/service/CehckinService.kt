package org.ryudev.com.attendanceservice.service

import org.ryudev.com.attendanceservice.dto.CheckinRequest
import org.ryudev.com.attendanceservice.dto.CheckinResponse
import org.ryudev.com.attendanceservice.dto.PesertaResponse
import org.ryudev.com.attendanceservice.dto.ReportItem
import org.ryudev.com.attendanceservice.dto.ReportResponse
import org.ryudev.com.attendanceservice.dto.toResponse
import org.ryudev.com.attendanceservice.entity.Checkin
import org.ryudev.com.attendanceservice.entity.CheckinMethod
import org.ryudev.com.attendanceservice.exception.DuplicateException
import org.ryudev.com.attendanceservice.exception.NotFoundException
import org.ryudev.com.attendanceservice.repository.CheckinRepository
import org.ryudev.com.attendanceservice.repository.EventRepository
import org.ryudev.com.attendanceservice.repository.PesertaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CheckinService(
    private val checkinRepo : CheckinRepository,
    private val pesertaRepo : PesertaRepository,
    private val eventRepo   : EventRepository
) {

    // ── Checkin / daftar hadir ────────────────────────────────────────────────
    @Transactional
    fun checkin(req: CheckinRequest): CheckinResponse {
        val peserta = pesertaRepo.findById(req.pesertaId)
            .orElseThrow { NotFoundException("Peserta ${req.pesertaId} tidak ditemukan") }

        val event = eventRepo.findById(req.eventId)
            .orElseThrow { NotFoundException("Event ${req.eventId} tidak ditemukan") }

        if (checkinRepo.existsByPesertaIdAndEventId(peserta.id, event.id))
            throw DuplicateException("${peserta.nama} sudah tercatat hadir")

        val checkin = Checkin(
            peserta = peserta,
            event = event,
            waktu = LocalDateTime.now(),
            method = CheckinMethod.MANUAL,
            catatan = req.catatan
        )
        return checkinRepo.save(checkin).toResponse()
    }

    // ── List report ────────────────────────────────────────────────────────────
    // filter: "present" | "belumhadir" | null (semua)
    @Transactional(readOnly = true)
    fun getReport(eventId: Long, filter: String?): ReportResponse {
        val event = eventRepo.findById(eventId)
            .orElseThrow { NotFoundException("Event $eventId tidak ditemukan") }

        val semuaPeserta = pesertaRepo.findAll()
        val checkinList  = checkinRepo.findByEventWithDetail(event.id)
        val hadirMap     = checkinList.associateBy { it.peserta.id }

        val allItems = semuaPeserta.map { peserta ->
            val checkin = hadirMap[peserta.id]
            ReportItem(
                peserta = peserta.toResponse(),
                status = if (checkin != null) "HADIR" else "BELUM_HADIR",
                waktuCheckin = checkin?.waktu,
                method = checkin?.method?.name
            )
        }

        val filtered = when (filter?.lowercase()) {
            "present"    , "hadir"       -> allItems.filter { it.status == "HADIR" }
            "belumhadir" , "tidakhadir"  -> allItems.filter { it.status == "BELUM_HADIR" }
            else                         -> allItems
        }

        val totalHadir = allItems.count { it.status == "HADIR" }
        val totalBelum = allItems.count { it.status == "BELUM_HADIR" }
        val persen     = if (semuaPeserta.isEmpty()) 0
        else (totalHadir * 100) / semuaPeserta.size

        return ReportResponse(
            totalPeserta    = semuaPeserta.size,
            totalHadir      = totalHadir,
            totalBelumHadir = totalBelum,
            persenHadir     = persen,
            list            = filtered
        )
    }

    // ── Search peserta by nama ────────────────────────────────────────────────
    fun searchPeserta(nama: String): List<PesertaResponse> =
        pesertaRepo.findByNamaContainingIgnoreCase(nama).map { it.toResponse() }

    // ── Total hadir ───────────────────────────────────────────────────────────
    fun getTotalHadir(eventId: Long): Map<String, Any> {
        val event = eventRepo.findById(eventId)
            .orElseThrow { NotFoundException("Event $eventId tidak ditemukan") }

        val total       = pesertaRepo.count()
        val hadir       = checkinRepo.countByEventId(event.id)
        val belum       = total - hadir
        val persen      = if (total == 0L) 0 else (hadir * 100) / total

        return mapOf(
            "event_id"        to event.id,
            "event_nama"      to event.nama,
            "total_peserta"   to total,
            "total_hadir"     to hadir,
            "total_belum"     to belum,
            "persen_hadir"    to persen
        )
    }
}