package org.ryudev.com.attendanceservice.controller

import org.ryudev.com.attendanceservice.dto.CheckinRequest
import org.ryudev.com.attendanceservice.service.CheckinService
import jakarta.validation.Valid
import org.ryudev.com.attendanceservice.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CheckinController(private val service: CheckinService) {

    // POST /api/checkin  — atau /api/daftar
    @PostMapping("/checkin")
    fun checkin(@Valid @RequestBody req: CheckinRequest) =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok(service.checkin(req), "Kehadiran berhasil dicatat"))

    // alias /daftar
    @PostMapping("/daftar")
    fun daftar(@Valid @RequestBody req: CheckinRequest) = checkin(req)

    // GET /api/search/{nama}
    @GetMapping("/search/{nama}")
    fun search(@PathVariable nama: String) =
        ResponseEntity.ok(ApiResponse.ok(service.searchPeserta(nama)))

    // GET /api/list?filter=present|belumhadir|tidakhadir&eventId=1
    @GetMapping("/list")
    fun list(
        @RequestParam(defaultValue = "0") eventId: Long,
        @RequestParam(required = false)   filter: String?
    ) = ResponseEntity.ok(ApiResponse.ok(service.getReport(eventId, filter)))

    // GET /api/total?eventId=1
    @GetMapping("/total")
    fun total(@RequestParam(defaultValue = "0") eventId: Long) =
        ResponseEntity.ok(ApiResponse.ok(service.getTotalHadir(eventId)))
}