package com.github.arhor.aws.microservices.playground.expenses.web.controller.error

import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
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
    fun handleException(generalException: Exception, requestLocale: Locale) =
        createErrorResponse(
            exception = generalException,
            errorCode = ErrorCode.UNCATEGORIZED,
            locale = requestLocale,
        )

    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNoHandlerFoundException(noHandlerFoundException: NoHandlerFoundException, requestLocale: Locale) =
        createErrorResponse(
            exception = noHandlerFoundException,
            errorCode = ErrorCode.NO_HANDLER_FOUND,
            locale = requestLocale,
            args = arrayOf(
                noHandlerFoundException.httpMethod,
                noHandlerFoundException.requestURL,
            )
        )

    private fun createErrorResponse(
        exception: Exception,
        errorCode: ErrorCode,
        locale: Locale,
        details: List<String> = emptyList(),
        vararg args: Any?
    ): ErrorResponse {
        log.error(exception.message, exception)

        val localizedMessage = messages.getMessage(errorCode.label, args, locale)
        val currentTimestamp = currentDateTimeSupplier.get()

        return ErrorResponse(errorCode, localizedMessage, details, currentTimestamp)
    }
}
