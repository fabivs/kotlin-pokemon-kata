package com.pokedex

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertContains

class ApplicationTest {
    @Test
    fun `pokemon endpoint returns the pokemon information`() = testApplication {
        application { module() }

        val pokemonName = "mewtwo"

        val response = client.get("/pokemon/$pokemonName")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("json", response.contentType()?.contentSubtype)
        assertContains(response.bodyAsText(), pokemonName)
    }

    @Test
    fun `pokemon endpoint returns 404 not found if the pokemon does not exist`() = testApplication {
        application { module() }

        val pokemonName = "aquaphant"
        val response = client.get("/pokemon/$pokemonName")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertContains(response.bodyAsText(), "Could not find a Pokemon.")
    }

    @Test
    fun `pokemon translated endpoint returns pokemon information with Yoda translation for a legendary pokemon`() =
        testApplication {
            application { module() }

            val pokemonName = "mewtwo"
            val response = client.get("/pokemon/translated/$pokemonName")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("json", response.contentType()?.contentSubtype)
            assertContains(response.bodyAsText(), pokemonName)

            val mapper = jacksonObjectMapper()

            val responseBody: String = response.bodyAsText()
            val responseMap = mapper.readValue<Map<String, String>>(responseBody)
            assertEquals(
                "Created by a scientist after years of horrific gene splicing and dna engineering experiments,  it was.",
                responseMap["description"]
            )
        }

    @Test
    fun `pokemon translated endpoint returns pokemon information with Yoda translation for a habitat cave pokemon`() =
        testApplication {
            application { module() }

            val pokemonName = "zubat"
            val responseBody = client.get("/pokemon/translated/$pokemonName").bodyAsText()

            val mapper = jacksonObjectMapper()
            val responseMap = mapper.readValue<Map<String, String>>(responseBody)

            assertEquals(
                "Forms colonies in perpetually dark places.Ultrasonic waves to identify and approach targets,  uses.",
                responseMap["description"]
            )
        }

    @Test
    fun `pokemon translated endpoint returns the translated pokemon information with Shakespeare translation`() =
        testApplication {
            application { module() }

            val pokemonName = "pikachu"
            val responseBody = client.get("/pokemon/translated/$pokemonName").bodyAsText()

            val mapper = jacksonObjectMapper()
            val responseMap = mapper.readValue<Map<String, String>>(responseBody)

            assertEquals(
                "At which hour several of these pokémon gather,  their electricity couldst buildeth and cause lightning storms.",
                responseMap["description"]
            )
        }

    @Test
    fun `pokemon translated endpoint returns 404 not found if the pokemon does not exist`() =
        testApplication {
            application { module() }

            val pokemonName = "aquaphant"
            val response = client.get("/pokemon/$pokemonName")
            assertEquals(HttpStatusCode.NotFound, response.status)
            assertContains(response.bodyAsText(), "Could not find a Pokemon.")
        }
}
