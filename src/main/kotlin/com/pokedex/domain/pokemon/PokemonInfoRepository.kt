package com.pokedex.domain.pokemon

interface PokemonInfoRepository {
    fun getBy(pokemonName: String): PokemonInfo?
}
