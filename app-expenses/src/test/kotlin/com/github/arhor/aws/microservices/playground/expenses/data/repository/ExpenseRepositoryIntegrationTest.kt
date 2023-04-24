package com.github.arhor.aws.microservices.playground.expenses.data.repository

import com.github.arhor.aws.microservices.playground.expenses.config.ConfigureAdditionalBeans
import com.github.arhor.aws.microservices.playground.expenses.config.ConfigureDatabase
import com.github.arhor.aws.microservices.playground.expenses.config.props.ApplicationProps
import com.github.arhor.aws.microservices.playground.expenses.data.model.Expense
import com.github.arhor.aws.microservices.playground.expenses.data.model.listener.ExpenseStateChangedEventListener
import com.github.arhor.aws.microservices.playground.expenses.data.model.listener.ExpenseStateChangedMessage
import com.github.arhor.aws.microservices.playground.expenses.data.model.projection.BudgetOverrunDetails
import com.ninjasquad.springmockk.MockkBean
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.from
import org.assertj.core.api.InstanceOfAssertFactories
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.sql.Date
import java.sql.Statement
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.streams.toList

@Tag("integration")
@DataJdbcTest(
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE, classes = [
                ConfigureDatabase::class,
                ConfigureAdditionalBeans::class,
                ExpenseStateChangedEventListener::class,
            ]
        )
    ]
)
@DirtiesContext
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableConfigurationProperties(ApplicationProps::class)
internal class ExpenseRepositoryIntegrationTest {

    @Autowired
    private lateinit var expenseRepository: ExpenseRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @MockkBean
    private lateinit var messenger: NotificationMessagingTemplate

    @BeforeEach
    fun setUp() {
        every { messenger.convertAndSend<ExpenseStateChangedMessage>(any(), any()) } just runs
    }

    @Test
    fun `should return expected list of budget overruns within the given period without excluded users`() {
        // Given
        val dateFrom = LocalDate.of(2023, 3, 1)
        val dateTill = LocalDate.of(2023, 3, 30)

        val expectedExpenses = listOf(
            Expense(userId = 1, amount = BigDecimal("10.11"), date = dateFrom.plusDays(1)),
            Expense(userId = 1, amount = BigDecimal("20.22"), date = dateFrom.plusDays(2)),
            Expense(userId = 2, amount = BigDecimal("15.33"), date = dateFrom.plusDays(3)),
            Expense(userId = 2, amount = BigDecimal("15.44"), date = dateFrom.plusDays(4)),
        )
        val incorrectExpenses = listOf(
            Expense(userId = 1, amount = BigDecimal("11.11"), date = dateTill.plusDays(1)),
            Expense(userId = 2, amount = BigDecimal("22.22"), date = dateTill.plusDays(2)),
            Expense(userId = 3, amount = BigDecimal("33.33"), date = dateTill),
        )

        expenseRepository.saveAll(expectedExpenses + incorrectExpenses)

        // When
        val budgetOverrunDetails = expenseRepository
            .findBudgetOverrunsWithinDateRange(
                threshold = BigDecimal("30.00"),
                dateFrom = dateFrom,
                dateTill = dateTill,
                skipUserIds = listOf(3),
            )
            .use { it.toList() }

        // Then
        assertThat(budgetOverrunDetails)
            .containsExactlyInAnyOrder(
                BudgetOverrunDetails(userId = 1, amount = "0.33"),
                BudgetOverrunDetails(userId = 2, amount = "0.77"),
            )
    }

    @Test
    fun `should return expected list of expenses within the given period without excluded users`() {
        // Given
        val dateFrom = LocalDate.of(2023, 3, 1)
        val dateTill = LocalDate.of(2023, 3, 30)

        val expectedExpenses = expenseRepository.saveAll(
            listOf(
                Expense(userId = 1, amount = BigDecimal("10.11"), date = dateFrom.plusDays(1)),
                Expense(userId = 1, amount = BigDecimal("20.22"), date = dateFrom.plusDays(2)),
                Expense(userId = 2, amount = BigDecimal("15.33"), date = dateFrom.plusDays(3)),
                Expense(userId = 2, amount = BigDecimal("15.44"), date = dateFrom.plusDays(4)),
            )
        )
        val incorrectExpenses = expenseRepository.saveAll(
            listOf(
                Expense(userId = 1, amount = BigDecimal("11.11"), date = dateTill.plusDays(1)),
                Expense(userId = 2, amount = BigDecimal("22.22"), date = dateTill.plusDays(2)),
                Expense(userId = 3, amount = BigDecimal("33.33"), date = dateTill),
            )
        )

        // When
        val result = expenseRepository
            .findAllWithinDateRangeSkippingUserIds(
                skipUids = listOf(3),
                dateFrom = dateFrom,
                dateTill = dateTill,
            )
            .use { it.toList() }

        // Then
        assertThat(result)
            .containsExactlyElementsOf(expectedExpenses)
            .doesNotContainAnyElementsOf(incorrectExpenses)
    }

    @Test
    fun `should delete expenses associated with passed user id and return number deleted rows`() {
        // Given
        val userIdToDelete = 1L
        val userIdToRetain = 2L

        val today = LocalDate.now()
        val value = BigDecimal("1.00")

        val expensesToDelete = expenseRepository.saveAll((1..10).map {
            Expense(
                date = today,
                amount = value,
                userId = userIdToDelete,
            )
        }).toList()
        val expensesToRetain = expenseRepository.saveAll((1..10).map {
            Expense(
                date = today,
                amount = value,
                userId = userIdToRetain
            )
        }).toList()

        // When
        val deletedRowsNumber = expenseRepository.deleteByUserIdReturningNumberRecordsAffected(userIdToDelete)
        val remainingExpenses = expenseRepository.findAll()

        // Then
        assertThat(deletedRowsNumber)
            .isEqualTo(expensesToDelete.size)

        assertThat(remainingExpenses)
            .containsExactlyInAnyOrderElementsOf(expensesToRetain)
    }

