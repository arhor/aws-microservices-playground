package com.github.arhor.aws.microservices.playground.users.controller.error

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = ErrorCode.Serializer::class)
enum class ErrorCode(val type: Type, val value: Int, val label: String) {
    // @formatter:off
    UNCATEGORIZED   (Type.GEN, 0x00000, "error.server.internal"),
    NO_HANDLER_FOUND(Type.GEN, 0x00001, "error.server.no-handler"),

    ENTITY_NOT_FOUND(Type.DAT, 0x00000, "error.entity.not-found"),
    ENTITY_DUPLICATE(Type.DAT, 0x00001, "error.entity.duplicate"),
    // @formatter:on
    ;

    enum class Type {
        GEN,
        DAT,
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
