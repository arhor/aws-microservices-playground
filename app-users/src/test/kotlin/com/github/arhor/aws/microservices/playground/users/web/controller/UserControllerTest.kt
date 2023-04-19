package com.github.arhor.aws.microservices.playground.users.web.controller

import com.github.arhor.aws.microservices.playground.users.DEFAULT_BUDGET_LIMIT
import com.github.arhor.aws.microservices.playground.users.Operation
import com.github.arhor.aws.microservices.playground.users.config.ConfigureAdditionalBeans
import com.github.arhor.aws.microservices.playground.users.service.UserService
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserUpdateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.exception.EntityDuplicateException
import com.github.arhor.aws.microservices.playground.users.service.exception.EntityNotFoundException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.from
import org.hamcrest.Matchers.endsWith
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.math.BigDecimal

@Tag("contract")
@Import(
    value = [
        ConfigureAdditionalBeans::class,
    ]
)
@WebMvcTest(
    controllers = [
        UserController::class,
    ]
)
internal class UserControllerTest {

    @Autowired
    private lateinit var http: MockMvc

    @MockkBean
    private lateinit var userService: UserService

    @Test
    fun `should return status 201, user info and location header creating new user`() {
        // Given
        val user = slot<UserCreateRequestDto>()

        val expectedId = 1L
        val expectedEmail = "test@email.com"
        val expectedPassword = "TestPassword123"
        val expectedBudgetLimit = BigDecimal("150")

        every { userService.createUser(createRequest = any()) } answers {
            UserResponseDto(
                id = expectedId,
                email = expectedEmail,
                budgetLimit = expectedBudgetLimit,
            )
        }

        // When
        val awaitResult = http.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "email": "$expectedEmail",
                    "password": "$expectedPassword",
                    "budgetLimit" : "$expectedBudgetLimit"
                }
            """.trimIndent()
        }

        // Then
        verify(exactly = 1) { userService.createUser(createRequest = capture(user)) }

        assertThat(user.captured)
            .returns(expectedEmail, from { it.email })
            .returns(expectedPassword, from { it.password })
            .returns(expectedBudgetLimit, from { it.budgetLimit })

        awaitResult
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                header { string("Location", endsWith("/users/1")) }
                jsonPath("$.id") { value(expectedId) }
                jsonPath("$.email") { value(expectedEmail) }
                jsonPath("$.budgetLimit") { value(expectedBudgetLimit) }
            }
    }

    @Test
    fun `should return status 201, user info and location header creating new user with default budget limit`() {
        // Given
        val user = slot<UserCreateRequestDto>()

        val expectedId = 1L
        val expectedEmail = "test@email.com"
        val expectedPassword = "TestPassword123"
        val expectedBudgetLimit = DEFAULT_BUDGET_LIMIT

        every { userService.createUser(createRequest = any()) } answers {
            UserResponseDto(
                id = expectedId,
                email = expectedEmail,
                budgetLimit = expectedBudgetLimit,
            )
        }

        // When
        val awaitResult = http.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "email": "$expectedEmail",
                    "password": "$expectedPassword"
                }
            """.trimIndent()
        }

        // Then
        verify(exactly = 1) { userService.createUser(createRequest = capture(user)) }

        assertThat(user.captured)
            .returns(expectedEmail, from { it.email })
            .returns(expectedPassword, from { it.password })
            .returns(expectedBudgetLimit, from { it.budgetLimit })

        awaitResult
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                header { string("Location", endsWith("/users/1")) }
                jsonPath("$.id") { value(expectedId) }
                jsonPath("$.email") { value(expectedEmail) }
                jsonPath("$.budgetLimit") { value(expectedBudgetLimit) }
            }
    }

    @Test
    fun `should return status 406 and error info creating new user with already taken email`() {
        // Given
        val user = slot<UserCreateRequestDto>()

        val expectedEmail = "test@email.com"
        val expectedPassword = "TestPassword123"
        val expectedBudgetLimit = DEFAULT_BUDGET_LIMIT

        every { userService.createUser(createRequest = any()) } answers {
            throw EntityDuplicateException(
                entity = "User",
                condition = "email=${arg<UserCreateRequestDto>(0).email}",
                operation = Operation.CREATE,
            )
        }

        // When
        val awaitResult = http.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "email": "$expectedEmail",
                    "password": "$expectedPassword"
                }
            """.trimIndent()
        }

        // Then
        verify(exactly = 1) { userService.createUser(createRequest = capture(user)) }

        assertThat(user.captured)
            .returns(expectedEmail, from { it.email })
            .returns(expectedPassword, from { it.password })
            .returns(expectedBudgetLimit, from { it.budgetLimit })

        awaitResult
            .andDo { print() }
            .andExpect {
                status { isConflict() }
                jsonPath("$.code") { exists() }
                jsonPath("$.message") { exists() }
                jsonPath("$.details") { exists() }
                jsonPath("$.timestamp") { exists() }
            }
    }

    @Test
    fun `should return status 200 and user info updating an existing user`() {
        // Given
        val user = slot<UserUpdateRequestDto>()

        val expectedId = 1L
        val expectedEmail = "test@email.com"
        val expectedPassword = "TestPassword123"
        val expectedBudgetLimit = BigDecimal("-10.0")

        every { userService.updateUser(userId = any(), updateRequest = any()) } answers {
            UserResponseDto(
                id = expectedId,
                email = expectedEmail,
                budgetLimit = expectedBudgetLimit,
            )
        }

        // When
        val awaitResult = http.patch("/users/$expectedId") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "password": "$expectedPassword",
                    "budgetLimit": $expectedBudgetLimit
                }
            """
        }

        // Then
        verify(exactly = 1) { userService.updateUser(userId = 1L, updateRequest = capture(user)) }

        assertThat(user.captured)
            .returns(expectedPassword, from { it.password })
            .returns(expectedBudgetLimit, from { it.budgetLimit })

        awaitResult
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(expectedId) }
                jsonPath("$.email") { value(expectedEmail) }
                jsonPath("$.budgetLimit") { value(expectedBudgetLimit) }
            }
    }

    @Test
    fun `should return status 404 and error info updating a non-existing user`() {
        // Given
        val user = slot<UserUpdateRequestDto>()

        val expectedId = 1L
        val expectedPassword = "TestPassword123"
        val expectedBudgetLimit = DEFAULT_BUDGET_LIMIT

        every { userService.updateUser(userId = any(), updateRequest = any()) } answers {
            throw EntityNotFoundException(
                entity = "User",
                condition = "id=${arg<Long>(0)}",
                operation = Operation.UPDATE,
            )
        }

        // When
        val awaitResult = http.patch("/users/{userId}", expectedId) {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "password": "$expectedPassword",
                    "budgetLimit": $expectedBudgetLimit
                }
            """
        }

        // Then
        verify(exactly = 1) { userService.updateUser(userId = expectedId, updateRequest = capture(user)) }

        assertThat(user.captured)
            .returns(expectedPassword, from { it.password })
            .returns(expectedBudgetLimit, from { it.budgetLimit })

        awaitResult
            .andDo { print() }
            .andExpect {
                status { isNotFound() }
                jsonPath("$.code") { exists() }
                jsonPath("$.message") { exists() }
                jsonPath("$.details") { exists() }
                jsonPath("$.timestamp") { exists() }
            }
    }
}
