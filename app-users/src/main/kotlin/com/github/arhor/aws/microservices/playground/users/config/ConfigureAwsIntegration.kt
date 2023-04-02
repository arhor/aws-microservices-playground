package com.github.arhor.aws.microservices.playground.users.config

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class ConfigureAwsIntegration {

    @Value("\${application-props.aws.url}")
    private lateinit var awsUrl: String

    @Value("\${application-props.aws.region}")
    private lateinit var awsRegion: String

    @Value("\${application-props.aws.access-key}")
    private lateinit var awsAccessKey: String

    @Value("\${application-props.aws.secret-key}")
    private lateinit var awsSecretKey: String

    @Bean
    fun awsCredentialsProvider(): AWSStaticCredentialsProvider =
        AWSStaticCredentialsProvider(
            BasicAWSCredentials(
                awsAccessKey,
                awsSecretKey,
            )
        )

    @Bean
    fun endpointConfiguration(): EndpointConfiguration =
        EndpointConfiguration(
            awsUrl,
            awsRegion,
        )

    @Bean
    fun amazonSQS(credentials: AWSCredentialsProvider, endpointConfiguration: EndpointConfiguration): AmazonSQS =
        AmazonSQSClientBuilder
            .standard()
            .withCredentials(credentials)
            .withEndpointConfiguration(endpointConfiguration)
            .build()

    @Bean
    fun sqsConnectionFactory(amazonSQS: AmazonSQS): SQSConnectionFactory =
        SQSConnectionFactory(
            ProviderConfiguration(),
            amazonSQS,
        )
}
