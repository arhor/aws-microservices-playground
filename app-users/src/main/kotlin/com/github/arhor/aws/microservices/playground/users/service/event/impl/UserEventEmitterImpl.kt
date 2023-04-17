package com.github.arhor.aws.microservices.playground.users.service.event.impl

import com.github.arhor.aws.microservices.playground.users.service.event.UserEvent
import com.github.arhor.aws.microservices.playground.users.service.event.UserEventEmitter
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.MessagingException
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class UserEventEmitterImpl(
    @Value("\${application-props.aws.user-deleted-topic-name}")
    private val userDeletedTopicName: String,
    private val notificationMessagingTemplate: NotificationMessagingTemplate,
) : UserEventEmitter {

    @Retryable(
        include = [MessagingException::class],
        maxAttemptsExpression = "\${application-props.retry-attempts:3}"
    )
    override fun emit(event: UserEvent) {
        val targetTopicName = when (event) {
            is UserEvent.Deleted -> userDeletedTopicName
        }
        notificationMessagingTemplate.convertAndSend(targetTopicName, event)
    }
}
