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
        assertEquals("Hello World!", response.bodyAsText())
    }

    @Test
    fun testFirstEndpoint() = testApplication {
        application { module() }

        val pokemonName = "mewtwo"

        val response = client.get("/pokemon/$pokemonName")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("json", response.contentType()?.contentSubtype)
        assertContains(response.bodyAsText(), pokemonName)
    }

    // TODO(delete-me): this is just an example for actual error tests later
    @Test
    fun testSimpleError() = testApplication {
        application { module() }

        val pokemonName = "mewtwo"

        val response = client.get("/pokemon/$pokemonName")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("json", response.contentType()?.contentSubtype)
        assertContains(response.bodyAsText(), pokemonName)
    }
}
