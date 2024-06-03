package com.pokedex

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertContains

class ApplicationTest {
    @Test
    fun testRoot(): Unit = testApplication {
        application {
            module()
        }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello World!", response.bodyAsText())
    }

    @Test
    fun testFirstEndpoint() = testApplication {
        application {
            module()
        }

        val pokemonName = "mewtwo"

        val response = client.get("/pokemon/$pokemonName")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("json", response.contentType()?.contentSubtype)
        assertContains(response.bodyAsText(), pokemonName)
    }
}