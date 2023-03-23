package com.github.arhor.aws.microservices.playground.users.controller.error

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.time.temporal.Temporal

@JsonPropertyOrder(
    "code",
    "message",
    "details",
    "timestamp",
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val code: ErrorCode,
    val message: String,
    val details: List<String>,
    val timestamp: Temporal,
)
