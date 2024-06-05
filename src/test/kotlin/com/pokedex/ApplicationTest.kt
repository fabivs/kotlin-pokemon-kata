package com.pokedex

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.assertContains
import org.junit.Assert.assertEquals
import org.junit.Test

class ApplicationTest {
    @Test
    fun testRoot(): Unit = testApplication {
        application { module() }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Please call one of the available APIs.", response.bodyAsText())
    }

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
    fun `pokemon endpoint returns not found if the pokemon does not exist`() = testApplication {
        application { module() }

        val pokemonName = "aquaphant"
        val response = client.get("/pokemon/$pokemonName")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertContains(response.bodyAsText(), "Could not find a Pokemon.")
    }

    @Test
    fun `pokemon translated endpoint returns the translated pokemon information`() =
        testApplication {
            application { module() }

            val pokemonName = "mewtwo"

            val response = client.get("/pokemon/translated/$pokemonName")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("json", response.contentType()?.contentSubtype)
            assertContains(response.bodyAsText(), pokemonName)
        }

    @Test
    fun `pokemon translatedendpoint returns not found if the pokemon does not exist`() =
        testApplication {
            application { module() }

            val pokemonName = "aquaphant"
            val response = client.get("/pokemon/$pokemonName")
            assertEquals(HttpStatusCode.NotFound, response.status)
            assertContains(response.bodyAsText(), "Could not find a Pokemon.")
        }
}
