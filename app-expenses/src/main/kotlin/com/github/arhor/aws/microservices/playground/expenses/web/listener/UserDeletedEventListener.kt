package com.github.arhor.aws.microservices.playground.expenses.web.listener

import com.github.arhor.aws.microservices.playground.expenses.service.ExpenseService
import com.github.arhor.aws.microservices.playground.expenses.service.event.UserDeletedEvent
import io.awspring.cloud.messaging.listener.annotation.SqsListener
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UserDeletedEventListener(
    private val expenseService: ExpenseService,
) {

    @SqsListener(value = ["\${application-props.aws.user-deleted-queue-name}"])
    fun deleteUserExpenses(@Payload event: UserDeletedEvent) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        logger.info("Handled: {}", event)
        try {
            logger.debug("Processing event: {}", event)
            expenseService.deleteUserExpenses(event.userId)
            logger.debug("Processing succeed")
        } catch (exception: Throwable) {
            logger.error("An exception occurred processing event: {}", event, exception)
            throw exception
        }
        logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserDeletedEventListener::class.java)
    }
}