    @Test
    fun `should send ExpenseStateChangedMessage$Updated message on a new Expense entity save`() {
        // Given
        val newExpense = Expense(
            date = LocalDate.now(),
            amount = BigDecimal("10.00"),
            userId = 1L,
        )
        val stateChangedMessage = slot<ExpenseStateChangedMessage>()

        // When
        val createdExpense = expenseRepository.save(newExpense)

        // Then
        verify(exactly = 1) { messenger.convertAndSend(EXPENSE_UPDATED_TEST_TOPIC, capture(stateChangedMessage)) }

        assertThat(stateChangedMessage.captured)
            .asInstanceOf(InstanceOfAssertFactories.type(ExpenseStateChangedMessage.Updated::class.java))
            .returns(createdExpense.id, from { it.expenseId })
            .returns(createdExpense.date, from { it.date })
            .returns(createdExpense.amount, from { it.amount })
            .returns(createdExpense.userId, from { it.userId })
    }

    @Test
    fun `should send ExpenseStateChangedMessage$Updated message on an existing Expense entity save`() {
        // Given
        val expenseId = createExpenseUsingJDBC(
            date = LocalDate.now(),
            amount = BigDecimal("10.00"),
            userId = 1L,
        )
        val existingExpense = expenseRepository.findByIdOrNull(expenseId)!!
        val stateChangedMessage = slot<ExpenseStateChangedMessage>()

        // When
        val updatedExpense = expenseRepository.save(existingExpense.copy(amount = BigDecimal("20.00")))

        // Then
        verify(exactly = 1) { messenger.convertAndSend(EXPENSE_UPDATED_TEST_TOPIC, capture(stateChangedMessage)) }

        assertThat(stateChangedMessage.captured)
            .asInstanceOf(InstanceOfAssertFactories.type(ExpenseStateChangedMessage.Updated::class.java))
            .returns(updatedExpense.id, from { it.expenseId })
            .returns(updatedExpense.date, from { it.date })
            .returns(updatedExpense.amount, from { it.amount })
            .returns(updatedExpense.userId, from { it.userId })
    }

    @Test
    fun `should send ExpenseStateChangedMessage$Deleted message on Expense entity delete`() {
        // Given
        val expenseId = createExpenseUsingJDBC(
            date = LocalDate.now(),
            amount = BigDecimal("10.00"),
            userId = 1L,
        )
        val existingExpense = expenseRepository.findByIdOrNull(expenseId)!!

        // When
        expenseRepository.delete(existingExpense)

        // Then
        verify(exactly = 1) {
            messenger.convertAndSend(
                EXPENSE_DELETED_TEST_TOPIC,
                ExpenseStateChangedMessage.Deleted(expenseId),
            )
        }
    }

    @Test
    fun `should send ExpenseStateChangedMessage$Deleted message on Expense entity delete by id`() {
        // Given
        val expenseId = createExpenseUsingJDBC(
            date = LocalDate.now(),
            amount = BigDecimal("10.00"),
            userId = 1L,
        )

        // When
        expenseRepository.deleteById(expenseId)

        // Then
        verify(exactly = 1) {
            messenger.convertAndSend(
                EXPENSE_DELETED_TEST_TOPIC,
                ExpenseStateChangedMessage.Deleted(expenseId),
            )
        }
    }

    @Suppress("UNUSED_CHANGED_VALUE")
    private fun createExpenseUsingJDBC(date: LocalDate, amount: BigDecimal, userId: Long): Long {
        val initialVersion = 1L
        val currentTimestamp = Timestamp.valueOf(LocalDateTime.now())
        val generatedKeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({
            it.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS).apply {
                var idx = 1
                // @formatter:off
                      setDate(idx++, Date.valueOf(date))
                setBigDecimal(idx++, amount)
                      setLong(idx++, userId)
                      setLong(idx++, initialVersion)
                 setTimestamp(idx++, currentTimestamp)
                // @formatter:on
            }
        }, generatedKeyHolder)

        return generatedKeyHolder.keys!!["id"] as Long
    }

    companion object {
        private const val EXPENSE_UPDATED_TEST_TOPIC = "expense-updated-test-topic"
        private const val EXPENSE_DELETED_TEST_TOPIC = "expense-deleted-test-topic"

        /* language=SQL */
        private val INSERT_QUERY = """
            INSERT INTO "expenses" (
                "date",
                "amount",
                "user_id",
                "version",
                "created_date_time"
            ) VALUES (?, ?, ?, ?, ?);
        """.trimIndent()

        @JvmStatic
        @Container
        private val db = PostgreSQLContainer("postgres:12")

        @JvmStatic
        @DynamicPropertySource
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.run {
                add("spring.datasource.url", db::getJdbcUrl)
                add("spring.datasource.username", db::getUsername)
                add("spring.datasource.password", db::getPassword)
                add("application-props.aws.expense-updated-topic-name") { EXPENSE_UPDATED_TEST_TOPIC }
                add("application-props.aws.expense-deleted-topic-name") { EXPENSE_DELETED_TEST_TOPIC }
            }
        }
    }
}
