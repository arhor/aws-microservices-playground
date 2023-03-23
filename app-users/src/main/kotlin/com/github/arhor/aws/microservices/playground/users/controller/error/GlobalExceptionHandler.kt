package com.github.arhor.aws.microservices.playground.users.controller.error

import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.ZonedDateTime
import java.util.*
import java.util.function.Supplier

@RestControllerAdvice
class GlobalExceptionHandler(
    private val messages: MessageSource,
    private val currentDateTimeSupplier: Supplier<ZonedDateTime>,
) {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: Exception, locale: Locale): ErrorResponse {
        return createErrorResponse(ErrorCode.UNCATEGORIZED, locale)
    }

    private fun createErrorResponse(
        code: ErrorCode,
        locale: Locale,
        details: List<String> = emptyList(),
        vararg args: Any?
    ): ErrorResponse {
        val localizedMessage = messages.getMessage(code.label, args, locale)
        val currentTimestamp = currentDateTimeSupplier.get()

        return ErrorResponse(code, localizedMessage, details, currentTimestamp)
    }
}
