package org.ryudev.com.attendanceservice.repository

import org.ryudev.com.attendanceservice.entity.Checkin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CheckinRepository : JpaRepository<Checkin, Long> {

    fun existsByPesertaIdAndEventId(pesertaId: Long, eventId: Long): Boolean

    fun findAllByEventIdOrderByWaktuDesc(eventId: Long): List<Checkin>

    fun countByEventId(eventId: Long): Long

    @Query("""
        SELECT c FROM Checkin c
        JOIN FETCH c.peserta
        JOIN FETCH c.event
        WHERE c.event.id = :eventId
        ORDER BY c.waktu DESC
    """)
    fun findByEventWithDetail(@Param("eventId") eventId: Long): List<Checkin>

    fun findByPesertaIdAndEventId(pesertaId: Long, eventId: Long): Checkin?
}