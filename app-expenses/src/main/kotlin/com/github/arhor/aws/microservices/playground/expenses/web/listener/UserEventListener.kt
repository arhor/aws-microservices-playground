package com.github.arhor.aws.microservices.playground.expenses.web.listener

import com.github.arhor.aws.microservices.playground.expenses.service.ExpenseService
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy
import io.awspring.cloud.messaging.listener.annotation.SqsListener
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UserEventListener(
    private val expenseService: ExpenseService,
) {

    @SqsListener(
        value = ["\${application-props.aws.user-deleted-queue-name}"],
        deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS
    )
    fun handleUserDeletedEvent(@Payload event: UserEvent.Deleted) {
        // sometimes the same event being processed several times - why?
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
        private val logger = LoggerFactory.getLogger(UserEventListener::class.java)
    }
}
