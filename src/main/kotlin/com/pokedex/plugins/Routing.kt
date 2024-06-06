package com.pokedex.plugins

import com.pokedex.DependencyContainer
import com.pokedex.domain.pokemon.PokemonInfo
import com.pokedex.usecase.pokemon.ObtainPokemonInfoUseCase.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(dependencyContainer: DependencyContainer) {
    routing {
        get("/pokemon/{name}") {
            val pokemonName =
                call.parameters["name"]
                    ?: return@get call.respondText("Bad Request", status = BadRequest)

            val obtainPokemonInfo = dependencyContainer.getObtainPokemonInfoUseCase()
            val pokemonInfo = obtainPokemonInfo.execute(pokemonName)
            call.respond(HttpStatusCode.OK, pokemonInfo.serialize())
        }

        get("/pokemon/translated/{name}") {
            val pokemonName =
                call.parameters["name"]
                    ?: return@get call.respondText("Bad Request", status = BadRequest)

            val obtainPokemonTranslatedInfo =
                dependencyContainer.getObtainTranslatedPokemonInfoUseCase()
            val pokemonInfo = obtainPokemonTranslatedInfo.execute(pokemonName)
            call.respond(HttpStatusCode.OK, pokemonInfo.serialize())
        }
    }

    install(StatusPages) {
        exception<PokemonNotFoundException> { call, cause ->
            call.respond(NotFound, ErrorReason("Could not find a Pokemon."))
        }
    }
}

private fun PokemonInfo.serialize() =
    mapOf(
        "name" to name,
        "description" to description,
        "habitat" to habitat,
        "isLegendary" to isLegendary
    )

private data class ErrorReason(var reason: String)
