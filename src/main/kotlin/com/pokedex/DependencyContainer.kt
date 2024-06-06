package com.pokedex

import com.pokedex.infrastructure.HttpPokemonInfoRepository
import com.pokedex.usecase.pokemon.ObtainPokemonInfoUseCase
import com.pokedex.usecase.pokemon.ObtainTranslatedPokemonInfoUseCase

// In an actual production environment, this would be an environment variable
private const val POKE_BASE_API = "https://pokeapi.co"

class DependencyContainer {
    fun getObtainPokemonInfoUseCase() =
        ObtainPokemonInfoUseCase(
            pokemonInfoRepository = HttpPokemonInfoRepository(pokeApiBaseUrl = POKE_BASE_API)
        )

    fun getObtainTranslatedPokemonInfoUseCase() =
        ObtainTranslatedPokemonInfoUseCase(
            pokemonInfoRepository = HttpPokemonInfoRepository(pokeApiBaseUrl = POKE_BASE_API)
        )
}
