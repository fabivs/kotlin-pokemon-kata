package com.pokedex.plugins

import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/pokemon/{pokemonName}") {
            val pokemonName = call.parameters["pokemonName"]
            call.respondText(status = HttpStatusCode.OK, contentType = Json) {"$pokemonName"}
        }
    }
}
