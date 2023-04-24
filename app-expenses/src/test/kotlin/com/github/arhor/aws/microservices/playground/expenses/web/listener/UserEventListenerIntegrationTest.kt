package com.github.arhor.aws.microservices.playground.expenses.web.listener

import com.github.arhor.aws.microservices.playground.expenses.service.ExpenseService
import com.ninjasquad.springmockk.MockkBean
import io.awspring.cloud.messaging.core.QueueMessagingTemplate
import io.awspring.cloud.messaging.listener.SimpleMessageListenerContainer
import io.awspring.cloud.test.sqs.SqsTest
import io.mockk.verify
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Tag("integration")
@SqsTest(UserEventListener::class)
@Testcontainers(disabledWithoutDocker = true)
internal class UserEventListenerIntegrationTest {

    @Autowired
    private lateinit var queueMessagingTemplate: QueueMessagingTemplate

    @MockkBean(relaxUnitFun = true)
    private lateinit var expenseService: ExpenseService

    @Test
    fun `should send an event to SQS then correctly consume it via UserDeletedEventListener instance`() {
        // Given
        val event = UserEvent.Deleted(userId = 1L)

        // When
        queueMessagingTemplate.convertAndSend(TEST_QUEUE_NAME, event)

        // Then
        verify(exactly = 1, timeout = 3000) { expenseService.deleteUserExpenses(event.userId) }
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
            registry.add("cloud.aws.region.static") { localstack.region }
            registry.add("cloud.aws.credentials.access-key") { localstack.accessKey }
            registry.add("cloud.aws.credentials.secret-key") { localstack.secretKey }
            registry.add("cloud.aws.sqs.endpoint") { localstack.getEndpointOverride(SQS) }
            registry.add("application-props.aws.user-deleted-queue-name") { TEST_QUEUE_NAME }
        }

        @JvmStatic
        @BeforeAll
        fun setUpClass() {
            localstack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", TEST_QUEUE_NAME)
        }

        @JvmStatic
        @AfterAll
        fun tearDownClass(@Autowired messageListenerContainer: SimpleMessageListenerContainer) {
            messageListenerContainer.stop()
        }
    }
}
