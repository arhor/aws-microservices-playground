package com.github.arhor.aws.microservices.playground.users.controller.error

import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.invoke.MethodHandles
import java.time.ZonedDateTime
import java.util.Locale
import java.util.function.Supplier

@RestControllerAdvice
class GlobalExceptionHandler(
    private val messages: MessageSource,
    private val currentDateTimeSupplier: Supplier<ZonedDateTime>,
) {
    private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: Exception, locale: Locale): ErrorResponse {
        return createErrorResponse(exception, ErrorCode.UNCATEGORIZED, locale)
    }

    private fun createErrorResponse(
        exception: Exception,
        code: ErrorCode,
        locale: Locale,
        details: List<String> = emptyList(),
        vararg args: Any?
    ): ErrorResponse {
        log.error(exception.message, exception)

        val localizedMessage = messages.getMessage(code.label, args, locale)
        val currentTimestamp = currentDateTimeSupplier.get()

        return ErrorResponse(code, localizedMessage, details, currentTimestamp)
    }
}
