package org.ryudev.com.attendanceservice.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "attendances",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_name_event", columnNames = ["name", "event_id"])
    ]
)
data class Attendance(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 100)
    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, foreignKey = ForeignKey(name = "fk_attendance_event"))
    val event: Event,

    @Column(nullable = false, length = 20)
    val status: String = "Hadir",

    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    val updatedAt: LocalDateTime? = null
)