package com.github.arhor.aws.microservices.playground.users.controller

import com.github.arhor.aws.microservices.playground.users.service.UserService
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserUpdateRequestDto
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    suspend fun createUser(
        @RequestBody
        createRequest: UserCreateRequestDto,
        uriBuilder: UriComponentsBuilder,
    ): ResponseEntity<UserResponseDto> {
        val createdUser = userService.createUser(createRequest)
        val locationUri = uriBuilder.path("/{userId}").build(createdUser.id)

        return ResponseEntity.created(locationUri).body(createdUser)
    }

    @PatchMapping("/{userId}")
    suspend fun updateUser(
        @PathVariable userId: Long,
        @RequestBody updateRequest: UserUpdateRequestDto
    ): UserResponseDto =
        userService.updateUser(userId, updateRequest)

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteUser(@PathVariable userId: Long): Unit =
        userService.deleteUserById(userId)

    @GetMapping("/{userId}")
    suspend fun getUserById(@PathVariable userId: Long): UserResponseDto =
        userService.getUserById(userId)

    @GetMapping
    fun getAllUsers(): Flow<UserResponseDto> =
        userService.getAllUsers()
}
