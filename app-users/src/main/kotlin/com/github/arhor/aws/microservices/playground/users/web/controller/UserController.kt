package com.github.arhor.aws.microservices.playground.users.web.controller

import com.github.arhor.aws.microservices.playground.users.service.UserService
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserUpdateRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Validated
@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    fun createUser(@RequestBody createRequest: UserCreateRequestDto): ResponseEntity<UserResponseDto> {
        val createdUser = userService.createUser(createRequest)
        val locationUri =
            ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{userId}")
                .build(createdUser.id)

        return ResponseEntity.created(locationUri).body(createdUser)
    }

    @PatchMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody updateRequest: UserUpdateRequestDto
    ): UserResponseDto {
        return userService.updateUser(userId, updateRequest)
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable userId: Long) {
        userService.deleteUserById(userId)
    }

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): UserResponseDto {
        return userService.getUserById(userId)
    }

    @GetMapping
    fun getAllUsers(): List<UserResponseDto> {
        return userService.getAllUsers()
    }
}
