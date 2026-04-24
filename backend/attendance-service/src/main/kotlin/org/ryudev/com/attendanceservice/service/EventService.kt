package org.ryudev.com.attendanceservice.service

import org.ryudev.com.attendanceservice.dto.CreateEventRequest
import org.ryudev.com.attendanceservice.dto.EventResponse
import org.ryudev.com.attendanceservice.dto.toResponse
import org.ryudev.com.attendanceservice.entity.Event
import org.ryudev.com.attendanceservice.exception.NotFoundException
import org.ryudev.com.attendanceservice.repository.EventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventService(private val repo: EventRepository) {

    fun getActive(): EventResponse =
        repo.findFirstByIsActiveTrueOrderByIdDesc()
            .orElseThrow { NotFoundException("Tidak ada event aktif") }
            .toResponse()

    fun getAll(): List<EventResponse> =
        repo.findAll().map { it.toResponse() }

    fun getById(id: Long): EventResponse =
        repo.findById(id)
            .orElseThrow { NotFoundException("Event $id tidak ditemukan") }
            .toResponse()

    @Transactional
    fun create(req: CreateEventRequest): EventResponse {
        val event = Event(
            nama = req.nama,
            tanggal = req.tanggal,
            lokasi = req.lokasi,
            isActive = req.isActive
        )
        return repo.save(event).toResponse()
    }

    @Transactional
    fun setActive(id: Long): EventResponse {
        repo.findAll().filter { it.isActive }.forEach {
            repo.save(it.copy(isActive = false))
        }
        val event = repo.findById(id)
            .orElseThrow { NotFoundException("Event $id tidak ditemukan") }
        return repo.save(event.copy(isActive = true)).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        if (!repo.existsById(id)) throw NotFoundException("Event $id tidak ditemukan")
        repo.deleteById(id)
    }
}