package com.pokedex.usecase.pokemon

import com.pokedex.domain.pokemon.PokemonInfo
import com.pokedex.domain.pokemon.PokemonInfoRepository
import com.pokedex.domain.translation.TranslationService

class ObtainTranslatedPokemonInfoUseCase(
    private val pokemonInfoRepository: PokemonInfoRepository,
    private val translationService: TranslationService
) {
    // TODO: implement error handling for TranslationService!
    fun execute(pokemonName: String): PokemonInfo {
        val pokemonInfo =
            pokemonInfoRepository.getBy(pokemonName) ?: throw PokemonNotFoundException(pokemonName)

        val updatedDescription =
            if (pokemonInfo.isLegendary || pokemonInfo.habitat == "cave") {
                translationService.getYodaTranslation(pokemonInfo.description)
            } else {
                translationService.getShakespeareTranslation(pokemonInfo.description)
            }

        return pokemonInfo.copy(description = updatedDescription)
    }
}

data class PokemonNotFoundException(val pokemonName: String) :
    RuntimeException("Pokemon not found with name $pokemonName.")
