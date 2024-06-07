package com.pokedex.usecase.pokemon

import com.pokedex.domain.pokemon.PokemonInfo
import com.pokedex.domain.pokemon.PokemonInfoRepository
import com.pokedex.domain.translation.TranslationService
import io.ktor.client.plugins.*

class ObtainTranslatedPokemonInfoUseCase(
    private val pokemonInfoRepository: PokemonInfoRepository,
    private val translationService: TranslationService
) {
    fun execute(pokemonName: String): PokemonInfo {
        val pokemonInfo =
            pokemonInfoRepository.getBy(pokemonName) ?: throw PokemonNotFoundException(pokemonName)

        val updatedDescription = try {
            if (pokemonInfo.isLegendary || pokemonInfo.habitat == "cave") {
                translationService.getYodaTranslation(pokemonInfo.description)
            } else {
                translationService.getShakespeareTranslation(pokemonInfo.description)
            }
        } catch (e: ClientRequestException) {
            throw UnableToObtainTranslationException()
        }

        return pokemonInfo.copy(description = updatedDescription)
    }
}

data class PokemonNotFoundException(val pokemonName: String) :
    RuntimeException("Pokemon not found with name $pokemonName.")

class UnableToObtainTranslationException : RuntimeException("Unable to obtain translation.")