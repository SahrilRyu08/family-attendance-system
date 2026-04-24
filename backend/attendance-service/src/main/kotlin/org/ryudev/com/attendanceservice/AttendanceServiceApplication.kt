package org.ryudev.com.attendanceservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AttendanceServiceApplication

fun main(args: Array<String>) {
    runApplication<AttendanceServiceApplication>(*args)
}
