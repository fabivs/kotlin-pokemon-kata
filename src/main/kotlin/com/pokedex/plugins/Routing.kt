package com.pokedex.plugins

import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") { call.respondText("Hello World!") }

        get("/pokemon/{name}") {
            // TODO: this BadRequest could be thrown as an exception by the usecase ?
            val pokemonName =
                call.parameters["name"]
                    ?: return@get call.respondText("Bad Request", status = BadRequest)
            call.respondText(status = HttpStatusCode.OK, contentType = Json) { pokemonName }
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

private data class ErrorReason(var reason: String)
