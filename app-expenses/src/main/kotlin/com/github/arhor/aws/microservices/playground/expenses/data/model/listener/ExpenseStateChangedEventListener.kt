package com.github.arhor.aws.microservices.playground.expenses.data.model.listener

import com.github.arhor.aws.microservices.playground.expenses.config.props.ApplicationProps
import com.github.arhor.aws.microservices.playground.expenses.data.model.Expense
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate
import org.springframework.data.relational.core.mapping.event.AbstractRelationalEventListener
import org.springframework.data.relational.core.mapping.event.AfterDeleteEvent
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent
import org.springframework.messaging.MessagingException
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
@Retryable(
    include = [MessagingException::class],
    maxAttemptsExpression = "\${application-props.retry-attempts:3}"
)
class ExpenseStateChangedEventListener(
    private val appProps: ApplicationProps,
    private val messenger: NotificationMessagingTemplate,
) : AbstractRelationalEventListener<Expense>() {


    override fun onAfterSave(event: AfterSaveEvent<Expense>) {
        super.onAfterSave(event)

        val payload = event.entity.let { (id, date, amount, userId) ->
            ExpenseStateChangedMessage.Updated(
                expenseId = id!!,
                date = date,
                amount = amount,
                userId = userId,
            )
        }
        messenger.convertAndSend(appProps.aws.expenseUpdatedTopicName, payload)
    }

    override fun onAfterDelete(event: AfterDeleteEvent<Expense>) {
        super.onAfterDelete(event)

        val payload = ExpenseStateChangedMessage.Deleted(
            expenseId = event.id.value as Long
        )
        messenger.convertAndSend(appProps.aws.expenseDeletedTopicName, payload)
    }
}
