package com.pokedex.infrastructure.translation

import com.fasterxml.jackson.annotation.JsonProperty
import com.pokedex.domain.translation.TranslationService
import com.pokedex.infrastructure.ktorHttpClient
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

class HttpTranslationService(translationBaseUrl: String) : TranslationService {
    private val httpClient = ktorHttpClient(translationBaseUrl, shouldExpectSuccess = true)

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
