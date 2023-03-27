package com.github.arhor.aws.microservices.playground.users.data.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Immutable
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime

@Immutable
@Table("users")
data class User(
    @Id
    @Column("id")
    val id: Long? = null,

    @Column("email")
    val email: String,

    @Column("password")
    val password: String,

    @Embedded.Empty(prefix = "budget_")
    val budget: Budget,

    @Version
    @Column("version")
    val version: Long? = null,

    @CreatedDate
    @Column("created_date_time")
    val createdDateTime: ZonedDateTime? = null,

    @LastModifiedDate
    @Column("updated_date_time")
    val updatedDateTime: ZonedDateTime? = null,
)
