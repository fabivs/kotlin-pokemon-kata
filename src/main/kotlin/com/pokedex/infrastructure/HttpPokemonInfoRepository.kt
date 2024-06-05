package com.pokedex.infrastructure

import com.pokedex.domain.pokemon.PokemonInfo
import com.pokedex.domain.pokemon.PokemonInfoRepository
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
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

        //        install(ContentNegotiation) {
        //            jackson()
        //        }

        install(DefaultRequest)
        defaultRequest { url(pokeApiBaseUrl) }
    }

    // Note: opted for runBlocking instead of suspend, to avoid having suspend in the domain usecase
    override fun getBy(pokemonName: String): PokemonInfo? = runBlocking {
        val response = httpClient.get("/api/v2/pokemon-species/$pokemonName/")
        if (response.status == HttpStatusCode.NotFound) return@runBlocking null

        return@runBlocking pokemonInfoFrom(response.bodyAsText())
    }

    private fun pokemonInfoFrom(pokemonInfoRaw: String): PokemonInfo =
        PokemonInfo(
            name = "wip",
            description = pokemonInfoRaw,
            habitat = "wip",
            isLegendary = false
        )
}
