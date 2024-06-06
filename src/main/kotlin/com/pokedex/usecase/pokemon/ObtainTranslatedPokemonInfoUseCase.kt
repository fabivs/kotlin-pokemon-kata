package com.pokedex.usecase.pokemon

import com.pokedex.domain.pokemon.PokemonInfo
import com.pokedex.domain.pokemon.PokemonInfoRepository

class ObtainTranslatedPokemonInfoUseCase(private val pokemonInfoRepository: PokemonInfoRepository) {
    fun execute(pokemonName: String): PokemonInfo {
        val pokemonInfo =
            pokemonInfoRepository.getBy(pokemonName) ?: throw PokemonNotFoundException(pokemonName)
        return pokemonInfo
    }

    data class PokemonNotFoundException(val pokemonName: String) :
        RuntimeException("Pokemon not found with name $pokemonName.")
}
