package com.github.arhor.aws.microservices.playground.users.data.repository

import com.github.arhor.aws.microservices.playground.users.data.model.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Long> {

    suspend fun existsByEmail(email: String): Boolean
}
