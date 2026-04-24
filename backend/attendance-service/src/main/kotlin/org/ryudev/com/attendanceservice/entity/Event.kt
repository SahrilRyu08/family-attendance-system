package org.ryudev.com.attendanceservice.entity

import jakarta.persistence.*

@Entity
@Table(name = "events")
data class Event(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val nama: String = "",

    @Column(nullable = false)
    val tanggal: String = "",

    @Column(nullable = false)
    val lokasi: String = "",

    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = false
)