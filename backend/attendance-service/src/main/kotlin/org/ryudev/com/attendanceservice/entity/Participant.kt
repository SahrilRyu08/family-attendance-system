package org.ryudev.com.attendanceservice.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "participants",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name", "event_id"])]
)
data class Participant(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 100)
    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    val event: Event,

    @Column(nullable = false, length = 20)
    var status: String = "Belum Hadir", // Mutable untuk diupdate saat check-in

    @Column(updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)