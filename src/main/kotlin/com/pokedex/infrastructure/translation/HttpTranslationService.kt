package com.pokedex.infrastructure.translation

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.pokedex.domain.translation.TranslationService
import com.pokedex.infrastructure.installRetryConfiguration
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.runBlocking

class HttpTranslationService(translationBaseUrl: String) : TranslationService {
    private val httpClient: HttpClient = HttpClient {
        expectSuccess = true
        followRedirects = false

        installRetryConfiguration()
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }

        install(ContentNegotiation) { jackson { configure(FAIL_ON_UNKNOWN_PROPERTIES, false) } }

        install(DefaultRequest)
        defaultRequest { url(translationBaseUrl) }
    }

    override fun getYodaTranslation(text: String): String = runBlocking {
        return@runBlocking getTranslationFor(text, "yoda")
    }

    override fun getShakespeareTranslation(text: String): String = runBlocking {
        return@runBlocking getTranslationFor(text, "shakespeare")
    }

    private suspend fun getTranslationFor(text: String, character: String): String {
        val response =
            try {
                httpClient
                    .get("translate/$character") { url { parameters.append("text", text) } }
                    .body<TranslationResponse>()
            } catch (e: ClientRequestException) {
                throw UnableToObtainTranslationException(text, character)
            }

        return response.contents.translated
    }

    private data class TranslationResponse(
        @JsonProperty("contents") val contents: TranslationContents
    )

    private data class TranslationContents(@JsonProperty("translated") val translated: String)

    class UnableToObtainTranslationException(text: String, character: String) :
        RuntimeException("Unable to obtain '$character' translation for text: '$text'")
}
