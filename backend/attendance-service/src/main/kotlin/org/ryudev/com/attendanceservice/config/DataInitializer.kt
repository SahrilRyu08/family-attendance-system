package org.ryudev.com.attendanceservice.config

import org.ryudev.com.attendanceservice.entity.Event
import org.ryudev.com.attendanceservice.entity.Peserta
import org.ryudev.com.attendanceservice.repository.EventRepository
import org.ryudev.com.attendanceservice.repository.PesertaRepository
import org.springframework.stereotype.Component


import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner

@Component
class DataInitializer(
    private val eventRepo   : EventRepository,
    private val pesertaRepo : PesertaRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        if (eventRepo.count() == 0L) {
            eventRepo.save(Event(
                nama     = "Reuni Keluarga Besar 2025",
                tanggal  = "Sabtu, 12 Juli 2025",
                lokasi   = "Aula Serbaguna",
                isActive = true
            ))
            println("✅ Event seed berhasil")
        }

        if (pesertaRepo.count() == 0L) {
            val list = listOf(
                Peserta(nama = "Hendra Kusuma", kodeKeluarga = "KLG-001", noHp = "08111"),
                Peserta(nama = "Ibu Siti Rahayu", kodeKeluarga = "KLG-002", noHp = "08112"),
                Peserta(nama = "Bapak Agus S.",   kodeKeluarga = "KLG-003", noHp = "08113"),
                Peserta(nama = "Rini Pertiwi",    kodeKeluarga = "KLG-004", noHp = "08114"),
                Peserta(nama = "Deni Kurniawan",  kodeKeluarga = "KLG-005", noHp = "08115"),
            )
            pesertaRepo.saveAll(list)
            println("✅ Peserta seed berhasil: ${list.size} peserta")
        }
    }
}