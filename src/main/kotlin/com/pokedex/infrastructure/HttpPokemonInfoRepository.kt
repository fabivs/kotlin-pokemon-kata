package com.pokedex.infrastructure

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.pokedex.domain.pokemon.PokemonInfo
import com.pokedex.domain.pokemon.PokemonInfoRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.runBlocking

class HttpPokemonInfoRepository(pokeApiBaseUrl: String) : PokemonInfoRepository {
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
        defaultRequest { url(pokeApiBaseUrl) }
    }

    // Note: opted for runBlocking instead of suspend, to avoid having suspend in the domain usecase
    override fun getBy(pokemonName: String): PokemonInfo? = runBlocking {
        val response = httpClient.get("/api/v2/pokemon-species/$pokemonName/")
        if (response.status == HttpStatusCode.NotFound) return@runBlocking null

        return@runBlocking pokemonInfoFrom(response.body<PokemonSpeciesResponse>())
    }

    private data class PokemonSpeciesResponse(
        @JsonProperty("name") val name: String,
        @JsonProperty("flavor_text_entries") val flavorTexts: List<FlavorTextEntry>,
        @JsonProperty("habitat") val habitat: Habitat,
        @JsonProperty("is_legendary") val isLegendary: Boolean
    )

    private data class FlavorTextEntry(
        @JsonProperty("flavor_text") val flavorText: String,
        @JsonProperty("language") val language: Map<String, String>
    )

    private data class Habitat(
        @JsonProperty("name") val name: String,
    )

    private fun pokemonInfoFrom(response: PokemonSpeciesResponse): PokemonInfo =
        PokemonInfo(
            name = response.name,
            // TODO: get the first description with english as language!
            description = response.flavorTexts.first().flavorText,
            habitat = response.habitat.name,
            isLegendary = response.isLegendary
        )
}
