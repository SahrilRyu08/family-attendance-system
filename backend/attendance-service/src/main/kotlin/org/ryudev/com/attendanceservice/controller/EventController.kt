package org.ryudev.com.attendanceservice.controller

import jakarta.validation.Valid
import org.ryudev.com.attendanceservice.dto.ApiResponse
import org.ryudev.com.attendanceservice.dto.CreateEventRequest
import org.ryudev.com.attendanceservice.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/event")
class EventController(private val service: EventService) {

    // GET /api/event
    @GetMapping
    fun getAll() =
        ResponseEntity.ok(ApiResponse.ok(service.getAll()))

    // GET /api/event/active
    @GetMapping("/active")
    fun getActive() =
        ResponseEntity.ok(ApiResponse.ok(service.getActive()))

    // GET /api/event/{id}
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        ResponseEntity.ok(ApiResponse.ok(service.getById(id)))

    // POST /api/event
    @PostMapping
    fun create(@Valid @RequestBody req: CreateEventRequest) =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok(service.create(req), "Event berhasil dibuat"))

    // PATCH /api/event/{id}/activate
    @PatchMapping("/{id}/activate")
    fun activate(@PathVariable id: Long) =
        ResponseEntity.ok(ApiResponse.ok(service.setActive(id), "Event diaktifkan"))

    // DELETE /api/event/{id}
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ApiResponse<Unit>> {
        service.delete(id)
        return ResponseEntity.ok(ApiResponse.ok(Unit, "Event dihapus"))
    }
}