package com.github.arhor.aws.microservices.playground.expenses.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.arhor.aws.microservices.playground.expenses.web.listener.UserDeletedEvent
import jakarta.jms.ConnectionFactory
import jakarta.jms.Session
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import org.springframework.jms.support.destination.DestinationResolver
import org.springframework.jms.support.destination.DynamicDestinationResolver

@EnableJms
@Configuration(proxyBeanMethods = false)
class ConfigureMessaging {

    @Bean
    fun dynamicDestinationResolver(): DynamicDestinationResolver {
        return DynamicDestinationResolver()
    }

    @Bean
    fun jacksonJmsMessageConverter(): MappingJackson2MessageConverter {

        return MappingJackson2MessageConverter().also {
            it.setTargetType(MessageType.TEXT)
            it.setTypeIdPropertyName("_type")
            it.setObjectMapper(jacksonObjectMapper())
            it.setTypeIdMappings(mapOf("user-deleted-event" to UserDeletedEvent::class.java))
        }
    }

    @Bean
    fun jmsListenerContainerFactory(
        destinationResolver: DestinationResolver,
        connectionFactory: ConnectionFactory,
        messageConverter: MessageConverter,
    ): DefaultJmsListenerContainerFactory {

        return DefaultJmsListenerContainerFactory().also {
            it.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE)
            it.setDestinationResolver(destinationResolver)
            it.setConnectionFactory(connectionFactory)
            it.setMessageConverter(messageConverter)
            it.setConcurrency("3-10")
        }
    }
}
