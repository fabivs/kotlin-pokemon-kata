package com.pokedex.infrastructure

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*

fun HttpClientConfig<*>.installRetryConfiguration() {
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
