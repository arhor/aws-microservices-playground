package com.github.arhor.aws.microservices.playground.users.service.mapper

import com.github.arhor.aws.microservices.playground.users.data.model.User
import com.github.arhor.aws.microservices.playground.users.service.dto.UserCreateRequestDto
import com.github.arhor.aws.microservices.playground.users.service.dto.UserResponseDto
import org.mapstruct.*

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
)
interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "budget.limit", source = "budgetLimit")
    fun mapToUser(createRequest: UserCreateRequestDto): User

    @Mapping(target = "budgetLimit", source = "budget.limit")
    fun mapToUserResponse(user: User): UserResponseDto
}
