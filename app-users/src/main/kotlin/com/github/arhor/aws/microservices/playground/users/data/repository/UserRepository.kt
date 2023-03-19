package com.github.arhor.aws.microservices.playground.users.data.repository

import com.github.arhor.aws.microservices.playground.users.data.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {

    fun existsByEmail(email: String): Boolean
}
