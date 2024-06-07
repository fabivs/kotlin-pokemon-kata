package com.pokedex.infrastructure.translation

import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HttpTranslationServiceIntegrationTest {
    private val service =
        HttpTranslationService(translationBaseUrl = "https://api.funtranslations.com")

    @Test
    fun `get yoda translation for a string`() = runTest {
        val originalString =
            "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments."
        val translatedString =
            "Created by a scientist after years of horrific gene splicing and dna engineering experiments,  it was."
        assertEquals(translatedString, service.getYodaTranslation(originalString))
    }

    @Test
    fun `get shakespeare translation for a string`() = runTest {
        val originalString =
            "When several of these POKéMON gather, their electricity could build and cause lightning storms."
        val translatedString =
            "At which hour several of these pokémon gather,  their electricity couldst buildeth and cause lightning storms."
        assertEquals(translatedString, service.getShakespeareTranslation(originalString))
    }
}
