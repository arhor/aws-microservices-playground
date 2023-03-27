package com.github.arhor.aws.microservices.playground.users.service

import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserUpdateRequestDto

interface UserService {
    fun createUser(createRequest: UserCreateRequestDto): UserResponseDto
    fun updateUser(userId: Long, updateRequest: UserUpdateRequestDto): UserResponseDto
    fun deleteUserById(userId: Long)
    fun getAllUsers(): List<UserResponseDto>
    fun getUserById(userId: Long): UserResponseDto
}
