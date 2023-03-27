package com.github.arhor.aws.microservices.playground.users.service.impl

import com.github.arhor.aws.microservices.playground.users.data.model.User
import com.github.arhor.aws.microservices.playground.users.data.repository.UserRepository
import com.github.arhor.aws.microservices.playground.users.service.UserService
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserUpdateRequestDto
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
) : UserService {

    @Transactional
    override fun createUser(createRequest: UserCreateRequestDto): UserResponseDto {
        if (userRepository.existsByEmail(createRequest.email)) {
            throw EntityDuplicateException("User", "email=${createRequest.email}")
        }
        return userMapper.mapToUser(createRequest)
            .let { userRepository.save(it) }
            .let(userMapper::mapToUserResponse)
    }

    @Retryable(
        retryFor = [OptimisticLockingFailureException::class],
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
            throw EntityNotFoundException("User", "id=${userId}")
        }
        userRepository.deleteById(userId)
    }

    override fun getUserById(userId: Long): UserResponseDto =
        getUserOrThrowException(userId)
            .let(userMapper::mapToUserResponse)

    override fun getAllUsers(): List<UserResponseDto> =
        userRepository.findAll()
            .map(userMapper::mapToUserResponse)

    private fun getUserOrThrowException(userId: Long): User =
        userRepository.findByIdOrNull(userId)
            ?: throw EntityNotFoundException("User", "id=${userId}")
}
