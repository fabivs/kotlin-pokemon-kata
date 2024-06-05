package com.pokedex.infrastructure

import kotlinx.coroutines.test.runTest
import org.junit.Test

// TODO: aggiungere tag integration se necessario
class HttpPokemonInfoRepositoryTest {

    private val repository = HttpPokemonInfoRepository(pokeApiBaseUrl = "https://pokeapi.co")

    @Test
    fun `get pokemon information by name`() = runTest {
        val pokemonInfo = repository.getBy("pikachu")
        // TODO: asserts
        print(pokemonInfo.toString())
    }
}
