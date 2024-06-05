package com.pokedex.plugins

import com.pokedex.DependencyContainer
import com.pokedex.domain.pokemon.PokemonInfo
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(dependencyContainer: DependencyContainer) {
    routing {
        get("/") { call.respondText("Please call one of the available APIs.") }

        get("/pokemon/{name}") {
            // TODO: this BadRequest could be thrown as an exception by the usecase ?
            val pokemonName =
                call.parameters["name"]
                    ?: return@get call.respondText("Bad Request", status = BadRequest)

            val obtainPokemonInfo = dependencyContainer.getObtainPokemonInfoUseCase()
            val pokemonInfo = obtainPokemonInfo.execute(pokemonName)
            call.respond(HttpStatusCode.OK, pokemonInfo.serialize())
        }

        // TODO(delete-me): example of an error
        get("/error-test") { throw BadRequestException("Too Busy") }
    }

    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            // TODO(delete-me): example of an error
            call.respond(BadRequest, ErrorReason("Just a bad request."))
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
