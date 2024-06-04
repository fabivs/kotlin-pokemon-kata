package com.pokedex.usecase.pokemon

import com.pokedex.domain.pokemon.PokemonInfo
import com.pokedex.domain.pokemon.PokemonInfoRepository

class ObtainPokemonInfoUseCase(private val pokemonInfoRepository: PokemonInfoRepository) {
    // TODO: add logger

    fun execute(pokemonName: String): PokemonInfo {
        return pokemonInfoRepository.getBy(pokemonName)
            ?: throw PokemonNotFoundException(pokemonName)
    }

    data class PokemonNotFoundException(val pokemonName: String) :
        RuntimeException("Pokemon not found with name $pokemonName.")
}
