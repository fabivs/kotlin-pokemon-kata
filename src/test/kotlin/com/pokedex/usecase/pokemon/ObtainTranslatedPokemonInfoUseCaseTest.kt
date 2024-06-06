package com.pokedex.usecase.pokemon

import com.pokedex.domain.pokemon.PokemonInfoRepository
import io.mockk.mockk
import org.junit.Ignore
import org.junit.Test

class ObtainTranslatedPokemonInfoUseCaseTest {

    private val pokemonInfoRepository = mockk<PokemonInfoRepository>()
    private val obtainTranslatedInfo =
        ObtainTranslatedPokemonInfoUseCase(pokemonInfoRepository = pokemonInfoRepository)

    @Test
    @Ignore
    fun `apply Yoda translation if pokemon habitat is cave`() {
        TODO("Not yet implemented")
    }

    @Test
    @Ignore
    fun `apply Yoda translation if the pokemon is legendary`() {
        TODO("Not yet implemented")
    }

    @Test
    @Ignore
    fun `apply Shakespeare translation for every other pokemon`() {
        TODO("Not yet implemented")
    }

    @Test
    @Ignore
    fun `maintain the regular description if unable to obtain a translation`() {
        TODO("Not yet implemented")
    }
}
