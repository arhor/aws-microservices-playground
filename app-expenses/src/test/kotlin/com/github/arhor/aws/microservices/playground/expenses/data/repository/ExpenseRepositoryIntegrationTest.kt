package com.github.arhor.aws.microservices.playground.expenses.data.repository

import com.github.arhor.aws.microservices.playground.expenses.config.ConfigureDatabase
import com.github.arhor.aws.microservices.playground.expenses.data.model.Expense
import com.github.arhor.aws.microservices.playground.expenses.data.model.projection.BudgetOverrunDetails
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.time.LocalDate

@Tag("integration")
@DataJdbcTest
@DirtiesContext
@Testcontainers(disabledWithoutDocker = true)
@ContextConfiguration(classes = [ConfigureDatabase::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class ExpenseRepositoryIntegrationTest {

    @Autowired
    private lateinit var expenseRepository: ExpenseRepository

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
        })
        val expensesToRetain = expenseRepository.saveAll((1..10).map {
            Expense(
                date = today,
                amount = value,
                userId = userIdToRetain
            )
        })

        // When
        val deletedRowsNumber = expenseRepository.deleteByUserId(userIdToDelete)
        val remainingExpenses = expenseRepository.findAll()

        // Then
        assertThat(deletedRowsNumber)
            .isEqualTo(expensesToDelete.size)

        assertThat(remainingExpenses)
            .containsExactlyInAnyOrderElementsOf(expensesToRetain)
    }

    companion object {
        @JvmStatic
        @Container
        private val db = PostgreSQLContainer("postgres:12")

        @JvmStatic
        @DynamicPropertySource
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", db::getJdbcUrl)
            registry.add("spring.datasource.username", db::getUsername)
            registry.add("spring.datasource.password", db::getPassword)
        }
    }
}
