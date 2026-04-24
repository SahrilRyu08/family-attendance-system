package org.ryudev.com.attendanceservice.repository

import org.ryudev.com.attendanceservice.entity.Peserta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PesertaRepository : JpaRepository<Peserta, Long> {

    fun findByKodeKeluarga(kodeKeluarga: String): Optional<Peserta>

    fun existsByKodeKeluarga(kodeKeluarga: String): Boolean

    fun existsByKodeKeluargaAndIdNot(kodeKeluarga: String, id: String): Boolean

//    fun findAllByRoleOrderByNamaAsc(role: UserRole): List<Peserta>

    fun findByNamaContainingIgnoreCase(nama: String): List<Peserta>
}