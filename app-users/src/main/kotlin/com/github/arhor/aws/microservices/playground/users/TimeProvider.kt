package com.github.arhor.aws.microservices.playground.users

import java.time.ZonedDateTime

interface TimeProvider {

    fun currentDateTime(): ZonedDateTime
}
