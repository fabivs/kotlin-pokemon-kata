package com.pokedex.usecase.pokemon

import com.pokedex.domain.pokemon.PokemonInfo
import com.pokedex.domain.pokemon.PokemonInfoRepository
import com.pokedex.domain.translation.TranslationService
import com.pokedex.infrastructure.translation.HttpTranslationService.UnableToObtainTranslationException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObtainTranslatedPokemonInfoUseCaseTest {

    private val pokemonInfoRepository = mockk<PokemonInfoRepository>()
    private val translationService = mockk<TranslationService>()
    private val obtainTranslatedInfo =
        ObtainTranslatedPokemonInfoUseCase(
            pokemonInfoRepository = pokemonInfoRepository,
            translationService = translationService
        )

    private val originalPikachuDescription = "A cute pokemon that generates electricity"

    @Before
    fun setUp() {
        every { pokemonInfoRepository.getBy("zubat") } returns
            PokemonInfo(
                name = "zubat",
                description = "A bat.",
                habitat = "cave",
                isLegendary = false
            )
        every { pokemonInfoRepository.getBy("mewtwo") } returns
            PokemonInfo(
                name = "mewtwo",
                description = "It was created by a scientist...",
                habitat = "rare",
                isLegendary = true
            )
        every { pokemonInfoRepository.getBy("pikachu") } returns
            PokemonInfo(
                name = "pikachu",
                description = originalPikachuDescription,
                habitat = "grass",
                isLegendary = false
            )
    }

    @Test
    fun `apply Yoda translation if pokemon habitat is cave`() {
        val yodaText = "This is a Yoda sentence"
        every { translationService.getYodaTranslation(any()) } returns yodaText

        assertEquals(yodaText, obtainTranslatedInfo.execute("zubat").description)

        verify(exactly = 1) { pokemonInfoRepository.getBy("zubat") }
        verify(exactly = 1) { translationService.getYodaTranslation(any()) }
    }

    @Test
    fun `apply Yoda translation if the pokemon is legendary`() {
        val yodaText = "This is a Yoda sentence"
        every { translationService.getYodaTranslation(any()) } returns yodaText

        assertEquals(yodaText, obtainTranslatedInfo.execute("mewtwo").description)

        verify(exactly = 1) { pokemonInfoRepository.getBy("mewtwo") }
        verify(exactly = 1) { translationService.getYodaTranslation(any()) }
    }

    @Test
    fun `apply Shakespeare translation for every other pokemon`() {
        val shakespeareText = "This is a Shakespeare sentence"
        every { translationService.getShakespeareTranslation(any()) } returns shakespeareText

        assertEquals(shakespeareText, obtainTranslatedInfo.execute("pikachu").description)

        verify(exactly = 1) { pokemonInfoRepository.getBy("pikachu") }
        verify(exactly = 1) { translationService.getShakespeareTranslation(any()) }
    }

    @Test
    fun `use the original description if unable to obtain a translation`() {
        every { translationService.getShakespeareTranslation(any()) } throws
            UnableToObtainTranslationException("", "")

        assertEquals(
            originalPikachuDescription,
            obtainTranslatedInfo.execute("pikachu").description
        )

        verify(exactly = 1) { pokemonInfoRepository.getBy("pikachu") }
        verify(exactly = 1) { translationService.getShakespeareTranslation(any()) }
    }
}
