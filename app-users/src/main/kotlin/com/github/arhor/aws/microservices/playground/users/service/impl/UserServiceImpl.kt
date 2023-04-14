package com.github.arhor.aws.microservices.playground.users.service.impl

import com.github.arhor.aws.microservices.playground.users.data.model.User
import com.github.arhor.aws.microservices.playground.users.data.repository.UserRepository
import com.github.arhor.aws.microservices.playground.users.service.UserService
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserUpdateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.event.UserDeletedEvent
import com.github.arhor.aws.microservices.playground.users.service.exception.EntityDuplicateException
import com.github.arhor.aws.microservices.playground.users.service.exception.EntityNotFoundException
import com.github.arhor.aws.microservices.playground.users.service.mapper.UserMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jms.core.JmsTemplate
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    @Value("\${application-props.aws.user-deleted-queue-name}")
    private val queueName: String,
    private val jmsTemplate: JmsTemplate,
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
) : UserService {

    @Transactional
    override fun createUser(createRequest: UserCreateRequestDto): UserResponseDto {
        if (userRepository.existsByEmail(createRequest.email)) {
            throw EntityDuplicateException("User", "email = ${createRequest.email}")
        }
        return userMapper.mapToUser(createRequest)
            .let { userRepository.save(it) }
            .let(userMapper::mapToUserResponse)
    }

    @Retryable(
        include = [OptimisticLockingFailureException::class],
        maxAttemptsExpression = "\${application-props.retry-attempts}"
    )
    @Transactional
    override fun updateUser(userId: Long, updateRequest: UserUpdateRequestDto): UserResponseDto {
        var user = getUserOrThrowException(userId)

        updateRequest.password?.let {
            user = user.copy(password = it)
        }
        updateRequest.budgetLimit?.let {
            user = user.copy(budget = user.budget.copy(limit = it))
        }

        return userRepository.save(user)
            .let(userMapper::mapToUserResponse)
    }

    @Transactional
    override fun deleteUserById(userId: Long) {
        if (!userRepository.existsById(userId)) {
            throw EntityNotFoundException("User", "id = $userId")
        }
        userRepository.deleteById(userId)
        jmsTemplate.convertAndSend(queueName, UserDeletedEvent(userId))
    }

    override fun getUserById(userId: Long): UserResponseDto {
        return getUserOrThrowException(userId)
            .let(userMapper::mapToUserResponse)
    }

    override fun getAllUsers(): List<UserResponseDto> {
        return userRepository
            .findAll()
            .map(userMapper::mapToUserResponse)
    }

    private fun getUserOrThrowException(userId: Long): User =
        userRepository.findByIdOrNull(userId)
            ?: throw EntityNotFoundException("User", "id = $userId")
}
