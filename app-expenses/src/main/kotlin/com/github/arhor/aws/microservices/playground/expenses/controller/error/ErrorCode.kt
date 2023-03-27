package com.github.arhor.aws.microservices.playground.expenses.controller.error

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = ErrorCode.Serializer::class)
enum class ErrorCode(val type: Type, val value: Int, val label: String) {

    UNCATEGORIZED(Type.GEN, 0x00000, "error.server.internal"),
    ;

    enum class Type {
        GEN,
        ;
    }

    class Serializer : StdSerializer<ErrorCode>(ErrorCode::class.java) {

        override fun serialize(value: ErrorCode, generator: JsonGenerator, provider: SerializerProvider) {
            val errorCodeHexString = Integer.toHexString(value.value)

            val type = value.type
            val code = errorCodeHexString.padStart(CODE_MAX_LENGTH, CODE_PAD_SYMBOL)

            val result = "$type-$code".uppercase()

            generator.writeString(result)
        }

        companion object {
            private const val CODE_MAX_LENGTH = 5
            private const val CODE_PAD_SYMBOL = '0'
        }
    }
}
