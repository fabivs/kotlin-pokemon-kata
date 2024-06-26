package com.pokedex.infrastructure.pokemon

import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HttpPokemonInfoRepositoryIntegrationTest {

    private val repository = HttpPokemonInfoRepository(pokeApiBaseUrl = "https://pokeapi.co")

    @Test
    fun `get pokemon information by name`() = runTest {
        val pokemonInfo = repository.getBy("pikachu")
        assertNotNull(pokemonInfo)
        assertEquals("pikachu", pokemonInfo.name)
        assertContains(pokemonInfo.description, "electricity")
        assertEquals("forest", pokemonInfo.habitat)
        assertFalse { pokemonInfo.isLegendary }
    }
}
