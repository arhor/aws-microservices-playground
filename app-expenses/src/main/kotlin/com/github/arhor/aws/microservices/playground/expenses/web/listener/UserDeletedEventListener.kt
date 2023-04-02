package com.github.arhor.aws.microservices.playground.expenses.web.listener

import com.github.arhor.aws.microservices.playground.expenses.service.ExpenseService
import com.github.arhor.aws.microservices.playground.expenses.service.event.UserDeletedEvent
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
class UserDeletedEventListener(
    private val expenseService: ExpenseService,
) {

    @JmsListener(destination = "\${application-props.aws.user-deleted-queue-name}")
    fun deleteUserExpenses(event: UserDeletedEvent) {
        try {
            logger.debug("Processing event: {}", event)
            expenseService.deleteUserExpenses(event.userId)
            logger.debug("Processing succeed")
        } catch (exception: Throwable) {
            logger.error("An exception occurred processing event: {}", event, exception)
            throw exception
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserDeletedEventListener::class.java)
    }
}
