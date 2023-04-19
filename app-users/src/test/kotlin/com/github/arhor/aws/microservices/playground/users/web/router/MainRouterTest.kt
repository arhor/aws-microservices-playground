package com.github.arhor.aws.microservices.playground.users.web.router

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.from
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest
import java.net.URI
import java.util.stream.Stream

internal class MainRouterTest {

    private val mainRouter: MainRouter = MainRouter()

    @MethodSource
    @ParameterizedTest
    fun `should have handler function returning expected status for the given HTTP request method and URI`(
        requestURI: URI,
        httpMethod: HttpMethod,
        httpStatus: HttpStatus,
    ) {
        // Given
        val serverRequest = createMockServerRequest(httpMethod, requestURI)

        // When
        val handlerFunction = mainRouter.route(serverRequest)

        // Then
        assertThat(handlerFunction)
            .isNotNull
            .isNotEmpty
            .get()
            .returns(httpStatus, from { it.handle(serverRequest).statusCode() })
    }

    private fun createMockServerRequest(httpMethod: HttpMethod, requestURI: URI): ServerRequest {
        val servletRequest = MockHttpServletRequest(httpMethod.name, requestURI.toString())
        val messageReaders = emptyList<HttpMessageConverter<*>>()

        return ServerRequest.create(servletRequest, messageReaders)
    }

    companion object {
        @JvmStatic
        fun `should have handler function returning expected status for the given HTTP request method and URI`(): Stream<Arguments> {
            return Stream.of(
                // @formatter:off
                arguments(URI.create("/favicon.ico"), HttpMethod.GET, HttpStatus.NO_CONTENT),
                arguments(URI.create("/health")     , HttpMethod.GET, HttpStatus.OK),
                // @formatter:on
            )
        }
    }
}
