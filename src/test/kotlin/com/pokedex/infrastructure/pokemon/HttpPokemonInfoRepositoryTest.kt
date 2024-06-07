package com.pokedex.infrastructure.pokemon

import com.pokedex.infrastructure.pokemon.HttpPokemonInfoRepository.*
import io.mockk.coEvery
import io.mockk.spyk
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HttpPokemonInfoRepositoryTest {
    // Note: this test does not actually call any external service, it's technically a Unit test.
    // The purpose of these tests is testing internal logic of the repository.

    private val mockedRepository = spyk(HttpPokemonInfoRepository(pokeApiBaseUrl = ""))

    @Test
    fun `pokemon description is empty if there is no english flavor text`() = runTest {
        val response =
            PokemonSpeciesResponse(
                name = "Mocked Squirtle",
                flavorTexts =
                    listOf(
                        FlavorTextEntry(
                            description = "水面から　水を　噴射して\nエサを　取る。危なくなると　甲羅に\n手足を　ひっこめて　身を　守る。",
                            language = mapOf("name" to "ja")
                        )
                    ),
                habitat = Habitat(name = "forest"),
                isLegendary = false
            )

        coEvery { mockedRepository.obtainPokemonSpecies("Mocked Squirtle") } returns response

        val pokemonInfo = mockedRepository.getBy("Mocked Squirtle")
        assertNotNull(pokemonInfo)
        assertEquals("", pokemonInfo.description)
    }

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
