package com.github.arhor.aws.microservices.playground.users.service

import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserUpdateRequestDto
import kotlinx.coroutines.flow.Flow

interface UserService {
    suspend fun createUser(createRequest: UserCreateRequestDto): UserResponseDto
    suspend fun updateUser(userId: Long, updateRequest: UserUpdateRequestDto): UserResponseDto
    suspend fun deleteUserById(userId: Long)
    fun getAllUsers(): Flow<UserResponseDto>
    suspend fun getUserById(userId: Long): UserResponseDto
}
