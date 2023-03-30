package com.github.arhor.aws.microservices.playground.expenses.web.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.arhor.aws.microservices.playground.expenses.service.ExpenseService
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import java.lang.invoke.MethodHandles

@Component
class UserDeletedEventListener(
    private val expenseService: ExpenseService,
    private val objectMapper: ObjectMapper,
) {
    private val logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

    @JmsListener(destination = "user-deleted-events")
    fun deleteUserExpenses(eventBody: String) {
        logger.debug("Processing [user-deleted-event] with the following payload: {}", eventBody)

        objectMapper
            .readValue<UserDeletedEvent>(eventBody)
            .let(UserDeletedEvent::userId)
            .let(expenseService::deleteUserExpenses)

        logger.debug("Processing [user-deleted-event] finished successfully")
    }

    data class UserDeletedEvent(val userId: Long)
}
