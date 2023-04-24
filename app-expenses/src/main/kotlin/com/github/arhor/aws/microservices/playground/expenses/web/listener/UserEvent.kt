package com.github.arhor.aws.microservices.playground.expenses.web.listener

sealed interface UserEvent {

    data class Deleted(val userId: Long) : UserEvent
}
