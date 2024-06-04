package com.pokedex.usecase.pokemon

import com.pokedex.domain.pokemon.PokemonInfo
import com.pokedex.domain.pokemon.PokemonInfoRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.junit.Test

class ObtainPokemonInfoUseCaseTest {

    private val pokemonInfoRepository = mockk<PokemonInfoRepository>()
    private val obtainPokemonInfo =
        ObtainPokemonInfoUseCase(pokemonInfoRepository = pokemonInfoRepository)

    @Test
    fun `obtain pokemon info successfully`() {
        val pokemonName = "pikachu"
        val expectedPokemonInfo =
            PokemonInfo(
                name = "pikachu",
                description = "A cute pokemon that generates electricity",
                habitat = "grass",
                isLegendary = false
            )

        every { pokemonInfoRepository.getBy(pokemonName) } returns expectedPokemonInfo

        val actualResult = obtainPokemonInfo.execute(pokemonName)
        assertEquals(expectedPokemonInfo, actualResult)
    }

    @Test
    fun `throw an exception when pokemon can't be found`() {
        val pokemonName = "Aquaphant"
        every { pokemonInfoRepository.getBy(pokemonName) } returns null

        assertFailsWith<RuntimeException> { obtainPokemonInfo.execute(pokemonName) }
    }
}
