package com.github.arhor.aws.microservices.playground.users.service

import com.github.arhor.aws.microservices.playground.users.data.model.Budget
import com.github.arhor.aws.microservices.playground.users.data.model.User
import com.github.arhor.aws.microservices.playground.users.data.repository.UserRepository
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.event.UserEventEmitter
import com.github.arhor.aws.microservices.playground.users.service.exception.EntityDuplicateException
import com.github.arhor.aws.microservices.playground.users.service.impl.UserServiceImpl
import com.github.arhor.aws.microservices.playground.users.service.mapper.UserMapper
import io.mockk.Call
import io.mockk.MockKAnswerScope
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.Assertions.from
import org.assertj.core.api.InstanceOfAssertFactories.throwable
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class UserServiceTest {

    private val userMapper: UserMapper = mockk()
    private val userRepository: UserRepository = mockk()
    private val userEventEmitter: UserEventEmitter = mockk()

    private val userService: UserService = UserServiceImpl(
        userMapper,
        userRepository,
        userEventEmitter,
    )

    @Nested
    inner class `UserService # createUser` {
        @Test
        fun `should correctly save new user and return it with assigned id`() {
            // Given
            val expectedId = 1L
            val expectedEmail = "test@email.com"
            val expectedPassword = "TestPassword123"
            val expectedBudgetLimit = BigDecimal("150.00")

            every { userRepository.existsByEmail(any()) } returns false
            every { userMapper.mapToUser(any()) } answers convertingDtoToUser()
            every { userRepository.save(any()) } answers copyingUserWithAssignedId(id = expectedId)
            every { userMapper.mapToUserResponse(any()) } answers convertingUserToDto()

            // When
            val result = userService.createUser(
                UserCreateRequestDto(
                    email = expectedEmail,
                    password = expectedPassword,
                    budgetLimit = expectedBudgetLimit,
                )
            )

            // Then
            assertThat(result)
                .returns(expectedId, from { it.id })
                .returns(expectedEmail, from { it.email })
                .returns(expectedBudgetLimit, from { it.budgetLimit })

            verify(exactly = 1) { userRepository.existsByEmail(expectedEmail) }
        }

        @Test
        fun `should throw EntityDuplicateException creating user with already taken email`() {
            // Given
            val userCreateRequest = UserCreateRequestDto(
                email = "test@email.com",
                password = "TestPassword123",
                budgetLimit = BigDecimal("150.00")
            )
            val expectedEntity = "User"
            val expectedOperation = "CREATE"
            val expectedCondition = "email = ${userCreateRequest.email}"
            val expectedExceptionType = EntityDuplicateException::class.java

            every { userRepository.existsByEmail(any()) } returns true

            // When
            val action = ThrowingCallable { userService.createUser(userCreateRequest) }

            // Then
            assertThatThrownBy(action)
                .isInstanceOf(expectedExceptionType)
                .asInstanceOf(throwable(expectedExceptionType))
                .satisfies(
                    { assertThat(it.entity).describedAs("entity").isEqualTo(expectedEntity) },
                    { assertThat(it.operation).describedAs("operation").isEqualTo(expectedOperation) },
                    { assertThat(it.condition).describedAs("condition").isEqualTo(expectedCondition) },
                )

            verify(exactly = 1) { userRepository.existsByEmail(userCreateRequest.email) }
        }
    }

    private fun convertingDtoToUser(): MockKAnswerScope<User, *>.(Call) -> User = {
        firstArg<UserCreateRequestDto>().let {
            User(
                email = it.email,
                password = it.password,
                budget = Budget(it.budgetLimit),
            )
        }
    }

    private fun copyingUserWithAssignedId(id: Long): MockKAnswerScope<User, *>.(Call) -> User = {
        firstArg<User>().copy(id = id)
    }

    private fun convertingUserToDto(): MockKAnswerScope<UserResponseDto, *>.(Call) -> UserResponseDto = {
        firstArg<User>().let {
            UserResponseDto(
                id = it.id!!,
                email = it.email,
                budgetLimit = it.budget.limit,
            )
        }
    }
}
