package org.ryudev.com.attendanceservice.entity

import jakarta.persistence.*

@Entity
@Table(name = "peserta")
data class Peserta(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val nama: String = "",

    @Column(name = "kode_keluarga", nullable = false, unique = true)
    val kodeKeluarga: String = "",

    @Column(nullable = false)
    val noHp: String = ""
)