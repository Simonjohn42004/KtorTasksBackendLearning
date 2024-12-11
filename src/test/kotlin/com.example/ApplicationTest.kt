package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun taskCanBeFoundByPriority() = testApplication {
        application {
            module()
        }
        val response = client.get("/tasks/byPriority/Medium")
        val body = response.bodyAsText()
        println(body)
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun invalidPriorityProduces400() = testApplication {
        application {
            module()
        }
        val response = client.get("/tasks/byPriority/Invalid")

        assertEquals(expected = HttpStatusCode.BadRequest, actual = response.status)
    }

    @Test
    fun noListForPriorityProduces404() = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks/byPriority/Vital")
//        val body = response.bodyAsText()

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun newTaskCanBeAdded() = testApplication {
        application {
            module()
        }
        val response1 = client.post("/tasks"){
            header(
                HttpHeaders.ContentType,
                ContentType.Application.FormUrlEncoded.toString()
            )
            setBody(
                listOf(
                    "Name" to "Swimming",
                    "Description" to "Go to the beach",
                    "Priority" to "Low"
                ).formUrlEncode()
            )
        }
        assertEquals(HttpStatusCode.NoContent, response1.status)

        val response2 = client.get("/tasks/byName/Swimming")
        assertEquals(HttpStatusCode.OK, response2.status)


    }

}