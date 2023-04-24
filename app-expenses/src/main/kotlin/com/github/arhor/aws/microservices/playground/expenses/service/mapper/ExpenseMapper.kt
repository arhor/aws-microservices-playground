package com.github.arhor.aws.microservices.playground.expenses.service.mapper

import com.github.arhor.aws.microservices.playground.expenses.data.model.Expense
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseCreateDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseResultDTO
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
)
interface ExpenseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "updatedDateTime", ignore = true)
    fun mapCreateExpenseDtoToEntity(dto: ExpenseCreateDTO): Expense

    fun mapExpenseToResultDto(expense: Expense): ExpenseResultDTO
}
