package org.ryudev.com.attendanceservice.exception

import org.ryudev.com.attendanceservice.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

class NotFoundException(message: String)   : RuntimeException(message)
class DuplicateException(message: String)  : RuntimeException(message)
class BadRequestException(message: String) : RuntimeException(message)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun notFound(ex: NotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(ex.message ?: "Tidak ditemukan"))

    @ExceptionHandler(DuplicateException::class)
    fun duplicate(ex: DuplicateException) =
        ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(ex.message ?: "Data sudah ada"))

    @ExceptionHandler(BadRequestException::class)
    fun badRequest(ex: BadRequestException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ex.message ?: "Request tidak valid"))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validation(ex: MethodArgumentNotValidException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(
                ex.bindingResult.fieldErrors
                    .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            ))

    @ExceptionHandler(Exception::class)
    fun general(ex: Exception) =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("Server error: ${ex.message}"))
}