package com.github.arhor.aws.microservices.playground.users.data.repository

import com.github.arhor.aws.microservices.playground.users.config.ConfigureAdditionalBeans
import com.github.arhor.aws.microservices.playground.users.config.ConfigureDatabase
import com.github.arhor.aws.microservices.playground.users.config.props.ApplicationProps
import com.github.arhor.aws.microservices.playground.users.data.model.Budget
import com.github.arhor.aws.microservices.playground.users.data.model.User
import com.github.arhor.aws.microservices.playground.users.data.model.listener.UserStateChangedEventListener
import com.github.arhor.aws.microservices.playground.users.data.model.listener.UserStateChangedMessage
import com.ninjasquad.springmockk.MockkBean
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.from
import org.assertj.core.api.InstanceOfAssertFactories.type
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal

@Tag("integration")
@DataJdbcTest(
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE, classes = [
                ConfigureDatabase::class,
                ConfigureAdditionalBeans::class,
                UserStateChangedEventListener::class,
            ]
        )
    ]
)
@DirtiesContext
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableConfigurationProperties(ApplicationProps::class)
internal class UserRepositoryIntegrationTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @MockkBean
    private lateinit var messenger: NotificationMessagingTemplate

    @BeforeEach
    fun setUp() {
        every { messenger.convertAndSend<UserStateChangedMessage>(any(), any()) } just runs
    }

    @Test
    fun `should return true for the email of an existing user`() {
        // Given
        val existingUser = userRepository.save(
            User(
                email = "test1@email.com",
                password = "TestPassword123",
                budget = Budget(
                    limit = BigDecimal("10.00")
                )
            )
        )

        // When
        val result = userRepository.existsByEmail(existingUser.email)

        // Then
        assertThat(result)
            .isTrue()
    }

    @Test
    fun `should return false for the email of a non-existing user`() {
        // Given
        val notPersistedUser = User(
            email = "test2@email.com",
            password = "TestPassword123",
            budget = Budget(
                limit = BigDecimal("10.00")
            )
        )

        // When
        val result = userRepository.existsByEmail(notPersistedUser.email)

        // Then
        assertThat(result)
            .isFalse()
    }

    @Test
    fun `should send UserStateChangedMessage$Updated message on User entity save`() {
        // Given
        val newUser = User(
            email = "test2@email.com",
            password = "TestPassword123",
            budget = Budget(
                limit = BigDecimal("10.00")
            )
        )
        val slot = slot<UserStateChangedMessage>()

        // When
        val createdUser = userRepository.save(newUser)

        // Then
        verify(exactly = 1) { messenger.convertAndSend(USER_UPDATED_TEST_TOPIC, capture(slot)) }

        assertThat(slot.captured)
            .asInstanceOf(type(UserStateChangedMessage.Updated::class.java))
            .returns(createdUser.id, from { it.userId })
            .returns(createdUser.email, from { it.email })
            .returns(createdUser.budget.limit, from { it.budgetLimit })
    }

    companion object {
        private const val USER_UPDATED_TEST_TOPIC = "user-updated-test-topic"
        private const val USER_DELETED_TEST_TOPIC = "user-deleted-test-topic"

        @JvmStatic
        @Container
        private val db = PostgreSQLContainer("postgres:12")

        @JvmStatic
        @DynamicPropertySource
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            with(registry) {
                add("spring.datasource.url", db::getJdbcUrl)
                add("spring.datasource.username", db::getUsername)
                add("spring.datasource.password", db::getPassword)
                add("application-props.aws.user-updated-topic-name") { USER_UPDATED_TEST_TOPIC }
                add("application-props.aws.user-deleted-topic-name") { USER_DELETED_TEST_TOPIC }
            }
        }
    }
}
