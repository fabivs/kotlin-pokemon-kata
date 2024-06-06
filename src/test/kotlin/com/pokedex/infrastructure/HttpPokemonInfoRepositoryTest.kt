package com.pokedex.infrastructure

import com.pokedex.infrastructure.HttpPokemonInfoRepository.*
import io.mockk.coEvery
import io.mockk.spyk
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HttpPokemonInfoRepositoryTest {

    private val mockedRepository = spyk(HttpPokemonInfoRepository(pokeApiBaseUrl = ""))

    @Test
    fun `pokemon description does not have special characters`() = runTest {
        val response =
            PokemonSpeciesResponse(
                name = "Mocked Squirtle",
                flavorTexts =
                listOf(
                    FlavorTextEntry(
                        description =
                        "After birth, its\nback swells and\nhardens into a\u000cshell. Powerfully\nsprays foam from\nits mouth.",
                        language = mapOf("name" to "en")
                    )
                ),
                habitat = Habitat(name = "forest"),
                isLegendary = false
            )

        coEvery { mockedRepository.obtainPokemonSpecies("Mocked Squirtle") } returns response

        val pokemonInfo = mockedRepository.getBy("Mocked Squirtle")
        assertNotNull(pokemonInfo)
        assertEquals(
            "After birth, its back swells and hardens into a shell. Powerfully sprays foam from its mouth.",
            pokemonInfo.description
        )
    }
}
