package com.github.arhor.aws.microservices.playground.expenses.web.listener

import com.github.arhor.aws.microservices.playground.expenses.config.ConfigureAwsIntegration
import com.github.arhor.aws.microservices.playground.expenses.config.ConfigureMessaging
import com.github.arhor.aws.microservices.playground.expenses.service.ExpenseService
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration
import org.springframework.jms.core.JmsTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@ExtendWith(SpringExtension::class)
@Testcontainers(disabledWithoutDocker = true)
@ContextConfiguration(
    classes = [
        ConfigureAwsIntegration::class,
        ConfigureMessaging::class,
        JmsAutoConfiguration::class,
    ]
)
@MockkBean(classes = [ExpenseService::class], relaxUnitFun = true)
class UserDeletedEventListenerTest {

    @SpykBean
    private lateinit var userDeletedEventListener: UserDeletedEventListener

    @Autowired
    private lateinit var jmsTemplate: JmsTemplate

    @Test
    fun `should send an event to SQS then correctly consume it via UserDeletedEventListener instance`() {
        // Given
        val event = UserDeletedEvent(userId = 1L)

        // When
        jmsTemplate.convertAndSend(TEST_QUEUE_NAME, event)

        // Then
        verify(exactly = 1, timeout = 3000) { userDeletedEventListener.deleteUserExpenses(event) }
    }

    companion object {
        private const val TEST_QUEUE_NAME = "user-deleted-events-test-queue"

        @JvmStatic
        @Container
        private val localstack =
            LocalStackContainer(DockerImageName.parse("localstack/localstack:1.4.0"))
                .withServices(SQS)

        @JvmStatic
        @DynamicPropertySource
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("application-props.aws.url") { localstack.getEndpointOverride(SQS) }
            registry.add("application-props.aws.region") { localstack.region }
            registry.add("application-props.aws.access-key") { localstack.accessKey }
            registry.add("application-props.aws.secret-key") { localstack.secretKey }
            registry.add("application-props.aws.user-deleted-queue-name") { TEST_QUEUE_NAME }
        }

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            localstack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", TEST_QUEUE_NAME)
        }
    }
}
