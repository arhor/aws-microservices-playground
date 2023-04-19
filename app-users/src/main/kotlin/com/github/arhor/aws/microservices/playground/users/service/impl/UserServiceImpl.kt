package com.github.arhor.aws.microservices.playground.users.service.impl

import com.github.arhor.aws.microservices.playground.users.Operation
import com.github.arhor.aws.microservices.playground.users.data.repository.UserRepository
import com.github.arhor.aws.microservices.playground.users.service.UserService
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserUpdateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.event.UserEvent
import com.github.arhor.aws.microservices.playground.users.service.event.UserEventEmitter
import com.github.arhor.aws.microservices.playground.users.service.exception.EntityDuplicateException
import com.github.arhor.aws.microservices.playground.users.service.exception.EntityNotFoundException
import com.github.arhor.aws.microservices.playground.users.service.mapper.UserMapper
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
    private val userEventEmitter: UserEventEmitter,
) : UserService {

    @Transactional
    override fun createUser(createRequest: UserCreateRequestDto): UserResponseDto {
        if (userRepository.existsByEmail(createRequest.email)) {
            throw EntityDuplicateException(
                entity = "User",
                condition = "email = ${createRequest.email}",
                operation = Operation.CREATE,
            )
        }
        return userMapper.mapToUser(createRequest)
            .let { userRepository.save(it) }
            .let(userMapper::mapToUserResponse)
    }

    @Retryable(
        include = [OptimisticLockingFailureException::class],
        maxAttemptsExpression = "\${application-props.retry-attempts:3}"
    )
    @Transactional
    override fun updateUser(userId: Long, updateRequest: UserUpdateRequestDto): UserResponseDto {
        var user = userRepository.findByIdOrNull(userId)
            ?: throw EntityNotFoundException(
                entity = "User",
                condition = "id = $userId",
                operation = Operation.UPDATE,
            )

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
            throw EntityNotFoundException(
                entity = "User",
                condition = "id = $userId",
                operation = Operation.DELETE,
            )
        }
        userRepository.deleteById(userId)
        userEventEmitter.emit(UserEvent.Deleted(userId))
    }

    override fun getUserById(userId: Long): UserResponseDto {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw EntityNotFoundException(
                entity = "User",
                condition = "id = $userId",
                operation = Operation.READ,
            )
        return user.let(userMapper::mapToUserResponse)
    }

    override fun getAllUsers(): List<UserResponseDto> {
        return userRepository
            .findAll()
            .map(userMapper::mapToUserResponse)
    }
}
