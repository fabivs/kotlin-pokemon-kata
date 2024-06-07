package com.pokedex

import com.pokedex.infrastructure.pokemon.HttpPokemonInfoRepository
import com.pokedex.infrastructure.translation.HttpTranslationService
import com.pokedex.usecase.pokemon.ObtainPokemonInfoUseCase
import com.pokedex.usecase.pokemon.ObtainTranslatedPokemonInfoUseCase

// In an actual production environment, this would be an environment variable
private const val POKE_BASE_API = "https://pokeapi.co"
private const val FUN_TRANSLATIONS_BASE_API = "https://api.funtranslations.com"

class DependencyContainer {
    fun getObtainPokemonInfoUseCase() =
        ObtainPokemonInfoUseCase(
            pokemonInfoRepository = HttpPokemonInfoRepository(pokeApiBaseUrl = POKE_BASE_API)
        )

    fun getObtainTranslatedPokemonInfoUseCase() =
        ObtainTranslatedPokemonInfoUseCase(
            pokemonInfoRepository = HttpPokemonInfoRepository(pokeApiBaseUrl = POKE_BASE_API),
            translationService =
                HttpTranslationService(translationBaseUrl = FUN_TRANSLATIONS_BASE_API)
        )
}
