package org.ryudev.com.attendanceservice.repository

import org.ryudev.com.attendanceservice.entity.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findFirstByIsActiveTrueOrderByIdDesc(): Optional<Event>
}