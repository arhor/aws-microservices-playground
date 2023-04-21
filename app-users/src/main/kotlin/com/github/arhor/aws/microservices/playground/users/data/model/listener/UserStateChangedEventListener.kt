package com.github.arhor.aws.microservices.playground.users.data.model.listener

import com.github.arhor.aws.microservices.playground.users.config.props.ApplicationProps
import com.github.arhor.aws.microservices.playground.users.data.model.User
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
class UserStateChangedEventListener(
    private val appProps: ApplicationProps,
    private val messenger: NotificationMessagingTemplate,
) : AbstractRelationalEventListener<User>() {


    override fun onAfterSave(event: AfterSaveEvent<User>) {
        val payload = event.entity.let { (id, email, _, budget) ->
            UserStateChangedMessage.Updated(
                userId = id!!,
                email = email,
                budgetLimit = budget.limit
            )
        }

        messenger.convertAndSend(appProps.aws.userUpdatedTopicName, payload)
    }

    override fun onAfterDelete(event: AfterDeleteEvent<User>) {
        val payload = UserStateChangedMessage.Deleted(
            userId = event.id.value as Long
        )

        messenger.convertAndSend(appProps.aws.userDeletedTopicName, payload)
    }
}
