package com.pokedex.domain.translation

interface TranslationService {
    fun getYodaTranslation(text: String): String

    fun getShakespeareTranslation(text: String): String
}
