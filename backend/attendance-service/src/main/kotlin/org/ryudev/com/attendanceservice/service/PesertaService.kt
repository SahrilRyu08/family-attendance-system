package org.ryudev.com.attendanceservice.service

import org.ryudev.com.attendanceservice.dto.CreatePesertaRequest
import org.ryudev.com.attendanceservice.dto.PesertaResponse
import org.ryudev.com.attendanceservice.dto.toResponse
import org.ryudev.com.attendanceservice.entity.Peserta
import org.ryudev.com.attendanceservice.exception.DuplicateException
import org.ryudev.com.attendanceservice.exception.NotFoundException
import org.ryudev.com.attendanceservice.repository.PesertaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PesertaService(private val repo: PesertaRepository) {

    fun getAll(): List<PesertaResponse> =
        repo.findAll().map { it.toResponse() }

    fun getById(id: Long): PesertaResponse =
        repo.findById(id)
            .orElseThrow { NotFoundException("Peserta $id tidak ditemukan") }
            .toResponse()

    fun searchByNama(nama: String): List<PesertaResponse> =
        repo.findByNamaContainingIgnoreCase(nama).map { it.toResponse() }

    @Transactional
    fun create(req: CreatePesertaRequest): PesertaResponse {
        if (repo.existsByKodeKeluarga(req.kodeKeluarga))
            throw DuplicateException("Kode ${req.kodeKeluarga} sudah digunakan")

        return repo.save(
            Peserta(
                nama = req.nama.trim(),
                kodeKeluarga = req.kodeKeluarga.trim().uppercase(),
                noHp = req.noHp
            )
        ).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        if (!repo.existsById(id)) throw NotFoundException("Peserta $id tidak ditemukan")
        repo.deleteById(id)
    }
}