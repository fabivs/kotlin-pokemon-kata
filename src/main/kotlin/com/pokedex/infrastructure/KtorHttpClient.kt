package com.pokedex.infrastructure

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.utils.io.errors.*

internal fun ktorHttpClient(baseUrl: String, shouldExpectSuccess: Boolean): HttpClient =
    HttpClient {
        expectSuccess = shouldExpectSuccess
        followRedirects = false

        installRetryConfiguration()
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }

        install(ContentNegotiation) { jackson { configure(FAIL_ON_UNKNOWN_PROPERTIES, false) } }

        install(DefaultRequest)
        defaultRequest { url(baseUrl) }
    }

private fun HttpClientConfig<*>.installRetryConfiguration() {
    install(HttpRequestRetry) {
        maxRetries = 3
        retryIf { _, response ->
            response.status == HttpStatusCode.TooManyRequests ||
                response.status == HttpStatusCode.ServiceUnavailable
        }
        retryOnExceptionIf { _, cause -> cause is IOException }
        delayMillis { retry -> retry * 1_500L }
    }
}
