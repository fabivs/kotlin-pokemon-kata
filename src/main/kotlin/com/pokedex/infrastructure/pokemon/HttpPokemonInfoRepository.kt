package com.pokedex.infrastructure.pokemon

import com.fasterxml.jackson.annotation.JsonProperty
import com.pokedex.domain.pokemon.PokemonInfo
import com.pokedex.domain.pokemon.PokemonInfoRepository
import com.pokedex.infrastructure.ktorHttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class HttpPokemonInfoRepository(pokeApiBaseUrl: String) : PokemonInfoRepository {
    private val httpClient = ktorHttpClient(pokeApiBaseUrl, shouldExpectSuccess = false)

    override fun getBy(pokemonName: String): PokemonInfo? = runBlocking {
        return@runBlocking obtainPokemonSpecies(pokemonName)?.let { adaptPokemonInfoFrom(it) }
    }

    internal suspend fun obtainPokemonSpecies(pokemonName: String): PokemonSpeciesResponse? {
        val response = httpClient.get("/api/v2/pokemon-species/$pokemonName/")
        if (response.status == HttpStatusCode.NotFound) return null
        return response.body<PokemonSpeciesResponse>()
    }

    internal data class PokemonSpeciesResponse(
        @JsonProperty("name") val name: String,
        @JsonProperty("flavor_text_entries") val flavorTexts: List<FlavorTextEntry>,
        @JsonProperty("habitat") val habitat: Habitat,
        @JsonProperty("is_legendary") val isLegendary: Boolean
    )

    internal data class FlavorTextEntry(
        @JsonProperty("flavor_text") val description: String,
        @JsonProperty("language") val language: Map<String, String>
    )

    internal data class Habitat(
        @JsonProperty("name") val name: String,
    )

    private fun adaptPokemonInfoFrom(response: PokemonSpeciesResponse): PokemonInfo =
        PokemonInfo(
            name = response.name,
            description = response.flavorTexts.getFirstEnglishDescription().cleanUpDescription(),
            habitat = response.habitat.name,
            isLegendary = response.isLegendary
        )

    private fun List<FlavorTextEntry>.getFirstEnglishDescription(): String {
        return find { it.language["name"] == "en" }?.description ?: ""
    }

    // see: https://github.com/veekun/pokedex/issues/218#issuecomment-339841781
    private fun String.cleanUpDescription(): String {
        return this.replace(Regex("\\f"), " ")
            .replace(Regex("\\u00ad\\n"), "")
            .replace(Regex("\\u00ad"), "")
            .replace(Regex(" -\\n"), " - ")
            .replace(Regex("-\\n"), "-")
            .replace(Regex("\\n"), " ")
    }
}
