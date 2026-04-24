package org.ryudev.com.attendanceservice.entity
import jakarta.persistence.*

import java.time.LocalDateTime

enum class CheckinMethod { MANUAL, QR_SCAN }

@Entity
@Table(
    name = "checkin",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_peserta_event",
            columnNames = ["peserta_id", "event_id"]
        )
    ]
)
data class Checkin(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peserta_id", nullable = false)
    val peserta: Peserta = Peserta(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    val event: Event = Event(),

    @Column(nullable = false)
    val waktu: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val method: CheckinMethod = CheckinMethod.MANUAL,

    @Column(nullable = true)
    val catatan: String? = null
)