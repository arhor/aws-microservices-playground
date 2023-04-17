package com.github.arhor.aws.microservices.playground.users.web.controller

import com.github.arhor.aws.microservices.playground.users.DEFAULT_BUDGET_LIMIT
import com.github.arhor.aws.microservices.playground.users.config.ConfigureAdditionalBeans
import com.github.arhor.aws.microservices.playground.users.service.UserService
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
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
import org.springframework.test.web.servlet.post
import java.math.BigDecimal

@Tag("contract")
@Import(ConfigureAdditionalBeans::class)
@WebMvcTest(UserController::class)
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
        val expectedEmail = "TestUser.username"
        val expectedPassword = "TestUser.password"
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
        val expectedEmail = "TestUser.username"
        val expectedPassword = "TestUser.password"
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
}
