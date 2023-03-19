package com.github.arhor.aws.microservices.playground.users.controller

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): Map<*, *> {
        return mapOf(
            "message" to exception.message,
        )
    }
}
