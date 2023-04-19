package com.github.arhor.aws.microservices.playground.expenses.service.event.impl

import com.github.arhor.aws.microservices.playground.expenses.service.event.ExpenseEvent
import com.github.arhor.aws.microservices.playground.expenses.service.event.ExpenseEventEmitter
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.MessagingException
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class ExpenseEventEmitterImpl(
    @Value("\${application-props.aws.expense-updated-topic-name}")
    private val expenseUpdatedTopicName: String,
    @Value("\${application-props.aws.expense-deleted-topic-name}")
    private val expenseDeletedTopicName: String,
    private val notificationMessagingTemplate: NotificationMessagingTemplate,
) : ExpenseEventEmitter {

    @Retryable(
        include = [MessagingException::class],
        maxAttemptsExpression = "\${application-props.retry-attempts:3}"
    )
    override fun emit(event: ExpenseEvent) {
        val destinationTopic = when (event) {
            is ExpenseEvent.Updated -> expenseUpdatedTopicName
            is ExpenseEvent.Deleted -> expenseDeletedTopicName
        }
        notificationMessagingTemplate.convertAndSend(destinationTopic, event)
    }
}
