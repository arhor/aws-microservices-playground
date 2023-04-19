package com.github.arhor.aws.microservices.playground.expenses.service.event

sealed interface UserEvent {

    data class Deleted(val userId: Long) : UserEvent
}
