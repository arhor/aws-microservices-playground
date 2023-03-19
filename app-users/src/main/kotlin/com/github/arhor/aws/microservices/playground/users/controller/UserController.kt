package com.github.arhor.aws.microservices.playground.users.controller

import com.github.arhor.aws.microservices.playground.users.service.UserService
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserUpdateRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    fun createUser(
        @RequestBody
        createRequest: UserCreateRequestDto,
        uriBuilder: UriComponentsBuilder,
    ): ResponseEntity<UserResponseDto> {
        val createdUser = userService.createUser(createRequest)
        val locationUri = uriBuilder.path("/{userId}").build(createdUser.id)

        return ResponseEntity.created(locationUri).body(createdUser)
    }

    @PatchMapping("/{userId}")
    fun updateUser(@RequestBody updateRequest: UserUpdateRequestDto, userId: Long): UserResponseDto =
        userService.updateUser(userId, updateRequest)

    @DeleteMapping("/{userId}")
    fun deleteUser(userId: Long): Unit =
        userService.deleteUserById(userId)

    @GetMapping("/{userId}")
    fun getUserById(userId: Long): UserResponseDto =
        userService.getUserById(userId)

    @GetMapping
    fun getAllUsers(): List<UserResponseDto> =
        userService.getAllUsers()
}
