package com.github.arhor.aws.microservices.playground.users.service.mapper

import com.github.arhor.aws.microservices.playground.users.data.model.User
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "budget.limit", source = "budgetLimit")
    fun mapToUser(createRequest: UserCreateRequestDto): User

    @Mapping(target = "budgetLimit", source = "budget.limit")
    fun mapToUserResponse(user: User): UserResponseDto
}
