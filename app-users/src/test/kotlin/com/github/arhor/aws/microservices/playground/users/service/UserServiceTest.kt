package com.github.arhor.aws.microservices.playground.users.service

import com.github.arhor.aws.microservices.playground.users.data.model.Budget
import com.github.arhor.aws.microservices.playground.users.data.model.User
import com.github.arhor.aws.microservices.playground.users.data.repository.UserRepository
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserUpdateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.event.UserEvent
import com.github.arhor.aws.microservices.playground.users.service.event.UserEventEmitter
import com.github.arhor.aws.microservices.playground.users.service.exception.EntityDuplicateException
import com.github.arhor.aws.microservices.playground.users.service.exception.EntityNotFoundException
import com.github.arhor.aws.microservices.playground.users.service.impl.UserServiceImpl
import com.github.arhor.aws.microservices.playground.users.service.mapper.UserMapper
import io.mockk.Call
import io.mockk.MockKAnswerScope
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchException
import org.assertj.core.api.Assertions.from
import org.assertj.core.api.InstanceOfAssertFactories.throwable
import org.assertj.core.api.InstanceOfAssertFactories.type
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.Optional

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

            val userCreateRequest = UserCreateRequestDto(
                email = expectedEmail,
                password = expectedPassword,
                budgetLimit = expectedBudgetLimit,
            )

            every { userRepository.existsByEmail(any()) } returns false
            every { userMapper.mapToUser(any()) } answers convertingDtoToUser()
            every { userRepository.save(any()) } answers copyingUserWithAssignedId(id = expectedId)
            every { userMapper.mapEntityToResponseDTO(any()) } answers convertingUserToDto()

            // When
            val result = userService.createUser(userCreateRequest)

            // Then
            assertThat(result)
                .returns(expectedId, from { it.id })
                .returns(expectedEmail, from { it.email })
                .returns(expectedBudgetLimit, from { it.budgetLimit })

            verify(exactly = 1) { userRepository.existsByEmail(expectedEmail) }
            verify(exactly = 1) { userMapper.mapToUser(userCreateRequest) }
            verify(exactly = 1) { userRepository.save(any()) }
            verify(exactly = 1) { userMapper.mapEntityToResponseDTO(any()) }
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
            val result = catchException { userService.createUser(userCreateRequest) }

            // Then
            assertThat(result)
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

    @Nested
    inner class `UserService # updateUser` {
        @Test
        fun `should correctly update an existing user and return it with updated fields`() {
            // Given
            val expectedId = 1L
            val expectedEmail = "test@email.com"
            val expectedPassword = "UpdatedPassword123"
            val expectedBudgetLimit = BigDecimal("150.00")
            val expectedUserEventType = UserEvent.Updated::class.java

            val initialBudgetLimit = BigDecimal("100.00")
            val initialPassword = "InitialPassword123"

            val initialUser = User(
                id = expectedId,
                email = expectedEmail,
                password = initialPassword,
                budget = Budget(limit = initialBudgetLimit)
            )

            val userOnSave = slot<User>()
            val userEventOnSave = slot<UserEvent>()

            every { userRepository.findById(any()) } returns Optional.of(initialUser)
            every { userRepository.save(any()) } answers copyingUser()
            every { userEventEmitter.emit(any()) } just runs
            every { userMapper.mapEntityToResponseDTO(any()) } answers convertingUserToDto()

            // When
            val result = userService.updateUser(
                expectedId,
                UserUpdateRequestDto(
                    password = expectedPassword,
                    budgetLimit = expectedBudgetLimit,
                )
            )

            // Then
            verify(exactly = 1) { userRepository.findById(expectedId) }
            verify(exactly = 1) { userRepository.save(capture(userOnSave)) }
            verify(exactly = 1) { userEventEmitter.emit(capture(userEventOnSave)) }
            verify(exactly = 1) { userMapper.mapEntityToResponseDTO(userOnSave.captured) }

            assertThat(result)
                .returns(expectedId, from { it.id })
                .returns(expectedEmail, from { it.email })
                .returns(expectedBudgetLimit, from { it.budgetLimit })

            assertThat(userOnSave.captured)
                .returns(expectedId, from { it.id })
                .returns(expectedEmail, from { it.email })
                .returns(expectedPassword, from { it.password })
                .returns(expectedBudgetLimit, from { it.budget.limit })

            assertThat(userEventOnSave.captured)
                .isInstanceOf(expectedUserEventType)
                .asInstanceOf(type(expectedUserEventType))
                .returns(expectedId, from { it.userId })
        }

        @Test
        fun `should throw EntityNotFoundException updating a non-existing user`() {
            // Given
            val userCreateRequest = UserUpdateRequestDto(
                password = "TestPassword123",
                budgetLimit = BigDecimal("150.00")
            )
            val expectedId = 1L
            val expectedEntity = "User"
            val expectedOperation = "UPDATE"
            val expectedCondition = "id = $expectedId"
            val expectedExceptionType = EntityNotFoundException::class.java

            every { userRepository.findById(any()) } returns Optional.empty()

            // When
            val result = catchException { userService.updateUser(expectedId, userCreateRequest) }

            // Then
            verify(exactly = 1) { userRepository.findById(expectedId) }

            assertThat(result)
                .isInstanceOf(expectedExceptionType)
                .asInstanceOf(throwable(expectedExceptionType))
                .satisfies(
                    { assertThat(it.entity).describedAs("entity").isEqualTo(expectedEntity) },
                    { assertThat(it.operation).describedAs("operation").isEqualTo(expectedOperation) },
                    { assertThat(it.condition).describedAs("condition").isEqualTo(expectedCondition) },
                )
        }
    }

    @Nested
    inner class `UserService # deleteUserById` {

        @Test
        fun `should correctly delete an existing user also emitting corresponding event`() {
            // Given

            // When

            // Then

        }

        @Test
        fun `should throw EntityNotFoundException updating a non-existing user`() {
            // Given

            // When

            // Then

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

    private fun copyingUser(): MockKAnswerScope<User, *>.(Call) -> User = {
        firstArg<User>().copy()
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


object A {
    @JvmStatic
    fun main(args: Array<String>) {

    }
}
