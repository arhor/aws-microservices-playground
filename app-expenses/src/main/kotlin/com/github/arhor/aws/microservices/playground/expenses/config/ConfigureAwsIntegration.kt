package com.github.arhor.aws.microservices.playground.expenses.config

import com.amazonaws.services.sns.AmazonSNS
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class ConfigureAwsIntegration {

    @Bean
    fun notificationMessagingTemplate(amazonSNS: AmazonSNS): NotificationMessagingTemplate {
        return NotificationMessagingTemplate(amazonSNS)
    }
}
