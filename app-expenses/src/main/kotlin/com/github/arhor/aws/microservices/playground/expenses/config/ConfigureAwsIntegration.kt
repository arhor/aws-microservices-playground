package com.github.arhor.aws.microservices.playground.expenses.config

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.github.arhor.aws.microservices.playground.expenses.config.props.ApplicationProps
import jakarta.jms.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class ConfigureAwsIntegration(private val applicationProps: ApplicationProps) {

    @Bean
    fun awsCredentialsProvider(): AWSCredentialsProvider {
        return AWSStaticCredentialsProvider(
            BasicAWSCredentials(
                applicationProps.aws.accessKey,
                applicationProps.aws.secretKey,
            )
        )
    }

    @Bean
    fun endpointConfiguration(): EndpointConfiguration {
        return EndpointConfiguration(
            applicationProps.aws.url,
            applicationProps.aws.region,
        )
    }

    @Bean
    fun amazonSQS(credentials: AWSCredentialsProvider, endpointConfiguration: EndpointConfiguration): AmazonSQS {
        return AmazonSQSClientBuilder
            .standard()
            .withCredentials(credentials)
            .withEndpointConfiguration(endpointConfiguration)
            .build()
    }

    @Bean
    fun sqsConnectionFactory(amazonSQS: AmazonSQS): ConnectionFactory {
        return SQSConnectionFactory(
            ProviderConfiguration(),
            amazonSQS,
        )
    }
}
